module com.example.pain_t {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.pain_t to javafx.fxml;
    exports com.example.pain_t;
}