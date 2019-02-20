package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.data.Order;
import main.data.OrderedItems;

import java.util.List;

public class OrderDetailsController {

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

    String oid;
    String os;
    String od ;
    String osid ;
    // String osn = order.getSupplier    potem dokoncze
    String ot;
    List<OrderedItems> list;
    ObservableList<OrderedItems> olist;

    Order order;

    public OrderDetailsController() {
    }

    void setOrder(Order order){
        this.order = order;
        oid = order.getOrderNumber().toString();
        os = order.getStatus().toString();
        od = order.getDate().toString();
        osid = order.getSupplierId().toString();
        ot = order.getTotalOrderValue().toString();
        list = order.getOrderedItems();
        olist  = FXCollections.observableList(list);

        orderDetailId.setText(oid);
        orderDetailStatus.setText(os);
        orderDetailDate.setText(od);
        orderDetailSupplierId.setText(osid);
        orderDetailSupplierName.setText("");
        orderDetailTotal.setText(ot);
        //orderDetailItems.getItems().addAll(olist)
        orderDetailItems.setItems(olist);
    }

    public void initialize(){

    }
}