package application;

import java.sql.*;

public class DatabaseModel {
	
	private String url = "jdbc:sqlite:db/Database.db";

	public DatabaseModel() {
		try {
			Class.forName("org.sqlite.JDBC");  // use SQLite
			this.connect();  // connect DB
			System.out.println("SQLite DB Connected");  // to confirm connection
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection connect() {
		
		Connection con = null;  // Connector
		
		try {
			con = DriverManager.getConnection(url);  // link DB file driver to Connector through file url
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return con;
	}
	
	public void createNewTable(String tname, String[] columns) {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS "+tname+" (\n";
		for (int i=0; i<columns.length-1; i++)
			sql = sql + columns[i] + ", ";
		sql = sql + columns[columns.length-1] + ");";
        
        try (Connection con = this.connect();
        		Statement stmt = con.createStatement();) {	
	    	// create a new table
	    	stmt.execute(sql);
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
	}

	// Search table with username(primary key)
	public boolean exist(String tname, String column, String username) {
		String sql = "SELECT COUNT(*) FROM "+ tname +" WHERE "+ column +" = ?";
        
        try (Connection con = this.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
	}
	
	// Get value of element by username
	public String getElement(String tname, String element, String username) {
		String sql = "SELECT "+ element +" FROM "+ tname +" WHERE username = '" + username + "'";
		String value = "";
		
		try (Connection con = this.connect();
        		Statement stmt = con.createStatement();) {	
	    	// get element by username
	    	ResultSet rs = stmt.executeQuery(sql);
	    	value = rs.getString(1);
	    	return value;
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
		return value;
	}
	
	public void insert(String tname, String[] elements) {

		// SQL statement for inserting a value
		String sql = "INSERT INTO "+tname+" VALUES ('";
		for (int i=0; i<elements.length-1; i++)
			sql = sql + elements[i] + "', '";
		sql = sql + elements[elements.length-1] + "');";
        
        try (Connection con = this.connect();
        		Statement stmt = con.createStatement();) {	
	    	// create a new table
	    	stmt.execute(sql);
	    	System.out.println("DB is Successfully initialized.");
        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
	}
	
	public void update() {
		
	}
	
}
