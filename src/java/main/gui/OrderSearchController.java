package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.EnumConverter;
import main.OrderStatus;
import main.data.Order;
import javafx.fxml.FXML;
import main.factory.Sessions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

public class OrderSearchController {

    @FXML
    TextField
            supplierIdTextField,
            supplierNameTextField,
            orderIdTextField;

    @FXML
    Button
            searchByIdButton,
            searchByNameButton,
            searchOrderByIdButton,
            orderAgainButton,
            showAllOrdersButton;

    @FXML
    TableView<Order>
            orderList;

    @FXML
    TableColumn<Order, Integer>
            orderNoCol,
            orderSupplierIdCol;

    @FXML
    TableColumn<Order, OrderStatus>
            orderStatusCol;

    @FXML
    TableColumn<Order, Double>
            orderTotalValueCol;

    @FXML
    TableColumn<Order, Date>
            orderDateCol;


    ObservableList<Order> data;
    SessionFactory sessionFactory;
    OrderStatus orderStatus;

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();

        orderNoCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber")); //nazwa pola w klasie
        orderStatusCol.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("status"));
        orderTotalValueCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalOrderValue"));
        orderSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("supplierId"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));
        orderList.getColumns().setAll(orderNoCol, orderStatusCol, orderTotalValueCol, orderSupplierIdCol, orderDateCol);

        orderNoCol.setCellFactory(TextFieldTableCell.<Order, Integer>forTableColumn(new IntegerStringConverter()));
        orderStatusCol.setCellFactory(TextFieldTableCell.<Order, OrderStatus>forTableColumn(new EnumConverter()));
        orderTotalValueCol.setCellFactory(TextFieldTableCell.<Order, Double>forTableColumn(new DoubleStringConverter()));
        orderSupplierIdCol.setCellFactory(TextFieldTableCell.<Order, Integer>forTableColumn(new IntegerStringConverter()));
        orderDateCol.setCellFactory(TextFieldTableCell.<Order, Date>forTableColumn(new DateStringConverter()));

        orderList.setItems(createOrderList());
    }

        public ObservableList<Order> createOrderList () {
            data = FXCollections.observableArrayList();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Order> orders = session.createQuery("from Order ").list();
            for (Order o : orders)
                data.add(o);
            return data;
        }

        public void onButtonSearchByIdClicked () {
        }

        public void onButtonSearchByNameClicked () {
        }

        public void onButtonSearchOrderByIdClicked () {
        }

        public void onButtonOrderAgainClicked () {
        }

        public void onButtonShowAllOrdersClicked () {
        }


    }
