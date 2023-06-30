package control;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.ResourceBundle;

import Model.Section;
import Model.Snack;
import Model.SnackBar;
import Model.Visitor;
import Model.Zoo;
import Utils.Discount;
import Utils.Gender;
import Utils.TicketType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageVisitors implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private TableView<Visitor> visitors;

	@FXML
	private TableColumn<Visitor, String> fName;

	@FXML
	private TableColumn<Visitor, String> lName;

	@FXML
	private TableColumn<Visitor, LocalDate> date;

	@FXML
	private TableColumn<Visitor, Gender> gender;

	@FXML
	private TableColumn<Visitor, Section> section;
	@FXML
	private TableColumn<Visitor, TicketType> ticket;

	@FXML
	private TableColumn<Visitor, Discount> discount;

	@FXML
	private RadioButton move;

	final ToggleGroup f = new ToggleGroup();

	@FXML
	private RadioButton purchace;

	@FXML
	private ComboBox<Snack> snacks;
	@FXML
	private ComboBox<Section> sections;

	@FXML
	private Label labelChoice;

	@FXML
	private Button choiceButton;

	@FXML
	void addVisitorButton(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		AddVisitorControl addV = new AddVisitorControl();
		try {
			addV.start(stage);// go to the add visitor page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void removeVisitorButton(MouseEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (visitors.getSelectionModel().isEmpty() == true) {// check if the user select any visitor
			Alert a = new Alert(AlertType.ERROR, "You have to select a visitor from the visitors!",
					ButtonType.OK);
			a.showAndWait();
		} else {
			Visitor v = visitors.getSelectionModel().getSelectedItem();// get the selected visitor
			if (Zoo.getInstance().removeVisitor(v) == true) {// if the remove method return true
				Alert a = new Alert(AlertType.CONFIRMATION, "visitor was removed!", ButtonType.OK);
				a.showAndWait();
				Zoo.getInstance().saveToSerFile();// save the data to the .ser file
				initialize(null, null);// reinitialize the page
			} else {
				Alert a = new Alert(AlertType.ERROR, "didn't remove visitor", ButtonType.OK);
				a.showAndWait();
			}
		}
	}

	@FXML
	void choiceButtonSelected(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (move.isSelected() == true) {// if the user want to move visitor to another section

			Section sec = sections.getSelectionModel().getSelectedItem();

			if (sec != null) {// if he selected a section
				visitors.getSelectionModel().getSelectedItem().moveVisitorToSection(sec);
			} else {
				Alert a = new Alert(AlertType.ERROR, "you have to select section", ButtonType.OK);
				a.showAndWait();
			}
		} else {// if the user want to purchase snack
			Snack snack = null;
			SnackBar sb = visitors.getSelectionModel().getSelectedItem().getSection().getBar();
			if (sb != null) {// if he selected a snack
				snack = snacks.getSelectionModel().getSelectedItem();
			}
			if (visitors.getSelectionModel().getSelectedItem().purchaseSnack(snack) == true) {// if the move method
																								// return true
				Alert a = new Alert(AlertType.CONFIRMATION, "visitor bought snack!", ButtonType.OK);
				a.showAndWait();

			} else {
				Alert a = new Alert(AlertType.ERROR, "visitor didn't buy the snack!", ButtonType.OK);
				a.showAndWait();
			}
		}

		initialize(null, null);// reinitialize the page
		// null
	}

	@FXML
	void returnToPage(ActionEvent event) {
//play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		ManagePage managePage = new ManagePage();
		try {
			managePage.start(stage);// return to the manage page
		} catch (Exception e) {
			Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
			a1.showAndWait();
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
			// load the ManageVisitors fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageVisitors.fxml"));
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
			// e.getMessage();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// set al the radio buttons to be invisible
		move.setSelected(false);
		purchace.setSelected(false);
		labelChoice.setVisible(false);
		snacks.setVisible(false);
		sections.setVisible(false);
		choiceButton.setVisible(false);

		purchace.setToggleGroup(f);
		move.setToggleGroup(f);
		f.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
				if (move.isSelected() == true) {
					if (visitors.getSelectionModel().isEmpty() == true) {// check if the user selected a visitor
						Alert a = new Alert(AlertType.ERROR,
								"You have to select a visitor from the visitors!", ButtonType.OK);
						a.showAndWait();
						move.setSelected(false);
						labelChoice.setVisible(false);
						snacks.setVisible(false);
						sections.setVisible(false);
						choiceButton.setVisible(false);
					} else {
						snacks.setVisible(false);
						labelChoice.setVisible(true);
						sections.setVisible(true);
						choiceButton.setVisible(true);
						// set the labels
						labelChoice.setText("Where to move?");
						choiceButton.setText("Move");
						ArrayList<Section> arrayList = new ArrayList<Section>();
						arrayList.addAll(Zoo.getInstance().getSections().values());// add all the sections except the
																					// visitor section
						arrayList.remove(visitors.getSelectionModel().getSelectedItem().getSection());
						sections.getItems().clear();// clear the sections combo box
						sections.getItems().addAll(arrayList);

					}
				} else if (purchace.isSelected() == true) {
					if (visitors.getSelectionModel().isEmpty() == true) {// check if the user selected a visitor
						Alert a = new Alert(AlertType.ERROR,
								"You have to select a visitor from the visitors!", ButtonType.OK);
						a.showAndWait();
						purchace.setSelected(false);
						move.setSelected(false);
						labelChoice.setVisible(false);
						snacks.setVisible(false);
						sections.setVisible(false);
						choiceButton.setVisible(false);
					} else {
						sections.setVisible(false);
						labelChoice.setVisible(true);
						snacks.setVisible(true);
						choiceButton.setVisible(true);
						// set the labels
						labelChoice.setText("What to Purchase?");
						choiceButton.setText("Purchase");
						if (visitors.getSelectionModel().getSelectedItem().getSection().getBar() != null) {// check if
																											// the
																											// vivitor
																											// section
																											// has a bar
							SnackBar sb = visitors.getSelectionModel().getSelectedItem().getSection().getBar();
							// get the snakcs from the bar of the visitor
							snacks.getItems().clear();
							snacks.getItems().addAll(sb.getSnacks());

						} else {
							Alert alerta = new Alert(AlertType.ERROR, "There is no bar in this section!",
									ButtonType.OK);
							alerta.showAndWait();
							purchace.setSelected(false);
							move.setSelected(false);
							labelChoice.setVisible(false);
							snacks.setVisible(false);
							sections.setVisible(false);
							choiceButton.setVisible(false);
						}

					}
				}
			}

		});
		// set the value of each field

		fName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		date.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		section.setCellValueFactory(new PropertyValueFactory<>("section"));
		ticket.setCellValueFactory(new PropertyValueFactory<>("ticket"));
		discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
		visitors.getItems().clear();// clear the table view
		ObservableList<Visitor> visitorsZoo;
		if (Zoo.getLoggedUser().getKey().getKey().equals("admin")) {// if the user was admin then the table view will
																	// contain all the visitors
			visitorsZoo = FXCollections.observableArrayList(Zoo.getInstance().getVisitors().values());
			visitors.setItems(visitorsZoo);
		} else {// if he was employee then the table view will contain the visitors in the same
				// section as the user
			Collection<Visitor> zooTempVisitors = Zoo.getInstance().getVisitors().values();
			ArrayList<Visitor> zooVisitors = new ArrayList<>(zooTempVisitors);
			visitorsZoo = FXCollections.observableArrayList();
			for (Visitor v : zooVisitors) {
				if (v.getSection().equals(Zoo.getLoggedUser().getValue().getSection()))
					visitorsZoo.add(v);
			}
			visitors.setItems(visitorsZoo);

		}
	}

}
