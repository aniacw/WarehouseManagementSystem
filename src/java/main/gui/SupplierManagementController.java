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

import javax.persistence.Entity;

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
        createSupplierList();

        supplierNameCol = new TableColumn<Supplier, String>("supplier_name");
        supplierNameCol.setCellValueFactory(new PropertyValueFactory<Supplier, String>("name"));

        supplierList.setItems(data);

        supplierList.getColumns().clear();
        supplierList.getColumns().add(supplierNameCol);

    }

    public void createSupplierList() {
        //supplierList = new TableView<Supplier>();
        data = FXCollections.observableArrayList();
        data.add(new Supplier("vvv", 1, "emai", 33));

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
