package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			// LoginController loginController = new LoginController();
			// loginController.setStage(primaryStage);
			// loader.setController(loginController);
			// VBox root = loader.load();
			
			// Scene scene = new Scene(root);
			// primaryStage.setScene(scene);
			// primaryStage.setTitle("Login view");
			// primaryStage.show();

			LoginController loginController = new LoginController();
			loginController.view(primaryStage);

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
