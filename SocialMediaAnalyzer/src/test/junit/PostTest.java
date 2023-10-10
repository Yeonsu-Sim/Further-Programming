package test.junit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import console.program.Post;

class PostTest {
	
	@Test
	public void invalidFormatTest() {
		Exception e1 = assertThrows(Exception.class, 
				() -> new Post.Date("9.12.2020 13:13"));  // invalid date format
	        assertTrue(e1.getMessage().contains("Please check the date-time format(/,:, spacing)."));
	    
	        
        Exception e2 = assertThrows(Exception.class,
	    		() -> new Post.Date("9/12/2020 13:13:13"));  // invalid time format
			assertTrue(e2.getMessage().contains("Please check the time format(:)."));
			
        Exception e3 = assertThrows(Exception.class,
	    		() -> new Post.Date("9/12/2 020 13:13"));  // invalid spacing
			assertTrue(e3.getMessage().contains("Please check the date-time format(spacing)."));
	}
	
	@Test
	public void invalidDayTest() {
		Exception e = assertThrows(Exception.class, 
				() -> new Post.Date("99/12/2023 3:3"));  // invalid day 99
	        assertTrue(e.getMessage().contains("Day should be [1-31]."));
	}
	@Test
	public void invalidMonthTest() {
		Exception e = assertThrows(Exception.class,
				() -> new Post.Date("10/mm/2010 23:23"));  // invalid month "mm"
		assertTrue(e.getMessage().contains("Date and Time should be number."));
	}
	@Test
	public void invalidYearTest() {
		Exception e = assertThrows(Exception.class, 
				() -> new Post.Date("9/12/-10 3:3"));  // invalid year -10
	        assertTrue(e.getMessage().contains("Year should be positive."));		
	}
	@Test
	public void invalidHourTest() {
		Exception e = assertThrows(Exception.class, 
				() -> new Post.Date("9/12/2023 hh:3"));  // invalid hour "hh"
	        assertTrue(e.getMessage().contains("Date and Time should be number."));		
	}
	@Test
	public void invalidMuniteTest() {
		Exception e = assertThrows(Exception.class, 
				() -> new Post.Date("9/12/2023 3:99"));  // invalid time 99
	        assertTrue(e.getMessage().contains("Munite should be [0-59]."));		
	}
	
	@Test
	public void toStringTest() {  // test printing Date format is correct.
		Post.Date date = new Post.Date("1/1/99 10:8");
		assertEquals(date.toString(), "01/01/0099 10:08");
	}

}
