package hu.ppke.itk.itkStock.dbCreate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class Upload {
	
	public static Connection conn = null;
	public static String url = "jdbc:mysql://localhost:3306/";
	public static String dbName = "test";
	public static String uname = "root";
	public static String passwd = "";
	public static String path = "data";
	
	public static void main(String[] args) {
		System.out.println("MySQL uploading script started");
		System.out.println("Url: " + url);
		try {
			conn = DriverManager.getConnection(url + dbName, uname, passwd);
			System.out.println("Connected");
			loadCommands();
			loadDatas();
			conn.close();
			System.out.println("Disconnected");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void loadCommands() throws SQLException {
		String source = "SqlCommands.txt";
		Statement stmt;
		stmt = conn.createStatement();
		String command = null;
		System.out.println("Loading commands...");
		FileReader fr;
		try {
			fr = new FileReader(source);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				command = br.readLine();
				System.out.print("The command is: " + command);
				if (!command.startsWith(" ")) {
					stmt.executeUpdate(command);
					System.out.println(" Done");
				} else
					System.out.println("");
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void loadDatas()
			throws Exception {
		String[] list = null;
		File dir = new File(path);
		File[] files = dir.listFiles();
		try {
			String s2 = files[0].toString() + ",";
			for (int i = 1; i < files.length; i++) {
				s2 += files[i].toString() + ",";
			}
			list = s2.split(",");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		String source;
		for (int i = 0; i < list.length; i++) {
			source = list[i];
			Statement stmt;
			stmt = conn.createStatement();
			String s = "";
			String[] temp;
			String c = ",";
			String papername;
			int per, date, time, close, volume;
			String command;
			FileReader fr = new FileReader(source);
			BufferedReader br = new BufferedReader(fr);
			br.readLine();
			System.out.println("Inserting " + source + " ...");
			while (br.ready()) {
				s = br.readLine();
				temp = s.split(c);
				papername = temp[0];
				per = Integer.parseInt(temp[1]);
				date = Integer.parseInt(temp[2]);
				time = Integer.parseInt(temp[3]);
				close = Integer.parseInt(temp[4]);
				volume = Integer.parseInt(temp[5]);//long
				command = "INSERT INTO StockData values('" + papername + "',"
						+ per + "," + date + "," + time + "," + close + ","
						+ volume + ");";
				stmt.executeUpdate(command);
			}
			br.close();
			fr.close();
			System.out.println("Done!");
		}
	}
}

