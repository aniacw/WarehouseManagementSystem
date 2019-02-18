package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.PointLight;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.OrderStatus;
import main.data.Order;
import main.data.OrderedItems;
import main.data.Supplier;
import main.data.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.factory.Sessions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;

public class OrderPlaceController {

    @FXML
    Button
            addToOrderFilteredButton,
            submitOrderButton,
            addProduct;

    @FXML
    ComboBox<String>
            suppliersComboBox;

    @FXML
    ComboBox<String>
            productsComboBox;

    @FXML
    TextField
            quantity;

    @FXML
    TableView<Product>
            supplierProducts;

    @FXML
    TableView<OrderedItems>
            orderDraft;

    @FXML
    TableColumn<Product, Integer>
            productsQtyCol,
            productsProductIdCol;

    @FXML
    TableColumn<Product, Double>
            productsPriceCol;

    @FXML
    TableColumn<Product, String>
            productsProductNameCol;

    @FXML
    TableColumn<Product, Supplier>
            productsSupplierNameCol;

    @FXML
    TableColumn<OrderedItems, Integer>
            orderProductIdCol,
            orderQuantityCol;

    @FXML
    TableColumn<OrderedItems, Double>
            orderTotalItemValueCol,
            orderDiscountCol;


    SessionFactory sessionFactory;
    List<String> supplierNames;
    List<String> products;
    String selectedSupplier;
    ObservableList<Product> data;
    ObservableList<Product> dataProducts;
    List<Product> allProducts;
    Map<String, Field> productsPerSupplier;
    List<Product> filteredProducts;
    ObservableList<Product> filteredProductsOL;
    Session session;

    public void createMap() {
        Field[] fields = Product.class.getDeclaredFields();
        productsPerSupplier = new HashMap<String, Field>();

        for (Field f : fields) {
            f.setAccessible(true);
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                productsPerSupplier.put(column.name(), f);
            }
        }
    }

    //ok
    private List<String> supplierColumnOrder() {
        session.beginTransaction();
        supplierNames = session.createQuery("select supplier.name from Supplier  as supplier ").list();
        return supplierNames;
    }

    //ok
    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
        session = sessionFactory.openSession();

        initializeProductsTV();

        supplierColumnOrder();
        for (String suppliers : supplierNames)
            suppliersComboBox.getItems().add(suppliers);

        orderProductIdCol.setCellValueFactory(new PropertyValueFactory<OrderedItems, Integer>("productId"));
        orderQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderTotalItemValueCol.setCellValueFactory(new PropertyValueFactory<>("totalItemValue"));
        orderDiscountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));

        orderQuantityCol.setEditable(true);
        createMap();
    }

    public void initializeProductsTV() {
        productsProductIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productsProductNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitPrice"));
        productsSupplierNameCol.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));
        productsQtyCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("quantityOnStock"));

        // productsProductIdCol.setCellFactory(TextFieldTableCell.forTableColumn().call());**************************
        productsProductIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        productsProductNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        productsPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        productsSupplierNameCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier object) {
                return object.getName();
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        }));

        productsQtyCol.setCellFactory(column -> {
            return new TableCell<Product, Integer>() {

                protected void updateItem(Integer qty) {
                    if (qty <= 5) {
                        setTextFill(Color.CHOCOLATE);
                        setStyle("-fx-background-color: yellow");
                    } else {
                        setTextFill(Color.BLACK);
                        setStyle("");
                    }
                }
            };
        });

        supplierProducts.setItems(showproductsList());
        supplierProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    //usunac?
    public ObservableList<Product> createOrderProductList() {

        String selectedItem = productsComboBox.getSelectionModel().getSelectedItem();
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery("from Product ").list();
//        List<Product> products = session.createQuery(
//                "from Product as product where product.name = : pname").setParameter("pname", selectedItem).list();
        for (Product p : products)
            data.add(p);
        return data;
    }

    //ok
    public ObservableList<Product> showproductsList() {
        dataProducts = FXCollections.observableArrayList();
        session.beginTransaction();
        allProducts = session.createQuery("from Product").list();
        for (Product p : allProducts)
            dataProducts.add(p);
        session.getTransaction().commit();
        return dataProducts;
    }

    //ok
    public void onSuppliersSelected(ActionEvent e) {
        session.beginTransaction();
        selectedSupplier = suppliersComboBox.getSelectionModel().getSelectedItem();

        filteredProducts = session.createQuery(
                "from Product as product where product.supplier.name = : name").setParameter("name", selectedSupplier).list();

        filteredProductsOL = FXCollections.observableArrayList(filteredProducts);

        supplierProducts.setItems(filteredProductsOL);
    }


    public void onButtonAddOrderProductsButtonClicked() {
    }

    //ok
    public void onButtonAddToOrderClicked() {
        Product selectedProduct = supplierProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            OrderedItems item = new OrderedItems(
                    null,
                    selectedProduct.getId(),
                    1,
                    0.0,
                    selectedProduct.getUnitPrice(),
                    null);
            item.setProduct(selectedProduct);
            orderDraft.getItems().add(item);
        }
    }

    //
    public static Object getValueAt(TableView<Product> tableView, int col, int row) {
        return tableView.getColumns().get(col).getCellObservableValue(row).getValue();
    }

    public void onButtonSubmiteOrderClicked() {
        if (orderDraft.getItems().size() == 0)
            return; //komunikat

        session.beginTransaction();

        Order newOrder = new Order(
                null,
                OrderStatus.PENDING,
                0.0,
                orderDraft.getItems().get(0).getProduct().getSupplierId(),
                new Date());

        newOrder.setOrderedItems(new ArrayList<>());
        session.save(newOrder);
        session.getTransaction().commit();

        session.beginTransaction();

        for (OrderedItems item : orderDraft.getItems()) {
            Double total = item.getQuantity() * item.getProduct().getUnitPrice();
            item.setTotalItemValue(total);
            newOrder.getOrderedItems().add(item);
            item.setOrder(newOrder);
            item.setOrderId(newOrder.getOrderNumber());
        }
        session.save(newOrder);
        session.getTransaction().commit();
    }
}