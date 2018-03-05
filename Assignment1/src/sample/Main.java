package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = (Parent)loader.load();

        Controller controller = (Controller) loader.getController();
        controller.setStage(primaryStage);

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("MayHam3000");

        primaryStage.setScene(scene);

        primaryStage.show();
    }




}
