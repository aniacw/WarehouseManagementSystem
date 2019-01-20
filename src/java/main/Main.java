package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.data.CSVLoader;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

       // CSVLoader.loadProductsTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBItems.csv");
        URL url = getClass().getResource("src/res/gui/fxml/mainGui.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/mainGui.fxml"));
        primaryStage.setTitle("Warehouse Management");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();

//       CSVLoader.loadSuppliersTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBSuppliers.csv");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
