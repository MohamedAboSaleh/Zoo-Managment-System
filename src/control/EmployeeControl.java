package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import Exceptions.AnimalsVisitsException;
import Exceptions.WrongZooEmployeeTypeException;
import Model.Animal;
import Model.Bird;
import Model.Mammal;
import Model.Section;
import Model.Visitor;
import Model.Zoo;
import Model.ZooEmployee;
import Utils.Gender;
import Utils.Job;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class EmployeeControl implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private ImageView treatAnimal;

	@FXML
	private TableView<ZooEmployee> employees;

	@FXML
	private TableColumn<ZooEmployee, String> fName;

	@FXML
	private TableColumn<ZooEmployee, String> lName;

	@FXML
	private TableColumn<ZooEmployee, LocalDate> date;

	@FXML
	private TableColumn<ZooEmployee, Gender> gender;

	@FXML
	private TableColumn<ZooEmployee, Section> section;

	@FXML
	private TableColumn<ZooEmployee, Job> job;

	@FXML
	private ComboBox<Section> chooseSec;

	@FXML
	private RadioButton add;

	@FXML
	private ToggleGroup a;

	@FXML
	private RadioButton remove;

	@FXML
	private ComboBox<Animal> choiceComboBox;

	@FXML
	private Button submitButton;

	@FXML
	private Label choiceLabel;

	@FXML
	void treatAnimalPressed(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		try {
			if (Zoo.getLoggedUser().getKey().getKey().equals("admin")) {// if the user was admin he should select
																		// employee first to treat animal
				if (employees.getSelectionModel().isEmpty() == true) {
					Alert a = new Alert(AlertType.ERROR, "Select employee first!", ButtonType.OK);
					a.showAndWait();
				} else {// if the user was employee he should be vet to treat animals
					if (!employees.getSelectionModel().getSelectedItem().getJob().equals(Job.Vet)) {
						Alert a = new Alert(AlertType.ERROR, "only Vet can treat Animal!", ButtonType.OK);
						a.showAndWait();
						throw new WrongZooEmployeeTypeException();

					} else {
						// if the selected employee was vet
						submitButton.setVisible(true);
						choiceComboBox.setVisible(true);
						choiceLabel.setVisible(true);
						choiceComboBox.getItems().clear();
						ArrayList<Animal> anmls = new ArrayList<>();// add the mammals and the birds to the combo box
																	// because reptiles don't implement AnimalVisits
																	// interface
						anmls.addAll(employees.getSelectionModel().getSelectedItem().getSection().getMammals());
						anmls.addAll(employees.getSelectionModel().getSelectedItem().getSection().getBirds());
						if (anmls.size() == 0) {
							Alert a = new Alert(AlertType.ERROR, "No Animals In This Section!",
									ButtonType.OK);
							a.showAndWait();
						} else
							choiceComboBox.getItems().addAll(anmls);
					}

				}
			} else {// if the user was employee he should be vet to treat animals

				if (!Zoo.getLoggedUser().getValue().getJob().equals(Job.Vet)) {
					Alert a = new Alert(AlertType.ERROR, "You are not vet, only Vet can treat Animal!",
							ButtonType.OK);
					a.showAndWait();
					throw new WrongZooEmployeeTypeException();

				} else {// if he is a vet
					submitButton.setVisible(true);
					choiceComboBox.setVisible(true);
					choiceLabel.setVisible(true);
					choiceComboBox.getItems().clear();
					ArrayList<Animal> anmls = new ArrayList<>();
					// add the mammals and the birds to the combo box
					// because reptiles don't implement AnimalVisits interface
					anmls.addAll(Zoo.getLoggedUser().getValue().getSection().getBirds());// get all the animals in the
																							// employee's section
					anmls.addAll(Zoo.getLoggedUser().getValue().getSection().getMammals());
					if (anmls.size() == 0) {// check if there is no animals in the employee section
						Alert a = new Alert(AlertType.ERROR, "No Animals In Ur Section!", ButtonType.OK);
						a.showAndWait();
					} else
						choiceComboBox.getItems().addAll(anmls);

				}

			}
		} catch (WrongZooEmployeeTypeException e) {

			initialize(null, null);

		}

	}

	@FXML
	void submitButtonPreesed(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (Zoo.getLoggedUser().getKey().getKey().equals("admin")) {// if the user was admin
			if (choiceComboBox.getValue() != null) {
				if (choiceComboBox.getValue() instanceof Bird) {// this means that he want to treat animal
					try {// call the method
						((Bird) choiceComboBox.getValue()).visitcount(employees.getSelectionModel().getSelectedItem());
						Alert a = new Alert(AlertType.CONFIRMATION, "Employee will treat this bird!",
								ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the data to the .ser file
					} catch (AnimalsVisitsException e) {
						Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a.showAndWait();
					}
				} else {
					try {// call the method
						((Mammal) choiceComboBox.getValue())
								.visitcount(employees.getSelectionModel().getSelectedItem());
						Alert a = new Alert(AlertType.CONFIRMATION, "Employee will treat this mammal!",
								ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the data to the .ser file
					} catch (AnimalsVisitsException e) {
						Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a.showAndWait();
					}
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR, "Select animal first", ButtonType.OK);
				alert.showAndWait();
			}
		} else {// if the user was employee
			if (choiceComboBox.getValue() != null) {
				if (choiceComboBox.getValue() instanceof Bird) {
					try {// call the method
						((Bird) choiceComboBox.getValue()).visitcount(Zoo.getLoggedUser().getValue());
						Alert a = new Alert(AlertType.CONFIRMATION, "You will treat this bird!",
								ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the data to the .ser file
					} catch (AnimalsVisitsException e) {
						Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a.showAndWait();
					}
				} else {
					try {// call the method
						((Mammal) choiceComboBox.getValue()).visitcount(Zoo.getLoggedUser().getValue());
						Alert a = new Alert(AlertType.CONFIRMATION, "You will treat this mammal!",
								ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();// save the data to the .ser file
					} catch (AnimalsVisitsException e) {
						Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a.showAndWait();
					}
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR, "Select animal first", ButtonType.OK);
				alert.showAndWait();
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
		ManagePage managePage = new ManagePage();
		try {
			managePage.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}

	}

	@FXML
	void addEmployee(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddEmployeeControl addEmployeeControl = new AddEmployeeControl();
		try {
			addEmployeeControl.start(stage);// go to the add employee page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void removeEmployee(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (employees.getSelectionModel().isEmpty() == true) {// check if we didn't select an employee
			Alert a = new Alert(AlertType.ERROR, "Select employee to remove", ButtonType.OK);
			a.showAndWait();
		} else {
			ZooEmployee e = employees.getSelectionModel().getSelectedItem();
			if (Zoo.getInstance().removeEmployee(e)) {// if the remove method return true
				Zoo.getInstance().getUsers().remove(getKey(Zoo.getInstance().getUsers(), e));// remove the deleted
																								// employee from the
																								// ueres
				Alert a = new Alert(AlertType.CONFIRMATION, "employee was removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				initialize(null, null);// reinitialize the page
			} else {
				Alert a = new Alert(AlertType.ERROR, "didn't remove employee", ButtonType.OK);
				a.showAndWait();
			}
		}
	}

	private static <K, V> K getKey(Map<K, V> map, V value) {// private method that return the key of v in the map
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
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
			// load the ManageEmployees fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageEmployees.fxml"));
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
			primaryStage.setTitle("Manage Employees");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		submitButton.setVisible(false);
		choiceComboBox.setVisible(false);
		choiceLabel.setVisible(false);
		// set what is the value of every field
		fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		date.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		job.setCellValueFactory(new PropertyValueFactory<>("job"));

		ObservableList<ZooEmployee> empZoo;
		if (Zoo.getLoggedUser().getKey().getKey().equals("admin")) {// if the user was admin we add all the zoo
																	// employees
			empZoo = FXCollections.observableArrayList(Zoo.getInstance().getEmployees().values());
			employees.setItems(empZoo);
		} else {// if he was employee we add all the employees in the same section as the logged
				// employee except him self
			Collection<ZooEmployee> zooTempEmp = Zoo.getInstance().getEmployees().values();
			ArrayList<ZooEmployee> zooEmp = new ArrayList<>(zooTempEmp);
			empZoo = FXCollections.observableArrayList();
			for (ZooEmployee e : zooEmp) {
				if (e.getSection().equals(Zoo.getLoggedUser().getValue().getSection())
						&& e.equals(Zoo.getLoggedUser().getValue()) == false)
					empZoo.add(e);
			}
			employees.setItems(empZoo);

		}
	}
}
