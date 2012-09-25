package hu.ppke.itk.itkStock.stockdata;


/**
	Record representing data about a single transaction;
*/
public class Transaction {
	private final int price;
	private final int volume;

	public Transaction(int price, int volume) {
		this.price = price;
		this.volume = volume;
	}

	public int getPrice() {
		return price;
	}
	public int getVolume() {
		return volume;
	}

	@Override public String toString() {
		return new StringBuilder().append(volume).append(" @ ").append(price).toString();
	}
}
