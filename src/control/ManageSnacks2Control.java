package control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Model.Drink;
import Model.Snack;
import Model.Zoo;
import control.ManageSnacksMainControler;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageSnacks2Control implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private Label snackLabel;

	@FXML
	private TableView<Snack> snacks;

	@FXML
	private TableColumn<Snack, String> fName;

	@FXML
	private TableColumn<Snack, String> snackType;

	@FXML
	private TableColumn<Snack, String> bar;

	@FXML
	private TableColumn<Snack, Boolean> fattening;

	@FXML
	private TableColumn<Snack, Double> price;

	@FXML
	private TableColumn<Snack, Boolean> firstTrait;

	@FXML
	private TableColumn<Snack, Boolean> secondTrait;

	@FXML
	private TableColumn<Snack, Boolean> thirdTrait;

	@FXML
	private TextField minPriceField;

	@FXML
	private TextField maxPriceField;

	@FXML
	void addSSnackSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddSnack2Control addSnack2Control = new AddSnack2Control();
		try {
			addSnack2Control.start(stage);// go to the add snack page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void removeSnackSelected(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (snacks.getSelectionModel().isEmpty() == true) {// check if the user selected a snack to be removed
			Alert a = new Alert(AlertType.ERROR, "Select Snack First", ButtonType.OK);
			a.showAndWait();
		} else {
			Snack s = snacks.getSelectionModel().getSelectedItem();
			if (Zoo.getInstance().removeSnack(s)) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "Snack removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				initialize(null, null);
			} else {
				Alert a = new Alert(AlertType.ERROR, "Couldn't remove Snack!", ButtonType.OK);
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
		ManageSnacksMainControler managePage = new ManageSnacksMainControler();
		try {
			managePage.start(stage);// return to the manage page
		} catch (Exception e) {
			Alert a = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a.showAndWait();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set the value of each field
		fName.setCellValueFactory(new PropertyValueFactory<>("snackName"));
		snackType.setCellValueFactory(new PropertyValueFactory<>("type"));
		bar.setCellValueFactory(new PropertyValueFactory<>("bar"));
		fattening.setCellValueFactory(new PropertyValueFactory<>("Fattening"));
		price.setCellValueFactory(new PropertyValueFactory<>("price"));
		// set the text for the fields
		firstTrait.setText("plate");
		secondTrait.setText("spaciy");
		thirdTrait.setText("glutenFree");
		firstTrait.setCellValueFactory(new PropertyValueFactory<>("plate"));
		secondTrait.setCellValueFactory(new PropertyValueFactory<>("spaciy"));
		thirdTrait.setCellValueFactory(new PropertyValueFactory<>("glutenFree"));
		snacks.getItems().clear();
		ArrayList<Snack> finalAllSnacks = new ArrayList<Snack>();
		ArrayList<Snack> allSnacks = new ArrayList<Snack>(Zoo.getInstance().getSnacks().values());
		for (Snack s : allSnacks) {// get the snacks
			if ((s instanceof Drink) == false)
				finalAllSnacks.add(s);
		}
		snacks.setItems(FXCollections.observableArrayList(finalAllSnacks));// add the snacks

		minPriceField.textProperty().addListener((observable, oldValue, newValue) -> {// this listener will change the
			// snacks in the table view, the
			// table view will contain only
			// the snacks with price like
			// the minimum price or higher
			if (!maxPriceField.getText().isEmpty()) {
				snacks.getItems().clear();
				ArrayList<Snack> finalAllSnacks1 = new ArrayList<Snack>();
				ArrayList<Snack> allSnacks1 = new ArrayList<Snack>(Zoo.getInstance().getSnacks().values());
				try {
					for (Snack s : allSnacks1) {
						if (s.getPrice() >= Double.parseDouble(minPriceField.getText())
								&& s.getPrice() <= Double.parseDouble(maxPriceField.getText())) {

							if (!(s instanceof Drink))
								finalAllSnacks1.add(s);

						}
					}
					snacks.setItems(FXCollections.observableArrayList(finalAllSnacks1));
				} catch (NumberFormatException numE) {
					Alert a = new Alert(AlertType.ERROR, "This Should Be Double Value!", ButtonType.OK);
					a.showAndWait();
				}
			}
		});
		maxPriceField.textProperty().addListener((observable, oldValue, newValue) -> {// this listener will change the
			// snacks in the table view, the
			// table view will contain only
			// the snacks with price like
			// the maximum price or lowers
			if (!minPriceField.getText().isEmpty()) {
				snacks.getItems().clear();
				ArrayList<Snack> finalAllSnacks2 = new ArrayList<Snack>();
				ArrayList<Snack> allSnacks2 = new ArrayList<Snack>(Zoo.getInstance().getSnacks().values());
				try {
					for (Snack s : allSnacks2) {
						if (s.getPrice() >= Double.valueOf(minPriceField.getText())
								&& s.getPrice() <= Double.valueOf(maxPriceField.getText())) {
							if (!(s instanceof Drink))
								finalAllSnacks2.add(s);
						}
					}
					snacks.setItems(FXCollections.observableArrayList(finalAllSnacks2));
				} catch (NumberFormatException numE) {
					Alert a = new Alert(AlertType.ERROR, "This Should Be Double Value!", ButtonType.OK);
					a.showAndWait();
				}
			}
		});
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
			// load the ManageSnacks2 fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageSnacks2.fxml"));
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
			stage.setTitle("Manage Food");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
