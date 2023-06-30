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
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PersonalInformation implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private Label fName;

	@FXML
	private Label lName;

	@FXML
	private Label birthday;

	@FXML
	private Label entered;

	@FXML
	private Label job;

	@FXML
	private Label section;

	@FXML
	private Label gender;

	@FXML
	private Button retrunButton;

	@FXML
	private Label firstName;

	@FXML
	private Label lastName;

	@FXML
	private Label birthDay;

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
			page.start(stage);// return to the profile page
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
			// load the PersonalInformation fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/PersonalInformation.fxml"));
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
			primaryStage.setTitle("Information");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set the value of each label
		ZooEmployee emp = Zoo.getLoggedUser().getValue();
		firstName.setText(emp.getFirstName());
		lastName.setText(emp.getLastName());
		birthDay.setText(emp.getBirthDay().toString());
		gender.setText(emp.getGender().toString());
		section.setText(emp.getSection().toString());
		job.setText(emp.getJob().toString());
	}
}
