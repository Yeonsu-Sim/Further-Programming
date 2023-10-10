package console.program;
import java.util.*;  // for input by user
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String CSVpath = "/data/posts.csv";

		try {
			SocialMedia sns = new SocialMedia(CSVpath);
			sns.run();
		} catch (InvalidAttributeException e) {  // need to shut down for a check
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {  // need to shut down for a check
			System.out.println(e.getMessage());
			System.out.println("Please check does the file exist in the file path or is the file path is correct.");
		} catch (IOException e) {  // need to shut down for a check
			e.printStackTrace();
		} catch (Exception e) {  // need to shut down for a check
			e.printStackTrace();
		} finally {
			System.out.println("Shut Down the Program.");
		}
	}
}

