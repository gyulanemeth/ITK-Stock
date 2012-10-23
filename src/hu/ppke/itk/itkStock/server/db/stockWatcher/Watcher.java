package hu.ppke.itk.itkStock.server.db.stockWatcher;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObject;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;

/**
 * Current price watcher class. Its task is to watch the price of a given stock.
 * One can define upper or lower bound.
 * 
 * @see WatcherManager
 */
public class Watcher extends BusinessObject {

	/**
	 * This class holds the id for lower/upper bounds.
	 */
	public static class BoundTypes {
		public static final int UPPER_BOUND = 1, LOWER_BOUND = -1;
	}

	private int userId;
	private String paperName;
	private int boundType;
	private double boundValue;
	private boolean changed = false;

	public Watcher(WatcherManager manager, int id) {
		super(manager, id);
	}

	@Override
	public boolean get() throws SQLException, BusinessObjectException {
		Watcher temp = null;

		if (this.id != 0)
			temp = (Watcher) this.manager.get(this.id);
		else
			throw new BusinessObjectException("No id was specified.");

		if (temp == null)
			return false;

		this.id = temp.id;

		this.identified = true;
		this.changed = false;

		return true;
	}

	@Override
	public void update() throws SQLException, BusinessObjectException {
		if (!this.identified)
			throw new BusinessObjectException(
					"Must identify BusinessObject before updating in database.");

		if (this.changed)
			((WatcherManager) this.manager).update(this);
	}

	@Override
	public void create() throws SQLException, BusinessObjectException {
		if (this.identified)
			throw new BusinessObjectException(
					"Identified object should not be created.");

		((WatcherManager) this.manager).create(this);
	}

	public void setData(int userId, String paperName, double boundValue,
			int boundType) {
		setUserId(userId);
		setPaperName(paperName);
		setBoundValue(boundValue);
		setBoundType(boundType);
		changed = true;
	}

	public int getBoundType() {
		return boundType;
	}

	public void setBoundType(int boundType) {
		this.boundType = boundType;
	}

	public double getBoundValue() {
		return boundValue;
	}

	public void setBoundValue(double value) {
		this.boundValue = value;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

}
