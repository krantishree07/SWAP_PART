module com.example.swapmart {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    //requires mysql.connector.java;
    requires mysql.connector.j;
    requires jdk.jdi;
    requires java.desktop;

    opens com.example.swapmart to javafx.fxml;
    exports com.example.swapmart;

    exports com.example.swapmart.usercontrol;
    opens  com.example.swapmart.usercontrol;
    opens com.example.swapmart.userxml ;

    exports com.example.swapmart.sellercontrol;
    opens  com.example.swapmart.sellercontrol;
    opens com.example.swapmart.sellerxml ;

    exports com.example.swapmart.useranalysiscontrol;
    opens com.example.swapmart.useranalysiscontrol;
    opens com.example.swapmart.useranalysiscontrolxml;

    exports com.example.swapmart.centralhubcontrol;
    opens com.example.swapmart.centralhubcontrol;
    opens com.example.swapmart.centralhubxml;

    exports com.example.swapmart.userrecordcontrol;
    opens com.example.swapmart.userrecordcontrol;
    opens com.example.swapmart.userrecordxml;

    exports com.example.swapmart.buyerinterestcontrol;
    opens com.example.swapmart.buyerinterestcontrol;
    opens com.example.swapmart.buyerinterestxml;

    exports com.example.swapmart.securedealcontrol;
    opens com.example.swapmart.securedealcontrol;
    opens com.example.swapmart.securedealxml;

    exports com.example.swapmart.searchcontrol;
    opens com.example.swapmart.searchcontrol;
    opens com.example.swapmart.searchcontrolxml;

    exports com.example.swapmart.productrecordcontrol;
    opens com.example.swapmart.productrecordcontrol;
    opens com.example.swapmart.productrecordxml;

    exports com.example.swapmart.logincontrol;
    opens com.example.swapmart.logincontrol;
    opens com.example.swapmart.loginxml;

    exports com.example.swapmart.dashboardcontrol;
    opens com.example.swapmart.dashboardcontrol;
    opens com.example.swapmart.dashboardxml;

    exports com.example.swapmart.dealrecordcontrol;
    opens com.example.swapmart.dealrecordcontrol;
    opens com.example.swapmart.dealrecordxml;

    exports com.example.swapmart.interestrecordcontrol;
    opens com.example.swapmart.interestrecordcontrol;
    opens com.example.swapmart.interestrecordxml;





}
