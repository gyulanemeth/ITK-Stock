package hu.ppke.itk.itkStock.server;

/**
 * A singleton for the global settings of the server, members should be initialized from a server config xml later.
 * If the config file is not found, one should be created with the default values.
 * 
 * @author Németh Gyula
 *
 */
public enum ServerSettings {
	INSTANCE;
	
	private String	dbHost = "localhost";
	private int		dbPort = 3306;
	private String	dbName = "itkStock";
	
	private String	dbUser = "itkStock";
	private String	dbPass = "itkStock";
	
	private ServerSettings() {
		// TODO Init from a serverConfig.xml file, if the file is not found, create one with default values.
	}
	
	
	public String getDbUrl () {
		return "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
	}
	
	public String getDbUser() {
		return dbUser;
	}
	
	public String getDbPass() {
		return dbPass;
	}
}
