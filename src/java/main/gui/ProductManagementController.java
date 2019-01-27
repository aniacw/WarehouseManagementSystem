package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.data.ColumnNameAnnotation;
import main.data.Product;
import javafx.fxml.FXML;
import main.data.Supplier;
import main.factory.Sessions;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.RollbackException;
import java.lang.reflect.Field;
import java.util.ArrayList;
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
            productList;

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
    TextField
            search;

    @FXML
    ComboBox<String>
            columnNamesCombobox;

    Product product;
    SessionFactory sessionFactory;
    ObservableList<Product> data;
    Product newProduct;
    List<String> columnNames;


    private List<String> productColumnNames() {
        Field[] fields = Product.class.getDeclaredFields();
        columnNames = new ArrayList<>(fields.length);

        for (Field f : fields) {
            f.setAccessible(true);
            columnNames.add(f.getName());
        }
        return columnNames;
    }

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
//        productList.getColumns().clear();
        productIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id")); //nazwa pola w klasie
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productCategoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitPrice"));
        productSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("supplierId"));
//        productList.getColumns().setAll(productIdCol, productNameCol, productCategoryCol, productPriceCol, productSupplierIdCol);

        productIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        productNameCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productCategoryCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productPriceCol.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        productSupplierIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        productList.setItems(createProductList());

        productList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

        productList.setEditable(true);

        productColumnNames();
        for(String columns : columnNames)
            columnNamesCombobox.getItems().add(columns);
    }



    public ObservableList<Product> createProductList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery("from Product").list();
        for (Product p : products)
            data.add(p);
        return data;
    }

    public void onSearchButtonClicked2() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        String selectedAttribute = columnNamesCombobox.getSelectionModel().getSelectedItem();
        String searchByInput = search.getText();


        List<Product> productList = session.createQuery("from Product where name='A7'").list();
        data.setAll(productList);

        session.save(productList);
        session.getTransaction().commit();
        sessionFactory.close();
    }



    public void onSearchButtonClicked() {
        String idText = search.getText();
        if (idText.isEmpty()) {
            productList.setItems(data.filtered(
                    p -> p.getSupplierId() == null));
        } else {
            int id = Integer.parseInt(search.getText());
            productList.setItems(data.filtered(
                    p -> {
                        Integer pid = p.getSupplierId();
                        return pid != null && pid.equals(id);
                    }));
        }
        //List<Product> productsFoundList = new ArrayList<Product>();
        //Product productsFound = session.get(Product.class, id);
    }

    public void onRefreshButtonClicked() {
    }

    public void onButtonAddRowClicked() {
        newProduct = new Product(0, "", "", 0, 0);
        productList.getItems().add(newProduct);
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(newProduct);
        session.getTransaction().commit();
    }

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
    }

    public void onEditProductButtonClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(product);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    //z bazy usuwa wszystko, ale z TV w 1 sesji 1 row
    public void onRemoveProductButtonClicked() {
        ObservableList<Product> selectedItems = productList.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            try {
                Session session = sessionFactory.openSession();
                session.beginTransaction();

                for (Product selected : selectedItems)
                    session.delete(selected);
                for (int i = 0; i < selectedItems.size(); i++)
                    productList.getItems().remove(selectedItems.get(i));

                session.getTransaction().commit();
                session.close();
            } catch (HibernateException | RollbackException | IllegalStateException e) {
                //....
            }
        }
    }

}