package hu.ppke.itk.itkStock.server.db.historicData;


/**
	A very simple time format to represent H:i:s values. It doesn't have a terribly well-chosen name.
*/

public class StockTime implements Comparable<StockTime> {
	private final byte hour;
	private final byte minute;
	private final byte second;

	public StockTime(int hour, int minute, int second) {
		this.hour = (byte)hour;
		this.minute = (byte)minute;
		this.second = (byte)second;
	}

	public int getHour() {
		return (int)hour;
	}
	public int getMinute() {
		return (int)minute;
	}
	public int getSecond() {
		return (int)second;
	}

	@Override public int compareTo(StockTime rhs) {
		if(hour - rhs.hour == 0) {
			if(minute - rhs.minute == 0) {
				return second - rhs.second;
			} else return minute - rhs.minute;
		} else return hour - rhs.hour;
	}

	@Override public String toString() {
		return new StringBuilder().append(hour).append(":").append(minute).append(":").append(second).toString();
	}
}
