package main.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;


public class PriceCheckController {
    @FXML
    TextField urlTextField;

    @FXML
    TextArea contentTextField;

    @FXML
    Button downloadButton;

    public void initialize(){

    }


    public void downloadButtonClicked(ActionEvent actionEvent) {
        try {
            URL url = new URL("http://nokaut.io/api/v2/" + urlTextField.getText());
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Authorization", "Bearer 140075");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            InputStream in = connection.getInputStream();
            Scanner scanner = new Scanner(in);
            StringBuilder builder=new StringBuilder();
            while (scanner.hasNextLine()){
                builder.append(scanner.nextLine());
                builder.append("\n");
            }
            in.close();
            contentTextField.setText(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
