package main.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import main.data.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import main.factory.Sessions;
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

        supplierList.setItems(createSupplierList());
    }

    @SuppressWarnings("unchecked")
    public ObservableList<Supplier> createSupplierList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Supplier> suppliers = session.createQuery("from Supplier ").list();
        for(Supplier s : suppliers)
            data.add(s);
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

    public void onButtonEditSupplierClicked() {
    }

    public void onButtonRemoveSupplierClicked() {
    }

}
