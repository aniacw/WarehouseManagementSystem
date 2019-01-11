package main.gui;

import main.data.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

    public void onButtonSearchByIdClicked(){}

    public void onButtonSearchByNameClicked(){}

    public void onButtonSearchOrderByIdClicked(){}

    public void onButtonOrderAgainClicked(){}

    public void onButtonShowAllOrdersClicked(){}


}
