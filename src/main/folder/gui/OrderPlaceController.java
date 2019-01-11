package gui;

import Product.Product;
import Product.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OrderPlaceController {

    @FXML
    Button
            showLowStockButton,
            addToOrderLowStockButton,
            addToOrderFilteredButton,
            submitOrderButton;

    @FXML
    ComboBox<Supplier>
            suppliersCombobox;

    @FXML
    ComboBox<Product>
            productsCombobox;

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
