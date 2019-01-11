package main.gui;

import main.data.Supplier;
import main.data.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class OrderPlaceController {

    @FXML
    Button
            showLowStockButton,
            addToOrderLowStockButton,
            addToOrderFilteredButton,
            submitOrderButton;

    @FXML
    ChoiceBox<Supplier>
            suppliersComboBox;

    @FXML
    ChoiceBox<Product>
            productsComboBox;

    @FXML
    TextField
            quantity;

    @FXML
    TableView<Product>
            lowStockTable;

 //   @FXML
//    TableView<Order>
//            orderDraft;


    public void onButtonShowLowStockButtonClicked(){}

    public void onButtonAddOrderLowStockButtonClicked(){}

    public void onButtonAddToOrderFilteredButton(){}

    public void onButtonSubmiteOrderClicked(){}
}
