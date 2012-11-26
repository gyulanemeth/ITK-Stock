package hu.ppke.itk.itkStock.gui;

import hu.ppke.itk.itkStock.gui.chart.candlestick.CandleStickChart;
import hu.ppke.itk.itkStock.server.db.historicData.StockDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class IntervalDataCandleComposite extends Composite{

	public IntervalDataCandleComposite(Composite parent, int style) {
		super(parent, style);
		//TODO make a dialog where the user can specify the paper_name and the date
//		CandleStickChart csc = new CandleStickChart("MTELEKOM", new StockDate(2010, 12, 30));
//		JFreeChart chart = csc.createChart();
//	    ChartComposite comp = new ChartComposite(this, SWT.NONE);
//	    comp.setChart(chart);
//	    comp.pack();
//	    comp.setSize(800, 550);
	}

}
