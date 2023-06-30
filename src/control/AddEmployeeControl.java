package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import Model.Section;
import Model.Visitor;
import Model.Zoo;
import Model.ZooEmployee;
import Utils.Gender;
import Utils.Job;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class AddEmployeeControl implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TextField fName;

	@FXML
	private TextField lName;

	@FXML
	private DatePicker date;

	@FXML
	private ComboBox<Gender> gender;

	@FXML
	private ComboBox<Section> section;

	@FXML
	private ComboBox<Job> job;

	@FXML
	private TextField userName;

	@FXML
	private PasswordField pass;

	@FXML
	private PasswordField confirmPass;

	private int userExistFlag = 0;

	@FXML
	void addEmp(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		// check if one of the fields is empty
		if (fName.getText().isEmpty() || lName.getText().isEmpty() || date.getValue() == null
				|| gender.getValue() == null || section.getValue() == null || job.getValue() == null
				|| userName.getText() == null || pass.getText().isEmpty() || confirmPass.getText().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR, "Some fields are missing!", ButtonType.OK);
			a.showAndWait();
		} else {
			for (Pair<String, String> user : Zoo.getInstance().getUsers().keySet()) {// check if the user name already
																						// taken
				if (user.getKey().equals(userName.getText())) {
					Alert a = new Alert(AlertType.ERROR, "username is taken!", ButtonType.OK);
					a.showAndWait();
					userExistFlag = 1;
					break;
				}
			}
			if (userExistFlag == 0) {
				// check if the employee is 18 at least
				LocalDate tempDate = LocalDate.of(LocalDate.now().getYear() - 18, LocalDate.now().getMonth(),
						LocalDate.now().getDayOfMonth());
				if (date.getValue().isAfter(tempDate)) {
					Alert error = new Alert(AlertType.ERROR,
							"employee should be 18 at least, date must be until " + tempDate, ButtonType.OK);
					error.showAndWait();
				} else {
					if (pass.getText().equals(confirmPass.getText())) {// if the password and the confirmed password are
																		// the same
						// define a new employee
						ZooEmployee emp = new ZooEmployee(fName.getText(), lName.getText(), date.getValue(),
								gender.getValue(), section.getValue(), job.getValue());
						if (Zoo.getInstance().addEmployee(emp)) {// if the add method return true
							Alert a = new Alert(AlertType.CONFIRMATION, "employee has been added!", ButtonType.OK);
							a.showAndWait();
							// save the employee as a user so he can log in to the system
							Pair<String, String> pp = new Pair<>(userName.getText(), pass.getText());
							Zoo.addUser(pp, emp);
							Zoo.getInstance().saveToSerFile();// save the data in the .ser file
							Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
							EmployeeControl employeeControl = new EmployeeControl();
							try {
								employeeControl.start(stage);// return to the previous page
							} catch (Exception e) {
								Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
								a1.showAndWait();
							}
						} else {
							Alert a = new Alert(AlertType.ERROR, "Something went wrong while adding the employee",
									ButtonType.OK);
							a.showAndWait();
						}
					} else {
						Alert a = new Alert(AlertType.ERROR, "Password doesn't match!", ButtonType.OK);
						a.showAndWait();
					}
				}
			} else {
				userExistFlag = 0;
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
		EmployeeControl employeeControl = new EmployeeControl();
		try {
			employeeControl.start(stage);// return to the previous page
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
			// load the Add add employee fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddEmployee.fxml"));
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
		date.setValue(LocalDate.now());// set the date in the date picker to be today's date
		gender.getItems().addAll(Gender.Female, Gender.Male, Gender.Unknown);// add all the Gender values
		job.getItems().addAll(Job.values());// add all the jobs
		if (Zoo.getLoggedUser().getKey().getKey().equals("admin")) {
			// if the user was admin he can add employee to any section he want
			section.getItems().addAll(Zoo.getInstance().getSections().values());
		} else {
			// if he was employee he can add employee just to his sections
			section.getItems().addAll(Zoo.getLoggedUser().getValue().getSection());
		}

	}

}
