package control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Model.SnackBar;
import Model.Zoo;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageSnackBars implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<SnackBar> snackBars;

	@FXML
	private TableColumn<SnackBar, String> name;

	@FXML
	private TableColumn<SnackBar, String> section;

	@FXML
	private TableColumn<SnackBar, Double> profit;

	@FXML
	private TableColumn<SnackBar, Double> zooPercentage;

	@FXML
	void addSnackBarSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddSnackBarControler addSnackBar = new AddSnackBarControler();
		try {
			addSnackBar.start(stage);// go to add snack page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	@FXML
	void removeSnackBarSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (snackBars.getSelectionModel().isEmpty() == true) {// if the user didn't select snack bar
			Alert a = new Alert(AlertType.ERROR, "Select SnackBar to be removed please!", ButtonType.OK);
			a.showAndWait();
		} else {
			SnackBar sb = snackBars.getSelectionModel().getSelectedItem();
			if (Zoo.getInstance().removeSnackBar(sb)) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "SnackBar Removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				initialize(null, null);// reinitialize the page
			} else {
				Alert a = new Alert(AlertType.ERROR, "Something wen't wrong!", ButtonType.OK);
				a.showAndWait();
			}

		}
	}

	@FXML
	void returnToPage(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManagePage managePage = new ManagePage();
		try {
			managePage.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set the value for each field
		name.setCellValueFactory(new PropertyValueFactory<>("barName"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		profit.setCellValueFactory(new PropertyValueFactory<>("profit"));
		zooPercentage.setCellValueFactory(new PropertyValueFactory<>("zooPercentage"));
		//set all the snack bars
		snackBars.setItems(
				FXCollections.observableArrayList(new ArrayList<SnackBar>(Zoo.getInstance().getBars().values())));

	}

	public void start(Stage stage) {
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
			// load the ManageSnackBar fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageSnackBar.fxml"));
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
			stage.setTitle("Manage SnackBars");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
