module com.example.battleship_4x4 {
    requires javafx.controls;
    requires javafx.fxml;
            
                                requires com.almasb.fxgl.all;
    
    opens com.example.battleship_4x4 to javafx.fxml;
    exports com.example.battleship_4x4;
}