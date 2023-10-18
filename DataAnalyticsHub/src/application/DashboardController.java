package application;

import java.util.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.fxml.FXMLLoader;

public class DashboardController {
	
	private Stage stage;
	private User user;
	private SocialMedia sns;
	private TableView<Post> postsTable;
	private TableView<Post> searchedTable;
	private PieChart pieChart;
	private final String CSVpath = "/db/posts.csv";
	
	@FXML private Label firstname;
	@FXML private Label lastname;
	@FXML private Button modifyInfoBtn;
	@FXML private Button logOutBtn;
	
	@FXML private Button addBtn;
	@FXML private Label addPostMessage;
	@FXML private TextField postId;
	@FXML private TextField author;
	@FXML private TextArea content;
	@FXML private TextField likes;
	@FXML private TextField shares;
	@FXML private TextField date_time;
	
	@FXML private ScrollPane Posts;
	@FXML private Tab postsTab;
	@FXML private Button sortBtn;
	@FXML private MenuButton orderMenu;
	@FXML private MenuItem likesOrder;
	@FXML private MenuItem sharesOrder;
	@FXML private MenuItem resetOrder;
	@FXML private TextField sortCount;
	@FXML private Label sortMessage;
	
	@FXML private ScrollPane Search;
	@FXML private Tab searchTab;
	@FXML private Button searchBtn;
	@FXML private Button deleteBtn;
	@FXML private Button exportBtn;
	@FXML private TextField searchId;
	@FXML private Label searchMessage;
	
	@FXML private ScrollPane VIP;
	@FXML private Tab vipTab;
	@FXML private MenuButton categoryMenu;
	@FXML private MenuItem likesCategory;
	@FXML private MenuItem sharesCategory;
	@FXML private Label distributeMessage;
	@FXML private Button importBtn;
	@FXML private Label importMessage;
	
	
	public DashboardController(User user) { this.user = user; }
	public User getUser() { return this.user; }
	
	@FXML
	public void initialize() {
		

		//// INITIIAL SETTING ////
		
		try {
			sns = new SocialMedia(CSVpath);  // create sns object
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// print full names on dashboard
		firstname.setText(user.getFirstName());
		lastname.setText(user.getLastName());
		
		// make table of Posts tab
		postsTable = makeTableView(sns.getPosts());
		Posts.setContent(postsTable);  // Display table
		
		// for non-vip user
		if (user.getVip().equals("-")) {
			alertSubscribe();  // alert confirmation of VIP subscribe
		}
		

		//// SET EVENT HANDLER  ////

		// modify user information
		modifyInfoBtn.setOnAction(e -> {
			// view modify information page on a new stage
			ModifyInfoController modifyInfoController = new ModifyInfoController(this.user);
			modifyInfoController.view(this.stage);
		});
		
		// add post
		addBtn.setOnAction(e -> {
			try {
				sns.addPost(postId.getText(), content.getText(), author.getText(), likes.getText(), shares.getText(), date_time.getText());
				
				postId.setText("");
				content.setText("");
				author.setText("");
				likes.setText("");
				shares.setText("");
				date_time.setText("");
			
				// reset Posts tab
				addPostMessage.setTextFill(Color.BLACK);
				addPostMessage.setText("Fill the blanks below if you want to add a new post.");
				
				refresh();  // refresh tables and pie chart
				
			} catch (InvalidAttributeException e1) {
				addPostMessage.setTextFill(Color.RED);
				addPostMessage.setText(e1.getMessage());  // display error message on screen
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		
		// select "Likes" menu
		likesOrder.setOnAction(e -> {
			orderMenu.setText("Likes");
		});
		
		// select "Shares" menu
		sharesOrder.setOnAction(e -> {
			orderMenu.setText("Shares");
		});
		
		// reset sort menu option
		resetOrder.setOnAction(e -> {
			orderMenu.setText("Order");
			sortCount.setText(null);
			sortMessage.setText("Sort by Order.");
			
			// refresh table of Posts tab
			postsTable = makeTableView(sns.getPosts());
			Posts.setContent(postsTable);
		});
		
		// sort posts by menu option
		sortBtn.setOnAction(e -> {
			sortTable(orderMenu.getText(), sortCount.getText());
		});
		
		// retrieve post by Post ID
		searchBtn.setOnAction(e -> {
			String id = searchId.getText();
			try {
				Post post = sns.retrievePost(id);  // bring post from database
				searchedTable = makeTableView(post);  // make new table view for searched post
				Search.setContent(searchedTable);  // show table on Posts tab
				
				// disable delete, export buttons
				deleteBtn.setDisable(true);
				deleteBtn.setOpacity(0.5);
				exportBtn.setDisable(true);
				exportBtn.setOpacity(0.5);
				
				// when the row of the table is clicked
				searchedTable.setOnMouseClicked(event -> {
					String Id = getSelectedRowId(searchedTable);
					if (Id != null) {
						// able delete, export buttons
						deleteBtn.setDisable(false);
						deleteBtn.setOpacity(1);
						exportBtn.setDisable(false);
						exportBtn.setOpacity(1);
					}
				});
			} catch (NegativeNumberException | InvalidAttributeException e1) {
				searchMessage.setText(e1.getMessage());  // display error message on screen
			}
		});
		
		// reset Search tab
		searchId.setOnKeyPressed(e -> {
			if (searchId.getText().equals("")) {
				resetSearch();
				searchMessage.setText("Search by Post ID.");
			}
		});
		
		// delete post by Post ID
		deleteBtn.setOnAction(e -> {
			String id = getSelectedRowId(searchedTable);
			
			try {
				sns.deletePost(id);  // delete post from database

				resetSearch();  // reset Search tab

				refresh();  // refresh tables and pie chart

			} catch (NegativeNumberException | InvalidAttributeException e1) {
				searchMessage.setText(e1.getMessage());  // display error message on screen
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		
		// export post by Post ID
		exportBtn.setOnAction(e -> {
			
	        FileChooser fileChooser = new FileChooser();
	        
	        // Set extension filter
			ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");

			fileChooser.getExtensionFilters().add(extFilter);
	        fileChooser.setTitle("Save CSV File");
	        fileChooser.setInitialFileName("data.csv");

			String path = "";
	        // show file chooser Dialog
	        File selectedFile = fileChooser.showSaveDialog(this.stage);

	        if (selectedFile != null) {
	            path = selectedFile.getAbsolutePath();  // store choosed absolute path
	            String id = getSelectedRowId(searchedTable);
	            try {
					exportCSV(path, sns.retrievePost(id));
				} catch (NegativeNumberException | InvalidAttributeException e1) {
					searchMessage.setText(e1.getMessage());
				}
	        } else {
	            System.out.println("No directory selected.");
	        }
		});
		
		// distribute the portion of likes
		likesCategory.setOnAction(e -> {
			categoryMenu.setText("Likes");
			distributeMessage.setText("Distribute portion by Likes.");
			// load pie chart on VIP tab
			pieChart = distribute("Likes");
			VIP.setContent(pieChart);
		
		});
		
		// distribute the portion of shares
		sharesCategory.setOnAction(e -> {
			categoryMenu.setText("Shares");
			distributeMessage.setText("Distribute portion by Shares.");
			// load pie chart on VIP tab
			pieChart = distribute("Shares");
			VIP.setContent(pieChart);
			
		});
		
		// bulk import social media posts from ".csv" file
		importBtn.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
	        
	        // Set extension filter
			ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");

			fileChooser.getExtensionFilters().add(extFilter);
	        fileChooser.setTitle("Import CSV File");

			String path = "";
	        // show file chooser Dialog
	        File selectedFile = fileChooser.showOpenDialog(this.stage);

	        if (selectedFile != null) {
	            path = selectedFile.getAbsolutePath();  //  store choosed absolute path
	            try {
					sns.readCSV(path);
					importMessage.setText("Imported.");
					System.out.println("The CSV File has been imported to the collection!");
				} catch (InvalidAttributeException | FileNotFoundException e1) {
					importMessage.setText(e1.getMessage());
				} catch (IOException e2) {
					e2.printStackTrace();
				}
	        } else {
	            System.out.println("No directory selected.");
	        }
	        refresh();  // refresh tables and pie chart
		});
		
		// log out
		logOutBtn.setOnAction(e -> {
			user.logout();  // remove user information
			// view login page on this stage
			LoginController loginController = new LoginController(this.user);
			loginController.view(this.stage);
			
		});
	}


	//// HELPER FUNCTION  ////

	// alert VIP subscribe
	public void alertSubscribe() {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	        alert.setTitle("Confirmation Dialog");
	        alert.setHeaderText("Subscribe VIP");
	        alert.setContentText("Would you like to subscribe to the application for a monthly fee of $0?");

	        // add "Yes" and "No" Button
	        ButtonType yesButton = new ButtonType("Yes");
	        ButtonType noButton = new ButtonType("No");

	        alert.getButtonTypes().setAll(yesButton, noButton);

	        // wait for user's respond
	        Optional<ButtonType> result = alert.showAndWait();

	        if (result.isPresent() && result.get() == yesButton) {
	            System.out.println("User clicked Yes");
				// update user information of database
	            user.modify(user.getNumber(), user.getUserName(), user.getPassword(), user.getFirstName(), user.getLastName(), "vip");
	        } else {
	            System.out.println("User clicked No or closed the dialog");
	            vipTab.setDisable(true);  // disable VIP tab
	        }
	}
	
	// make new table view
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
        
		// add columns to table
        table.getColumns().addAll(idCol, contentCol, authorCol, likesCol, sharesCol, date_timeCol);

        // convert ArrayList to ObservableList
        ObservableList<Post> data = FXCollections.observableArrayList(posts);

        // set data to table
        table.setItems(data);
        
		return table;
	}	
	// method override (for when importing posts from a CSV File)
	public TableView<Post> makeTableView(Post post) {
		ArrayList<Post> postArr = new ArrayList<>();
		postArr.add(post);
		
		TableView<Post> table = makeTableView(postArr);
		return table;
	}

	public void sortTable(String selectedMenu, String count) {
		try {
				if (selectedMenu.equals("Likes")) {
					postsTable = makeTableView(sns.topLikesPosts(count));  // bring top n posts from database
					Posts.setContent(postsTable);  // show table on Posts tab
					
					sortMessage.setText("Sorted by number of Likes.");
				}
				else if (selectedMenu.equals("Shares")){
					postsTable = makeTableView(sns.topSharesPosts(count));  // bring top n posts from database
					Posts.setContent(postsTable);  // show table on Posts tab
					
					sortMessage.setText("Sorted by number of Shares.");
				}				
			} catch (NegativeNumberException | InvalidAttributeException e1) {
				sortMessage.setText(e1.getMessage());  // display error message on screen
			}
	}

	// export a post to a ".csv" file by Post ID
	public void exportCSV(String path, Post post) {
		File csv = new File(path);
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(csv));
			String colName = "ID,Content,Author,Likes,Shares,Date-Time";
			bw.write(colName);
			bw.newLine();

			String row = post.getId()+","+post.getContent()+","+post.getAuthor()+","+post.getLikes()+","+post.getShares()+","+post.getDate();
			bw.write(row);
			bw.newLine();
			
			if (bw != null) {
				bw.flush();  // clean rest of data
				bw.close();  // close BufferedWriter
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Successfully exported to '"+path+"'");
	}
	
	// distribute portion of posts by category
	public PieChart distribute(String category) {

		int low = sns.countCategory(category,0,99);
		int medium = sns.countCategory(category,100,999);
		int high = sns.countCategory(category,1000);
				
		ArrayList<PieChart.Data> dataList = new ArrayList<>();
        dataList.add(new PieChart.Data("0 to 99", low));
        dataList.add(new PieChart.Data("100 to  999", medium));
        dataList.add(new PieChart.Data("exceeding 1000", high));
        System.out.println("0 to 99: "+low +", 100 to 999: " + medium + ", exceeding 1000: " + high);
        
        PieChart pie = new PieChart();
        pie.getData().addAll(dataList);  // add data list to pie chart

        // set pie chart title
        pie.setTitle("Distribution of "+category+" Pie Chart");
        return pie;
	}

	// refresh tables and pie charts
	public void refresh() {

		// refresh Posts tab
		if (orderMenu.getText().equals("Order")) {
			postsTable = makeTableView(sns.getPosts());
			Posts.setContent(postsTable);
		} else {
			sortTable(orderMenu.getText(), sortCount.getText());
		}

		// refresh VIP tab
		if (categoryMenu.getText().equals("Category"))
			return;
		pieChart = distribute(categoryMenu.getText());
		VIP.setContent(pieChart);
	}
	
	// get post id of selected row in table
	public String getSelectedRowId(TableView<Post> table) {
		String id = null;
		TableColumn column = (TableColumn) searchedTable.getColumns().get(0);  // get column object from table
		Post selectedItem = (Post) searchedTable.getSelectionModel().getSelectedItem();  // bring post id of selected row
		if (selectedItem != null) {
			id = Integer.toString((int) column.getCellObservableValue(selectedItem).getValue());  // get post ID of clicked row
		}
		return id;
	}
	
	// reset Search tab
	public void resetSearch() {
		searchId.setText("");
		Search.setContent(null);

		// disable delete, export button
		deleteBtn.setDisable(true);
		deleteBtn.setOpacity(0.5);
		exportBtn.setDisable(true);
		exportBtn.setOpacity(0.5);
	}
	
	public void setStage(Stage stage) { this.stage = stage; }
	public Stage getStage() { return this.stage; }
	
	// view dashboard page on current stage
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
