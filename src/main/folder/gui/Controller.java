package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    Button productManagementButton,
            ordersButton;

    @FXML
    Stage stage;

    public void initialize(){
        stage = new Stage();
    }


    public void onProductManagementButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("productManagement.fxml"));
        stage.setTitle("Product Management");
        stage.setScene(new Scene(root, 884, 528));
        stage.show();
    }

    public void onOrdersButtonClicked() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("productManagement.fxml"));
        stage.setTitle("Orders");
        stage.setScene(new Scene(root, 884, 528));
        stage.show();
    }
}
