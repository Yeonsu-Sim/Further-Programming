package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class DashboardController {
	
	private Stage stage;
	private User user;
	
	public DashboardController(User user) { this.user = user; }
	public User getUser() { return this.user; }
	
	@FXML
	public void initialize() {
		
	}
	
	public void setStage(Stage stage) { this.stage = stage; }
	public Stage getStage() { return this.stage; }
	
	public void view(Stage stage) {
		try {  // load Dashboard View
			FXMLLoader loader = new FXMLLoader(getClass().getResource("DashboardView.fxml"));
			loader.setController(this);
			this.setStage(stage);
			BorderPane root = loader.load();
		
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Dashboard view");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
