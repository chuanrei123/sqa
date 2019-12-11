package client;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

public class Main extends Application {

    Controller controller;
    FXMLLoader loader;
    String loginUsername;

    @Override
    public void start(Stage primaryStage) throws Exception{

        URL fxmlURL = ClassLoader.getSystemResource("sample.fxml");
        System.out.println(fxmlURL);
        loader = new FXMLLoader(fxmlURL);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("My Chat");
        primaryStage.setScene(scene);
        primaryStage.show();

        controller = loader.getController();
        controller.createLoginDialog();
        loginUsername = controller.getLoginUsername();
        System.out.println("Get " + loginUsername);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
