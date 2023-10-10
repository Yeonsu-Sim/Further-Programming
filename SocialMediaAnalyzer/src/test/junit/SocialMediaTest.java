package test.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import console.program.Post;
import console.program.SocialMedia;
import java.io.*;

class SocialMediaTest {
	
	@Test
	public void invalidCSVfileTest() {
		Exception e1 = assertThrows(Exception.class, 
				() -> new SocialMedia("/data/miss_content_in_index5.csv"));  // miss content
		assertTrue(e1.getMessage().contains("Some Attribute of CSV File Is Null. Please check index 5"));		
		
		Exception e2 = assertThrows(Exception.class, 
				() -> new SocialMedia("/data/null_value_of_likes_in_index3.csv"));  // invalid likes null
		assertTrue(e2.getMessage().contains("Invalid value in likes column. Please check index 3"));		
		
		Exception e3 = assertThrows(Exception.class, 
				() -> new SocialMedia("/data/invalid_month_in_index4.csv"));  // invalid month "FEB"
	        assertTrue(e3.getMessage().contains("Date and Time should be number. Please check index 4"));		
	}

	
	@Test
	public void invalidMenuOptionTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/data/postsTest.csv");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		String input = "A\n11\n6";  // Input invalid option "A" and 9 and valid option 6.
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		try {
			assertTrue(sns.run() == 6);  // Only allow valid option 6(1-6)
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	
	@Test
	public void invalidIdTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/date/postsTest.csv");
		} catch (Exception e) {
		}
		
		// invalid id "yeonsu" and valid id 100
		String input = "yeonsu\n100\nHello Java.\nyeonsu\n33\n14\n12/12/12 12:12\ny";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		
		try {
			sns.addPost();
			assertEquals(sns.db.get(100).getID(), 100);  // Only allow valid id 100
		} catch (Exception e) {
		}
	}
	
	@Test
	public void invalidContentTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/date/postsTest.csv");
		} catch (Exception e) {
		}
		
		// invalid content "Hello, Java." and valid content Hello Java.
		String input = "Hello, Java.\nHello Java.\nyeonsu\n33\n14\n12/12/12 12:12\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		
		try {
			sns.addPost();
			assertEquals(sns.db.get(100).getContent(), "Hello Java.");  // Only allow valid content "Hello Java."
		} catch (Exception e) {
		}
	}
	
	@Test
	public void invalidLikesTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/date/postsTest.csv");
		} catch (Exception e) {
		}
		
		// invalid likes "#a" and valid likes 33
		String input = "100\nHello Java.\nyeonsu\n#a\n33\n14\n12/12/12 12:12\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		
		try {
			sns.addPost();
			assertEquals(sns.db.get(100).getLikes(), 33);  // Only allow valid likes 33
		} catch (Exception e) {
		}
	}
	
	@Test
	public void invalidSharesTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/date/postsTest.csv");
		} catch (Exception e) {
		}
		
		// invalid shares "#a" and valid shares 33
		String input = "100\nHello Java.\nyeonsu\n#a\n14\n12/12/12 12:12\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		
		try {
			sns.addPost();
			assertEquals(sns.db.get(100).getShares(), 14);  // Only allow valid shares 14
		} catch (Exception e) {
		}
	}
	
	@Test
	public void invalidDateTimeTest() {
		SocialMedia sns = null;
		try {
			sns = new SocialMedia("/date/postsTest.csv");
		} catch (Exception e) {
		}
		
		// invalid date-time "12.12.12 12:12" and valid date-time "12/12/12 12:12"
		String input = "100\nHello Java.\nyeonsu\n33\n14\n12.12.12 12:12\n12/12/12 12:12\n\n";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);  // put on the stack;
		
		try {
			sns.addPost();
			assertEquals(sns.db.get(100).getDate(), "12/12/12 12:12");  // Only allow valid id 100
		} catch (Exception e) {
		}
	}
	

}
