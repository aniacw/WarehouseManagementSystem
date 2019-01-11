package main.gui;

import main.data.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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



    public void onButtonAddSupplierClicked(){}

    public void onButtonEditSupplierClicked(){}

    public void onButtonRemoveSupplierClicked(){}

}
