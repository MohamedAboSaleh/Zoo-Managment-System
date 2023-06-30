package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Model.Bird;
import Model.Section;
import Model.Zoo;
import Utils.AnimalFood;
import Utils.Gender;
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

public class ManageBirds implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<Bird> birds;

	@FXML
	private TableColumn<Bird, String> fName;

	@FXML
	private TableColumn<Bird, LocalDate> birthday;

	@FXML
	private TableColumn<Bird, Gender> gender;

	@FXML
	private TableColumn<Bird, AnimalFood> food;

	@FXML
	private TableColumn<Bird, Section> section;

	@FXML
	private TableColumn<Bird, Integer> visitCount;

	@FXML
	private TableColumn<Bird, Boolean> firstTrait;

	@FXML
	private TableColumn<Bird, Boolean> secondTrait;

	@FXML
	void addBirdButtonSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddAnimalControler addAnim = new AddAnimalControler();
		try {
			addAnim.start(stage);// go to the add animal page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void removeBirdButtonSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (birds.getSelectionModel().isEmpty() == true) {// check if the user didn't select any bird
			Alert a = new Alert(AlertType.ERROR, "Select bird to remove", ButtonType.OK);
			a.showAndWait();
		} else {
			Bird b = birds.getSelectionModel().getSelectedItem();
			if (Zoo.getInstance().removeBird(b)) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "bird was removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to .ser file
				initialize(null, null);
			} else {
				Alert a = new Alert(AlertType.ERROR, "didn't remove bird", ButtonType.OK);
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
		ManageAnimals managePage = new ManageAnimals();
		try {
			managePage.start(stage);// return to the manage page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set what is the value of each field
		fName.setCellValueFactory(new PropertyValueFactory<>("name"));
		birthday.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		food.setCellValueFactory(new PropertyValueFactory<>("food"));
		visitCount.setCellValueFactory(new PropertyValueFactory<>("visitCounter"));
		gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		firstTrait.setCellValueFactory(new PropertyValueFactory<>("canFly"));
		secondTrait.setCellValueFactory(new PropertyValueFactory<>("canTakePic"));
		firstTrait.setText("canFly");
		secondTrait.setText("canTakePic");// get all the birds
		birds.setItems(FXCollections.observableArrayList(new ArrayList<>(Zoo.getInstance().getBirds().values())));
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
			// load the ManageBirds fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageBirds.fxml"));
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
			stage.setTitle("Manage Birds");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
