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
	
	// private Stage stage;
	private User user;

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
		
		user = new User();
		
		btn.setOnAction(e -> {
			String id = username.getText();
			String pw = password.getText();
			String message = user.login(id,pw);
			
			loginMessage.setText(message);
			if (message.contains("Successfully")) {
				loginMessage.setTextFill(Color.BLACK);
				
			} 
			else {
				username.setText("");
				password.setText("");
				loginMessage.setTextFill(Color.RED);
			}
			
			
		});
		
		signup.setOnMousePressed(e -> {
			
		});
		
		
	}

	// public void setStage(Stage stage) {
	// 	this.stage = stage;
	// }

	public void view(Stage stage) {
		try {  // load Login View
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			loader.setController(this);
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