package hu.ppke.itk.itkStock.SaveDailyDatas;

import hu.ppke.itk.itkStock.server.db.historicData.StockDate;
import hu.ppke.itk.itkStock.server.db.historicData.StockTime;
import hu.ppke.itk.itkStock.server.db.historicData.Transaction;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Aktiális napi adatok beolvasásáért felelős szál
 * @author ki-csen
 *
 */
public class DailyDataListener extends Thread {
	protected Date nextupdate;
	protected StockDataManager manager;
	protected int sleep = 6000;
	protected int period = 12000;
	
	/**
	 * @param num Szringként beolvasott szám
	 * @return Megtisztított szrting (Eltávolíja a space, vessző, + és - karaktereket.)
	 */
	public static String cleanNumber(String num) {
		return num.trim().replaceAll(",", "").replaceAll(" ", "")
				.replaceAll("\\+", "").replaceAll("\\-", "");
	}

	/**
	 * @param manager Memőriában tárolt kötéseket kezelő Manager osztály.
	 */
	public DailyDataListener(StockDataManager manager) {
		super();
		this.nextupdate = new Date();
		this.manager = manager;
	}

	/**
	 * @param manager Memőriában tárolt kötéseket kezelő Manager osztály.
	 * @param sleep Várakozás két futás között. (Thread.sleep()) [ms]
	 * @param period Várakozás két beolvasás között.(Read from website.) [ms]
	 */
	public DailyDataListener(StockDataManager manager, int sleep, int period) {
		super();
		this.nextupdate = new Date();
		this.manager = manager;
		this.sleep = sleep;
		this.period = period;
	}
	
	@Override
	public void run() {
		boolean interrupted = false;
		while (!interrupted) {
			//System.out.println(this.nextupdate.compareTo(new Date()) <= 0);
			try {
				/// Ha elértük a következő beolvasás időpontját.
				if (this.nextupdate.compareTo(new Date()) <= 0) {
					this.nextupdate = setNextUpdate();
					///Kötések beolvasása a portfolio.hu-ról.
					readDailyDataFromPF();
				}
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				interrupted = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ImplementationLogicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Kötések beolvasása a portfolio.hu-ról.
	 * 
	 * Beolvasott kötéseket hozzáadja a Manager átmeneti tárolójához, majd ez után próbálja rögzíteni azokat. 
	 * Erre a beolvasott és még nem tárolt intervallum megállapítsáa maitt van szűkség.
	 * 
	 * @throws IOException
	 * @throws ImplementationLogicException Átvett osztályok nem jól képezik le a valóságot.
	 */
	protected void readDailyDataFromPF() throws IOException,
			ImplementationLogicException {
		Transaction Stransaction = null;
		Document doc = Jsoup
				.connect("http://www.portfolio.hu/tozsde/koteslista.tdp")
				.data("listmax", "3").userAgent("Mozilla")
				.cookie("auth", "token").timeout(3000).post();
		Element stockdate = doc.select("td.l").get(0);
		Element stockdata = doc.getElementsByTag("pre").get(1);
		String date = stockdate.html();
		date = date.replace("&nbsp;", "");
		date = date.substring(0, 11);
		StockDate Sdate = new StockDate(Integer.parseInt(date.substring(0, 4)),
				Integer.parseInt(date.substring(5, 7)), Integer.parseInt(date
						.substring(8, 10)));

		Scanner scanner = new Scanner(stockdata.html()).useDelimiter("\n");
		while (scanner.hasNext()) {
			String line = scanner.next();
			line = line.replaceAll(" +", " ");
			String[] datas_of_line = line.split(" ");
			StockTime Stime = new StockTime(Integer.parseInt(datas_of_line[0]
					.substring(0, 2)), Integer.parseInt(datas_of_line[0]
					.substring(3, 5)), Integer.parseInt(datas_of_line[0]
					.substring(6, 8)));
			if (!datas_of_line[4].contains("%")) {
				Stransaction = new Transaction(
						Integer.parseInt(cleanNumber(datas_of_line[2])),
						Integer.parseInt(cleanNumber(datas_of_line[4])));
			} else {
				Stransaction = new Transaction(
						Integer.parseInt(cleanNumber(datas_of_line[3])),
						Integer.parseInt(cleanNumber(datas_of_line[5])));
			}

			this.manager.add(datas_of_line[1], Sdate, Stime, Stransaction);
		}
		this.manager.comit();

		scanner.close();

	}

	/**
	 * @return következp frissítés lekérése. Itt lehet majd implementálni, hogy éjjel és hétvégén nincs kereskedés.
	 */
	protected Date setNextUpdate() {
		// Nem teljes implementció - Mikor nincs kereskedés
		return new Date(new Date().getTime() + this.period);
	}
	
	/**
	 * @return Beolvaásá periódusa. [ms]
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @return következő beolvasás
	 */
	public Date getNextupdate() {
		return nextupdate;
	}

	/**
	 * @return Thread.sleep időtartama [ms]
	 */
	public int getSleep() {
		return sleep;
	}
}
