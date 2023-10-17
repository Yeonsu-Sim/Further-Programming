package application;

import java.util.*;
import java.io.IOException;
import application.Post.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

public class DashboardController {
	
	private Stage stage;
	private User user;
	private SocialMedia sns;
	private final String CSVpath = "/db/posts.csv";
	
	@FXML
	private Label firstname;
	@FXML
	private Label lastname;
	@FXML
	private Button modifyInfoBtn;
	@FXML
	private Button logOutBtn;
	@FXML
	private Button addPostBtn;
	@FXML
	private Label addPostMessage;
	@FXML 
	private TextField postId;
	@FXML 
	private TextField author;
	@FXML 
	private TextArea content;
	@FXML 
	private TextField likes;
	@FXML 
	private TextField shares;
	@FXML 
	private TextField date_time;
	@FXML
	private ScrollPane Posts;
	@FXML
	private Tab postsTab;
	@FXML
	private Button sortBtn;
	@FXML
	private ScrollPane Search;
	@FXML
	private Tab searchTab;
	@FXML
	private Button searchBtn;
	@FXML
	private Button deleteBtn;
	@FXML
	private ScrollPane VIP;
	@FXML
	private Tab vipTab;

	
	
	public DashboardController(User user) { this.user = user; }
	public User getUser() { return this.user; }
	
	@FXML
	public void initialize() {
		
		// Initial Setting
		try {
			sns = new SocialMedia(CSVpath);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		firstname.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		
		// make table
		TableView<Post> postsTable = makeTableView(sns.getPosts());
		Posts.setContent(postsTable);  // Display table
		
		
		// modify user information
		modifyInfoBtn.setOnAction(e -> {
			ModifyInfoController modifyInfoController = new ModifyInfoController(this.user);
			modifyInfoController.view(this.stage);
		});
		
		// add post
		addPostBtn.setOnAction(e -> {
			try {
				sns.addPost(postId.getText(), content.getText(), author.getText(), likes.getText(), shares.getText(), date_time.getText());
				
				postId.setText("");
				content.setText("");
				author.setText("");
				likes.setText("");
				shares.setText("");
				date_time.setText("");
			
				addPostMessage.setTextFill(Color.BLACK);
				addPostMessage.setText("Fill the blanks below if you want to add a new post.");
				
			} catch (InvalidAttributeException e1) {
				addPostMessage.setTextFill(Color.RED);
				addPostMessage.setText(e1.getMessage());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		
		// log out
		logOutBtn.setOnAction(e -> {
			user.logout();
			LoginController loginController = new LoginController(this.user);
			loginController.view(this.stage);
			
		});
	}
	
	public TableView<Post> makeTableView(ArrayList<Post> posts) {
		
		TableView<Post> table = new TableView<>();
		
		
		TableColumn<Post, Integer> idCol = new TableColumn<>("Id");
		TableColumn<Post, String> contentCol = new TableColumn<>("Content");
        TableColumn<Post, String> authorCol = new TableColumn<>("Author");
        TableColumn<Post, Integer> likesCol = new TableColumn<>("Likes");
        TableColumn<Post, Integer> sharesCol = new TableColumn<>("Shares");
        TableColumn<Post, Post.Date> date_timeCol = new TableColumn<>("Date_Time");
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likes"));
        sharesCol.setCellValueFactory(new PropertyValueFactory<>("shares"));
        date_timeCol.setCellValueFactory(new PropertyValueFactory<>("date_time"));
        
        table.getColumns().addAll(idCol, contentCol, authorCol, likesCol, sharesCol, date_timeCol);

        // convert ArrayList to ObservableList
        ObservableList<Post> data = FXCollections.observableArrayList(posts);

        // set data to table
        table.setItems(data);
        
		return table;
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
