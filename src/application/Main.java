package application;
	
import java.nio.file.Paths;

import Model.SnackBar;
import Model.Zoo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class Main extends Application {
	 int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
	
	
	private static Zoo zoo = Zoo.getInstance();
		
	@Override
	public void start(Stage primaryStage) {
		music();
		Zoo.getInstance().getDataFromSerFile();
		// set the scene width and height
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
			// load the LoginPage fxml file
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
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.setTitle("Log in");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	MediaPlayer mp;
	public void music() {
		String path = "/vedios/JungleSounds.mp3";
		Media m = new Media(getClass().getResource(path).toExternalForm());
		mp = new MediaPlayer(m);
		mp.setCycleCount(MediaPlayer.INDEFINITE);
		mp.play();
	}
	
	
	public static void main(String[] args) {
		SnackBar.setZooPercentage(0.2);
		launch(args);
	}

	public static Zoo getZoo() {
		return zoo;
	}
}
///design and sound effects