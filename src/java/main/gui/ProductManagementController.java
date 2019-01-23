package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import main.data.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.data.Supplier;
import main.factory.Sessions;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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

    @FXML
    TableColumn<Product, Integer>
            productIdCol,
            productSupplierIdCol;

    @FXML
    TableColumn<Product, String>
            productNameCol,
            productCategoryCol;

    @FXML
    TableColumn<Product, Double>
            productPriceCol;

    @FXML
            TextField
            idSearch;

    Product product;
    SessionFactory sessionFactory;
    ObservableList<Product> data;


    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
        productList.getColumns().clear();
        productIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("product_id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productCategoryCol.setCellValueFactory(new PropertyValueFactory<Product, String>("category"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
        productSupplierIdCol.setCellValueFactory(new PropertyValueFactory<Product, Integer>("supplier_id"));
        productList.getColumns().setAll(productIdCol, productNameCol, productCategoryCol, productPriceCol, productSupplierIdCol);
        productList.setItems(createProductList());
        productList.setEditable(true);
        productIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));
        productNameCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productCategoryCol.setCellFactory(TextFieldTableCell.<Product>forTableColumn());
        productPriceCol.setCellFactory(TextFieldTableCell.<Product, Double>forTableColumn(new DoubleStringConverter()));
        productSupplierIdCol.setCellFactory(TextFieldTableCell.<Product, Integer>forTableColumn(new IntegerStringConverter()));

    }

    public ObservableList<Product> createProductList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Product> products = session.createQuery("from Product ").list();
        for(Product p : products)
            data.add(p);
        return data;
    }

    public void onSearchButtonClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        int id = Integer.parseInt(idSearch.getText());
        List<Product> productsFoundList = new ArrayList<Product>();
        Product productsFound = session.get(Product.class, id);




        session.save(product);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    public void onRefreshButtonClicked() {
    }

    public void onAddNewProductButtonClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        productIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Product, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Product, Integer> event) {
                        ((Product) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setId(event.getNewValue());
                    }
                }
        );

        productNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
                    public void handle(TableColumn.CellEditEvent<Product, String> event) {
                        ((Product) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setName(event.getNewValue());
                    }
                }
        );

        productCategoryCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Product, String>>() {
                    public void handle(TableColumn.CellEditEvent<Product, String> event) {
                        ((Product) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setCategory(event.getNewValue());
                    }
                }
        );

        productPriceCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Product, Double>>() {
                    public void handle(TableColumn.CellEditEvent<Product, Double> event) {
                        ((Product) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setUnitPrice(event.getNewValue());
                    }
                }
        );

        productSupplierIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Product, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Product, Integer> event) {
                        ((Product) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setSupplierId(event.getNewValue());
                    }
                }
        );

        session.save(product);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    public void onEditProductButtonClicked() {
    }

    public void onRemoveProductButtonClicked() {
    }

}
