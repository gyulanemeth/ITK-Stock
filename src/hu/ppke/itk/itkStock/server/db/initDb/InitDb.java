package hu.ppke.itk.itkStock.server.db.initDb;

import hu.ppke.itk.itkStock.server.ServerSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class InitDb {
	private static Connection conn = null;

	private static String sqlCommandsPath = "SqlCommands.txt";
	private static String dataFolderPath = "data";

	public static void main(String[] args) {
		String dbUrl = ServerSettings.INSTANCE.getDbUrl();
		String dbUser = ServerSettings.INSTANCE.getDbUser();
		String dbPass = ServerSettings.INSTANCE.getDbPass();

		System.out.println("MySQL uploading script started");
		System.out.println("Url: " + dbUrl);
		try {
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
			System.out.println("Connected");
			loadCommands();
			loadDatas();
			conn.close();
			System.out.println("Disconnected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void loadCommands() {
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
			 br = new BufferedReader(new FileReader(sqlCommandsPath));
			 String line = null;
			 while ((line = br.readLine()) != null) {
				 sb.append(line);
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			
			System.out.println("Loading commands...");
			String[] sqlCommands = sb.toString().split(";");
			for (int i = 0; i < sqlCommands.length; i++) {
				System.out.println("The command is: " + sqlCommands[i]);
				try {
					stmt.executeUpdate(sqlCommands[i]);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void loadDataFromFile (File file) throws IOException, SQLException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String papername;
		int date, time;
		double close;
		long volume;
		
		Statement stmt = conn.createStatement();
		
		String line = br.readLine(); //skipping the header line
		while ((line = br.readLine()) != null) {
			String[] lineParts = line.split(",");
			papername = lineParts[0].trim();
			date = Integer.parseInt(lineParts[2].trim());
			time = Integer.parseInt(lineParts[3].trim());
			close = Double.parseDouble(lineParts[4].trim());
			volume = Long.parseLong(lineParts[5].trim());
			
			String command = "INSERT INTO StockData values('" + papername + "',"
					+ date + "," + time + "," + close + ","
					+ volume + ");";
			
			stmt.executeUpdate(command);
		}
		
		stmt.close();
	}

	private static void loadDatas() throws Exception {
		File dir = new File(dataFolderPath);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			loadDataFromFile(files[i]);
			System.out.println("uploaded: " + (i+1) + "/" + files.length);
		}
	}
}
