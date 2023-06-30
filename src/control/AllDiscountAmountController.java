package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Model.Section;
import Model.Visitor;
import Model.Zoo;
import Utils.Discount;
import Utils.Gender;
import Utils.TicketType;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AllDiscountAmountController implements Initializable{
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	 int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	 int initialX;
	 int initialY;
    @FXML
    private TableView<Visitor> visitors;

    @FXML
    private TableColumn<Visitor, String> fName;

    @FXML
    private TableColumn<Visitor, String> LName;

    @FXML
    private TableColumn<Visitor, LocalDate> BirthDay;

    @FXML
    private TableColumn<Visitor, Gender> gender;

    @FXML
    private TableColumn<Visitor, Section> section;

    @FXML
    private TableColumn<Visitor, TicketType> ticketTypeV;

    @FXML
    private TableColumn<Visitor, Discount> discountV;
	
	@FXML
	void returnToPage(ActionEvent event) {
		//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManageQueries managePage = new ManageQueries();
		try {
			managePage.start(stage);//return to the previous page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set what value every field will contain
		fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		LName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		BirthDay.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		ticketTypeV.setCellValueFactory(new PropertyValueFactory<>("ticket"));
		discountV.setCellValueFactory(new PropertyValueFactory<>("discount"));
		//add all the visitors 
		visitors.setItems(FXCollections.observableArrayList(Zoo.getInstance().geAllDiscountAmount().keySet()));
		
	}

	public void start(Stage stage) {
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
			//load the AllDiscountAmount fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AllDiscountAmount.fxml"));
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
			stage.setTitle("Query 5");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
