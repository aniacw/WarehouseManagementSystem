package gui;

import Product.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ProductManagementController {

    @FXML
    TextField
            search;

    @FXML
    Button
            searchButton,
            refreshButton,
            addNewProductButton,
            editProductButton,
            removeProductButton;

    @FXML
    TableView<Product>
            productList;

    public void initialize(){
        //display products
    }

    public void onSearchButtonClicked(){}

    public void onRefreshButtonClicked(){}

    public void onAddNewProductButtonClicked(){}

    public void onEditProductButtonClicked(){}

    public void onRemoveProductButtonClicked(){}

}
