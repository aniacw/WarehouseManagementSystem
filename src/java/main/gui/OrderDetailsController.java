package main.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import main.data.OrderedItems;

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




}
