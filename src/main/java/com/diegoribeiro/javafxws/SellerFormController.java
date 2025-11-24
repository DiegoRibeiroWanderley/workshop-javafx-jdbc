package com.diegoribeiro.javafxws;

import com.diegoribeiro.javafxws.db.DbException;
import com.diegoribeiro.javafxws.listeners.DataChangeListener;
import com.diegoribeiro.javafxws.model.entities.Seller;
import com.diegoribeiro.javafxws.model.exceptions.ValidationException;
import com.diegoribeiro.javafxws.model.services.SellerService;
import com.diegoribeiro.javafxws.util.Alerts;
import com.diegoribeiro.javafxws.util.Constraints;
import com.diegoribeiro.javafxws.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class SellerFormController implements Initializable {

    private Seller entity;

    private SellerService sellerService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public void setSeller(Seller entity) {
        this.entity = entity;
    }

    public void setSellerService(SellerService SellerService) {
        this.sellerService = SellerService;
    }

    public void subscribeDataChangeListener(DataChangeListener dataChangeListener) {
        dataChangeListeners.add(dataChangeListener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (sellerService == null) {
            throw new IllegalStateException("Seller Service was null");
        }

        try {
            entity = getFormData();
            sellerService.saveOrUpdate(entity);

            notifyDataChangeListeners();

            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorsMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener dataChangeListener : dataChangeListeners) {
            dataChangeListener.onDataChanged();
        }
    }

    private Seller getFormData() {
        Seller obj = new Seller();

        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().isEmpty()) {
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (!exception.getErrors().isEmpty()) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 40);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

    private void setErrorsMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("name")) {
            labelErrorName.setText(errors.get("name"));
        }
    }
}