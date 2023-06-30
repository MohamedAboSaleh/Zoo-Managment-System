package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Snack;
import Model.SnackBar;
import Model.Zoo;
import Utils.SnackType;
import javafx.collections.FXCollections;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class FindAllSnackByWorkerController implements Initializable{
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
    @FXML
    private TableView<Snack> snacks;

    @FXML
    private TableColumn<Snack, String> fName;

    @FXML
    private TableColumn<Snack, SnackType> snackType;

    @FXML
    private TableColumn<Snack, Boolean> fattening;

    @FXML
    private TableColumn<Snack, SnackBar> bar;

    @FXML
    private TableColumn<Snack, Double> price;

    @FXML
    private ComboBox<SnackBar> snackBarBox;
    
    @FXML
    void submitButtonSelected(ActionEvent event) {
    	//play the button pressed sound
    	String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
    	if(snackBarBox.getValue() == null) {
			Alert a = new Alert(AlertType.ERROR, "Select snackBar first!", ButtonType.OK);
			a.showAndWait();
    	}else {
    		//call the method and set all the snacks
    		snacks.setItems(FXCollections.observableArrayList(Zoo.getInstance().findAllSnackByWorker(snackBarBox.getValue())));
    	}
    }
	
	@FXML
	void returnToPage(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageQueries managePage = new ManageQueries();
		try {
			managePage.start(stage);//return to the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set what is the value of each field
		fName.setCellValueFactory(new PropertyValueFactory<>("snackName"));
		snackType.setCellValueFactory(new PropertyValueFactory<>("type"));
		fattening.setCellValueFactory(new PropertyValueFactory<>("Fattening"));
		bar.setCellValueFactory(new PropertyValueFactory<>("bar"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		snackBarBox.getItems().addAll(Zoo.getInstance().getBars().values());//add all the snack bars to the combo box
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
			//load the FindAllSnackByWorker fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/FindAllSnackByWorker.fxml"));
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
			stage.setTitle("Query 3");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
