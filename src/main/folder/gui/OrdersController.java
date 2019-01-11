package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class OrdersController {

    @FXML
    Button
            searchOrder,
            placeOrder;

    @FXML
    Stage stage;

    public void initialize(){
        stage = new Stage();
    }

    public void onButtonSearchOrderClicked(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("fxml/orderSearch.fxml"));
            stage.setTitle("Search Order");
            stage.setScene(new Scene(root, 884, 528));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onButtonPlaceOrderClicked(){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("fxml/orderPlace.fxml"));
            stage.setTitle("Place new Order");
            stage.setScene(new Scene(root, 884, 528));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
