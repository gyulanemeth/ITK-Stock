package hu.ppke.itk.itkStock.server.db.historicData;


/**
	A very simple date format to represent Y-M-D values. It doesn't have a terribly well-chosen name.
*/
public class StockDate implements Comparable<StockDate> {
	private final short year;
	private final byte month;
	private final byte day;

	public StockDate(int ymd) {
		this.year = (short)( ymd/10000 );
		this.month = (byte)( (ymd%10000)/100 );
		this.day = (byte)( ymd%100 );
	}

	public StockDate(int year, int month, int day) {
		this.year = (short)year;
		this.month = (byte)month;
		this.day = (byte)day;
	}

	public int getYear() {
		return (int)year;
	}
	public int getMonth() {
		return (int)month;
	}
	public int getDay() {
		return (int)day;
	}

	@Override public int compareTo(StockDate right) {
		if(year - right.year == 0) {
			if(month - right.month == 0) {
				return day - right.day;
			} else return month - right.month;
		} else return year - right.year;
	}

	@Override public String toString() {
		return new StringBuilder().append(year).append("-").append(month).append("-").append(day).toString();
	}
}
