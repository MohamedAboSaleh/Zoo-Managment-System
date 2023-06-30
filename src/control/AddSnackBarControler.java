package control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import Model.Section;
import Model.SnackBar;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddSnackBarControler implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TextField name;

	@FXML
	private ComboBox<Section> sectionBox;

	@FXML
	void addSnackBarSelected(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (sectionBox.getValue() != null && !name.getText().isEmpty()) {// if all the fields are filled
			// define a new snack bar
			SnackBar sb = new SnackBar(name.getText(), sectionBox.getValue());
			if (Zoo.getInstance().addSnackBar(sb, sectionBox.getValue())) {// if the add method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "SnackBar Added!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				ManageSnackBars mSnackBars = new ManageSnackBars();
				try {
					mSnackBars.start(stage);// return to the previous page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			} else {
				Alert a = new Alert(AlertType.ERROR, "Couldn't add SnackBar.", ButtonType.OK);
				a.showAndWait();
			}
		} else {
			Alert a = new Alert(AlertType.ERROR, "Some Fields are Missing!", ButtonType.OK);
			a.showAndWait();
		}
	}

	@FXML
	void cancel(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageSnackBars mSnackBars = new ManageSnackBars();
		try {
			mSnackBars.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sectionBox.getItems().clear();// clear the section combo box
		ArrayList<Section> sbs = new ArrayList<Section>(Zoo.getInstance().getSections().values());
		ArrayList<Section> sbsNew = new ArrayList<Section>();
		for (Section s : sbs) {// add all the sections that its bar is null
			if (s.getBar() == null)
				sbsNew.add(s);
		}
		sectionBox.getItems().addAll(sbsNew);
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
			// load the AddSnackBar fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddSnackBar.fxml"));
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
			stage.setTitle("Add SnackBars");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
