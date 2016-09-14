package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;


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
    private boolean okClick = false;
    private boolean flagDefaultMess;

    UnaryOperator<TextFormatter.Change> filter = new UnaryOperator<TextFormatter.Change>() {


        @Override
        public TextFormatter.Change apply(TextFormatter.Change t) {



            if (t.isContentChange()){
                if (t.getText().matches("[^0-9]")){
                    t.setText("");
                }

            }

            return t;
        }
    };

    UnaryOperator<TextFormatter.Change> filterIp = new UnaryOperator<TextFormatter.Change>() {


        @Override
        public TextFormatter.Change apply(TextFormatter.Change t) {



            if (t.isContentChange()){
                if (t.getText().matches("[^0-9.]")){
                    t.setText("");
                }

            }

            return t;
        }
    };

    public void conBack(ActionEvent actionEvent) throws IOException {

    }

    public ControllerControl() {
        System.out.println("first");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("third");
        okClick = false;

        IpControl.textProperty().addListener(new MaxLeingthTextFiekd(IpControl, 15));
        PortControl.textProperty().addListener(new MaxLeingthTextFiekd(PortControl, 5));
        TelefonControl.textProperty().addListener(new MaxLeingthTextFiekd(TelefonControl, 11));
        TimeControl.textProperty().addListener(new MaxLeingthTextFiekd(TimeControl, 3));
        MessControl.textProperty().addListener(new MaxLeingthTextFiekd(MessControl, 90));



        PortControl.setTextFormatter(new TextFormatter<Object>(filter));
        TelefonControl.setTextFormatter(new TextFormatter<Object>(filter));

        TimeControl.setTextFormatter(new TextFormatter<Object>(filter));
        IpControl.setTextFormatter(new TextFormatter<Object>(filterIp));

    }




    public void ClickOk(ActionEvent actionEvent) {
        GenerMess();
        setTime(TimeControl.getText());
        setIpAddr(IpControl.getText());
        setPortAddr(PortControl.getText());
        setTelefon(TelefonControl.getText());
        System.out.println("mess= " + getMess()
                + " | time= " + getTime() + "min"
                + "| ip = " + getIpAddr()
                + " | port= " + getPortAddr()
                + " | telefon= " + getTelefon());
        okClick = true;
        Stage stage = (Stage) OkControl.getScene().getWindow();
        stage.close();
    }

    private void GenerMess() {
        if (DefaultCheckControl.isSelected()) {
            flagDefaultMess = true;
            //mess = "imei="+TelefonControl.getText() + "&rms="+DefaultMess;
            mess = DefaultMess;
        } else {
            //mess = "imei="+TelefonControl.getText() + "&rms="+MessControl.getText();
            flagDefaultMess = false;
            mess = MessControl.getText();
        }
        //System.out.println(mess);
    }

    public void ClickCansel(ActionEvent actionEvent)  {
        okClick = false;
        Stage stage = (Stage) CanselControl.getScene().getWindow();
        stage.close();
    }

    public void ClickDefault(ActionEvent actionEvent) {
        if (DefaultCheckControl.isSelected()) {
            MessControl.setDisable(true);
        } else {
            MessControl.setDisable(false);
        }
    }

    public double getTime() {
        if (Double.parseDouble(time) == 0.0)
        {
            return 1.0;
        }
        else {
            return Double.parseDouble(time);
        }
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

    public boolean isOkClick() {
        return okClick;
    }

    public void setOkClick(boolean okClick) {
        this.okClick = okClick;
    }


    public void setFormControl(String controlPort, String controlMess, String controlTelefon, String controlIp, boolean controlFlagMess, double controlTime) {
        PortControl.setText(controlPort);
        IpControl.setText(controlIp);
        mess = controlMess;
        TelefonControl.setText(controlTelefon);

        TimeControl.setText(String.valueOf((int) controlTime));
        DefaultCheckControl.setSelected(controlFlagMess);
        MessControl.setDisable(controlFlagMess);
        if(!controlFlagMess){
            MessControl.setText(controlMess);
        }
    }

    public boolean isFlagDefaultMess() {
        return flagDefaultMess;
    }

    public void setFlagDefaultMess(boolean flagDefaultMess) {
        this.flagDefaultMess = flagDefaultMess;
    }
}
