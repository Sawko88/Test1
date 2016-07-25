package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("TestSer");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaxWidth(300);
        primaryStage.setMaxHeight(200);
        primaryStage.setMinWidth(300);
        primaryStage.setMinHeight(200);
        primaryStage.show();
        //System.out.println(primaryStage.getScene().toString());
       // две письки ! были тут =)
    }


    public static void main(String[] args) {
        launch(args);
    }
}
