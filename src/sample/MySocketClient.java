package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static sample.MySocketClient.ErrorType.*;

/**
 * Created by shestakov.aa on 12.09.2016.
 */
public class MySocketClient implements Initializable{

    public String Ip="192.168.51.71";
    public String port = "6009";
    public boolean ControlFlagMess= true;

    public Timeline timerMessDelay;
    private Timeline timerMessSend;
    private double timeDelayMess = 10.0;
    private double timeSendMess = 5.0;
    private boolean controlTimerMessFlag = false;
    private int controlCountDosconect = 0;
    public boolean shutdownAutoCinnect = false;
    private final static Logger LOGGER
            = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private boolean isNew = false;

    private boolean connected;
    public boolean isAutoConnected = false;
    private boolean isControlOn = false;
    private boolean ControlIsClose = false;

    private String telefon = "79811050470";
    private String ControlMessToServer;
    private String mess;
    private String messToServer;




    public enum ConnectionDisplayState {

        DISCONNECTED, ATTEMPTING, CONNECTED, AUTOCONNECTED, AUTOATTEMPTING
    }
    public enum ErrorType {
        ERROR_DISCONECT, ERROR_NO_ANSWER, ERROR_MESS, NO_ERROR
    }
    public ErrorType error = NO_ERROR;

    public enum ComplectType {
        CONTROL, SATELLIT, MAGICK
    }
    public ComplectType complect;

    private FxSocketClient socket;

    public void setParam(String controlIp, String controlPort, double controlTime, String controlMess, String controlTelefon) {
        this.Ip = controlIp;
        this.port = controlPort;
        this.timeSendMess = controlTime;
        this.mess = controlMess;
        this.telefon = controlTelefon;
        switch (complect){
            case CONTROL:
                this.messToServer = "imei=" + telefon + "&rmc=" + this.mess;
                break;
            case SATELLIT:
                break;
            case MAGICK:
                break;
            default:break;
        }
    }

    public void setError(ErrorType error) {
        this.error = error;
        isNew = true;
    }
    public ErrorType getError() {
        isNew = false;
        return error;
    }



    public boolean getNewEvent(){
        return isNew;
    }

    public MySocketClient(String controlIp, String controlPort, double controlTime) {
        this.Ip = controlIp;
        this.port = controlPort;
        this.timeSendMess = controlTime;
        System.out.println("-----Const MySocket------");
        setIsConnected(false);
        isAutoConnected = false;
        displayState(ConnectionDisplayState.DISCONNECTED);
        Runtime.getRuntime().addShutdownHook(new ShutDownThread());
        autoConnect();

    }

    public void startSendMess() {
        System.out.println("----Start Send-----");
        isAutoConnected = true;
        autoConnect();
        timerMessSend = new Timeline(new KeyFrame(Duration.minutes(timeSendMess), ae -> {
            SendMess();
        }));
        timerMessSend.setCycleCount(Animation.INDEFINITE);
        timerMessSend.play();
    }

    public void stopSendMess() {
        System.out.println("----Stop Send-----");
        isAutoConnected = false;
        timerMessSend.stop();
        socket.shutdown();
    }

    private void SendMess() {
        if (isConnected()) {
            socket.sendMessage(messToServer);
            TimerDelayMessStart();
        }
    }

    private void TimerDelayMessStart() {
        timerMessDelay = new Timeline(new KeyFrame(Duration.seconds(timeDelayMess), ae -> {
            TimerDelayMessFunc();
        }));
        timerMessDelay.setCycleCount(Animation.INDEFINITE);
        timerMessDelay.play();
    }

    private void TimerDelayMessFunc() {
        if (!controlTimerMessFlag)
        {
            setError(ERROR_NO_ANSWER);
            //error = ERROR_NO_ANSWTR;
        }

        TimerDelayMessStop();
    }

    private void TimerDelayMessStop() {
        timerMessDelay.stop();
    }

    public void setComplect(ComplectType complect) {
        this.complect = complect;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

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
        socket = new FxSocketClient(new MySocketClient.FxSocketListener(),
                Ip,
                Integer.valueOf(port),
                Constants.instance().DEBUG_ALL);
        socket.connect();
    }

    private void autoConnect() {
        new Thread() {
            @Override
            public void run() {
                while (isAutoConnected) {

                        if (!isConnected()) {
                            System.out.println("---autoConnect start---");
                            socket = new FxSocketClient(new FxSocketListener(),
                                    Ip,
                                    Integer.valueOf(port),
                                    Constants.instance().DEBUG_ALL);
                            socket.connect();
                        }
                        waitForDisconnect();
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {

                    }

                }
                System.out.println("---autoConnect close----");
            }
        }.start();
    }



    private void displayState(MySocketClient.ConnectionDisplayState state) {
        switch (state) {
            case DISCONNECTED:
                System.out.println("-----Socket state DISCONNECTED------");
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
                switch (complect) {
                    case CONTROL:
                        if (line.contains("ACK")) {
                            setError(NO_ERROR);
                            //error = NO_ERROR;
                        } else {
                            //error = ErrorType.ERROR_MESS;
                            setError(ERROR_MESS);
                        }
                        break;
                    case SATELLIT:
                        break;
                    case MAGICK:
                        break;
                    default:break;
                }
            }
        }

        @Override
        public void onClosedStatus(boolean isClosed) {
            if (isClosed) {

                ControlIsClose = true;

                    controlCountDosconect++;
                    if (controlCountDosconect == 3) {
                        //error = ErrorType.ERROR_DISCONECT;
                        setError(ERROR_DISCONECT);
                    }


                notifyDisconnected();
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOATTEMPTING);
                } else {
                    displayState(ConnectionDisplayState.DISCONNECTED);
                }
            } else {
                ControlIsClose = false;

                    controlCountDosconect = 0;
                    //error = NO_ERROR;
                    setError(NO_ERROR);


                setIsConnected(true);
                if (isAutoConnected) {
                    displayState(ConnectionDisplayState.AUTOCONNECTED);
                } else {
                    displayState(ConnectionDisplayState.CONNECTED);
                }
            }
        }

    }
}
