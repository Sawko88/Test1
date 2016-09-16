package sample;

import com.sun.deploy.util.ArrayUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import javax.xml.bind.DatatypeConverter;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Created by shestakov.aa on 16.09.2016.
 */
public class SatellitControler implements Initializable{
    public CheckBox DefaultCheckSatellit;
    public TextField TelefonSatellit;
    public TextField PortSatellit;
    public TextField IpSatellit;
    public TextField MessSatellit;
    public TextField TimeSatellit;
    public Button OkSatellit;
    public Button CanselSatellit;
    public TextField PassSatellit;
    final int MAX_MESS = 80;
    final int MAX_DATA = 64;
    byte header = (byte) 0xAC;
    byte size = 0x50;
    byte sourse = 0x02;
    byte dest = 0x08;
    byte chanel = (byte) 0x84;
    private byte[] code = new byte[] {0x00, 0x00};
    byte[] arg1 = new byte[] {0x00, 0x00};
    byte[] arg2 = new byte[] {0x00, 0x00};
    byte[] arg3 = new byte[] {0x00, 0x00};
    private byte[] pass = new byte[] {0x00, 0x00};
    private byte cs = 0x00;
    private byte[] siganl = new byte[2];
    byte[] data = new byte[MAX_DATA];
    private byte[] messAunti = new byte[MAX_MESS];
    private byte[] messCom = new byte[MAX_MESS];
    public String telefon;
    private String messA;
    private String messC;

    String msg = "AC5002088407000000000000000000052B373931313739323531363900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getMessAunti();
        getMessA();
        getMessCom();
        getMessC();
    }

    public void ClickDefault(ActionEvent event) {
    }

    public void ClickOk(ActionEvent event) {
    }

    public void ClickCansel(ActionEvent event) {
    }

    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
    }

    public byte[] getPass() {
        return pass;
    }

    public void setPass(byte[] pass) {
        this.pass = pass;
    }

    public byte getCs() {
        return cs;
    }

    public void setCs(byte cs) {
        this.cs = cs;
    }

    public byte[] getMessAunti() {
        ByteBuffer target = ByteBuffer.wrap(messAunti);
        System.out.println("data dlina= " +String.valueOf(data.length));
        target.put(header);
        //messAunti[0] = header;
        target.put(size);// messAunti[1] = size;
        target.put(sourse);//messAunti[2] = sourse;
        target.put(dest);//messAunti[3] = dest;
        target.put(chanel);       // target.put(chanel);
        target.put(code);
        target.put(arg1);
        target.put(arg2);
        target.put(arg3);
        target.put(pass);
        target.put(cs);
        Arrays.fill(data, (byte) 0x00);
        telefon = TelefonSatellit.getText();
        data = telefon.getBytes();
        target.put(data);
        cs = 0x00;
        for (int i =0; i<messAunti.length; i++)
        {
            cs = (byte) (cs + messAunti[i]);
        }
        messAunti[15] = cs;
        System.out.println("messCom dlina= " +String.valueOf(messAunti.length));
        return messAunti;
    }

    public void setMessAunti(byte[] messAunti) {


        this.messAunti = messAunti;
    }

    public byte[] getMessCom() {
        ByteBuffer target = ByteBuffer.wrap(messCom);
        target.put(header);
        System.out.println("data dlina= " +String.valueOf(data.length));
        System.out.println("messCom dlina= " +String.valueOf(messCom.length));
        //messAunti[0] = header;
        target.put(size);// messAunti[1] = size;
        target.put(sourse);//messAunti[2] = sourse;
        target.put(dest);//messAunti[3] = dest;
        target.put(chanel);       // target.put(chanel);
        target.put(code);
        target.put(arg1);
        target.put(arg2);
        target.put(arg3);
        target.put(pass);
        target.put(cs);
        //Arrays.fill(data, (byte) 0x00);
        byte[] data1 = new byte[MAX_DATA];
        siganl = DatatypeConverter.parseHexBinary(MessSatellit.getText());
        data1[60] = siganl[0];
        //data1[57] = siganl[1];
        target.put(data1);


        cs = (byte) 0x00;
        for (int i =0; i<messCom.length; i++)
        {
            cs = (byte) (cs + messCom[i]);
        }
        messCom[15] = cs;
        return messCom;
    }

    public void setMessCom(byte[] messCom) {
        this.messCom = messCom;
    }

    public String getMessA() {

        messA = new String(messAunti, StandardCharsets.UTF_8);
        System.out.println("messA = "+messA);
       byte[] b  = new byte[80];
        b= DatatypeConverter.parseHexBinary(msg);
        return messA;
    }

    public void setMessA(String messA) {
        this.messA = messA;
    }

    public String getMessC() {
        messC = new String(messCom, StandardCharsets.UTF_8);
        System.out.println("messA = "+messC);
        return messC;
    }

    public void setMessC(String messC) {
        this.messC = messC;
    }

    public byte[] getSiganl() {
        return siganl;
    }

    public void setSiganl(byte[] siganl) {
        this.siganl = siganl;
    }
}
