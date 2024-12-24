module com.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.demo6 to javafx.fxml;
    exports com.example.demo6;
    opens com.example.demo6.server to javafx.fxml;
    exports com.example.demo6.server;
    opens com.example.demo6.muliPlayer to javafx.fxml;
    exports com.example.demo6.muliPlayer;
}