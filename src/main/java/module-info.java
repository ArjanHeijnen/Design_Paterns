module com.stendenstudenten.unogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.stendenstudenten.unogame to javafx.fxml;
    exports com.stendenstudenten.unogame;
    exports com.stendenstudenten.unogame.Controllers;
    opens com.stendenstudenten.unogame.Controllers to javafx.fxml;
}