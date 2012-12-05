package hu.ppke.itk.itkStock.server.db.historicData;

import java.util.*;
import java.sql.*;

import hu.ppke.itk.itkStock.server.db.dbAccess.DatabaseConnector;

public class StockData {
	/**
		Fetch daily transaction data from the server about a certain stock.
		@param    ticker Name of the stock.
		@param    date   The date on which the transactions happened. Only the year-month-day bit of the StockDate object is taken into account.
		@return          A Map holding the transactions for which transaction->date = date and transaction->stock = ticker
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > fetchData(String ticker, StockDate date) throws SQLException {
		return fetch(new String[]{ticker}, date, date);
	}
	/**
		Fetch transaction data from the server about a certain interval, about a certain ticker.

		@param    ticker  Names of the stock.
		@param    from    The start date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@param    to      The end date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@return   A Map holding the transactions for which: from <= transaction->datettime <= to and transaction->ticker = ticker.
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > >  fetchData(String ticker, StockDate from, StockDate to) throws SQLException {
		return fetch(new String[]{ticker}, from, to);
	}


	/**
		Fetch daily transaction data from the server about a certain stock.
		@param    tickers Names of the stocks.
		@param    from    The start date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@param    to      The end date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@return           A Map holding the transactions for which transaction->date = date and transaction->stock in tickers
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > fetchData(String[] tickers, StockDate date) throws SQLException {
		return fetch(tickers, date, date);
	}
	/**
		Fetch transaction data from the server about a certain interval, about a set of tickers.

		@param    tickers Names of the stocks.
		@param    from    The start date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@param    to      The end date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@return   A Map holding the transactions for which: from <= transaction->datettime <= to and transaction->ticker in tickers.
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > >  fetchData(String[] tickers, StockDate from, StockDate to) throws SQLException {
		return fetch(tickers, from, to);
	}

	/**
		Fetch all transaction data about a specific day.

		@param    date   The date. Only the year-month-day bit of the StockDate object is taken into account.
		@return   A Map holding the transactions for which: transaction->date = date.
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > fetchData(StockDate date) throws SQLException {
		return fetch(null, date, date);
	}
	/**
		Fetch all transaction data about a specific interval

		@param    from    The start date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@param    to      The end date of the interval, inclusive. Only the year-month-day bit of the StockDate object is taken into account.
		@return   A Map holding the transactions for which: from <= transaction->datettime <= to.
	*/
	public static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > >  fetchData(StockDate from, StockDate to) throws SQLException {
		return fetch(null, from, to);
	}


	/**
		The Master Function, all other methods are just thin wrappers for this one.

		Assumes the database to have CREATE TABLE StockData(papername VARCHAR(20), per INT, date INT, time INT, close INT, volume INT);
	*/
	protected static Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > fetch(String[] tickers, StockDate from, StockDate to) throws SQLException {
		Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > Result = new HashMap<String, SortedMap<StockDate, SortedMap<StockTime, Transaction>>>();
		DatabaseConnector dbc = new DatabaseConnector();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try {
			dbc.initConnection();
			if(tickers == null || tickers.length < 1) {
				stmt = dbc.prepareStatement("select paper_name, date, time, close, volume from StockData where date between ? and ?");
			}
			else {
				StringBuilder sb = new StringBuilder("?");
				for(int i=1; i<tickers.length; ++i) sb.append(", ?");
				stmt = dbc.prepareStatement("select paper_name, date, time, close, volume from StockData where date between ? and ? and paper_name in ("+sb.toString()+")");

				for(int i=0; i<tickers.length; ++i) stmt.setString(i+3, tickers[i]);
			}
			stmt.setInt(1, from.getYear()*10000 + from.getMonth()*100 + from.getDay() );
			stmt.setInt(2,   to.getYear()*10000 +   to.getMonth()*100 +   to.getDay() );
			rs = stmt.executeQuery();

			String stock;
//			int date;
//			int time;
//			int price;
//			int volume;
			StockDate key;
			SortedMap<StockDate, SortedMap<StockTime, Transaction> > tree;
			StockTime timekey;
			SortedMap<StockTime, Transaction> timetree;
			Transaction value = null;
			while(rs.next()) {
				stock = rs.getString("paper_name");
				key = new StockDate( rs.getInt("date") );
				timekey = new StockTime( rs.getInt("time") );
				value = new Transaction( rs.getDouble("close"), rs.getInt("volume") );
				if( Result.containsKey(stock) ) {
					tree = Result.get(stock);
					if( tree.containsKey(key) ) {
						timetree = tree.get(key);
						if(timetree.containsKey(timekey)) {
							value = Transaction.merge( timetree.get(timekey), value );
						}
					}
					else {
						timetree = new TreeMap<StockTime, Transaction>();
						tree.put(key, timetree);
					}
				}
				else {
					tree=new TreeMap<StockDate, SortedMap<StockTime, Transaction> >();
					timetree = new TreeMap<StockTime, Transaction>();
					Result.put( stock, tree );
					tree.put(key, timetree);
				}
				timetree.put(timekey, value);
			}
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if(dbc != null) dbc.closeConnection();
			if(stmt != null) stmt.close();
		}

		return Result;
	}

	/**
		Demo.
	*/
	public static void main(String[] args) {
		System.out.println("Usage examples for StockData.fetchData() family");
		try {
			Map<String, SortedMap<StockDate, SortedMap<StockTime, Transaction> > > db;

			db = StockData.fetchData(new StockDate(2011,01,05), new StockDate(2014,01,05));
			System.out.println(db.toString());

			db = StockData.fetchData(new StockDate(2011,01,05));
			System.out.println(db.toString());

			db = StockData.fetchData("ECONET", new StockDate(2011,01,05));
			System.out.println(db.toString());

			db = StockData.fetchData("ECONET", new StockDate(2011,01,05), new StockDate(2015,01,05));
			System.out.println(db.toString());

			db = StockData.fetchData(new String[]{"ECONET", "BOOK"}, new StockDate(2011,01,05));
			System.out.println(db.toString());

			db = StockData.fetchData(new String[]{"ECONET", "BOOK"}, new StockDate(2011,01,05), new StockDate(2015,01,05));
			System.out.println(db.toString());

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
