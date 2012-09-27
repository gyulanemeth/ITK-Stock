package hu.ppke.itk.itkStock.watcher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import hu.ppke.itk.itkStock.dbaccess.AbstractManager;
import hu.ppke.itk.itkStock.dbaccess.BusinessObjectException;
import hu.ppke.itk.itkStock.dbaccess.DatabaseConnector;

/**
 * Az árfolyamfigyelõket kezelõ osztály.
 */
public class WatcherManager extends AbstractManager<Watcher> {

	private PreparedStatement addWatcher = null;
	private PreparedStatement removeWatcher = null;
	private PreparedStatement updateWatcher = null;
	private PreparedStatement getWatcherById = null;
	private PreparedStatement getWatchersByStock = null;
	private PreparedStatement getWatchersByUser = null;
	private PreparedStatement setBoundValue = null;
	private PreparedStatement setBoundType = null;
	private PreparedStatement checkWatcherExistenceById = null;

	public WatcherManager(DatabaseConnector dbConnector) throws SQLException {
		super(dbConnector);

		if (this.dbConnector == null || !this.dbConnector.isInitialized())
			throw new SQLException("DatabaseConnector is not initialized.");

		addWatcher = this.dbConnector.prepareStatement("INSERT INTO watchers (username, stock, boundvalue, boundtype) VALUES (?, ?, ?, ?)");
		removeWatcher = this.dbConnector.prepareStatement("DELETE FROM watchers WHERE id =?");
		updateWatcher = this.dbConnector.prepareStatement("UPDATE watchers SET username = ?, stock = ?, boundvalue = ?, boundtype = ? WHERE id = ?");
		getWatcherById = this.dbConnector.prepareStatement("SELECT username, stock, boundvalue, boundtype FROM watchers WHERE id = ?");
		getWatchersByUser = this.dbConnector.prepareStatement("SELECT * FROM watchers WHERE username = ?");
		getWatchersByStock = this.dbConnector.prepareStatement("SELECT * FROM watchers WHERE stock = ?");
		setBoundValue = this.dbConnector.prepareStatement("UPDATE watchers SET boundvalue = ? WHERE id = ?");
		setBoundType = this.dbConnector.prepareStatement("UPDATE watchers SET boundtype = ? WHERE id = ?");
		checkWatcherExistenceById = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM watchers WHERE id = ? ) = 0, FALSE, TRUE )");

	}

	public WatcherManager(ResultSet rs) {
		super(new DatabaseConnector());
		_resultSet = rs;
	}

	@Override
	public void update(Watcher bo) throws SQLException {
		updateWatcher.setString(1, bo.getUserName());
		updateWatcher.setString(2, bo.getStockName());
		updateWatcher.setFloat(3, bo.getBoundValue());
		updateWatcher.setInt(4, bo.getBoundType());
		updateWatcher.setInt(5, bo.getId());
		updateWatcher.executeUpdate();
	}

	@Override
	public Watcher get(int id) throws SQLException, BusinessObjectException {
		if (!this.checkUserExistenceById(id))
			throw new BusinessObjectException("User with id = " + id + " does not exist.");

		getWatcherById.setInt(1, id);
		_resultSet = getWatcherById.executeQuery();

		if (!this._resultSet.first())
			return null;

		return getWatcherFromSet();
	}

	@Override
	public void create(Watcher bo) throws SQLException, BusinessObjectException {
		if (bo.getId() != 0 && this.checkUserExistenceById(bo.getId()))
			throw new BusinessObjectException("Watcher with id = " + bo.getId() + " already exists.");

		this.addWatcher(bo.getUserName(), bo.getStockName(), bo.getBoundValue(), bo.getBoundType());
		bo.get();

	}

	public void addWatcher(String userName, String stockName, float boundValue, int boundType) throws SQLException {
		addWatcher.setString(1, userName);
		addWatcher.setString(2, stockName);
		addWatcher.setFloat(3, boundValue);
		addWatcher.setInt(4, boundType);
		addWatcher.executeUpdate();
	}

	public void removeWatcher(int id) throws SQLException {
		removeWatcher.setInt(1, id);
		removeWatcher.executeUpdate();
	}

	public void setBoundValue(int id, float boundValue) throws SQLException {
		setBoundValue.setFloat(1, boundValue);
		setBoundValue.setInt(2, id);
		setBoundValue.executeUpdate();
	}

	public void setBoundType(int id, int boundType) throws SQLException {
		setBoundType.setInt(1, boundType);
		setBoundType.setInt(2, id);
		setBoundType.executeUpdate();
	}

	public WatcherSetIterator getWatchersByUser(String userName) throws SQLException {
		getWatchersByUser.setString(1, userName);
		ResultSet rs = getWatchersByUser.executeQuery();
		return new WatcherSetIterator(rs);
	}

	public WatcherSetIterator getWatchersByStock(String stockName) throws SQLException {
		getWatchersByStock.setString(1, stockName);
		ResultSet rs = getWatchersByStock.executeQuery();
		return new WatcherSetIterator(rs);
	}

	public boolean checkUserExistenceById(int id) throws SQLException {
		checkWatcherExistenceById.setInt(1, id);
		_resultSet = checkWatcherExistenceById.executeQuery();

		return _resultSet.first();
	}

	public Watcher getWatcherFromSet() throws SQLException {
		Watcher ret = new Watcher(this, _resultSet.getInt(1));
		ret.setData(_resultSet.getString(2), _resultSet.getString(3), _resultSet.getFloat(4), _resultSet.getInt(5));

		return ret;
	}

}
