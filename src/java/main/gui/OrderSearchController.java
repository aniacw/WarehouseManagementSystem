package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DateStringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.ConverterEnum;

import main.OrderStatus;
import main.data.Order;
import javafx.fxml.FXML;
import main.data.OrderedItems;
import main.data.Supplier;
import main.factory.Sessions;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OrderSearchController {

    @FXML
    TextField
            searchByTextField;

    @FXML
    ComboBox<String>
            searchByCombobox;

    @FXML
    Button
            searchButton,
            orderAgainButton,
            showAllOrdersButton,
            updateStatusButton;

    @FXML
    TableView<Order>
            orderTable;

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

    Order order;
    ObservableList<Order> data;
    SessionFactory sessionFactory;
    HashMap<String, Field> orderSQLColumnToFields;
    List<String> columnNames;

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();

        orderNoCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("orderNumber")); //nazwa pola w klasie
        orderStatusCol.setCellValueFactory(new PropertyValueFactory<Order, OrderStatus>("status"));
        orderTotalValueCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalOrderValue"));
        orderSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Order, Integer>("supplierId"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<Order, Date>("date"));
        orderTable.getColumns().setAll(orderNoCol, orderStatusCol, orderTotalValueCol, orderSupplierIdCol, orderDateCol);

        orderTable.setEditable(true);

//        orderNoCol.setCellFactory(TextFieldTableCell.<Order, Integer>forTableColumn(new IntegerStringConverter()));
        orderStatusCol.setCellFactory(ComboBoxTableCell.<Order, OrderStatus>forTableColumn(
                OrderStatus.CANCELLED, OrderStatus.COMPLETED, OrderStatus.PENDING));
//        orderTotalValueCol.setCellFactory(TextFieldTableCell.<Order, Double>forTableColumn(new DoubleStringConverter()));
//        orderSupplierIdCol.setCellFactory(TextFieldTableCell.<Order, Integer>forTableColumn(new IntegerStringConverter()));
//        orderDateCol.setCellFactory(TextFieldTableCell.<Order, Date>forTableColumn(new DateStringConverter()));

        orderTable.setItems(createorderTable());

        orderStatusCol.setOnEditCommit(
                event -> {
                    (event.getTableView().getItems().get(
                            event.getTablePosition().getRow())
                    ).setStatus(event.getNewValue());
                }
        );

        orderColumnNames();
        for (String columns : columnNames)
            searchByCombobox.getItems().add(columns);
    }

    //ok
    public ObservableList<Order> createorderTable() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Order> orders = session.createQuery("from Order ").list();
        for (Order o : orders)
            data.add(o);
        return data;
    }

    //ok
    List<String> orderColumnNames() {
        Field[] fields = Order.class.getDeclaredFields();
        orderSQLColumnToFields = new HashMap<>();
        columnNames = new ArrayList<>(fields.length);
        for (Field f : fields) {
            f.setAccessible(true);
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                columnNames.add(column.name());
                orderSQLColumnToFields.put(column.name(), f);
            }
        }
        return columnNames;
    }

    //ok
    public void onButtonSearchClicked() {
        String selectedColumn = searchByCombobox.getSelectionModel().getSelectedItem();
        String searchInput = searchByTextField.getText();
        Field selectedField = orderSQLColumnToFields.get(selectedColumn);

        if(selectedField.getType().getSuperclass().equals(Number.class)){
            Integer value = Integer.parseInt(searchInput);
            orderTable.setItems(data.filtered(order -> {
                try {
                    return selectedField.get(order).equals(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            }));

        } else {
            orderTable.setItems(data.filtered(order -> {
                Object fieldValue = null;
                try {
                    fieldValue = selectedField.get(order);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
                String fieldString = fieldValue.toString();
                return fieldString.toLowerCase().contains(searchInput.toLowerCase());
            }));
        }

    }

    //nie dziala
    public void onButtonStatusUpdateClicked(){
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Integer oid = orderTable.getSelectionModel().getSelectedItem().getOrderNumber();
        order = session.get(Order.class, oid);
        session.save(order);
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
    }

    //nie dziala
    public void onButtonOrderAgainClicked() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Order newOrder = new Order(
                null,
                OrderStatus.PENDING,
                0.0,
                orderTable.getSelectionModel().getSelectedItem().getSupplierId(),
                new Date());

        newOrder.setOrderedItems(new ArrayList<>());
        session.save(newOrder);
        session.getTransaction().commit();
        session.beginTransaction();

        Integer oldOrderNo = orderTable.getSelectionModel().getSelectedItem().getOrderNumber();//ok
        Order oldOrder = session.get(Order.class, oldOrderNo);//ok

        List<OrderedItems> filteredList =  session.createQuery(
                "from OrderedItems as orderedItems where orderedItems.orderId = : oid").setParameter("oid", oldOrderNo).list();



        for(OrderedItems b:filteredList) {
            System.out.println(b);
        }

        for (OrderedItems item : filteredList) {
            newOrder.getOrderedItems().add(item);
            item.setOrder(newOrder);
            item.setOrderId(newOrder.getOrderNumber());
        }
        session.save(newOrder);
        session.getTransaction().commit();
        session.close();
    }

    //ok
    public void onButtonShowAllOrdersClicked() {
        orderTable.setItems(data );
    }


}
