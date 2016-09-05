package sample;

import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Created by shestakov.aa on 21.07.2016.
 */
public class ControllerControl implements Initializable {



    
    public TextField TelefonControl;
    public TextField PortControl;
    public TextField IpControl;
    public TextField MessControl;
    public TextField TimeControl;
    public Button OkControl;
    public Button CanselControl;
    public AnchorPane ApHomeControl;
    public CheckBox DefaultCheckControl;
    private String DefaultMess = "CODE 0C A00000000000000000000000000000000000000000000000000000000";
    private String mess;
    private String time;
    private String IpAddr;
    private String PortAddr;
    private String Telefon;

    public void conBack(ActionEvent actionEvent) throws IOException {

    }

    public ControllerControl() {
        System.out.println("first");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("third");

        IpControl.textProperty().addListener(new MaxLeingthTextFiekd(IpControl,15));
        PortControl.textProperty().addListener(new MaxLeingthTextFiekd(PortControl,5));
        TelefonControl.textProperty().addListener(new MaxLeingthTextFiekd(TelefonControl,11));
        TimeControl.textProperty().addListener(new MaxLeingthTextFiekd(TimeControl,3));
        MessControl.textProperty().addListener(new MaxLeingthTextFiekd(MessControl,90));


    }


    public void ClickOk(ActionEvent actionEvent) {
        GenerMess();
        setTime(TimeControl.getText());
        setIpAddr(IpControl.getText());
        setPortAddr(PortControl.getText());
        setTelefon(TelefonControl.getText());
        System.out.println("mess= "+getMess()
        +" | time= "+getTime()+"min"
        + "| ip = "+ getIpAddr()
        +" | port= "+ getPortAddr()
        +" | telefon= "+getTelefon());
        Stage stage = (Stage) OkControl.getScene().getWindow();
        stage.close();
    }

    private void GenerMess() {
        if (DefaultCheckControl.isSelected()){
            mess = "imei="+TelefonControl.getText() + "&rms="+DefaultMess +"\0";
        }
        else {
            mess = "imei="+TelefonControl.getText() + "&rms="+MessControl.getText() +"\0";
        }
        //System.out.println(mess);
    }

    public void ClickCansel(ActionEvent actionEvent) {
        Stage stage = (Stage) CanselControl.getScene().getWindow();
        stage.close();
    }

    public void ClickDefault(ActionEvent actionEvent) {
        if(DefaultCheckControl.isSelected())
        {
            MessControl.setDisable(true);
        }
        else
        {
            MessControl.setDisable(false);
        }
    }

    public double getTime() {
        return Double.parseDouble(time);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getIpAddr() {
        return IpAddr;
    }

    public void setIpAddr(String ipAddr) {
        IpAddr = ipAddr;
    }

    public String getPortAddr() {
        return PortAddr;
    }

    public void setPortAddr(String portAddr) {
        PortAddr = portAddr;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }
}
