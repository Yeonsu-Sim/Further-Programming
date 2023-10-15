package application;

import java.sql.*;
import java.util.regex.Pattern;

public class User {
	private static DatabaseModel db;
	private static String username;
	private static String password;
	private static String firstname;
	private static String lastname;
	private final String tname = "users";

	
	public User() {
		db = new DatabaseModel();
	}
	
	public void initialize() {
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
	
	public void signup(String id, String pw, String fname, String lname) 
		throws InvalidUserNameException, InvalidPasswordException, InvalidFirstNameException, InvalidLastNameException {
		
		if (db.exist(tname, "username", id))
			throw new InvalidUserNameException("This Username already exist.");
		else if (id.equals(""))
			throw new InvalidUserNameException("Please insert a valid Username.");
		else if (pw.equals(""))
			throw new InvalidPasswordException("Please insert a valid Password.");
		else if (fname.equals(""))
			throw new InvalidFirstNameException("Please insert a valid First name");
		else if (lname.equals(""))
			throw new InvalidLastNameException("Please insert a valid Last name");
	
		String[] elements = {id, pw, fname, lname};
		db.insert(tname, elements);
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

// Exceptions
class InvalidUserNameException extends RuntimeException {
	public InvalidUserNameException(String errorMessage) {
		super(errorMessage);
	}
}

class InvalidPasswordException extends RuntimeException {
	public InvalidPasswordException(String errorMessage) {
		super(errorMessage);
	}
}

class InvalidFirstNameException extends RuntimeException {
	public InvalidFirstNameException(String errorMessage) {
		super(errorMessage);
	}
}

class InvalidLastNameException extends RuntimeException {
	public InvalidLastNameException(String errorMessage) {
		super(errorMessage);
	}
}
