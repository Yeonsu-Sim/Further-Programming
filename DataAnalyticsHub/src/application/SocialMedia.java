package application;

import java.util.*;  // for input by user
import java.io.*;
import java.util.Map.Entry;
import application.Post.Date;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class SocialMedia {

	Path path = FileSystems.getDefault().getPath("");
	final String basePath = path.toAbsolutePath().toString();
	private String CSVpath = "";
	public LinkedHashMap<Integer,Post> db;
	
	public SocialMedia(String CSVpath) throws FileNotFoundException, InvalidAttributeException, IOException, Exception{
		this.CSVpath = this.basePath + CSVpath;
		db = new LinkedHashMap<Integer, Post>();
		readCSV(this.CSVpath);
	}
	
	public void readCSV(String CSVpath) throws FileNotFoundException, InvalidAttributeException, IOException {
		File csv = new File(CSVpath);
		String line = "";
		int index = 1;

		BufferedReader br = new BufferedReader(new FileReader(csv));
		line = br.readLine();    // except first row (column name)
		while ((line = br.readLine()) != null) {    // split row
			String[] lineArr = line.split(",");    // split column
			if (lineArr.length > 6) {
				throw new InvalidAttributeException("CSV File Contains Invalid Attribute. Please check index "+index+" (row "+(index+1)+").");
			} else if (lineArr.length < 6) {
				throw new InvalidAttributeException("Some Attribute of CSV File Is Null. Please check index "+index+" (row "+(index+1)+").");
			}
			addPost(index++, lineArr[0], lineArr[1], lineArr[2], lineArr[3], lineArr[4], lineArr[5]);
		}
		try {
			if (br != null) {  // if the bufferReader is opened.
				br.close();
			}
		} catch (IOException e) {
			System.out.println("Can't close buffered reader.");
			e.printStackTrace();
		}
	}
	
	public void writeCSV(String CSVpath) throws IOException{
		File csv = new File(CSVpath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(csv));

		String colName = "ID,content,author,likes,shares,date-time";
		bw.write(colName);
		bw.newLine();

		for (int key : db.keySet()) {
			Post post = db.get(key);
			String row = post.getID()+","+post.getContent()+","+post.getAuthor()+","+post.getLikes()+","+post.getShares()+","+post.getDate();
			bw.write(row);
			bw.newLine();
		}
		try {
			if (bw != null) {
				bw.flush();  // clean rest of data
				bw.close();  // close BufferedWriter
			}
		} catch (IOException e) {
			System.out.println("Can't close buffered writer.");
			e.printStackTrace();
		}
	}
	
	// Add post
	public void addPost(String idStr, String content, String author, String likes, String shares, String date_time) 
				throws InvalidAttributeException, IOException {
		
		int postId;
		String cont;
		String auth;
		int likeNum;
		int shareNum;
		Date date;

		try {
			postId = scanInputNumber("Post ID", idStr);
			cont = scanInputText("Content", content);
			auth = scanInputText("Author", author);
			likeNum = scanInputNumber("Likes", likes);
			shareNum = scanInputNumber("Shares", shares);
			date = new Date(date_time);
		} catch (NegativeNumberException e) {
			throw new InvalidAttributeException(e.getMessage());
		} catch (CommaIsNotAllowedException e) {
			throw new InvalidAttributeException(e.getMessage());
		} catch (InvalidDateTimeFormatException e) {
			throw new InvalidAttributeException(e.getMessage());
		}

		if (db.containsKey(postId))  // Check post id already exist
			throw new InvalidAttributeException("Post of ID "+postId+" is already exist.");
		
		
		Post post = new Post(postId, cont, auth, likeNum, shareNum, date);
		db.put(postId, post);
		writeCSV(CSVpath);
		System.out.println("The post has been added to the collection!");
	}
	
	// Method Overloading (When readCSV)
	public void addPost(int index, String idStr, String content, String author, String likesStr, String sharesStr, String date_time)
		throws InvalidAttributeException, IOException {
		int id = 0;
		int likes= 0;
		int shares = 0;
		Date date = null;
		try { 
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {  // wrapping exception
			throw new InvalidAttributeException("Invalid value in id column. Please check index "+index+" (row "+(index+1)+").");
		}
		try {
			likes = Integer.parseInt(likesStr);
		} catch (NumberFormatException e) {  // wrapping exception
			throw new InvalidAttributeException("Invalid value in likes column. Please check index "+index+" (row "+(index+1)+").");
		}
		try {
			shares = Integer.parseInt(sharesStr);
		} catch (NumberFormatException e) {  // wrapping exception
			throw new InvalidAttributeException("Invalid value in shares column. Please check index "+index+" (row "+(index+1)+").");
		}
		try {
			date = new Date(date_time);
		} catch (InvalidDateTimeFormatException e) {  // wrapping exception
			throw new InvalidAttributeException(e.getMessage()+" Please check index "+index+" (row "+(index+1)+").");
		}

		Post post = new Post(id, content, author, likes, shares, date);
		db.put(id, post);			
	}
	
//	public void deletePost() throws IOException{
//		System.out.print("Please provide the post ID: ");
//		int id = scanInputNumber("id");
//		if (db.containsKey(id)) {
//			Post post = db.get(id);
//			System.out.println("ID "+ id +" | "+post.getContent()+" | "+post.getAuthor()+" | "+post.getDate()+ " is removed from the collection.");
//			db.remove(id);
//			writeCSV(CSVpath);
//		}
//		else
//			System.out.println("Sorry the post does not exist in the collection!");
//	}
//	
//	public void retrievePost() {
//		System.out.print("Please provide the post ID: ");
//		int id = scanInputNumber("id");
//		if (db.containsKey(id)) {
//			Post post = db.get(id);
//			System.out.println(post.getID()+" | "+post.getContent()+" | "+post.getAuthor()+" | "+post.getDate());
//		}
//		else
//			System.out.println("Sorry the post does not exist in the collection!");
//	}
//	
//	public void topLikesPosts() {
//		System.out.println("Please specify the number of posts to retrieve (N): ");
//		int number = scanInputNumber("number of likes");
//		
//		if (number <0 || number > db.size()) {
//			System.out.println("Only "+db.size()+" posts exist in the collection. Showing all of them.");
//			System.out.println("The top-"+db.size()+" posts with the most shares are:");
//			number = db.size();
//		}
//		
//		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
//		
//		db.forEach((key, value) -> {  // Lambda Expression
//			temp.put(key, value.getLikes());
//		});
//		
//		LinkedHashMap<Integer,Integer> sorted_temp = sortPosts(temp);
//		
//		int count = 0;
//		for (int id : sorted_temp.keySet()) {
//			System.out.print(++count + ") " + id + " | ");
//			System.out.print(db.get(id).getContent() + " | ");
//			System.out.println(db.get(id).getLikes());
//			
//			if (count == number) 
//				break;
//		}
//	}
//	
//	public void topSharesPosts() {
//		System.out.print("Please specify the number of posts to retrieve (N): ");
//		int number = scanInputNumber("number of shares");
//		
//		if (number <0 || number > db.size()) {
//			System.out.println("Only "+db.size()+" posts exist in the collection. Showing all of them.");
//			System.out.println("The top-"+db.size()+" posts with the most shares are:");
//			number = db.size();
//		}
//		
//		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
//		
//		db.forEach((key, value) -> {  // lambda statement
//			temp.put(key, value.getShares());
//		});
//		
//		LinkedHashMap<Integer,Integer> sorted_temp = sortPosts(temp);
//		
//		int count = 0;
//		for (int id : sorted_temp.keySet()) {
//			System.out.print(++count + ") " + id + " | ");
//			System.out.print(db.get(id).getContent() + " | ");
//			System.out.println(db.get(id).getShares());
//			
//			if (count == number) 
//				break;
//		}
//	}
	
	// Only return int number
	public int scanInputNumber(String column, String input) throws NegativeNumberException, InvalidAttributeException {  
		int number;
		try {  // use try-catch statement for error of casting user input
			number = Integer.parseInt(input);   // String input to Integer
			if (number < 0 )
				throw new NegativeNumberException("Please type Positive number in "+column+".");
		} catch (NumberFormatException e) {
			throw new InvalidAttributeException("Wrong approach. Please type a number in "+column+".");
		}
		return number;
	}

	// Only return String contains no comma
	public String scanInputText(String column, String input) throws CommaIsNotAllowedException{ 
		if (input.contains(","))  // checking input text contains comma
			throw new CommaIsNotAllowedException("Commas cannot be used in the "+column+".");
		return input;
	}

	// sortpost by given value
	public LinkedHashMap<Integer,Integer> sortPosts(HashMap<Integer, Integer> temp) {  
		LinkedHashMap<Integer,Integer> sorted_temp = new LinkedHashMap<Integer,Integer>();
		do {
			int highest = 0;
			int highestID = 0;
			Iterator<Entry<Integer,Integer>> it = temp.entrySet().iterator();
			do {
				int key = it.next().getKey();
				int value = temp.get(key);
				if(value >= highest) {
					highest = value;
					highestID = key;
				}
			} while(it.hasNext());
			sorted_temp.put(highestID, highest);  // put in order
			temp.remove(highestID);
		} while (temp.size()!=0);

		return sorted_temp;
	}
	
}

//Exceptions
class InvalidAttributeException extends Exception {  // when read CSV file -> must debug the error to run this program -> checked exception
	public InvalidAttributeException(String errorMessage) {
		super(errorMessage);
	}
}
class NegativeNumberException extends RuntimeException {
	public NegativeNumberException(String errorMessage) {
		super(errorMessage);
	}
}
class CommaIsNotAllowedException extends RuntimeException {
	public CommaIsNotAllowedException(String errorMessage) {
		super(errorMessage);
	}
}
