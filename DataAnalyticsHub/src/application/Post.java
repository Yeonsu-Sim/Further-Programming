package application;

public class Post {
	
	private int id;
	private String content;
	private String author;
	private int likes;
	private int shares;
	private Date date_time;
	
	public Post() {
	}
	
	public Post(int id) {
		this.id = id;
	}

	public Post(int id, String content, String author, int likes, int shares, Date date_time) {
		this.id = id;
		this.content = content;
		this.author = author;
		this.likes = likes;
		this.shares = shares;
		this.date_time = date_time;
	}
	
	public int getID() {
		return this.id;
	}
	public String getContent() {
		return this.content;
	}
	public String getAuthor() {
		return this.author;
	}
	public int getLikes() {
		return this.likes;
	}
	public int getShares() {
		return this.shares;
	}
	public Date getDate() {
		return this.date_time;
	}

	public static class Date {  // If "Sort by recent date" function is needed, making date class is more efficient. 
			int year;           // Cause member variables are Integer type.
			int month;
			int day;
			int hour;
			int munite;

		public Date(String date_time) throws InvalidDateTimeFormatException, InvalidDayException, InvalidMonthException, InvalidYearException, InvalidHourException, InvalidMuniteException{
			
			// Check date format
			if (!date_time.contains("/") || !date_time.contains(":") || !date_time.contains(" ")) {
				throw new InvalidDateTimeFormatException("Please check the date-time format(/,:, spacing).");
			}
			String[] day_month_yearHourMunite = date_time.split("/");
			if (day_month_yearHourMunite.length != 3) {
				throw new InvalidDateTimeFormatException("Please check the date format(/).");
			}
			String[] year_HourMunite = day_month_yearHourMunite[2].split(" ");
			if (year_HourMunite.length != 2) {
				throw new InvalidDateTimeFormatException("Please check the date-time format(spacing).");
			}
			String[] hour_munite = year_HourMunite[1].split(":");
			if (hour_munite.length != 2) {
				throw new InvalidDateTimeFormatException("Please check the time format(:).");
			}
			
			// Check is date number
			int day; int month; int year; int hour; int munite;
			try {
				day = Integer.parseInt(day_month_yearHourMunite[0]);
				month = Integer.parseInt(day_month_yearHourMunite[1]);
				year = Integer.parseInt(year_HourMunite[0]);
				hour = Integer.parseInt(hour_munite[0]);
				munite = Integer.parseInt(hour_munite[1]);
			} catch (NumberFormatException e) {  // wrapping exception
				throw new InvalidDateTimeFormatException("Date and Time should be number.");
			}
				
			// Check is date valid
			if (day < 1 || day > 31)
				throw new InvalidDayException("Day should be [1-31].");
			if (month < 1 || month > 12)
				throw new InvalidMonthException("Month should be [1-12].");
			if (year < 0)
				throw new InvalidYearException("Year should be positive.");
			if (hour < 0 || hour > 23)
				throw new InvalidHourException("Hour should be [0-23].");
			if (munite < 0 || munite > 59 )
				throw new InvalidMuniteException("Munite should be [0-59].");

			this.day = day;
			this.month = month;
			this.year = year;
			this.hour = hour;
			this.munite = munite;
		}

		// Method overloading (built-in function)
		public String toString() {

			String dayStr = Integer.toString(this.day);
			String monthStr = Integer.toString(this.month);
			String yearStr = Integer.toString(this.year);
			String hourStr = Integer.toString(this.hour);
			String muniteStr = Integer.toString(this.munite);

			// Add "0" to unificate format
			if (dayStr.length() != 2) dayStr = "0" + dayStr;
			if (monthStr.length() != 2) monthStr = "0" + monthStr;
			if (hourStr.length() != 2) hourStr = "0" + hourStr;
			if (muniteStr.length() != 2) muniteStr = "0" + muniteStr;

			switch(yearStr.length()) {
				case 4:
					break;
				case 3:
					yearStr = "0" + yearStr;
					break;
				case 2:
					yearStr = "00" + yearStr;
					break;
				case 1:
					yearStr = "000" + yearStr;
					break;
			}
			return dayStr + "/" + monthStr + "/" + yearStr + " " + hourStr + ":" + muniteStr;
		}
	}
}

// Exceptions
class InvalidDateTimeFormatException extends RuntimeException {  // For wrapping exception purpose
	public InvalidDateTimeFormatException(String errorMessage) {
		super(errorMessage);
	}
}
class InvalidDayException extends InvalidDateTimeFormatException {
	public InvalidDayException(String errorMessage) {
		super(errorMessage);
	}
}
class InvalidMonthException extends InvalidDateTimeFormatException {
	public InvalidMonthException(String errorMessage) {
		super(errorMessage);
	}
}
class InvalidYearException extends InvalidDateTimeFormatException {
	public InvalidYearException(String errorMessage) {
		super(errorMessage);
	}
}
class InvalidHourException extends InvalidDateTimeFormatException {
	public InvalidHourException(String errorMessage) {
		super(errorMessage);
	}
}
class InvalidMuniteException extends InvalidDateTimeFormatException {
	public InvalidMuniteException(String errorMessage) {
		super(errorMessage);
	}
}
