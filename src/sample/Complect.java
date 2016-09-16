package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.Timer;

/**
 * Created by shestakov.aa on 31.08.2016.
 */
public class Complect implements Initializable {
    public ToolBar ToolHome;
    public Label element;
    int count =0;
    String idComplect = "";
    Timer timer;
    Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Complect2");
        //timer = new Timer();
        //timer.schedule(new SayHello(),0,2000);
        timeline = new Timeline (
                new KeyFrame (
                        javafx.util.Duration.seconds(2), //1000 мс * 60 сек = 1 мин
                        ae -> doSomething()
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void doSomething() {
        System.out.println("Timer2");
    }

    private void SayHello() {
    }

    public Complect(){
        System.out.println("Complect1");
        //Button but1 = new Button();

    }



    public void DelTb(ActionEvent actionEvent) {
        ((VBox) ToolHome.getParent()).getChildren().remove(ToolHome);
        System.out.println("Delete element "+ idComplect);

    }


    public void SetParam(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Control.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        ControllerControl control = loader.getController();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Control");
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        //dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        dialogStage.showAndWait();



    }
}
