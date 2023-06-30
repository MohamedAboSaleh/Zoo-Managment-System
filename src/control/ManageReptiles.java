package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Mammal;
import Model.Reptile;
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

public class ManageReptiles implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<Reptile> reptiles;

	@FXML
	private TableColumn<Reptile, String> fName;

	@FXML
	private TableColumn<Reptile, LocalDate> birthday;

	@FXML
	private TableColumn<Reptile, Gender> gender;

	@FXML
	private TableColumn<Reptile, AnimalFood> food;

	@FXML
	private TableColumn<Reptile, Section> section;

	@FXML
	private TableColumn<Reptile, Integer> visitCount;

	@FXML
	private TableColumn<Reptile, Boolean> firstTrait;

	@FXML
	private TableColumn<Reptile, Boolean> secondTrait;

	@FXML
	void addReptileButtonSelected(MouseEvent event) {
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
	void removeReptileButtonSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (reptiles.getSelectionModel().isEmpty() == true) {// if the user didn't select any reptile
			Alert a = new Alert(AlertType.ERROR, "Select reptile to remove", ButtonType.OK);
			a.showAndWait();
		} else {
			// get the selected reptile
			Reptile r = reptiles.getSelectionModel().getSelectedItem();
			if (Zoo.getInstance().removeReptile(r)) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "reptile was removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				initialize(null, null);
			} else {
				Alert a = new Alert(AlertType.ERROR, "didn't remove reptile", ButtonType.OK);
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
			managePage.start(stage);// return to the previous page
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
		firstTrait.setCellValueFactory(new PropertyValueFactory<>("dangerous"));
		secondTrait.setCellValueFactory(new PropertyValueFactory<>("seasonSleep"));
		firstTrait.setText("dangerous");
		secondTrait.setText("seasonSleep");
		// get all the reptiles
		reptiles.setItems(FXCollections.observableArrayList(new ArrayList<>(Zoo.getInstance().getReptiles().values())));

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
			// load the ManageReptiles fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageReptiles.fxml"));
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
			stage.setTitle("Manage Reptiles");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
