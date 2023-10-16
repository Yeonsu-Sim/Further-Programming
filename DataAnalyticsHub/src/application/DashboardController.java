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
	
	@FXML
	private Label firstname;
	@FXML
	private Label lastname;
	@FXML
	private Button modifyInfoBtn;
	@FXML
	private Button addPostBtn;
	@FXML
	private Label addPostMessage;
	
	public DashboardController(User user) { this.user = user; }
	public User getUser() { return this.user; }
	
	@FXML
	public void initialize() {
		System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + "!");
		
		firstname.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		
		
		modifyInfoBtn.setOnAction(e -> {
			
		});
		
		addPostBtn.setOnAction(e -> {
			
		});
		
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
