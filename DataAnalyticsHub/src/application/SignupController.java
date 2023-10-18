package application;

import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignupController {
	
	private Stage stage;
	private User user;
	
	public SignupController(User user) { this.user = user; }
	public User getUer() { return this.user; }
	
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField firstname;
	@FXML
	private TextField lastname;

	@FXML
	private Button btn;
	
	@FXML
	public void initialize() {
		
		btn.setOnAction(e -> {
			String id = username.getText();
			String pw = password.getText();
			String fname = firstname.getText();
			String lname = lastname.getText();
			
			try {
				user.signup(id, pw, fname, lname);
				
				System.out.println("Successfully signed up.");
				// Close current Stage
				this.stage.close();
			} catch (InvalidUserNameException e1) {
				username.setText("");
				username.setPromptText(e1.getLocalizedMessage());
			} catch (InvalidPasswordException e2) {
				password.setText("");
				password.setPromptText(e2.getLocalizedMessage());
			} catch (InvalidFirstNameException e3) {
				firstname.setText("");
				firstname.setPromptText(e3.getLocalizedMessage());
			} catch (InvalidLastNameException e4) {
				lastname.setText("");
				lastname.setPromptText(e4.getLocalizedMessage());
			}
		});
	}


	public void view() {
		try {  // load Signup View
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SignupView.fxml"));
			loader.setController(this);
			GridPane root = loader.load();
			
			this.stage = new Stage();  // make a new stage
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
			this.stage.setTitle("Signup view");
			this.stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

