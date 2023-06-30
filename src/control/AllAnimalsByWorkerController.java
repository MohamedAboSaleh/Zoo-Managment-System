package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Model.Animal;
import Model.Section;
import Model.Zoo;
import Model.ZooEmployee;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AllAnimalsByWorkerController implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<Animal> animals;

	@FXML
	private TableColumn<Animal, String> fName;

	@FXML
	private TableColumn<Animal, LocalDate> date;

	@FXML
	private TableColumn<Animal, Gender> gender;

	@FXML
	private TableColumn<Animal, Integer> visitCount;

	@FXML
	private TableColumn<Animal, Section> section;

	@FXML
	private ComboBox<ZooEmployee> employee;

	@FXML
	void submitButtonSelected(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (employee.getValue() == null) {// check if employee was already selected
			Alert a = new Alert(AlertType.ERROR, "Select employee first!", ButtonType.OK);
			a.showAndWait();
		} else {
			if (Zoo.getInstance().allAnimalsByWorker(employee.getValue()) != null)// call the method and add the animals
				animals.setItems(
						FXCollections.observableArrayList(Zoo.getInstance().allAnimalsByWorker(employee.getValue())));
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
		ManageQueries managePage = new ManageQueries();
		try {
			managePage.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set what value every field will contain
		fName.setCellValueFactory(new PropertyValueFactory<>("name"));
		date.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		visitCount.setCellValueFactory(new PropertyValueFactory<>("visitCounter"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		employee.getItems().addAll(Zoo.getInstance().getEmployees().values());// add all the employees to the employee
																				// combo box
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
			// load the AllAnimalsByWorker fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AllAnimalsByWorker.fxml"));
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
			stage.setTitle("Query 2");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
