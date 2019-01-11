package main.gui;

import main.data.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.hibernate.Session;

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

    public void onAddNewProductButtonClicked(){
        Product product =new Product(1, "mleko", "nabial", 2.7, 1);


    }

    public void onEditProductButtonClicked(){}

    public void onRemoveProductButtonClicked(){}

}
