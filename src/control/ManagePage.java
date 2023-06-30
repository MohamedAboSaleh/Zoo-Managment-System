package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Zoo;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManagePage implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
	@FXML
	private ComboBox<String> manageList;
	
    @FXML
    private Label profitLabel;

	@FXML
	private Button returnButton;

	@FXML
	private Button submitButton;

	@FXML
	void returnToMainPage(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		MainPageControl mainPageControl = new MainPageControl();
		try {
			mainPageControl.start(stage);//return to the previous page
		} catch (Exception e2) {
			Alert a1 = new Alert(AlertType.ERROR, e2.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void submit(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		
		if (manageList.getSelectionModel().getSelectedItem()==null) {//check if the user selected what he want to manage
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setContentText("you should select correct value");
			a.showAndWait();
		}
		else {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			String string=manageList.getSelectionModel().getSelectedItem();
			if(string.equals("Visitors")) {//if we want to manage visitors
				ManageVisitors manageVisitors=new ManageVisitors();
				try {
					manageVisitors.start(stage);//go to the manage visitors page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if(string.equals("Employees")) {//if we want to manage employees 
				EmployeeControl employeeControl=new EmployeeControl();
				try {
					employeeControl.start(stage);//go to the manage employees page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if(string.equals("Sections")) {//if we want to manage sections 
				ManageSectionsControl manageSectionsControl=new ManageSectionsControl();
				try {
					manageSectionsControl.start(stage);//go to the manage sections page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
				
			}
			if(string.equals("Snacks")) {//if we want to manage snacks
				ManageSnacksMainControler manageSnacks = new ManageSnacksMainControler();
				try {
					manageSnacks.start(stage);//go to the manage snacks spage
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if(string.equals("SnackBars")) {//if we want to manage snack bars
				ManageSnackBars manageSnackBars = new ManageSnackBars();
				try {
					manageSnackBars.start(stage);//go to the bars page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if(string.equals("Animals")) {//if we want to manage animals 
				ManageAnimals manageAnimals = new ManageAnimals();
				try {
					manageAnimals.start(stage);//go to the manage animals page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		manageList.getItems().removeAll(manageList.getItems());//clear the manage list
		if(Zoo.getLoggedUser().getKey().getKey().equals("admin")) {//if the user was admin he can manage every thing
			manageList.getItems().addAll("Visitors", "Employees", "Sections", "Snacks", "SnackBars", "Animals");
		}
		else {//if he was employee he can manage visitors, employees,snacks
			manageList.getItems().addAll("Visitors", "Employees", "Snacks");
		}
		profitLabel.setText(String.valueOf(Zoo.getInstance().checkTotalRevenue()));//set the revenue

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
		try {
			//load the ManagePage fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManagePage.fxml"));
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
			primaryStage.setTitle("Manage Page");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
