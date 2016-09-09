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
import javafx.scene.paint.Color;
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
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
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
    public double ControlTime = 5.0;
    public String ControlIp="192.168.51.71";
    public String ControlPort = "6009";
    public boolean ControlFlagMess= true;
    public Timeline TimelineControl;
    private Timeline ControlTimerMess;
    private double ControlTimerDelayMess = 2.0;
    private boolean controlTimerMessFlag = false;
    private boolean controlIsSendMess = false;

    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private ObservableList<String> rcvdMsgsData;
    private ObservableList<String> sentMsgsData;
    private ListView lastSelectedListView;

    private boolean connected;
    private volatile boolean isAutoConnected = false;
    private boolean isControlOn = false;
    private boolean ControlIsClose = false;

    private static final int DEFAULT_RETRY_INTERVAL = 10000; // in milliseconds
    private String ControlTelefon = "79816902221";
    private String ControlMessToServer;

    public enum ConnectionDisplayState {

        DISCONNECTED, ATTEMPTING, CONNECTED, AUTOCONNECTED, AUTOATTEMPTING
    }

    private FxSocketClient socket;

    private synchronized void waitForDisconnect() {
        while (connected) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    private synchronized void notifyDisconnected() {
        connected = false;
        notifyAll();
    }

    private synchronized void setIsConnected(boolean connected) {
        this.connected = connected;
    }

    private synchronized boolean isConnected() {
        return (connected);
    }

    private void connect() {
        socket = new FxSocketClient(new FxSocketListener(),
                ControlIp,
                Integer.valueOf(ControlPort),
                Constants.instance().DEBUG_ALL);
        socket.connect();
    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (isAutoConnected) {
                        if (!isConnected()) {
                            socket = new FxSocketClient(new FxSocketListener(),
                                    ControlIp,
                                    Integer.valueOf(ControlPort),
                                    Constants.instance().DEBUG_ALL);
                            socket.connect();
                        }
                        waitForDisconnect();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                        }
                    }

                }
            }
        }.start();
    }



    private void displayState(ConnectionDisplayState state) {
        switch (state) {
            case DISCONNECTED:
                System.out.println("-----Socket state DISCONNECTED------");

                //TimelineControl.stop();
                break;
            case ATTEMPTING:
                System.out.println("-----Socket state ATTEMPTING------");
            case AUTOATTEMPTING:
                System.out.println("-----Socket state AUTOATTEMPTING------");
                break;
            case CONNECTED:
                System.out.println("-----Socket state CONNECTED------");
                break;
            case AUTOCONNECTED:
                System.out.println("-----Socket state AUTOCONNECTED------");
                break;
        }
    }



    public void Connect(ActionEvent actionEvent) throws IOException {


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("Control.fxml"));
        //Control1.setTelefon(ControlTelefon);
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
        }

    }
    public void ControlTimerMessStart(){
        ControlTimerMess = new Timeline(new KeyFrame(Duration.seconds(ControlTimerDelayMess), ae -> {
            ControlTimerMessFunc();
        }));
        ControlTimerMess.setCycleCount(Animation.INDEFINITE);
        ControlTimerMess.play();
    }

    public void AddMessToTa(String s)
    {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        lbControlLastSend.setText(dateFormat1.format(cal.getTime()));
        taErrorMess.appendText(dateFormat1.format(cal.getTime()) +" Control - "+s + "\n\r");
        taErrorMess.setStyle("-fx-text-fill: red;");
    }


    private void ControlTimerMessFunc() {
        if (!controlTimerMessFlag)
        {
            lbControlState.setText("ERROR:NO MESS");
            lbControlState.setTextFill(Color.RED);
            AddMessToTa("ERROR:NO MESS");

        }
        ControlTimerMessStop();
    }

    public void ControlTimerMessStop()
    {
        ControlTimerMess.stop();
    }


    public void ControlStart(ActionEvent actionEvent) {

        if (ControlBut.isSelected()){
            //connect();
            System.out.println("StartControl");
            isAutoConnected = true;
            isControlOn = true;

            TimelineControl = new Timeline(new KeyFrame(Duration.minutes(ControlTime), ae -> {
                ControlSendMess();
            }));
            TimelineControl.setCycleCount(Animation.INDEFINITE);
            TimelineControl.play();
        }
        else {
            System.out.println("StopControl");
            isAutoConnected = false;
            isControlOn = false;
            TimelineControl.stop();
            socket.shutdown();

            lbControlState.setText("--");
            lbControlState.setTextFill(Color.BLACK);
        }
    }

    private void ControlSendMess() {
        if(!ControlIsClose)
        {
            socket.sendMessage(ControlMessToServer);
            controlTimerMessFlag = false;
            ControlTimerMessStart();

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIsConnected(false);
        isAutoConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);

        sentMsgsData = FXCollections.observableArrayList();

        rcvdMsgsData = FXCollections.observableArrayList();


        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
        autoConnect();

    }

    class ShutDownThread extends Thread {

        @Override
        public void run() {
            if (socket != null) {
                if (socket.debugFlagIsSet(Constants.instance().DEBUG_STATUS)) {
                    LOGGER.info("ShutdownHook: Shutting down Server Socket");
                }
                socket.shutdown();
            }
        }
    }

    class FxSocketListener implements SocketListener {

        @Override
        public void onMessage(String line) {
            if (line != null && !line.equals("")) {
                controlTimerMessFlag = true;
               // rcvdMsgsData.add(line);
                if (line.contains("ACK")){
                    lbControlState.setText("OK");
                    lbControlState.setTextFill(Color.GREEN);

                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Calendar cal = Calendar.getInstance();
                    lbControlLastSend.setText(dateFormat1.format(cal.getTime()));
                    System.out.println(dateFormat1.format(cal.getTime()));

                }
                else {
                    lbControlState.setText("ERROR:MESS");
                    lbControlState.setTextFill(Color.RED);
                    AddMessToTa("ERROR:MESS");
                }
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {

                ControlIsClose = true;
                if (isControlOn)
                {
                    lbControlState.setText("ERROR:DISCONECT");
                    lbControlState.setTextFill(Color.RED);
                    AddMessToTa("ERROR:DISCONECT");
                }

                notifyDisconnected();
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOATTEMPTING);
                } else {
                    displayState(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
                ControlIsClose = false;
                if (isControlOn){
                    lbControlState.setText("OK");
                    lbControlState.setTextFill(Color.GREEN);
                }
                setIsConnected(true);
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOCONNECTED);
                } else {
                    displayState(ConnectionDisplayState.CONNECTED);
                }
            }
        }
    }

    @FXML
    private void handleClearRcvdMsgsButton(ActionEvent event) {
        rcvdMsgsData.clear();

    }

    @FXML
    private void handleClearSentMsgsButton(ActionEvent event) {
        sentMsgsData.clear();

    }

    @FXML
    private void handleSendMessageButton(ActionEvent event) {

            socket.sendMessage(ControlMess);
            sentMsgsData.add(ControlMess);

    }

    @FXML
    private void handleConnectButton(ActionEvent event) {
        displayState(ConnectionDisplayState.ATTEMPTING);
        connect();
    }

    @FXML
    private void handleDisconnectButton(ActionEvent event) {
        socket.shutdown();
    }

    @FXML
    private void handleAutoConnectCheckBox(ActionEvent event) {

    }

    @FXML
    private void handleRetryIntervalTextField(ActionEvent event) {

    }


}
