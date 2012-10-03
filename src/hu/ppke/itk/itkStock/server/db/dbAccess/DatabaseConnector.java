package hu.ppke.itk.itkStock.server.db.dbAccess;

import hu.ppke.itk.itkStock.server.ServerSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Ez az osztaly valositja meg az adatbazis kapcsolatot. A konstruktor meghivasa
 * nem elegendo a kapcsolat kiepitesehez. Szukseges az 'initConnection()'
 * tagfuggveny meghivasa.
 * 
 * @see #initConnection() A kapcsolat lezarasahoz a 'closeConnection()'
 *      fuggvenyt hivjuk meg. Ilyenkor az adatbazis csatlakozas lezarasa mellett
 *      felszabadulnak a 'PreparedStatement'-jeink is.
 * @see #closeConnection()
 * @see #connection
 * @see #preparedStatements
 */

public class DatabaseConnector {
	private Collection<PreparedStatement> preparedStatements = new ArrayList<PreparedStatement>();

	private Connection connection = null;
	private boolean initialized = false;

	public synchronized void initConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		String url = ServerSettings.INSTANCE.getDbUrl();
		String user = ServerSettings.INSTANCE.getDbUser();
		String pass = ServerSettings.INSTANCE.getDbPass();
		this.connection = DriverManager.getConnection(url, user, pass);
		this.initialized = true;
	}

	public synchronized void closeConnection() throws SQLException {
		if (!this.initialized)
			return;

		for (PreparedStatement ps : this.preparedStatements) {
			if (ps != null)
				ps.close();
		}

		if (this.connection != null)
			this.connection.close();
	}

	public PreparedStatement prepareStatement(String sqlString) throws SQLException {
		if (this.initialized) {
			PreparedStatement temp = this.connection.prepareStatement(sqlString);
			this.preparedStatements.add(temp);
			return temp;
		}

		return null;
	}

	protected void finalize() throws Throwable {
		this.closeConnection();
		super.finalize();
	}

	public boolean isInitialized() {
		return this.initialized;
	}
}
