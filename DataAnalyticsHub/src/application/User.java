package application;

import java.sql.*;

public class User {
	private DatabaseModel db;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private final String tname = "users";

	
	public User() {
		db = new DatabaseModel();

		// create a new table "users"
		String[] columns = {
			"username TEXT PRIMARY KEY",
			"password TEXT NOT NULL",
			"firstname TEXT NOT NULL",
			"lastname TEXT NOT NULL"
		};
		db.createNewTable(tname, columns);

		// initialize the "users" table
		String[] elements = {
			"admin",
			"pass",
			"yeonsu",
			"sim"
		};
		db.insert(tname, elements);
	}
	
	public String login(String id, String pw) {
		if (db.exist(tname,"username", id)) {
			if (db.getPassword(tname,id).equals(pw))
				return "Successfully loged in.";
			return "Wrong Password. Please try agin";
		}
		return "This username does not exist.";
	}
	
	public void logout() {
		
	}
	
	public String getUserName() { return this.username; }
	public String getPassword() { return this.password; }
	public String getLastName() { return this.lastname; }
	public String getFirstName() { return this.firstname; }
	
	public void setUserName(String username) { this.username = username; }
	public void setPassword(String password) { this.password = password; }
	public void setFirstName(String firstname) { this.firstname = firstname; }
	public void setLastName(String lastname) { this.lastname = lastname; }
	
}
