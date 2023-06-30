package control;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import Model.Section;
import Model.Zoo;
import Utils.MyFileLogWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageSectionsControl implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<Section> sections;

	@FXML
	private TableColumn<Section, String> name;

	@FXML
	private TableColumn<Section, String> bar;

	@FXML
	private TableColumn<Section, Integer> maxCapacity;

	@FXML
	private TableColumn<Section, Double> profit;

	@FXML
	private ComboBox<Section> newSecLabel;

	@FXML
	private Button submit;

	@FXML
	private Label askLabel;
	@FXML
	private Button details;

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
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void addSectionButton(MouseEvent event) {
		// play the button pressed page
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddSectionControl addSectionControl = new AddSectionControl();
		try {
			addSectionControl.start(stage);// go to the add section page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void submitButtonPressed(ActionEvent event) {
		// play the button pressed page
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (newSecLabel.getValue() != null) {// if we selected new section
			Section oldSec = sections.getSelectionModel().getSelectedItem();// get th selected section to be removed
			Section newSec = newSecLabel.getValue();// get the new section
			if (Zoo.getInstance().removeSection(oldSec, newSec)) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "Section Was Removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the file
				initialize(null, null);
			} else {
				Alert a = new Alert(AlertType.ERROR, "Faild TO Remove This Section!", ButtonType.OK);
				a.showAndWait();
				initialize(null, null);
			}
		} else {
			Alert a = new Alert(AlertType.ERROR, "Pick new section!", ButtonType.OK);
			a.showAndWait();
		}

	}

	@FXML
	void removeSectionButton(MouseEvent event) {
		// play the button pressed page
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (sections.getSelectionModel().isEmpty() == true) {// if the user didn't select any section
			Alert a = new Alert(AlertType.ERROR, "Select section to be removed please!", ButtonType.OK);
			a.showAndWait();
		} else {

			submit.setVisible(true);
			newSecLabel.setVisible(true);
			askLabel.setVisible(true);
			details.setVisible(false);
		}
	}

	@FXML
	void getDetails(ActionEvent event) {
		// play the button pressed page
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (sections.getSelectionModel().isEmpty()) {// check if the user didn't select any section
			Alert alert = new Alert(AlertType.ERROR, "You have to select a section first", ButtonType.OK);
			alert.showAndWait();
		} else {
			MyFileLogWriter fileLogWriter = new MyFileLogWriter();
			// Create file with the section name
			File file = new File(sections.getSelectionModel().getSelectedItem().getSectionName() + ".txt");
			// initialize the filelogwriter with the section name
			MyFileLogWriter
					.initializeMyFileWriter(sections.getSelectionModel().getSelectedItem().getSectionName() + ".txt");
			sections.getSelectionModel().getSelectedItem().getSectionDetails();// call the function and the file will be
																				// updated
			MyFileLogWriter.saveLogFile();// save the file
			if (Desktop.isDesktopSupported()) {// check if the desktop is supported
				try {
					Desktop.getDesktop().open(file);// open the file
				} catch (IOException e) {
					Alert alert = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					alert.showAndWait();
				}

			} else {
				Alert alert = new Alert(AlertType.ERROR, "Awt Desktop is not supported!,cannot open file",
						ButtonType.OK);
				alert.showAndWait();
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
			// load the ManageSections fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageSections.fxml"));
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		// set the buttons and labels to be invisible
		submit.setVisible(false);
		newSecLabel.setVisible(false);
		askLabel.setVisible(false);
		details.setVisible(true);
		// set what is the value of each field
		name.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
		bar.setCellValueFactory(new PropertyValueFactory<>("bar"));
		maxCapacity.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
		profit.setCellValueFactory(new PropertyValueFactory<>("todayRevenue"));
		// add all the sections
		sections.setItems(
				FXCollections.observableArrayList(new ArrayList<Section>(Zoo.getInstance().getSections().values())));

		// the selected section to be removed will be removed from the new sections list
		sections.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				newSecLabel.getItems().clear();
				Collection<Section> obSections = Zoo.getInstance().getSections().values();
				ArrayList<Section> ss = new ArrayList<Section>(obSections);
				ss.remove(sections.getSelectionModel().getSelectedItem());
				newSecLabel.getItems().addAll(ss);
			}
		});

	}

}
