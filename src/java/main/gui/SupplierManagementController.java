package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import main.data.Supplier;
import javafx.fxml.FXML;
import main.factory.Sessions;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import java.util.List;

public class SupplierManagementController {

    @FXML
    Button
            addSupplierButton,
            editSupplierButton,
            removeSupplierButton;

    @FXML
    TextField
            supplierName,
            supplierId,
            supplierEmail,
            supplierPhone;

    @FXML
    TableView<Supplier>
            supplierList;

    @FXML
    TableColumn<Supplier, String>
            supplierNameCol,
            supplierEmailCol;

    @FXML
    TableColumn<Supplier, Integer>
            supplierIdCol,
            supplierPhoneCol;


    Supplier supplier;
    SessionFactory sessionFactory;
    ObservableList<Supplier> data;

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
        supplierList.getColumns().clear();
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        supplierIdCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        supplierEmailCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
        supplierPhoneCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("phoneNumber"));
        supplierList.getColumns().setAll(supplierNameCol, supplierIdCol, supplierEmailCol, supplierPhoneCol);
        supplierList.setItems(createSupplierList());
        supplierList.setEditable(true);
        supplierNameCol.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        supplierIdCol.setCellFactory(TextFieldTableCell.<Supplier, Integer>forTableColumn(new IntegerStringConverter()));
        supplierEmailCol.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        supplierPhoneCol.setCellFactory(TextFieldTableCell.<Supplier, Integer>forTableColumn(new IntegerStringConverter()));
    }

//    public Supplier getSupplierById(int supplier_id){
//        Session sess = sessionFactory.openSession();
//        supplier = sess.get(Supplier.class, supplier_id);
//        Hibernate.initialize(supplier);
//        sess.close();
//        return supplier;
//    }

    @SuppressWarnings("unchecked")
    public ObservableList<Supplier> createSupplierList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Supplier> suppliers = session.createQuery("from Supplier").list();
     //   data = FXCollections.observableArrayList(suppliers);
        for(Supplier s : suppliers)
            data.add(s);
//        int index = 0;
//        for(Supplier s : suppliers) {
//            data.add(index, s);
//            index++;
//        }
        return data;
    }

    //OK. Wpisywanie danych w textfieldy
    public void onButtonAddSupplierClicked() {
        supplier = new Supplier(
                supplierName.getText(),
                Integer.parseInt(supplierId.getText()),
                supplierEmail.getText(),
                Integer.parseInt(supplierPhone.getText()));

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    public void onButtonAddSupplierTVClicked() {

    }

    // ok
    public void onButtonEditSupplierClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        supplier = session.get(Supplier.class, Integer.parseInt(supplierId.getText()));
        supplier.setEmail(supplierEmail.getText());
        session.save(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    public void onButtonEditSupplierTVClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        supplierNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, String>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, String> event) {
                        ((Supplier) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setName(event.getNewValue());
                    }
                }
        );

        supplierIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, Integer> event) {
                        ((Supplier) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setId(event.getNewValue());
                    }
                }
        );

        supplierEmailCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, String>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, String> event) {
                        ((Supplier) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setEmail(event.getNewValue());
                    }
                }
        );

        supplierPhoneCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, Integer> event) {
                        ((Supplier) event.getTableView().getItems().get(
                                event.getTablePosition().getRow())
                        ).setPhoneNumber(event.getNewValue());
                    }
                }
        );

        session.save(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    //ok
    public void onButtonRemoveSupplierClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        supplier = session.get(Supplier.class, Integer.parseInt(supplierId.getText()));
        session.delete(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

}
