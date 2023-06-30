package control;

import java.io.File;

import Model.Section;
import Model.Zoo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddSectionControl {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TextField name;

	@FXML
	private TextField capacity;

	@FXML
	void addSectionButton(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();

		if (name.getText().equals("") || capacity.getText().equals("")) {// if some of the fields still empty
			Alert a = new Alert(AlertType.ERROR, "Some fields are empty!", ButtonType.OK);
			a.showAndWait();
		} else {
			try {
				// check if the capacity field contains negative value
				if (Integer.parseInt(capacity.getText()) < 0) {// if the the capacity field was not integer this will
																// throw an exception and we catch it
					Alert a = new Alert(AlertType.ERROR, "Capacity should be a positive value",
							ButtonType.OK);
					a.showAndWait();
				} else {
					// define a new section
					Section section = new Section(name.getText(), Integer.parseInt(capacity.getText()));
					if (Zoo.getInstance().addSection(section)) {// if the add method return true
						Alert a = new Alert(AlertType.CONFIRMATION, "Section Added!", ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the date to the .ser file
						Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						ManageSectionsControl manageSectionsControl = new ManageSectionsControl();
						try {
							manageSectionsControl.start(stage);// return to the previous page
						} catch (Exception e) {
							Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
							a1.showAndWait();
						}
					} else {
						Alert a = new Alert(AlertType.ERROR, "Section wasn't added!Something is wrong!",
								ButtonType.OK);
						a.showAndWait();
					}
				}
			} catch (NumberFormatException e) {// catch the exception if the value of the capacity field was not integer
				Alert a = new Alert(AlertType.ERROR, "Capacity should be integer value", ButtonType.OK);
				a.showAndWait();
			}

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
		ManageSectionsControl manageSectionsControl = new ManageSectionsControl();
		try {
			manageSectionsControl.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	public void start(Stage primaryStage) {
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
			// load the Add Section fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddSection.fxml"));
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
			primaryStage.setTitle("Manage Secions");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
