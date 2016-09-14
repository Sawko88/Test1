package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;


public class Controller implements Initializable {

    public ToggleButton ControlBut;
    public AnchorPane home;
    public Label LbControlServer;
    public Label lbControlTime;
    public Label lbControlState;
    public Label lbControlLastSend;
    public TextArea taErrorMess;
    ControllerControl Control1;
    public String ControlMess="";
    public double ControlTime = 1.0;
    public String ControlIp="192.168.51.71";
    public String ControlPort = "6009";
    public boolean ControlFlagMess= true;

    private boolean controlTimerMessFlag = false;

    private boolean shutdownErrorThread = false;
    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());


    private String ControlTelefon = "79816902221";
    private String ControlMessToServer;
    volatile  boolean shutdown = true;
    MySocketClient.ErrorType ErrorControl;

    private MySocketClient sockketControl;
    MySocketClient.ComplectType Type;

    public File fileIni;
    public File fileLogControl;
    public File fileLogSattelit;
    public File fileLogMagick;

    public void ClearLogChat(ActionEvent event) {
        taErrorMess.clear();
    }

    public void Connect(ActionEvent actionEvent) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Control.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Control1 = loader.getController();
        Control1.setFormControl(ControlPort, ControlMess, ControlTelefon, ControlIp, ControlFlagMess, ControlTime);
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Control");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
        if (Control1.isOkClick()) {
            ControlMess = Control1.getMess();
            ControlTime = Control1.getTime();
            lbControlTime.setText(String.valueOf(ControlTime) + " мин");
            ControlIp = Control1.getIpAddr();
            ControlPort = Control1.getPortAddr();
            LbControlServer.setText(ControlIp + ":" + ControlPort);
            ControlTelefon = Control1.getTelefon();
            ControlFlagMess = Control1.isFlagDefaultMess();
            System.out.println(ControlMess);
            ControlMessToServer = "imei=" + ControlTelefon + "&rmc=" + ControlMess;
            sockketControl.setParam(ControlIp, ControlPort, ControlTime, ControlMess, ControlTelefon);
        }

    }



    public void ControlStart(ActionEvent actionEvent) {

        if (ControlBut.isSelected()){
            shutdown= false;
            sockketControl.startSendMess();
            System.out.println("StartControl");
            ControlBut.setText("OFF");



        }
        else {
            System.out.println("StopControl");

            shutdown = true;
            sockketControl.stopSendMess();

            lbControlState.setText("--");
            lbControlState.setTextFill(Color.BLACK);
            ControlBut.setText("ON");
        }
    }



    Thread thread= new Thread() {
        @Override
        public void run(){

            while(!shutdownErrorThread){
                if (!shutdown) {
                    if(sockketControl.getNewEvent()) {
                        ErrorControl = sockketControl.getError();
                        switch (ErrorControl){
                            case NO_ERROR:
                                //lbControlState.setText("OK");
                                //lbControlState.setTextFill(Color.GREEN);
                                setState(sockketControl.complect, "OK");
                                break;
                            case ERROR_DISCONECT:
                                setState(sockketControl.complect, "ERROR_DISCONECT");
                                break;
                            case ERROR_MESS:
                                setState(sockketControl.complect, "ERROR_MESS");
                                break;
                            case ERROR_NO_ANSWER:
                                setState(sockketControl.complect, "ERROR_NO_ANSWER");
                                break;
                            default:break;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
            System.out.println("-----count close------");
        }

    };

    private void setState(MySocketClient.ComplectType complect, String ok) {
        Platform.runLater(()-> {
            String nameComplect;
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            /*SimpleDateFormat dateFile = new SimpleDateFormat("yyyy.MM.dd");
            Calendar calFile = Calendar.getInstance();*/
            switch (complect) {
                case CONTROL:
                    nameComplect = "Control";
                    lbControlLastSend.setText(dateFormat1.format(cal.getTime()));

                    if (ok.contains("OK")) {
                        lbControlState.setText(ok);
                        lbControlState.setTextFill(Color.GREEN);
                    } else {
                        lbControlState.setText(ok);
                        lbControlState.setTextFill(Color.RED);
                        taErrorMess.appendText(dateFormat1.format(cal.getTime()) + " " + nameComplect + " - " + ok + "\n\r");
                        taErrorMess.setStyle("-fx-text-fill: red;");
                        addLog(complect, dateFormat1.format(cal.getTime()) + " " + nameComplect + " - " + ok + "\n\r");
                    }
                    break;
                case SATELLIT:
                    break;
                case MAGICK:
                    break;
                default:
                    break;
            }
        });

    }

    private void addLog(MySocketClient.ComplectType complect, String s)  {
        SimpleDateFormat dateFile = new SimpleDateFormat("yyyy.MM.dd");
        Calendar calFile = Calendar.getInstance();
        switch (complect){

            case CONTROL:
                String pathControl = "Control\\"+dateFile.format(calFile.getTime())+".txt";
                fileLogControl = new File(pathControl);
                try {
                    fileLogControl.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileWriter fileWriter = new FileWriter(fileLogControl, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(s);

                    bufferedWriter.newLine();

                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case SATELLIT:
                break;
            case MAGICK:
                break;
            default:break;
        }
    }

    public void shutsown(){
        shutdown = true  ;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //setIsConnected(false);
        System.out.println("-----Init contrl------");

        sockketControl = new MySocketClient(ControlIp, ControlPort, ControlTime);
        sockketControl.setComplect(sockketControl.complect.CONTROL);

        thread.setDaemon(true);
        thread.start();


    }

    public void shutdownAllTread() {
        shutdownErrorThread= true;
        sockketControl.stopSendMess();
    }

}
