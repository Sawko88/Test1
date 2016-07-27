package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by shestakov.aa on 21.07.2016.
 */
public class ControllerControl implements Initializable {


    public AnchorPane ap1;

    public void conBack(ActionEvent actionEvent) throws IOException {
        //закрываем текущую форму
        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
        System.out.println("close Control");

        // запускаем следующую форму
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.setMaxWidth(300);
        app_stage.setMaxHeight(200);
        app_stage.setMinWidth(300);
        app_stage.setMinHeight(200);
        app_stage.show();
        System.out.println("load Controller");
        

    }

    public ControllerControl() {
        System.out.println("first");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("third");
        ap1.setVisible(false);
    }


}
