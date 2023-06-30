package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Drink;
import Model.Food;
import Model.Snack;
import Model.SnackBar;
import Model.Zoo;
import Utils.SnackType;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddSnackControler implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private Label addSnackLabel;

	@FXML
	private ComboBox<Boolean> sFattening;

	@FXML
	private ComboBox<Boolean> firstTrait;

	@FXML
	private ComboBox<SnackType> sType;

	@FXML
	private ComboBox<SnackBar> sBar;

	@FXML
	private TextField sPrice;

	@FXML
	private TextField sName;

	@FXML
	private ComboBox<Boolean> secondTrait;

	@FXML
	private ComboBox<Boolean> thirdTrait;

	@FXML
	private Label firstTraitLabel;

	@FXML
	private Label secondTraitLabel;

	@FXML
	private Label thirdTraitLabel;

	@FXML
	void addSnackSelected(ActionEvent event) {
		// play the button pressed sounds
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		// check if some of the fields still empty
		if (sFattening.getValue() == null || firstTrait.getValue() == null || sType.getValue() == null
				|| sBar.getValue() == null || secondTrait.getValue() == null || thirdTrait.getValue() == null
				|| sPrice.getText().isEmpty() || sName.getText().isEmpty()) {
			Alert a = new Alert(AlertType.ERROR, "Some Firleds Are Missing!!", ButtonType.OK);
			a.showAndWait();
		} else {
			double doubleFlag = 0;
			try {
				doubleFlag = Double.parseDouble(sPrice.getText());// if the value of the price field is not integer this
																	// will throw an exception
				// define a new Drink
				Snack s = new Drink(SnackType.Drink, sName.getText(), sBar.getValue(), sFattening.getValue(),
						Double.parseDouble(sPrice.getText()), firstTrait.getValue(), secondTrait.getValue(),
						thirdTrait.getValue());
				if (Zoo.getInstance().addSnack(s)) {// if the add method return true
					Alert a = new Alert(AlertType.CONFIRMATION, "Drink added!", ButtonType.OK);
					a.showAndWait();
					Zoo.getInstance().saveToSerFile();// save all the date to the .ser file
					Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					ManageSnacks mSnacks = new ManageSnacks();
					try {
						mSnacks.start(stage);// return to the previous page
					} catch (Exception e) {
						Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a1.showAndWait();
					}
				} else {
					Alert a = new Alert(AlertType.ERROR, "Couldn't add Drink!", ButtonType.OK);
					a.showAndWait();
				}

			} catch (NumberFormatException numE) {// catch the exception if the value of the price field is not integer
				Alert a = new Alert(AlertType.ERROR, "The Price is not Double Value!", ButtonType.OK);
				a.showAndWait();
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
		ManageSnacks mSnacks = new ManageSnacks();
		try {
			mSnacks.start(stage);// return to the previous page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set those 4 combo boxes to be booleans
		sFattening.getItems().addAll(true, false);
		firstTrait.getItems().addAll(true, false);
		secondTrait.getItems().addAll(true, false);
		thirdTrait.getItems().addAll(true, false);
		sBar.getItems().addAll(Zoo.getInstance().getBars().values());
		// set the text in the labels
		firstTraitLabel.setText("sprinkel");
		secondTraitLabel.setText("straw");
		thirdTraitLabel.setText("iceCube");
		addSnackLabel.setText("Add Drink");

		sType.getItems().add(SnackType.Drink);// add the snack type to the combo box

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
			// load the AddSnack fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/AddSnack.fxml"));
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

			stage.setTitle("Add Drink");

			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
