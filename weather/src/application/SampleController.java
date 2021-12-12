package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import org.json.JSONObject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SampleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text cloud;

    @FXML
    private Text temp;

    @FXML
    private TextField field;

    @FXML
    private Text min_temp;

    @FXML
    private Text max_temp;

    @FXML
    private Button btn;

    @FXML
    private Text davl;

    @FXML
    void f8f8f8(ActionEvent event) {

    }

    @FXML
    void da1616(ActionEvent event) {

    }

    @FXML
    void initialize() {
        btn.setOnAction(event -> {
        	
        	String getUserCity = field.getText().trim();
        	if(!getUserCity.equals("")) {
	        	String output = getUrlContent(
	        			"https://api.openweathermap.org/data/2.5/weather?q=" + getUserCity +"&appid=7111009c12c5e424e8f02a4a7643e51d&units=metric&lang=ru");
	        	if(!output.isEmpty()) {
	        		JSONObject obj = new JSONObject(output);
	        		temp.setText("Температура: "+ Math.round(obj.getJSONObject("main").getDouble("temp"))+ " °C");
	        		cloud.setText("Облачность: "+ obj.getJSONArray("weather").getJSONObject(0).getString("description"));
	        		max_temp.setText("Максимум: "+ Math.round(obj.getJSONObject("main").getDouble("temp_max"))+ " °C");
	        		min_temp.setText("Минимум: "+ Math.round(obj.getJSONObject("main").getDouble("temp_min"))+ " °C");
	        		davl.setText("Давление: "+ Math.round(obj.getJSONObject("main").getDouble("pressure")));
	        	}
        	}
        });


    }
    
    private static String getUrlContent(String urlAdress) {
    	StringBuffer content = new StringBuffer();
    	
    	try{
    		URL url = new URL(urlAdress);
    		URLConnection urlConn = url.openConnection();
    		
    		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
    		String line;
    		
    		while((line = bufferedReader.readLine()) != null) {
    			content.append(line + "\n");
    		}
    		bufferedReader.close();
    		
    	} catch(Exception e) {
    		System.out.println("Такого города не найдено!");
    	}
    	return content.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
