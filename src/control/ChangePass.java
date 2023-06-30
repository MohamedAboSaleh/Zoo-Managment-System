package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Zoo;
import Model.ZooEmployee;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class ChangePass {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;

	@FXML
	private PasswordField oldPass;

	@FXML
	private PasswordField newPass;

	@FXML
	private PasswordField confirmNewPass;

	@FXML
	private Button changeButton;

	@FXML
	private Button returnButton;

	@FXML
	void change(ActionEvent event) {

	}

	@FXML
	void returnToPage(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ProfilePage page = new ProfilePage();
		try {
			page.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void changePasswordNow(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		// check if some of the fields still empty
		if (oldPass.getText().isEmpty() && newPass.getText().isEmpty() && confirmNewPass.getText().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR, "Some Fields are Empty!", ButtonType.OK);
			a.showAndWait();
		} else {
			Pair<String, String> acc = Zoo.getLoggedUser().getKey();
			if (acc.getValue().equals(oldPass.getText()) == false) {// check if the old password is incorrect
				Alert a = new Alert(AlertType.ERROR, "Old password is incorrect!", ButtonType.OK);
				a.showAndWait();
			} else {
				if (newPass.getText().equals(confirmNewPass.getText()) == false) {// check if the confirmed passsword
																					// matches to the password
					Alert a = new Alert(AlertType.ERROR, "New password doesn't match!", ButtonType.OK);
					a.showAndWait();
				} else {
					// set the new password to the user
					Pair<String, String> tempAcc = Zoo.getLoggedUser().getKey();
					ZooEmployee TempEmp = Zoo.getLoggedUser().getValue();
					Pair<String, String> newAcc = new Pair<String, String>(tempAcc.getKey(), newPass.getText());
					Zoo.setLoggedUser(new Pair<>(newAcc, TempEmp));
					Zoo.getInstance().getUsers().remove(tempAcc);
					Zoo.addUser(newAcc, TempEmp);
					Alert a = new Alert(AlertType.CONFIRMATION, "Your Password Was Changed!", ButtonType.OK);
					a.showAndWait();
					Zoo.getInstance().saveToSerFile();// save the data to the.ser file
				}
			}
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
			// load the ChangePassPage fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ChangePassPage.fxml"));
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
			primaryStage.setTitle("Change Password");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
