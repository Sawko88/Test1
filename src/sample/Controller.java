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
import java.lang.invoke.MethodHandles;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class Controller implements Initializable {

    public ToggleButton ControlBut;
    public AnchorPane home;
    ControllerControl Control1;
    public String ControlMess;
    public double ControlTime = 1.0;
    public String ControlIp;
    public String ControlPort;
    public Timeline TimelineControl;

    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private ObservableList<String> rcvdMsgsData;
    private ObservableList<String> sentMsgsData;
    private ListView lastSelectedListView;

    private boolean connected;
    private volatile boolean isAutoConnected;

    private static final int DEFAULT_RETRY_INTERVAL = 2000; // in milliseconds
    private String ControlTelefon;
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
                while (isAutoConnected) {
                    if (!isConnected()) {
                        socket = new FxSocketClient(new FxSocketListener(),
                                ControlIp,
                                Integer.valueOf(ControlPort),
                                Constants.instance().DEBUG_NONE);
                        socket.connect();
                    }
                    waitForDisconnect();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        }.start();
    }

    private void displayState(ConnectionDisplayState state) {
        switch (state) {
            case DISCONNECTED:

                break;
            case ATTEMPTING:
            case AUTOATTEMPTING:

                break;
            case CONNECTED:

                break;
            case AUTOCONNECTED:

                break;
        }
    }



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
        ControlTelefon = Control1.getTelefon();
        System.out.println(ControlMess);
        ControlMessToServer = "imei="+ControlTelefon+"&rmc="+ControlMess;
        connect();
    }


    public void ControlStart(ActionEvent actionEvent) {

        if (ControlBut.isSelected()){

            System.out.println("StartControl");

            TimelineControl = new Timeline(new KeyFrame(Duration.minutes(ControlTime), ae -> {
                ControlSendMess();
            }));
            TimelineControl.setCycleCount(Animation.INDEFINITE);
            TimelineControl.play();
        }
        else {
            System.out.println("StopControl");
            TimelineControl.stop();
            socket.shutdown();
        }
    }

    private void ControlSendMess() {
        socket.sendMessage(ControlMessToServer);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setIsConnected(false);
        isAutoConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);

        sentMsgsData = FXCollections.observableArrayList();

        rcvdMsgsData = FXCollections.observableArrayList();


        Runtime.getRuntime().addShutdownHook(new ShutDownThread());

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
                rcvdMsgsData.add(line);
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {
                notifyDisconnected();
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOATTEMPTING);
                } else {
                    displayState(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
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
