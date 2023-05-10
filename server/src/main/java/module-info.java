module me.siyum.server {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens me.siyum.server to javafx.fxml;
    exports me.siyum.server;
}