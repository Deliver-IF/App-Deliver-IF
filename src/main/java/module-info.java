module com.deliverif.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.apache.commons.lang3;
    requires java.xml;


    opens com.deliverif.app to javafx.fxml;
    opens com.deliverif.app.controllers to javafx.fxml;
    exports com.deliverif.app;

}