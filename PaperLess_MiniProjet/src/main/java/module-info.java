module com.java.paperless_miniprojet {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;
    requires mysql.connector.j;

    opens com.java.paperless_miniprojet to javafx.fxml;
    opens com.java.paperless_miniprojet.controller to javafx.fxml;
    opens com.java.paperless_miniprojet.model to javafx.base, javafx.fxml;

    exports com.java.paperless_miniprojet;
}