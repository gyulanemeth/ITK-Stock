package hu.ppke.itk.itkStock.stockdata;


/**
	A very simple date format to represent Y-M-D values. It doesn't have a terribly well-chosen name.
*/
public class StockDate implements Comparable<StockDate> {
	private final short year;
	private final byte month;
	private final byte day;

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

	@Override public int compareTo(StockDate left) {
		if(year - left.year == 0) {
			if(month - left.month == 0) {
				return day - left.day;
			} else return month - left.month;
		} else return year - left.year;
	}

	@Override public String toString() {
		return new StringBuilder().append(year).append("-").append(month).append("-").append(day).toString();
	}
}
