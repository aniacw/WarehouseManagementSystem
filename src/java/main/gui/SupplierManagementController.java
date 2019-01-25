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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import javax.persistence.RollbackException;
import java.util.List;

public class SupplierManagementController {

    @FXML
    Button
            addSupplierButton,
            editSupplierButton,
            removeSupplierButton,
            addRowButton;

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
    Supplier newSupplier;

    public void initialize() {
        sessionFactory = Sessions.getSessionFactory();
        supplierList.getColumns().clear();
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));
        supplierIdCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("id"));
        supplierEmailCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("email"));
        supplierPhoneCol.setCellValueFactory(new PropertyValueFactory<Supplier, Integer>("phoneNumber"));
        supplierList.getColumns().setAll(supplierNameCol, supplierIdCol, supplierEmailCol, supplierPhoneCol);
        supplierList.setItems(createSupplierList());

        supplierNameCol.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        supplierIdCol.setCellFactory(TextFieldTableCell.<Supplier, Integer>forTableColumn(new IntegerStringConverter()));
        supplierEmailCol.setCellFactory(TextFieldTableCell.<Supplier>forTableColumn());
        supplierPhoneCol.setCellFactory(TextFieldTableCell.<Supplier, Integer>forTableColumn(new IntegerStringConverter()));

        supplierList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        supplierNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, String>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, String> event) {
                        event.getTableView().getItems().get(
                                event.getTablePosition().getRow()
                        ).setName(event.getNewValue());
                    }
                }
        );

        supplierIdCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, Integer> event) {
                        event.getTableView().getItems().get(
                                event.getTablePosition().getRow()
                        ).setId(event.getNewValue());
                    }
                }
        );

        supplierEmailCol.setOnEditCommit(
                event -> {
                    event.getTableView().getItems().get(
                            event.getTablePosition().getRow()
                    ).setEmail(event.getNewValue());
                }
        );

        supplierPhoneCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Supplier, Integer>>() {
                    public void handle(TableColumn.CellEditEvent<Supplier, Integer> event) {
                        event.getTableView().getItems().get(
                                event.getTablePosition().getRow()
                        ).setPhoneNumber(event.getNewValue());
                    }
                }
        );

        supplierList.setEditable(true);
    }


    @SuppressWarnings("unchecked")
    public ObservableList<Supplier> createSupplierList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Supplier> suppliers = session.createQuery("from Supplier").list();
        //   data = FXCollections.observableArrayList(suppliers);
        for (Supplier s : suppliers)
            data.add(s);
        session.close();
        return data;
    }

    //OK. Wpisywanie danych w textfieldy - nie przypisane do buttona
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

    //ok
    public void onButtonAddRowClicked() {
        newSupplier = new Supplier("", 0, "", 0);
        supplierList.getItems().add(newSupplier);
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(newSupplier);
        session.getTransaction().commit();
    }

    //ok
    public void onButtonAddSupplierTVClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        supplierNameCol.setOnEditCommit(
                event -> event.getTableView().getItems().get(
                        event.getTablePosition().getRow()
                ).setName(event.getNewValue())
        );

        supplierEmailCol.setOnEditCommit(
                event -> event.getTableView().getItems().get(
                        event.getTablePosition().getRow()
                ).setEmail(event.getNewValue())
        );

        supplierPhoneCol.setOnEditCommit(
                event -> event.getTableView().getItems().get(
                        event.getTablePosition().getRow()
                ).setPhoneNumber(event.getNewValue())
        );

        session.save(newSupplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    // ok. Nieprzypisane do buttona
    public void onButtonEditSupplierClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        Supplier supplier = session.get(Supplier.class, Integer.parseInt(supplierId.getText()));
        supplier.setName(supplierEmail.getText());
        supplier.setEmail(supplierEmail.getText());
        supplier.setPhoneNumber(Integer.parseInt(supplierPhone.getText()));
        session.save(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    //ok
    public void onButtonEditSupplierTVClicked() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.save(supplier);
        session.getTransaction().commit();
        sessionFactory.close();
    }

    //ok uzupelnic catch
    public void onButtonRemoveSupplierClicked() {
        ObservableList<Supplier> selectedItems = supplierList.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            try {
                Session session = sessionFactory.openSession();
                session.beginTransaction();
                for (Supplier selected : selectedItems)
                    session.delete(selected);
                session.getTransaction().commit();
                session.close();
                supplierList.getItems().remove(selectedItems.get(0));
            } catch (HibernateException | RollbackException | IllegalStateException e) {
                //....
            }
        }
    }
}