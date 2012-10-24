package hu.ppke.itk.itkStock.client.watcherClient;

import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher;
import hu.ppke.itk.itkStock.server.db.stockWatcher.Watcher.BoundTypes;

/**
 * This is a simpler version of the {@link Watcher} class, it stores only the
 * necessary information for clients about a watcher.
 */
public class ClientsideWatcher {
	private String paperName;
	private double boundValue;
	private int boundType;
	private int userId;

	/**
	 * Constructs a new object of this class, width the given parameters.
	 * 
	 * @param paperName
	 *            the stocks's name, which should be observed.
	 * @param boundValue
	 *            the given bound value.
	 * @param boundType
	 *            the given bound type.
	 * @param userId the user who is registered this watcher. 
	 * @see BoundTypes
	 */
	public ClientsideWatcher(String paperName, double boundValue,
			int boundType, int userId) {
		this.paperName = paperName;
		this.boundValue = boundValue;
		this.boundType = boundType;
		this.userId = userId;
	}

	public String getPaperName() {
		return paperName;
	}

	public double getBoundValue() {
		return boundValue;
	}

	public int getBoundType() {
		return boundType;
	}

	public int getUserId() {
		return userId;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("papername: ");
		stringBuilder.append(paperName);
		stringBuilder.append(", bound value: ");
		stringBuilder.append(boundValue);
		stringBuilder.append(", bound type: ");
		stringBuilder.append((boundType == BoundTypes.LOWER_BOUND ? "lower bound" : "upper bound"));
		return stringBuilder.toString();
	}

}
