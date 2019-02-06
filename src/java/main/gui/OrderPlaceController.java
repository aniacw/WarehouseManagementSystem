package main.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.ImageCursor;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderPlaceController {

    @FXML
    Button
            addToOrderLowStockButton,
            addToOrderFilteredButton,
            submitOrderButton;

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

            lowStockProductIdCol;

    @FXML
    TableColumn<Product, Double>
            lowStockPriceCol;

    @FXML
    TableColumn<Product, String>
            lowStockProductNameCol;

    @FXML
    TableColumn<Product, Supplier>
            lowStockSupplierNameCol;

    @FXML
    TableColumn<OrderedItems, Integer>
            orderProductIdCol,
            orderQuantityCol,
            orderSupplierIdCol;

//    @FXML
//    TableColumn<OrderedItems, Supplier>
//            orderSupplierNameCol;
//    @FXML
//    TableColumn<OrderedItems, String>
//            orderProductNameCol;

    @FXML
    TableColumn<OrderedItems, Double>
            orderTotalItemValueCol,
            orderDiscountCol;


    @FXML
    TextField
            prodNameTemp,
            supplierNameTemp;

    SessionFactory sessionFactory;
    List<String> supplierNames;
    List<String> products;
    String selectedSupplier;
    ObservableList<Product> data;
    ObservableList<Product> dataLowStock;
    List<Product> productsLowStock;

    private List<String> supplierColumnOrder() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        supplierNames = session.createQuery("select supplier.name from Supplier  as supplier ").list();
        session.close();
        return supplierNames;
    }

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();

        initializeLowStockTV();

        supplierColumnOrder();
        for (String suppliers : supplierNames)
            suppliersComboBox.getItems().add(suppliers); //dotad ok

        //onSuppliersSelected();
        //for (String productColumns : products)
        //productsComboBox.getItems().add(productColumns);

        orderProductIdCol.setCellValueFactory(new PropertyValueFactory<OrderedItems, Integer>("productId"));
        orderQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        //orderSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("supplierId"));
        orderTotalItemValueCol.setCellValueFactory(new PropertyValueFactory<>("totalItemValue"));
        orderDiscountCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
       // orderProductNameCol.setCellValueFactory(new PropertyValueFactory<OrderedItems, String>("name"));
        //orderSupplierNameCol.setCellValueFactory(new PropertyValueFactory<OrderedItems, Supplier>("supplier"));

        orderQuantityCol.setEditable(true);

    }

    public void initializeLowStockTV() {
        lowStockProductIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        lowStockProductNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        lowStockPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("unitPrice"));
        lowStockSupplierNameCol.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));

        // lowStockProductIdCol.setCellFactory(TextFieldTableCell.forTableColumn().call());**************************
        lowStockProductIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        lowStockProductNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lowStockPriceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        lowStockSupplierNameCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Supplier>() {
            @Override
            public String toString(Supplier object) {
                return object.getName();
            }

            @Override
            public Supplier fromString(String string) {
                return null;
            }
        }));

        supplierProducts.setItems(showLowStockList());

        supplierProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


    }

    public ObservableList<Product> createOrderProductList() {

        String selectedItem = productsComboBox.getSelectionModel().getSelectedItem();//powinien byc combobox
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery(
                "from Product as product where product.name = : pname").setParameter("pname", selectedItem).list();
        for (Product p : products)
            data.add(p);

//        prodNameTemp.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//                 String selectedItem = newValue;
//                List<Product> products = session.createQuery(
//                        "from Product as product where product.name = : pname").setParameter("pname", selectedItem).list();
//                for (Product p : products)
//                    data.add(p);
//
//            }
//        });
        return data;
    }

    //ok
    public ObservableList<Product> showLowStockList() {
        dataLowStock = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        productsLowStock = session.createQuery("from Product as product where product.quantityOnStock < 5").list();
        for (Product p : productsLowStock)
            dataLowStock.add(p);
        return dataLowStock;
    }

    public void onSuppliersSelected(ActionEvent e) {
        selectedSupplier = suppliersComboBox.getSelectionModel().getSelectedItem();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        products = session.createQuery(
                "select product.name from Product as product where product.supplier.name = : supp")
                .setParameter("supp", selectedSupplier).list();

        for (String productColumns : products)
            productsComboBox.getItems().add(productColumns);
        session.close();
    }


    public void onButtonAddOrderLowStockButtonClicked() {
    }

    public void onButtonAddToOrderClicked() {
        Product selectedProduct = supplierProducts.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            OrderedItems item = new OrderedItems(null, selectedProduct.getId(), 1, 0.0, selectedProduct.getUnitPrice(), null);
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

        Session session = sessionFactory.openSession();
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
//        session.close();
//
//
//        session = sessionFactory.openSession();
        session.beginTransaction();

        for (OrderedItems item : orderDraft.getItems()) {
            Double total = item.getQuantity()*item.getProduct().getUnitPrice();
            item.setTotalItemValue(total);
            newOrder.getOrderedItems().add(item);
            item.setOrder(newOrder);
            item.setOrderId(newOrder.getOrderNumber());
            //session.save(item);
        }
        session.save(newOrder);
        session.getTransaction().commit();
        session.close();

    }
}
