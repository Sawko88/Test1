package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Main extends Application {
    public Parent root;
    Controller controller1;
    File fileIniControl;
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("-----Start main------");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        root = loader.load();
        controller1 = loader.getController();
        primaryStage.setTitle("TestSer");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        new File("Control").mkdir();
        new File("Sattelit").mkdir();
        new File("Magick").mkdir();
        //File file = new File("control1\\Control.txt");
        //file = new File(String.format(file.getAbsolutePath()+"\\Control"));

       // new File("Control").mkdir();
       //file.createTempFile("xz",".txt", new File(file.getAbsolutePath()));

        /*System.out.println(file.getAbsolutePath());
        boolean newFile = file.createNewFile();
        if (newFile){System.out.println("-----create file------");}
        else {System.out.println("-----no create file------");}*/


    }
    @Override
    public void init(){

        System.out.println("-----Init contrl------");
        fileIniControl = new File("iniControl.txt");
        try {
            fileIniControl.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop(){
        try {
            FileWriter fileWriter = new FileWriter(fileIniControl);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(controller1.ControlIp+";");

            bufferedWriter.newLine();

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {


            System.out.println("-----Stop contrl------");
            controller1.shutdownAllTread();
        }catch (RuntimeException e)
        {}
    }


    public static void main(String[] args) {
        launch(args);
    }
}
