package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.data.Product;
import javafx.fxml.FXML;
import main.data.Supplier;
import main.factory.Sessions;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import javax.persistence.RollbackException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductManagementController {

    @FXML
    Button
            searchButton,
            refreshButton,
            addNewProductButton,
            editProductButton,
            removeProductButton,
            addRowButton;

    @FXML
    TableView<Product>
            productTable;

    @FXML
    TableColumn<Product, Integer>
            productIdCol,
            productSupplierIdCol;

    @FXML
    TableColumn<Product, String>
            productNameCol,
            productCategoryCol;

    @FXML
    TableColumn<Product, Double>
            productPriceCol;

    @FXML
    TableColumn<Product, Supplier>
            productSupplierNameCol;

    @FXML
    TextField
            search;

    @FXML
    ComboBox<String>
            columnNamesCombobox;

    @FXML
    Label
            statusBar;

    SessionFactory sessionFactory;
    ObservableList<Product> data;
    Product newProduct;
    List<String> columnNames;
    HashMap<String, Field> productSQLColumnToFields;

//ok
    private List<String> productColumnNames() {
        Field[] fields = Product.class.getDeclaredFields();
        productSQLColumnToFields = new HashMap<String, Field>();
        columnNames = new ArrayList<>(fields.length);
        for (Field f : fields) {
            f.setAccessible(true);
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                columnNames.add(column.name());
                productSQLColumnToFields.put(column.name(), f);
            }
        }
        return columnNames;
    }


    public void onSearchButtonClicked2() {
        String selectedColumn = columnNamesCombobox.getSelectionModel().getSelectedItem();
        String searchInput = search.getText();
        Field selectedField = productSQLColumnToFields.get(selectedColumn);

//        List<Product> list = session.createQuery(
//                //  "from Product as product where " + param + " = " + searchByInput).list();
//                "from Product as product where product.name = : temp").setParameter("temp", searchByInput).list();

        if (selectedField.getType().getSuperclass().equals(Number.class)) {
            String[] parts = searchInput.split("\\s*-\\s*");
            if (parts.length == 1) {
                double value = Double.parseDouble(parts[0]);
                productTable.setItems(data.filtered(product -> {
                    try {
                        return selectedField.get(product).equals(value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        statusBar.setText("Value not found");
                        return false;
                    }
                }));

            } else if (parts.length == 2) {
                double min = Double.parseDouble(parts[0]);
                double max = Double.parseDouble(parts[1]);
                productTable.setItems(data.filtered(product -> {
                    try {
                        Number fieldValue = (Number) selectedField.get(product);
                        double d = fieldValue.doubleValue();
                        return d >= min && d <= max;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        statusBar.setText("Value not found");
                        return false;
                    }
                }));
            } else {
                //TODO: komunikat
                return;
            }
        } else {
            productTable.setItems(data.filtered(product -> {
                try {
                    Object fieldValue = selectedField.get(product);
                    String fieldStr = fieldValue.toString();
                    return fieldStr.toLowerCase().contains(searchInput.toLowerCase());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    statusBar.setText("Value not found");
                    return false;
                }
            }));
        }
    }

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
//        productTable.getColumns().clear();
        productIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id")); //nazwa pola w klasie
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productCategoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitPrice"));
        productSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("supplierId"));
        productSupplierNameCol.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));
        //productTable.getColumns().setAll(productIdCol, productNameCol, productCategoryCol, productPriceCol, productSupplierIdCol, );

        productIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        productNameCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productCategoryCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productPriceCol.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        productSupplierIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        productSupplierNameCol.setCellFactory(TextFieldTableCell.<Product, Supplier>forTableColumn(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier object) {
                if (object == null)
                    return "OK";
                else
                    return object.getName();
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        }));

        productTable.setItems(createproductTable());

        productTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        productIdCol.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(
                            event.getTablePosition().getRow())
                    ).setId(event.getNewValue());
                }
        );

        productNameCol.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(
                            event.getTablePosition().getRow())
                    ).setName(event.getNewValue());
                }
        );

        productCategoryCol.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setCategory(event.getNewValue())
        );

        productPriceCol.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setUnitPrice(event.getNewValue())
        );

        productSupplierIdCol.setOnEditCommit(
                event -> ((Product) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setSupplierId(event.getNewValue())
        );

        productTable.setEditable(true);

        productColumnNames();
        for (String columns : columnNames)
            columnNamesCombobox.getItems().add(columns);
    }

    public ObservableList<Product> createproductTable() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery("from Product").list();
        for (Product p : products)
            data.add(p);
        return data;
    }

//    public void onSearchButtonClicked() {
//        String idText = search.getText();
//        if (idText.isEmpty()) {
//            productTable.setItems(data.filtered(
//                    p -> p.getSupplierId() == null));
//        } else {
//            int id = Integer.parseInt(search.getText());
//            productTable.setItems(data.filtered(
//                    p -> {
//                        Integer pid = p.getSupplierId();
//                        return pid != null && pid.equals(id);
//                    }));
//        }
//    }

    //ok
    public void onRefreshButtonClicked() {
        productTable.setItems(createproductTable());
    }

    //ok
    public void onButtonAddRowClicked() {
        newProduct = new Product(0, "", "", 0, 0);
        productTable.getItems().add(newProduct);
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(newProduct);
        session.getTransaction().commit();
    }

    //ok
    public void onAddNewProductButtonClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        productNameCol.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(
                            event.getTablePosition().getRow())
                    ).setName(event.getNewValue());
                }
        );

        productCategoryCol.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setCategory(event.getNewValue())
        );

        productPriceCol.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setUnitPrice(event.getNewValue())
        );

        productSupplierIdCol.setOnEditCommit(
                event -> (event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setSupplierId(event.getNewValue())
        );

        session.save(newProduct);
        session.getTransaction().commit();
        sessionFactory.close();
        statusBar.setText("Product added successfully");
    }

    //tworzy nowy obiekt:(
    public void onEditProductButtonClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        if (productTable.getSelectionModel().getSelectedItem() != null) {
            Product selectedproduct = productTable.getSelectionModel().getSelectedItem();
            selectedproduct.setId(selectedproduct.getId());
            selectedproduct.setName(selectedproduct.getName());
            selectedproduct.setUnitPrice(selectedproduct.getUnitPrice());
            selectedproduct.setCategory(selectedproduct.getCategory());
            selectedproduct.setSupplierId(selectedproduct.getSupplierId());
            selectedproduct.setSupplier(selectedproduct.getSupplier());
            selectedproduct.setQuantityOnStock(selectedproduct.getQuantityOnStock());

            session.save(selectedproduct);
            session.getTransaction().commit();
        }
        session.close();
        sessionFactory.close();
        statusBar.setText("Product details edited successfully");
    }

    //z bazy usuwa wszystko, ale z TV w 1 sesji 1 row
    public void onRemoveProductButtonClicked() {
        ObservableList<Product> selectedItems = productTable.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            try {
                Session session = sessionFactory.openSession();
                session.beginTransaction();

                for (Product selected : selectedItems)
                    session.delete(selected);
                for (int i = 0; i < selectedItems.size(); i++)
                    productTable.getItems().remove(selectedItems.get(i));

                session.getTransaction().commit();
                session.close();
            } catch (HibernateException | RollbackException | IllegalStateException e) {
                statusBar.setText("Error: product not removed");
            }
        }
    }

}