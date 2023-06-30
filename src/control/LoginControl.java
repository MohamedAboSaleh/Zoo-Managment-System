
package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Zoo;
import Model.ZooEmployee;
import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;


public class LoginControl implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
	@FXML
	private TextField userNameText;

	@FXML
	private Button LoginButten;

	@FXML
	private PasswordField passField;

	@FXML
	private CheckBox showField;

	@FXML
	private TextField passText;
    @FXML
    private Button exitButton;

    @FXML
    void exit(ActionEvent event) {	
    	//close the stage and exit from the system
    	Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	stage.close();
    	System.exit(0);
    }
    
	@FXML
	void LoginButtenPressed(ActionEvent e) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();

		Pair<String, String> tempAcc = new Pair<>(userNameText.getText(), passField.getText());
		//validate user, if the username and password exists
		if(Main.getZoo().getUsers().containsKey(tempAcc)) {
			Zoo.setLoggedUser(new Pair<Pair<String,String>, ZooEmployee>(tempAcc, Main.getZoo().getUsers().get(tempAcc)));
			Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			MainPageControl mainPageControl = new MainPageControl();
			try {
				mainPageControl.start(stage);//go to the main page
			} catch (Exception e2) {
				Alert a1 = new Alert(AlertType.ERROR, e2.getMessage(), ButtonType.OK);
				a1.showAndWait();
			}
		}
		else {
			Alert a = new Alert(AlertType.ERROR, 
	                "Username or Password is incorrect!", 
	                ButtonType.OK);
			a.showAndWait();
		}
	}

	@FXML
	void showPass(ActionEvent event) {//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (showField.isSelected()) {//if we want to show the password
			passText.setText(passField.getText());//get the text from the password field and set it to the password text field
			passField.setVisible(false);//set the pass field to be invisible
			passText.setVisible(true);//set the text field to be visible
		} else {
			//if we don't want
			passField.setText(passText.getText());//get the text from the password text field and set it to the password field
			passField.setVisible(true);//set the pass field to be visible
			passText.setVisible(false);//set the text field to be invisible
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		passText.setVisible(false);//set the pass field to be visible
		passField.setVisible(true);//set the text field to be invisible
	}
	public void start(Stage primaryStage) {
		//set the scene width and height 
		int sceneWidth = 0;
        int sceneHeight = 0;
        if (screenWidth <= 800 && screenHeight <= 600) {
            sceneWidth = 600;
            sceneHeight = 350;
        } else if (screenWidth <= 1280 && screenHeight <= 768) {
            sceneWidth = 800;
            sceneHeight = 450;
        } else if (screenWidth <= 1920 && screenHeight <= 1080) {
            sceneWidth = 1000;
            sceneHeight = 650;
        }
		try {//load the LoginPage fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
			Scene scene = new Scene(root);
			//change the stage position according to mouse drag
			scene.setOnMousePressed(m -> {
	            if (m.getButton() == MouseButton.PRIMARY) {
	                scene.setCursor(Cursor.MOVE);
	                initialX = (int) (primaryStage.getX() - m.getScreenX());
	                initialY = (int) (primaryStage.getY() - m.getScreenY());
	            }
	        });

	        scene.setOnMouseDragged(m -> {
	            if (m.getButton() == MouseButton.PRIMARY) {
	            	primaryStage.setX(m.getScreenX() + initialX);
	            	primaryStage.setY(m.getScreenY() + initialY);
	            }
	        });

	        scene.setOnMouseReleased(m -> {
	            scene.setCursor(Cursor.DEFAULT);
	        });
			primaryStage.setScene(scene);
			primaryStage.setTitle("Log in");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

