package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Model.Bird;
import Model.Mammal;
import Model.Reptile;
import Model.Section;
import Model.Zoo;
import Utils.AnimalFood;
import Utils.Gender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddAnimalControler implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;

	@FXML
	private Label firstTraitLabel;

	@FXML
	private Label secondTraitLabel;

	@FXML
	private TextField nameCombo;

	@FXML
	private DatePicker dateF;

	@FXML
	private ComboBox<Gender> genderCombo;

	@FXML
	private ComboBox<Section> sectionCombo;

	@FXML
	private ComboBox<Boolean> firstTraitCombo;

	@FXML
	private ComboBox<AnimalFood> foodCombo;

	@FXML
	private ComboBox<Boolean> secondTraitCombo;

	@FXML
	private Label titleLabel;

	@FXML
	void addAnimalButtonSelected(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();

		if (nameCombo.getText().isEmpty() || dateF.getValue() == null || genderCombo.getValue() == null
				|| sectionCombo.getValue() == null || firstTraitCombo.getValue() == null || foodCombo.getValue() == null
				|| secondTraitCombo.getValue() == null) {// check if one of the fields still empty
			Alert a = new Alert(AlertType.ERROR, "Some fields are missing!", ButtonType.OK);
			a.showAndWait();
		} else {
			if (dateF.getValue().isAfter(LocalDate.now()))// check the value of the date picker
			{
				Alert a = new Alert(AlertType.ERROR, "Date should be until " + LocalDate.now().toString(),
						ButtonType.OK);
				a.showAndWait();
			} else {
				if (ManageAnimals.animalFlag == 0) { // Bird
					// get all the values from the fields and declare a new instance of Bird
					Bird b = new Bird(nameCombo.getText(), dateF.getValue(), foodCombo.getValue(),
							genderCombo.getValue(), sectionCombo.getValue(), firstTraitCombo.getValue(),
							secondTraitCombo.getValue());
					if (Zoo.getInstance().addBirdById(b)) {// if the add method return true
						Alert a = new Alert(AlertType.CONFIRMATION, "bird added!", ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the updated zoo instance to the .ser file
						Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						ManageBirds manaBirds = new ManageBirds();
						try {
							manaBirds.start(stage);// return to the previous page
						} catch (Exception e) {
							Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
							a1.showAndWait();
						}
					} else {
						// if the add method return false the alert we appear
						Alert a = new Alert(AlertType.ERROR, "Couldn't add bird!", ButtonType.OK);
						a.showAndWait();
					}
				} else {
					if (ManageAnimals.animalFlag == 1) { // Mammals
						// declare a new instance of Mammal
						Mammal m = new Mammal(nameCombo.getText(), dateF.getValue(), foodCombo.getValue(),
								genderCombo.getValue(), sectionCombo.getValue(), firstTraitCombo.getValue(),
								secondTraitCombo.getValue());
						if (Zoo.getInstance().addMammalById(m)) {// if the add method return true
							Alert a = new Alert(AlertType.CONFIRMATION, "mammal added!", ButtonType.OK);
							a.showAndWait();
							Zoo.getInstance().saveToSerFile();// save the updated zoo instance to the .ser file
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							ManageMammals manaMammals = new ManageMammals();
							try {
								manaMammals.start(stage);// return to the previous page
							} catch (Exception e) {
								Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
								a1.showAndWait();
							}

						} else {// if the add method return false
							Alert a = new Alert(AlertType.ERROR, "Couldn't add mammal!", ButtonType.OK);
							a.showAndWait();
						}
					} else { // Reptiles
						// declare a new instance of Reptile
						Reptile r = new Reptile(nameCombo.getText(), dateF.getValue(), foodCombo.getValue(),
								genderCombo.getValue(), sectionCombo.getValue(), firstTraitCombo.getValue(),
								secondTraitCombo.getValue());
						if (Zoo.getInstance().addReptileById(r)) {// if the add method return true
							Alert a = new Alert(AlertType.CONFIRMATION, "reptile added!", ButtonType.OK);
							a.showAndWait();
							Zoo.getInstance().saveToSerFile();// save the updated zoo instance to the .ser file
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							ManageReptiles manaReptiles = new ManageReptiles();
							try {
								manaReptiles.start(stage);// return to the previous page
							} catch (Exception e) {
								Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
								a1.showAndWait();
							}
						} else {
							Alert a = new Alert(AlertType.ERROR, "Couldn't add reptile!", ButtonType.OK);
							a.showAndWait();
						}
					}
				}
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
		// return to the previous page according to the page we are in now
		if (ManageAnimals.animalFlag == 0) {// if we were in the Add Bird page we return Manage Birds page
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			ManageBirds manaBirds = new ManageBirds();
			try {
				manaBirds.start(stage);
			} catch (Exception e) {
				Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				a.showAndWait();
			}
		} else {
			if (ManageAnimals.animalFlag == 1) {// if we were in the Add Mammal page we return to the Manage Mammal page
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				ManageMammals manaMammals = new ManageMammals();
				try {
					manaMammals.start(stage);
				} catch (Exception e) {
					Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a.showAndWait();
				}
			} else {// if we were in the Add Reptile page we return to the Manage Reptiles page
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				ManageReptiles manaReptiles = new ManageReptiles();
				try {
					manaReptiles.start(stage);
				} catch (Exception e) {
					Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a.showAndWait();
				}
			}
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dateF.setValue(LocalDate.now());// set the date in the date picker to be today's date
		genderCombo.getItems().addAll(Gender.Female, Gender.Male, Gender.Unknown);// put all the Gender values to the
																					// gender combo box
		foodCombo.getItems().addAll(AnimalFood.values());// put all the Animal Food values to the animal food combo box
		// set those two combo boxes to be boolean
		firstTraitCombo.getItems().addAll(true, false);
		secondTraitCombo.getItems().addAll(true, false);
		sectionCombo.getItems().addAll(Zoo.getInstance().getSections().values());// put all the sections in the section
																					// combo box
		// set the label according to the animal we want to add
		if (ManageAnimals.animalFlag == 0) {
			titleLabel.setText("Add Birds");
			firstTraitLabel.setText("canFly");
			secondTraitLabel.setText("canTakePic");
		} else {
			if (ManageAnimals.animalFlag == 1) {
				titleLabel.setText("Add Mammals");
				firstTraitLabel.setText("meatEater");
				secondTraitLabel.setText("canTakePic");
			} else {
				titleLabel.setText("Add Reptiles");
				firstTraitLabel.setText("isDangerous");
				secondTraitLabel.setText("seasonSleep");
			}
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
//load the Add animal fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddAnimal.fxml"));
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
			// set the stage title
			if (ManageAnimals.animalFlag == 0)
				stage.setTitle("Add Birds");
			else if (ManageAnimals.animalFlag == 1)
				stage.setTitle("Add Mammals");
			else
				stage.setTitle("Add Reptiles");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
