module io.github.mateuszlubian00.itemcompare {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;


    opens io.github.mateuszlubian00.itemcompare to javafx.fxml;
    exports io.github.mateuszlubian00.itemcompare;
    exports io.github.mateuszlubian00.itemcompare.model;
    exports io.github.mateuszlubian00.itemcompare.controller;
    opens io.github.mateuszlubian00.itemcompare.controller to javafx.fxml;
}