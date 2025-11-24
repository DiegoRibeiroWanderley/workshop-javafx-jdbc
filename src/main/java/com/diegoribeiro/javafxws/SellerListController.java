package com.diegoribeiro.javafxws;

import com.diegoribeiro.javafxws.db.DbException;
import com.diegoribeiro.javafxws.listeners.DataChangeListener;
import com.diegoribeiro.javafxws.model.entities.Seller;
import com.diegoribeiro.javafxws.model.services.SellerService;
import com.diegoribeiro.javafxws.util.Alerts;
import com.diegoribeiro.javafxws.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> colIdSeller;

    @FXML
    private TableColumn<Seller, String> colNameSeller;

    @FXML
    private TableColumn<Seller, String> colEmailSeller;

    @FXML
    private TableColumn<Seller, Date> colBirthDateSeller;

    @FXML
    private TableColumn<Seller, Double> colBaseSalarySeller;

    @FXML
    TableColumn<Seller, Seller> tableColumnEDIT;

    @FXML
    TableColumn<Seller, Seller> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Seller> sellerObservableList;

    @FXML
    public void onBtNewAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);

        Seller obj = new Seller();

        createDialogForm(obj, "SellerForm.fxml", parentStage);
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        colIdSeller.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNameSeller.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmailSeller.setCellValueFactory(new PropertyValueFactory<>("email"));

        colBirthDateSeller.setCellValueFactory(new PropertyValueFactory<>("birthdate"));
        Utils.formatTableColumnDate(colBirthDateSeller, "dd/MM/yyyy");

        colBaseSalarySeller.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(colBaseSalarySeller, 2);

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service is null");
        }

        List<Seller> list = service.findAll();
        sellerObservableList = FXCollections.observableArrayList(list);
        tableViewSeller.setItems(sellerObservableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
        /**
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setSellerService(new SellerService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Seller data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
        **/
    }



    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("edit");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "SellerForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("remove");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj, empty);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Service is null");
            }
            try {
                service.delete(obj);
                updateTableView();
            } catch (DbException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
}
