package pl.edu.pg.eti.miss.dla;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {

        String fxmlFile = "/dla.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Diffusion-Limited Aggregation");
        stage.setResizable(false);

        stage.show();
    }
}