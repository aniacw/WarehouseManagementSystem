package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
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
import java.io.IOException;
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

    @FXML
    Label
            statusBar;

    @FXML
    Stage stage;

    Order order;
    ObservableList<Order> data;
    SessionFactory sessionFactory;
    HashMap<String, Field> orderSQLColumnToFields;
    List<String> columnNames;
    Session session;

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
        session = sessionFactory.openSession();

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

//        orderTable.setRowFactory(table -> {
//                    TableRow<Object> row = new TableRow<>();
//                    row.setOnMouseClicked(event -> {
//                        if (event.getClickCount() == 2) {
//                            try {
//                                onRowDoubleClicked();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                    return null;
//                }
//        )
//        ;

//        EventHandler<MouseEvent> eventHandler =
////                event -> {
////            if (event.getClickCount() == 2) {
////                try {
////                    onRowDoubleClicked();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
//
//
//
//                new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getClickCount() == 2) {
//                    try {
//                        onRowDoubleClicked();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };

  //      orderTable.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    //ok
    public ObservableList<Order> createorderTable() {
        data = FXCollections.observableArrayList();
        session.beginTransaction();
        List<Order> orders = session.createQuery("from Order ").list();
        for (Order o : orders)
            data.add(o);
        session.getTransaction().commit();
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

        if (selectedField.getType().getSuperclass().equals(Number.class)) {
            Integer value = Integer.parseInt(searchInput);
            orderTable.setItems(data.filtered(order -> {
                try {
                    return selectedField.get(order).equals(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    statusBar.setText("Order not found");
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
                    statusBar.setText("Order not found");
                    return false;
                }
                String fieldString = fieldValue.toString();
                return fieldString.toLowerCase().contains(searchInput.toLowerCase());
            }));
        }

    }

    //ok
    public void onButtonStatusUpdateClicked() {
        session.beginTransaction();
        Integer oid = orderTable.getSelectionModel().getSelectedItem().getOrderNumber();
        order = session.get(Order.class, oid);
        session.update(order);
        session.getTransaction().commit();
        statusBar.setText("Order status successfully updated");
    }

    //nie dziala
    public void onButtonOrderAgainClicked() {
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

        List<OrderedItems> filteredList = session.createQuery(
                "from OrderedItems as orderedItems where orderedItems.orderId = : oid").setParameter("oid", oldOrderNo).list();


        for (OrderedItems b : filteredList) {
            System.out.println(b);
        }

        for (OrderedItems item : filteredList) {
            newOrder.getOrderedItems().add(item);
            item.setOrder(newOrder);
            item.setOrderId(newOrder.getOrderNumber());
        }
        session.save(newOrder);
        session.getTransaction().commit();
    }

    //ok
    public void onButtonShowAllOrdersClicked() {
        orderTable.setItems(data);
    }


    public void onRowDoubleClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/orderDetails.fxml"));
        loader.setController(OrderDetailsController.class);
        loader.load();
        StackPane root = new StackPane();
        stage.setTitle("Order Details");
        stage.setScene(new Scene(root , 400, 500));
        stage.show();
    }

    class OrderDetailsController {

        @FXML
        TextField
                orderDetailId,
                orderDetailStatus,
                orderDetailDate,
                orderDetailSupplierId,
                orderDetailSupplierName,
                orderDetailTotal;

        @FXML
        ListView<OrderedItems>
                orderDetailItems;


    }

}
