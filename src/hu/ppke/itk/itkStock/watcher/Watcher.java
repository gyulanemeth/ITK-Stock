package hu.ppke.itk.itkStock.watcher;

import java.sql.SQLException;

import hu.ppke.itk.itkStock.dbaccess.BusinessObject;
import hu.ppke.itk.itkStock.dbaccess.BusinessObjectException;

/**
 * Árfolyamfigyelú osztály. Feladata, hogy egy meghatározott értékpapírt figyeljen. Definoálni
 * lehet felsõ és alsó határt.
 * 
 * @see WatcherManager
 */
public class Watcher extends BusinessObject {

	/**
	 * Ez az osztály tartalmazza a felsõ és alsó határ idjét.
	 * 
	 * @author Kozák Csaba - kozcs - XJ6JXU
	 *
	 */
	public static class BoundTypes {
		public static final int UPPER_BOUND = 1, LOWER_BOUND = 0;
	}

	private String userName;
	private String stockName;
	private int boundType;
	private float boundValue;
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
			throw new BusinessObjectException("Must identify BusinessObject before updating in database.");

		if (this.changed)
			((WatcherManager) this.manager).update(this);
	}

	@Override
	public void create() throws SQLException, BusinessObjectException {
		if (this.identified)
			throw new BusinessObjectException("Identified object should not be created.");

		((WatcherManager) this.manager).create(this);
	}
	
	public void setData(String userName, String stockName, float boundValue, int boundType) {
		setUserName(userName);
		setStockName(stockName);
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

	public float getBoundValue() {
		return boundValue;
	}

	public void setBoundValue(float value) {
		this.boundValue = value;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

}
