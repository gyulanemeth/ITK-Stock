package hu.ppke.itk.itkStock.server.db.historicData;


/**
	Record representing data about a single transaction;
*/
public class Transaction {
	private final double price;
	private final int volume;

	public Transaction(double price, int volume) {
		this.price = price;
		this.volume = volume;
	}

	public double getPrice() {
		return price;
	}
	public int getVolume() {
		return volume;
	}

	public static Transaction merge(Transaction left, Transaction right) {
		return new Transaction( (left.price+right.price)/2, left.volume+right.volume);
	}

	@Override public String toString() {
		return new StringBuilder().append(volume).append(" @ ").append(price).toString();
	}
}
