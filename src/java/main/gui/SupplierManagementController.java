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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Column;
import javax.persistence.RollbackException;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierManagementController {

    @FXML
    Button
            addSupplierButton,
            editSupplierButton,
            removeSupplierButton,
            addRowButton,
            searchSupplierButton;

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

    @FXML
    ComboBox<String>
            filterComboBox;

    @FXML
    TextField
            filterTextField;


    Supplier supplier;
    SessionFactory sessionFactory;
    ObservableList<Supplier> data;
    Supplier newSupplier;
    List<String> columnNames;
    Map<String, Field> columnMap;

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

        supplierColumnNames();
        for (String columns : columnNames)
            filterComboBox.getItems().add(columns);
    }


    @SuppressWarnings("unchecked")
    public ObservableList<Supplier> createSupplierList() {
        data = FXCollections.observableArrayList();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        List<Supplier> suppliers = session.createQuery("from Supplier").list();
        for (Supplier s : suppliers)
            data.add(s);
        session.close();
        return data;
    }

    List<String> supplierColumnNames(){
        Field[] fields = Supplier.class.getDeclaredFields();
        columnMap = new HashMap<>();
        columnNames = new ArrayList<>(fields.length);
        for (Field f : fields) {
            f.setAccessible(true);
            Column column = f.getAnnotation(Column.class);
            if (column != null) {
                columnNames.add(column.name());
                columnMap.put(column.name(), f);
            }
        }
        return columnNames;
    }

    //ok
    public void onButtonSearchClicked(){
        String selectedColumn = filterComboBox.getSelectionModel().getSelectedItem();
        String searchInput = filterTextField.getText();
        Field selectedField = columnMap.get(selectedColumn);

        if(selectedField.getType().getSuperclass().equals(Number.class)){
            Integer value = Integer.parseInt(searchInput);
            supplierList.setItems(data.filtered(supplier1 -> {
                try {
                    return selectedField.get(supplier1).equals(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            }));
        } else {
            supplierList.setItems(data.filtered(supplier1 -> {
                Object fieldValue = null;
                try {
                    fieldValue = selectedField.get(supplier1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
                String fieldString = fieldValue.toString();
                return fieldString.toLowerCase().contains(searchInput.toLowerCase());
            }));
        }
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