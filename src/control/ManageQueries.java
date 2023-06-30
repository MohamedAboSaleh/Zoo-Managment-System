package control;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import Model.Zoo;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ManageQueries implements Initializable {
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
	int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
	int initialX;
	int initialY;
	@FXML
	private ComboBox<String> manageList;

	@FXML
	private Button returnButton;

	@FXML
	private Button submitButton;

	@FXML
	private Label profitLabel;

	@FXML
	void returnToMainPage(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		MainPageControl mainPageControl = new MainPageControl();
		try {
			mainPageControl.start(stage);// return to the previous page
		} catch (Exception e2) {
			Alert a1 = new Alert(AlertType.ERROR, e2.getMessage(), ButtonType.OK);
			a1.showAndWait();
		}
	}

	@FXML
	void submit(ActionEvent event) {
		// play the button pressed sound
		String path = "/vedios/button-3.mp3";
		Media sound = new Media(getClass().getResource(path).toExternalForm());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		if (manageList.getSelectionModel().getSelectedItem() == null) {// if the user didn't select a query
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setContentText("you should select correct value");
			a.showAndWait();
		} else {
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			String string = manageList.getSelectionModel().getSelectedItem();
			if (string.equals("getAllAnimalsBySectionMaxVisits")) {
				if (Zoo.getInstance().getSections().values().size() == 0) {// if there are no sections yet
					Alert a = new Alert(AlertType.ERROR, "There is no sections at the moment!",
							ButtonType.OK);
					a.showAndWait();
				} else {
					AllAnimalsBySectionMaxVisitsController query1 = new AllAnimalsBySectionMaxVisitsController();
					try {
						query1.start(stage);// go to the getAllAnimalsBySectionMaxVisits page
					} catch (Exception e) {
						Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a1.showAndWait();
					}
				}
			}
			if (string.equals("allAnimalsByWorker")) {
				if (Zoo.getInstance().getEmployees().values().size() == 0) {// if there are no employees
					Alert a = new Alert(AlertType.ERROR, "There is no employees at the moment!!",
							ButtonType.OK);
					a.showAndWait();
				} else {
					AllAnimalsByWorkerController query2 = new AllAnimalsByWorkerController();
					try {
						query2.start(stage);// go to the AllAnimalsByWorkerController page
					} catch (Exception e) {
						Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a1.showAndWait();
					}
				}
			}
			if (string.equals("findAllSnackByWorker")) {
				if (Zoo.getInstance().getBars().values().size() == 0) {// if there are no bars yet
					Alert a = new Alert(AlertType.ERROR, "There is no SnackBars at the moment!",
							ButtonType.OK);
					a.showAndWait();
				} else {
					FindAllSnackByWorkerController query3 = new FindAllSnackByWorkerController();
					try {
						query3.start(stage);// go to the FindAllSnackByWorkerController page
					} catch (Exception e) {
						Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
						a1.showAndWait();
					}
				}
			}
			if (string.equals("reptilesSleepAtSeasson")) {
				ReptilesSleepAtSeassonController query4 = new ReptilesSleepAtSeassonController();
				try {
					query4.start(stage);// go to the ReptilesSleepAtSeassonController page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if (string.equals("geAllDiscountAmount")) {
				AllDiscountAmountController query5 = new AllDiscountAmountController();
				try {
					query5.start(stage);// go to the AllDiscountAmountController page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
			if (string.equals("getMaxVisitorsVSMaxWorkers")) {
				MaxVisitorsVSMaxWorkersController query6 = new MaxVisitorsVSMaxWorkersController();
				try {
					query6.start(stage);// go to the MaxVisitorsVSMaxWorkersController page
				} catch (Exception e) {
					Alert a1 = new Alert(AlertType.ERROR, e.getMessage(), ButtonType.OK);
					a1.showAndWait();
				}
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		manageList.getItems().removeAll(manageList.getItems());// clear the queries list
		// add all the queries
		manageList.getItems().addAll("getAllAnimalsBySectionMaxVisits", "allAnimalsByWorker", "findAllSnackByWorker",
				"reptilesSleepAtSeasson", "geAllDiscountAmount", "getMaxVisitorsVSMaxWorkers");
		profitLabel.setText(String.valueOf(Zoo.getInstance().checkTotalRevenue()));// set the revenue

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
			// load the ManageQueries fxml file
			Parent root = FXMLLoader.load(getClass().getResource("/view/ManageQueries.fxml"));
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
			stage.setTitle("Queries");
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
