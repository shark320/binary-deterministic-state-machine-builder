module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.vpavlov to javafx.fxml;
    exports com.vpavlov;
    exports com.vpavlov.visualization.controller;
    opens com.vpavlov.visualization.controller to javafx.fxml;
    exports com.vpavlov.visualization.machineBuilder.controller;
    opens com.vpavlov.visualization.machineBuilder.controller to javafx.fxml;
    exports com.vpavlov.visualization.selection_window.controller;
    opens com.vpavlov.visualization.selection_window.controller to javafx.fxml;
}
