module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vpavlov to javafx.fxml;
    exports com.vpavlov;
    exports com.vpavlov.controller;
    opens com.vpavlov.controller to javafx.fxml;
}
