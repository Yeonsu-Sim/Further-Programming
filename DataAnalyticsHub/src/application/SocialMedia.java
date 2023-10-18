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

		String colName = "ID,Content,Author,Likes,Shares,Date-Time";
		bw.write(colName);
		bw.newLine();

		for (int key : db.keySet()) {
			Post post = db.get(key);
			String row = post.getId()+","+post.getContent()+","+post.getAuthor()+","+post.getLikes()+","+post.getShares()+","+post.getDate();
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
	
	public void deletePost(String input) throws IOException, NegativeNumberException, InvalidAttributeException{
		int postId = scanInputNumber("postId", input);
		if (db.containsKey(postId)) {
			Post post = db.get(postId);
			System.out.println("ID "+ input +" | "+post.getContent()+" | "+post.getAuthor()+" | "+post.getDate()+ " is removed from the collection.");
			db.remove(postId);
			writeCSV(CSVpath);
		}
		else
			throw new InvalidAttributeException("Sorry this post ID does not exist in the collection!");
	}
	
	public Post retrievePost(String input) throws NegativeNumberException, InvalidAttributeException {
		Post post;
		int id = scanInputNumber("Post ID", input);
		if (db.containsKey(id)) {
			post = db.get(id);
		}
		else
			throw new InvalidAttributeException("This Post ID does not exist in the collection!");
		return post;
	}
	
	public ArrayList<Post> topLikesPosts(String input) throws NegativeNumberException, InvalidAttributeException {
		int number = scanInputNumber("count", input);
		
		if (number <0 || number > db.size()) {
			System.out.println("Only "+db.size()+" posts exist in the collection. Showing all of them.");
			number = db.size();
		}
		
		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
		
		db.forEach((key, value) -> {  // Lambda Expression
			temp.put(key, value.getLikes());
		});
		
		LinkedHashMap<Integer,Integer> sorted_temp = sortPosts(temp);
		ArrayList<Post> sortedPosts = new ArrayList<>();
		
		int count = 0;
		for (int id : sorted_temp.keySet()) {
			sortedPosts.add(db.get(id));		
			if (++count == number) 
				break;
		}
		return sortedPosts;
	
	}
	
	public ArrayList<Post> topSharesPosts(String input) throws NegativeNumberException, InvalidAttributeException {
		int number = scanInputNumber("count", input);
		
		if (number <0 || number > db.size()) {
			System.out.println("Only "+db.size()+" posts exist in the collection. Showing all of them.");
			number = db.size();
		}
		
		HashMap<Integer,Integer> temp = new HashMap<Integer,Integer>();
		
		db.forEach((key, value) -> {  // Lambda Expression
			temp.put(key, value.getShares());
		});
		
		LinkedHashMap<Integer,Integer> sorted_temp = sortPosts(temp);
		ArrayList<Post> sortedPosts = new ArrayList<>();
		
		int count = 0;
		for (int id : sorted_temp.keySet()) {
			sortedPosts.add(db.get(id));		
			if (++count == number) 
				break;
		}
		return sortedPosts;
	
	}
	
	public Integer countCategory(String category, int from, int to) {
		int count = 0;
		
		for (int id: db.keySet()) {
			if (category.equals("Likes")) {
				if (from <= db.get(id).getLikes() && db.get(id).getLikes() <= to)
					count++;
			} else if (category.equals("Shares")) {
				if (from <= db.get(id).getShares() && db.get(id).getShares() <= to)
					count++;
			}
		}
		return count;
	}
	
	public Integer countCategory(String category, int from) {
		int count = 0;
		
		for (int id: db.keySet()) {
			if (category.equals("Likes")) {
				if (from <= db.get(id).getLikes())
					count++;
			} else if (category.equals("Shares")) {
				if (from < db.get(id).getShares())
					count++;
			}
		}
		return count;
	}
	
	public ArrayList<Post> getPosts() {
		ArrayList<Post> posts = new ArrayList<>();
		int count = 0;
		for (int id : db.keySet()) {
			posts.add(db.get(id));
			if (++count == db.size())
				break;
		}
		return posts;
	}
	
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
