package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;

import java.io.IOException;

public class Controller {



    public void Connect(ActionEvent actionEvent) throws IOException {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("Control.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.setMaxHeight(400);
        app_stage.setMaxWidth(630);
        app_stage.setMinWidth(630);
        app_stage.setMinHeight(400);
        app_stage.setWidth(630);
        app_stage.setHeight(400);
        app_stage.show();
    }
}
