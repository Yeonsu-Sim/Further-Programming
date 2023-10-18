package application;

import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ModifyInfoController {
	
	private Stage stage;
	private Stage primaryStage;
	private User user;
	
	public ModifyInfoController(User user) { this.user = user; }
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
		
		username.setText(user.getUserName());
		password.setText(user.getPassword());
		firstname.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		
		btn.setOnAction(e -> {
			
			try {
				String num = user.getNumber();
				String vip = user.getVip();
				
				String id = username.getText();
				String pw = password.getText();
				String fname = firstname.getText();
				String lname = lastname.getText();
				
				user.modify(num, id, pw, fname, lname, vip);
				
				// Close current Stage
				this.stage.close();
				DashboardController dashboardController = new DashboardController(this.user);
				dashboardController.view(primaryStage);
				
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


	public void view(Stage primaryStage) {
		this.primaryStage = primaryStage;
		try {  // load Signup View
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyInfoView.fxml"));
			loader.setController(this);
			GridPane root = loader.load();
			
			this.stage = new Stage();  // make a new stage
			Scene scene = new Scene(root);
			this.stage.setScene(scene);
			this.stage.setTitle("Modify Info view");
			this.stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

