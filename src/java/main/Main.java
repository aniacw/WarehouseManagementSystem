package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.data.CSVLoader;
import main.data.ColumnNameAnnotation;
import main.data.Product;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //  CSVLoader.loadProductsTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBItems.csv");
        //  URL url = getClass().getResource("src/res/gui/fxml/mainGui.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/mainGui.fxml"));
        primaryStage.setTitle("Warehouse Management");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
     //     CSVLoader.loadSuppliersTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBSuppliers.csv");
//        CSVLoader.loadOrdersTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBOrders.csv");
     //   CSVLoader.loadOrderedItemsTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBOrderedItems.csv");
    }


    private static class C{
        @ColumnNameAnnotation(name = "costam")
        private int i;
    }


    public static void main(String[] args) {
        launch(args);
    }
}