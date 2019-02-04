package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderPlaceController {

    @FXML
    Button
            showLowStockButton,
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
            lowStockTable;

    @FXML
    TableView<Product>
            orderDraft;

    @FXML
    TableColumn<Product, Integer>
            orderProductIdCol,
            orderQuantityCol,
            orderSupplierIdCol,
            lowStockProductIdCol;

    @FXML
    TableColumn<Product, Double>
            orderTotalItemValueCol,
            orderDiscountCol,
            lowStockPriceCol;

    @FXML
    TableColumn<Product, String>
            orderProductNameCol,
            lowStockProductNameCol;

    @FXML
    TableColumn<Product, Supplier>
            orderSupplierNameCol,
            lowStockSupplierNameCol;

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
    List<Product>productsLowStock;

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

//        supplierColumnOrder();
//        for (String suppliers : supplierNames)
//            suppliersComboBox.getItems().add(suppliers); //dotad ok
//
//        onSuppliersSelected();
//        for (String productColumns : products)
//            productsComboBox.getItems().add(productColumns);

        orderProductIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        //orderQuantityCol.setUserData(Integer.parseInt(quantity.getText()));///????
        orderSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("supplierId"));
        // orderTotalItemValueCol.setCellValueFactory();
        //orderDiscountCol.setCellValueFactory();
        orderProductNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        orderSupplierNameCol.setCellValueFactory(new PropertyValueFactory<Product, Supplier>("supplier"));


    }

    public void initializeLowStockTV(){
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

        lowStockTable.setItems(showLowStockList());
    }

    public ObservableList<Product> createOrderProductList() {
        String selectedItem = prodNameTemp.getText();
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery(
                "from Product as product where product.name = : pname").setParameter("pname", selectedItem).list();
        for (Product p : products)
            data.add(p);
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

    public List<String> onSuppliersSelected() {
        selectedSupplier = suppliersComboBox.getSelectionModel().getSelectedItem();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
//        products = session.createQuery(
//                "select product.name from Product as product where product.supplier.name = : supp")
//                .setParameter("supp", selectedSupplier).list();

        products = session.createQuery(
                "select product.name from Product as product where product.supplier.name = 'Samsung'").list();
        return products;
    }



    public void onButtonAddOrderLowStockButtonClicked() {
    }

    public void onButtonAddToOrderClicked() {
        orderDraft.setItems(createOrderProductList());
    }

    public void onButtonSubmiteOrderClicked() {
    }


}
