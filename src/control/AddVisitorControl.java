package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import Exceptions.DiscountCheckException;
import Exceptions.DuplicatedValues;
import Model.Section;
import Model.Visitor;
import Model.Zoo;
import Model.ZooEmployee;
import Utils.Discount;
import Utils.Gender;
import Utils.TicketType;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddVisitorControl implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private Button addVisitor;

	@FXML
	private TextField fName;

	@FXML
	private TextField lName;

	@FXML
	private DatePicker date;

	@FXML
	private ComboBox<Gender> gender;

	@FXML
	private ComboBox<TicketType> ticketType;

	@FXML
	private ComboBox<Discount> discount;
	@FXML
	private ComboBox<Section> sections;

	@FXML
	void addVisitorButton(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		//check if some of the field still empty
		if (fName.getText().isEmpty() || lName.getText().isEmpty() || date.getValue() == null
				|| gender.getValue() == null || ticketType.getValue() == null || discount.getValue() == null
				|| sections.getValue() == null) {
			Alert a = new Alert(AlertType.ERROR, "Some fields are missing!", ButtonType.OK);
			a.showAndWait();
		} else {
			try {
				//check if the is an employee with the same details
				ArrayList<ZooEmployee> listOfEmployees = new ArrayList<ZooEmployee>(
						Zoo.getInstance().getEmployees().values());
				for (ZooEmployee emp : listOfEmployees) {
					if (emp.getFirstName().equals(fName.getText()) && emp.getLastName().equals(lName.getText())
							&& emp.getBirthDay().isEqual(date.getValue()))
						throw new DuplicatedValues();
				}
				if (discount.getValue().getPercentage() > 25) {//check if the value of the discount is <=25
					throw new DiscountCheckException();
				}
				if(date.getValue().isAfter(LocalDate.now())) {//check the date in the date piecker 
					Alert error = new Alert(AlertType.ERROR, "date must be until "+LocalDate.now(), ButtonType.OK);
					error.showAndWait();
					
				}else {
					//define a new visitor
					Visitor v = new Visitor(fName.getText(), lName.getText(), date.getValue(), gender.getValue(),
							ticketType.getValue(), discount.getValue());
					if (Zoo.getInstance().newVisitorInZoo(v, sections.getValue()) == true) {//if the add method return true
						Alert a = new Alert(AlertType.CONFIRMATION, "visitor has been added!", ButtonType.OK);
						a.showAndWait();
						Zoo.getInstance().saveToSerFile();//save all the data to the .ser file
						Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
						ManageVisitors manageVisitors = new ManageVisitors();
						try {
							manageVisitors.start(stage);//return to the previous page
						} catch (Exception e) {
							Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
							a1.showAndWait();
						}

					} else {
						Alert a = new Alert(AlertType.ERROR, "Something went wrong while adding the visitor",
								ButtonType.OK);
						a.showAndWait();
					}
				}
				//catch the exceptions 
			} catch (DuplicatedValues e) {
				Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
				a.showAndWait();
			} catch (DiscountCheckException e) {
				Alert a = new Alert(AlertType.ERROR,
						"cant be more then 25% discount your reach the max amount", ButtonType.OK);
				a.showAndWait();
			}
		}
	}

	@FXML
	void cancel(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageVisitors manageVisitors = new ManageVisitors();
		try {
			manageVisitors.start(stage);//return to the previous page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	public void start(Stage primaryStage) {
		//set the scene width and height 
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
			//load the AddVisitor fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddVisitor.fxml"));
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
			primaryStage.setTitle("Add Visitor");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		date.setValue(LocalDate.now());//set the date in the date picker to be today's date
		gender.getItems().removeAll(gender.getItems());
		gender.getItems().addAll(Gender.Female, Gender.Male, Gender.Unknown);//add all the genders to the gender combo box
		ticketType.getItems().removeAll(ticketType.getItems());
		ticketType.getItems().addAll(TicketType.Adult, TicketType.Child, TicketType.EmployeeRelative, TicketType.Old,
				TicketType.Soldiar, TicketType.Student);//add all the ticket types to the ticket type cpmbo box
		discount.getItems().removeAll(discount.getItems());
		discount.getItems().addAll(Discount.Hever, Discount.Haifa_Student, Discount.Beyahad, Discount.Behatzdaa,
				Discount.Max, Discount.Kranot, Discount.No_Discount);//add all the discount values to the discount cobo box
		sections.getItems().addAll(Zoo.getInstance().getSections().values());//add all the sections to the section combo box

	}
}
