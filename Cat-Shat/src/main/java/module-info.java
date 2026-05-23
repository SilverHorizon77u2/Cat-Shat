module com.example.catshat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // This is the built-in Java SQL module, keep this!

    opens com.example.catshat to javafx.fxml;
    opens com.example.catshat.ui to javafx.fxml;

    exports com.example.catshat;
    exports com.example.catshat.ui;
}