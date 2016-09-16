package sample;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XML11Serializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;


public class Main extends Application {
    public Parent root;
    Controller controller1;
    File fileIniControl;
    public String versia = "1.01";
    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("-----Start main------");
        fileIniControl = new File("ini.xml");


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("sample.fxml"));
        root = loader.load();
        controller1 = loader.getController();
        primaryStage.setTitle("TestSer "+versia);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        new File("Control").mkdir();
        new File("Sattelit").mkdir();
        new File("Magick").mkdir();

        try {
            if (fileIniControl.createNewFile()){
                System.out.println("-----create file------");
                createXMLFile("Control");

            }
            else {
                LoadParamXML();
                System.out.println("-----no create file------");

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void init(){

        System.out.println("-----Init contrl------");

    }

    private void LoadParamXML() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileIniControl);
            //Node complects = doc.getFirstChild();
            Node complectControl = doc.getElementsByTagName("complect").item(0);
            NodeList list = complectControl.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);

                if ("IP".equals(node.getNodeName())) {
                    controller1.ControlIp = node.getTextContent();
                }
                if ("Port".equals(node.getNodeName())) {
                    controller1.ControlPort = node.getTextContent();//node.setTextContent(controller1.ControlPort);
                }
                if ("Mess".equals(node.getNodeName())) {
                    controller1.ControlMess = node.getTextContent();//node.setTextContent(controller1.ControlMess);
                }
                if ("Time".equals(node.getNodeName())) {
                    controller1.ControlTime = Double.parseDouble(node.getTextContent());//node.setTextContent(String.valueOf(controller1.ControlTime));
                }

            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createXMLFile(String complectName) throws ParserConfigurationException, IOException {
        DocumentBuilderFactory docFacktory = DocumentBuilderFactory.newInstance();
        DocumentBuilder doccBuilder = docFacktory.newDocumentBuilder();
        Document xmlDoc = doccBuilder.newDocument();
        Element complectsElement = xmlDoc.createElement("complects");
        Element complectElement;
        Element complectNameElement;
        Element complectIp;
        Element complectPort;
        Element complectMess;
        Element complectTime;
        for (int i =0; i<3; i++)
        {
            complectElement = xmlDoc.createElement("complect");
            complectNameElement = xmlDoc.createElement("name");
            complectElement.appendChild(complectNameElement);
            complectIp = xmlDoc.createElement("IP");
            complectElement.appendChild(complectIp);
            complectPort = xmlDoc.createElement("Port");
            complectElement.appendChild(complectPort);
            complectMess = xmlDoc.createElement("Mess");
            complectElement.appendChild(complectMess);
            complectTime = xmlDoc.createElement("Time");
            complectElement.appendChild(complectTime);
            complectsElement.appendChild(complectElement);

        }

        xmlDoc.appendChild(complectsElement);

        OutputFormat outputFormat = new OutputFormat(xmlDoc);
        outputFormat.setIndenting(true);
        FileOutputStream outStream = new FileOutputStream(fileIniControl);
        XML11Serializer serializer = new XML11Serializer(outStream, outputFormat);
        serializer.serialize(xmlDoc);
        outStream.close();

    }

    @Override
    public void stop(){

        try {

            SaveParamXML();
            System.out.println("-----Stop contrl------");
            controller1.shutdownAllTread();
        }catch (RuntimeException e)
        {}
    }

    private void SaveParamXML() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(fileIniControl);
            //Node complects = doc.getFirstChild();
            Node complectControl = doc.getElementsByTagName("complect").item(0);
            NodeList list = complectControl.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if ("name".equals(node.getNodeName())) {
                    node.setTextContent("Control");
                }
                if ("IP".equals(node.getNodeName())) {
                    node.setTextContent(controller1.ControlIp);
                }
                if ("Port".equals(node.getNodeName())) {
                    node.setTextContent(controller1.ControlPort);
                }
                if ("Mess".equals(node.getNodeName())) {
                    node.setTextContent(controller1.ControlMess);
                }
                if ("Time".equals(node.getNodeName())) {
                    node.setTextContent(String.valueOf(controller1.ControlTime));
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fileIniControl);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
