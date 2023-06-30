package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageAnimals implements Initializable{
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
    @FXML
    private Button returnButton;

    @FXML
    private Button birdButton;

    @FXML
    private Button mammalsButton;

    @FXML
    private Button reptileButton;
    
    public static int animalFlag = 0;
    
	@FXML
	void returnToPage(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManagePage managePage = new ManagePage();
		try {
			managePage.start(stage);//return the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}
	
	@FXML
	void birdsSelected(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageBirds birds = new ManageBirds();
		animalFlag = 0;
		try {
			birds.start(stage);//go to the birds page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}
	
	@FXML
	void mammalsSelected(ActionEvent event){
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageMammals mammals = new ManageMammals();
		animalFlag = 1;

	
		try {
			mammals.start(stage);//go to the mammals page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}
	
	@FXML
	void reptilesSelected(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageReptiles reptiles = new ManageReptiles();
		animalFlag = 2;
		try {
			reptiles.start(stage);//go to the reptiles page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	public void start(Stage stage) {
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
		try {
			//load the ManageAnimals fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageAnimals.fxml"));
			Scene scene = new Scene(root);
			//change the stage position according to mouse drag
			scene.setOnMousePressed(m -> {
	            if (m.getButton() == MouseButton.PRIMARY) {
	                scene.setCursor(Cursor.MOVE);
	                initialX = (int) (stage.getX() - m.getScreenX());
	                initialY = (int) (stage.getY() - m.getScreenY());
	            }
	        });

	        scene.setOnMouseDragged(m -> {
	            if (m.getButton() == MouseButton.PRIMARY) {
	            	stage.setX(m.getScreenX() + initialX);
	            	stage.setY(m.getScreenY() + initialY);
	            }
	        });

	        scene.setOnMouseReleased(m -> {
	            scene.setCursor(Cursor.DEFAULT);
	        });
			stage.setScene(scene);
			stage.setTitle("Manage Animals");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
