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
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class LoginController {
	
	private Stage stage;
	private User user;
	
	public LoginController(User user) { this.user = user; }
	public User getUser() { return this.user; } 

	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	
	private Button btn;
	@FXML
	private Label signup;
	@FXML
	private Label loginMessage;

	@FXML
	public void initialize() {
		
		btn.setOnAction(e -> {
			String id = username.getText();
			String pw = password.getText();
			
			String message = user.login(id,pw);
			
			loginMessage.setText(message);
			if (message.contains("Successfully")) {
				loginMessage.setTextFill(Color.BLACK);
				
				System.out.println("Successfully logged in.");
				
				// view Dashboard Vies
				DashboardController dashboardController = new DashboardController(user);
				dashboardController.view(this.getStage());
			} 
			else {
				username.setText("");
				password.setText("");
				loginMessage.setTextFill(Color.RED);
			}
			
			
		});
		
		signup.setOnMousePressed(e -> {
			SignupController signupController = new SignupController(user);
			signupController.view();
		});
		
		
	}

	 public void setStage(Stage stage) { this.stage = stage; }
	 public Stage getStage() { return this.stage; }

	public void view(Stage stage) {
		try {  // load Login View
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			loader.setController(this);
			this.setStage(stage);
			VBox root = loader.load();
		
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("Login view");
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
