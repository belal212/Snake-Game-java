module com.example.demo6 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jlayer;



    exports com.example.demo6;
    opens com.example.demo6 to javafx.fxml;

    exports com.example.demo6.threads;
    opens com.example.demo6.threads to javafx.fxml;


    exports com.example.demo6.lvls;
    opens com.example.demo6.lvls to javafx.fxml;

    exports com.example.demo6.obstacles;
    opens com.example.demo6.obstacles to javafx.fxml;
    exports com.example.demo6.multiPLayer;
    opens com.example.demo6.multiPLayer to javafx.fxml;
}