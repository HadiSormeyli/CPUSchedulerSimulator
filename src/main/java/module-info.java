module com.example.cpuschedulersimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cpuschedulersimulator to javafx.fxml;
    exports com.example.cpuschedulersimulator;
}