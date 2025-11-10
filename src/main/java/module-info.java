module com.diegoribeiro.javafxws {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.diegoribeiro.javafxws to javafx.fxml;
    exports com.diegoribeiro.javafxws;
}