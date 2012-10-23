package hu.ppke.itk.itkStock.server.db.stockWatcher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import hu.ppke.itk.itkStock.server.db.dbAccess.AbstractManager;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;
import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

/**
 * This class is handling the database operations for watchers.
 * 
 * @see Watcher
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
	private PreparedStatement clear = null;

	public WatcherManager(DatabaseConnector dbConnector) throws SQLException {
		super(dbConnector);

		if (this.dbConnector == null || !this.dbConnector.isInitialized())
			throw new SQLException("DatabaseConnector is not initialized.");

		addWatcher = this.dbConnector.prepareStatement("INSERT INTO watchers (user_id, paper_name, boundvalue, boundtype) VALUES (?, ?, ?, ?)");
		removeWatcher = this.dbConnector.prepareStatement("DELETE FROM watchers WHERE id =?");
		updateWatcher = this.dbConnector.prepareStatement("UPDATE watchers SET username = ?, paper_name = ?, boundvalue = ?, boundtype = ? WHERE id = ?");
		getWatcherById = this.dbConnector.prepareStatement("SELECT user_id, paper_name, boundvalue, boundtype FROM watchers WHERE id = ?");
		getWatchersByUser = this.dbConnector.prepareStatement("SELECT * FROM watchers WHERE user_id = ?");
		getWatchersByStock = this.dbConnector.prepareStatement("SELECT * FROM watchers WHERE stock = ?");
		setBoundValue = this.dbConnector.prepareStatement("UPDATE watchers SET boundvalue = ? WHERE id = ?");
		setBoundType = this.dbConnector.prepareStatement("UPDATE watchers SET boundtype = ? WHERE id = ?");
		checkWatcherExistenceById = this.dbConnector.prepareStatement("SELECT IF( ( SELECT COUNT( * ) FROM watchers WHERE id = ? ) = 0, FALSE, TRUE )");
		clear = this.dbConnector.prepareStatement("DELETE * FROM watchers");
	}

	public WatcherManager(ResultSet rs) {
		super(new DatabaseConnector());
		resultSet = rs;
	}

	@Override
	public void update(Watcher bo) throws SQLException {
		updateWatcher.setInt(1, bo.getId());
		updateWatcher.setString(2, bo.getPaperName());
		updateWatcher.setDouble(3, bo.getBoundValue());
		updateWatcher.setInt(4, bo.getBoundType());
		updateWatcher.setInt(5, bo.getId());
		updateWatcher.executeUpdate();
	}

	@Override
	public Watcher get(int id) throws SQLException, BusinessObjectException {
		if (!this.checkUserExistenceById(id))
			throw new BusinessObjectException("User with id = " + id + " does not exist.");

		getWatcherById.setInt(1, id);
		resultSet = getWatcherById.executeQuery();

		if (!this.resultSet.first())
			return null;

		return getWatcherFromSet();
	}

	@Override
	public void create(Watcher bo) throws SQLException, BusinessObjectException {
		if (bo.getId() != 0 && this.checkUserExistenceById(bo.getId()))
			throw new BusinessObjectException("Watcher with id = " + bo.getId() + " already exists.");

		this.addWatcher(bo.getUserId(), bo.getPaperName(), bo.getBoundValue(), bo.getBoundType());
		bo.get();

	}

	public void addWatcher(int userId, String paperName, double boundValue, int boundType) throws SQLException {
		addWatcher.setInt(1, userId);
		addWatcher.setString(2, paperName);
		addWatcher.setDouble(3, boundValue);
		addWatcher.setInt(4, boundType);
		addWatcher.executeUpdate();
	}

	public void removeWatcher(int id) throws SQLException {
		removeWatcher.setInt(1, id);
		removeWatcher.executeUpdate();
	}

	public void setBoundValue(int id, double boundValue) throws SQLException {
		setBoundValue.setDouble(1, boundValue);
		setBoundValue.setInt(2, id);
		setBoundValue.executeUpdate();
	}

	public void setBoundType(int id, int boundType) throws SQLException {
		setBoundType.setInt(1, boundType);
		setBoundType.setInt(2, id);
		setBoundType.executeUpdate();
	}

	public WatcherSetIterator getWatchersByUser(int userId) throws SQLException {
		getWatchersByUser.setInt(1, userId);
		ResultSet rs = getWatchersByUser.executeQuery();
		return new WatcherSetIterator(rs);
	}

	public WatcherSetIterator getWatchersByStock(String paperName) throws SQLException {
		getWatchersByStock.setString(1, paperName);
		ResultSet rs = getWatchersByStock.executeQuery();
		return new WatcherSetIterator(rs);
	}

	public boolean checkUserExistenceById(int id) throws SQLException {
		checkWatcherExistenceById.setInt(1, id);
		resultSet = checkWatcherExistenceById.executeQuery();

		return resultSet.first();
	}

	public Watcher getWatcherFromSet() throws SQLException {
		Watcher ret = new Watcher(this, resultSet.getInt(1));
		ret.setData(resultSet.getInt(2), resultSet.getString(3), resultSet.getDouble(4), resultSet.getInt(5));

		return ret;
	}

	public void clear() throws SQLException {
		clear.executeUpdate();
	}

}
