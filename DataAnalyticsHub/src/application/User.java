package application;

import java.sql.*;
import java.util.regex.Pattern;

public class User {
	private static DatabaseModel db;
	private static String username;
	private static String password;
	private static String firstname;
	private static String lastname;
	private static String number;
	private static String vip;
	private final String tname = "users";
	
	public User() {
		db = new DatabaseModel();
	}
	
	// initial setting
	public void initialize() {
		// create a new table "users"
		String[] columns = {
			"number TEXT PRIMARY KEY",
			"username TEXT NOT NULL",
			"password TEXT NOT NULL",
			"firstname TEXT NOT NULL",
			"lastname TEXT NOT NULL",
			"vip TEXT NOT NULL"
		};
		db.createNewTable(tname, columns);
	
		// add "admin" to the "users" table
		String[] elements = {
				"0", 
				"admin", 
				"pass", 
				"Yeonsu", 
				"Sim", 
				"vip"
		};
		db.insert(tname, elements);
	}
	
	// check validation of id and pw for log in
	public String login(String id, String pw) {
		if (db.exist(tname,"username", id)) {
			if (db.getElement(tname,"password",id).equals(pw)) {
				this.setInfo(id);
				return "Successfully loged in.";
			}
			return "Wrong Password. Please try agin";
		}
		return "This username does not exist.";
	}
	
	// log out
	public void logout() {
		this.setInfo();  // reset all user fields
	}
	
	// check validation for sign up
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
		
		int lastNumber = Integer.parseInt(db.getLastElement(tname, "number"));  // to make new number of user
	
		String[] elements = {Integer.toString(++lastNumber), id, pw, fname, lname, "-"};
		db.insert(tname, elements);
	}
	
	// confirm validation for modifying user information
	public void modify(String num, String id, String pw, String fname, String lname, String vip) {		
		
		if (db.exist(tname, "username", id) && !id.equals(this.username))
			throw new InvalidUserNameException("This Username already exist.");
		else if (id.equals(""))
			throw new InvalidUserNameException("Please insert a valid Username.");
		else if (pw.equals(""))
			throw new InvalidPasswordException("Please insert a valid Password.");
		else if (fname.equals(""))
			throw new InvalidFirstNameException("Please insert a valid First name");
		else if (lname.equals(""))
			throw new InvalidLastNameException("Please insert a valid Last name");
		
		String[] columns = {"number", "username", "password", "firstname", "lastname", "vip"};
		String[] elements = {num, id, pw, fname,lname, vip};
		db.update(tname, columns, elements);
		
		this.setInfo(id);  // refresh all user fields
	}
	
	// refresh all user fields
	public void setInfo(String username) {
		this.setUserName(username);
		this.setNumber(db.getElement(tname, "number", username));
		this.setPassword(db.getElement(tname, "password", username));
		this.setFirstName(db.getElement(tname, "firstname", username));
		this.setLastName(db.getElement(tname, "lastname", username));
		this.setVip(db.getElement(tname, "vip", username));
	}
	
	// reset all user fields
	public void setInfo() {
		this.setUserName(null);
		this.setPassword(null);
		this.setFirstName(null);
		this.setLastName(null);
		this.setNumber(null);
		this.setVip(null);
	}
	
	public String getNumber() { return this.number; }
	public String getUserName() { return this.username; }
	public String getPassword() { return this.password; }
	public String getFirstName() { return this.firstname; }
	public String getLastName() { return this.lastname; }
	public String getVip() { return this.vip; }
	
	public void setNumber(String number) { this.number = number; }
	public void setUserName(String username) { this.username = username; } 
	public void setPassword(String password) { this.password = password; } 
	public void setFirstName(String firstname) { this.firstname = firstname; } 
	public void setLastName(String lastname) { this.lastname = lastname; }
	public void setVip(String vip) { this.vip= vip; } 
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
