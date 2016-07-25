package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by shestakov.aa on 21.07.2016.
 */
public class ControllerControl {
    public void contBtn(ActionEvent actionEvent) throws IOException {
        Parent home_page_parent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene home_page_scene = new Scene(home_page_parent);
        Stage app_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        app_stage.setScene(home_page_scene);
        app_stage.setMaxWidth(300);
        app_stage.setMaxHeight(200);
        app_stage.setMinWidth(300);
        app_stage.setMinHeight(200);

        app_stage.show();
    }
}
