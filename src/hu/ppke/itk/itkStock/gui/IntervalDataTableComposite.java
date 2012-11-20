package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class IntervalDataTableComposite extends Composite{

	public IntervalDataTableComposite(Composite parent, int style) {
		super(parent, style);
		// TODO Auto-generated constructor stub
		Label label = new Label(this, SWT.NONE);
		label.setText("Intervallum adatok táblázatos megjelenítése");
		label.pack();
	}

}
