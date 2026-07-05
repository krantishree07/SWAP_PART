package com.example.swapmart;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
// done  //  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sellerxml/sellerview.fxml"));
// done  //  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userxml/userview.fxml"));//   FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("useranalysiscontrolxml/useranalysiscontrolview.fxml"));
// done // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("userrecordxml/userrecordview.fxml"));
 // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("buyerinterestxml/buyerinterestxml.fxml"));
// FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("securedealxml/securedealview.fxml"));
// done // FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("searchcontrolxml/searchcontrolview.fxml"));
// done //         FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("productrecordxml/productrecordcontrolview.fxml"));
  FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loginxml/loginview.fxml"));
// FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboardxml/dashboardview.fxml"));
// DONE //       FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dealrecordxml/dealrecordview.fxml"));
   //   FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("useranalysiscontrolxml/useranalysiscontrolview.fxml"));
// done// FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("interestrecordxml/interestrecordview.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 817, 641);
        stage.setTitle("Swap Mart");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
