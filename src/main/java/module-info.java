module com.example.sistdistribuiteexamen {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sistdistribuiteexamen to javafx.fxml;
    exports com.example.sistdistribuiteexamen;
}