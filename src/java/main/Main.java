package main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.data.CSVLoader;
import main.data.Supplier;
import main.factory.Sessions;
import main.gui.SupplierManagementController;
import org.hibernate.*;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.engine.spi.FilterDefinition;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.stat.Statistics;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // CSVLoader.loadProductsTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBItems.csv");
        // URL url = getClass().getResource("src/res/gui/fxml/mainGui.fxml");
        Parent root = FXMLLoader.load(getClass().getResource("gui/fxml/mainGui.fxml"));
        primaryStage.setTitle("Warehouse Management");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
//       CSVLoader.loadSuppliersTable("C:\\Users\\Ania\\IdeaProjects\\WarehouseManagementSystem\\src\\res\\WarehouseDBSuppliers.csv");
    }


    public static void main(String[] args) {
         launch(args);
//
//        Supplier supplier;
//        SessionFactory sessionFactory = Sessions.getSessionFactory();
//        ObservableList<Supplier> data;
//
//            data = FXCollections.observableArrayList();
//            Session session = sessionFactory.openSession();
//            session.beginTransaction();
//            List<Supplier> suppliers = session.createQuery("from Supplier").list();
//            for(Supplier s : suppliers)
//                data.add(s);
//
//            System.out.println(data);

    }
}
