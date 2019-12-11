package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = ClassLoader.getSystemResource("sample.fxml");
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("MyChat");
        primaryStage.setScene(new Scene(root, 640, 420));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
