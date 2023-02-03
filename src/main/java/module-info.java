module com.example.cpuschedulersimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cpuschedulersimulator to javafx.fxml;
    exports com.example.cpuschedulersimulator;
    exports com.example.cpuschedulersimulator.process;
    opens com.example.cpuschedulersimulator.process to javafx.fxml;
    exports com.example.cpuschedulersimulator.process.ui;
    opens com.example.cpuschedulersimulator.process.ui to javafx.fxml;
}