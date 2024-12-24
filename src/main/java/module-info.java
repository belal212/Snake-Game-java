module com.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo6 to javafx.fxml;
    exports com.example.demo6;
    exports com.example.demo6.threads;
    opens com.example.demo6.threads to javafx.fxml;
}