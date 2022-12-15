module com.example.testingjavafxia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.computerscienceia to javafx.fxml;
    exports com.example.computerscienceia;
}