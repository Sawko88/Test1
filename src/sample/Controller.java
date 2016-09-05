package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.stage.Window;
import javafx.util.Duration;

import javax.naming.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ToggleButton ControlBut;
    public AnchorPane home;
    ControllerControl Control1;
    public String ControlMess = "";
    public double ControlTime = 1.0;
    public String ControlIp;
    public String ControlPort;


    public void Connect(ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Control.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Control1 = loader.getController();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Control");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        ControlMess = Control1.getMess();
        ControlTime = Control1.getTime();
        ControlIp = Control1.getIpAddr();
        ControlPort = Control1.getPortAddr();
        System.out.println(ControlMess);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("start Controller");

        //TimelineControl.setCycleCount(Timeline.INDEFINITE);
    }

    private void ControlSendMess() throws IOException {
        System.out.println(ControlMess);
        /*try {



            Socket socket = new Socket(ControlIp, Integer.parseInt(ControlPort));
            PrintStream out = new PrintStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(ControlMess);
            out.println();

            String line = in.readLine();
            while (line != null) {
                System.out.println(line);
                line = in.readLine();
            }
            in.close();
            out.close();
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/
    }

    public void ControlStart(ActionEvent actionEvent) {
        Timeline TimelineControl = new Timeline(new KeyFrame(Duration.seconds(ControlTime), ae -> {
            try {
                ControlSendMess();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        TimelineControl.setCycleCount(Animation.INDEFINITE);
        if (ControlBut.isSelected()){
            System.out.println("StartControl");
            TimelineControl.play();
        }
        else {
            System.out.println("StopControl");
            TimelineControl.stop();
        }
    }
}
