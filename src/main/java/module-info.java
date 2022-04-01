module com.stendenstudenten.unogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stendenstudenten.unogame to javafx.fxml;
    exports com.stendenstudenten.unogame;
    exports com.stendenstudenten.unogame.controllers;
    exports com.stendenstudenten.unogame.card;
    opens com.stendenstudenten.unogame.controllers to javafx.fxml;
}