package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by shestakov.aa on 21.07.2016.
 */
public class ControllerControl implements Initializable {


    public AnchorPane ap1;
    public Label labCL;
    int count = 0;

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


    public void conSerCl(ActionEvent actionEvent) {
        try {
            ap1.setVisible(true);

            //Socket socCl = new Socket(ipAdrCl.getText(), Integer.parseInt(portCl.getText().toString()));
            Socket socCl = new Socket("84.204.102.210", 6009);
            System.out.println("Socket conect");

            String mess = "imei=79811050470&rmc=CODE 0B A053847.000,A,5955.9634,N,03017.8931,E,0.00,166.49,230614\0";
            socCl.getOutputStream().write(mess.getBytes());

            byte buf[] = new byte[64 * 1024];
            int r = socCl.getInputStream().read(buf);
            String data = new String(buf, 0, r);
            labCL.setText(data+count);
            System.out.println(data+count);
            count++;
            socCl.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
}
