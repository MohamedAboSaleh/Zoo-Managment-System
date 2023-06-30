package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Section;
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
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MaxVisitorsVSMaxWorkersController implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;

	@FXML
	private Label sectionID;

	@FXML
	private Label sectionName;

	@FXML
	private Button retrunButton;
	@FXML
	private Label sectionMaxCap;

	@FXML
	private Label sectionProfit;

	@FXML
	private Label sectionBar;

	@FXML
	void returnToPage(ActionEvent event) {

		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageQueries managePage = new ManageQueries();
		try {
			managePage.start(stage);// return to the manage page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Section s = Zoo.getInstance().getMaxVisitorsVSMaxWorkers();// get the section from the method
		if (s != null) {// if the method didnt return null then we set the section details
			sectionID.setText(String.valueOf(s.getId()));
			sectionName.setText(s.getSectionName());
			sectionMaxCap.setText(String.valueOf(s.getMaxCapacity()));
			sectionProfit.setText(String.valueOf(s.getTodayRevenue()));
			if (s.getBar() != null)// if the section has a bar
				sectionBar.setText(s.getBar().getBarName());
		}

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
			// load the MaxVisitorsVSMaxWorkers fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/MaxVisitorsVSMaxWorkers.fxml"));
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
			stage.setTitle("Query 6");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
