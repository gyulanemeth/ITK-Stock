package hu.ppke.itk.itkStock.gui;

import org.eclipse.swt.widgets.Composite;
import swing2swt.layout.BorderLayout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import java.util.ResourceBundle;

public class StockGameComposite extends Composite {
	private static String money_postfix = " Ft" ;
	private Text text;
	
	private List list ;
	private Label lblAvailableMoney;
	private Label lblUnitCost;
	private Label labelAvailableAmount;
	private Label lblCost;
	private Button btnSell;
	private Button btnBuy;
	
	private int getAvailableAmount(int id)
	{
		// TODO
		return 100 ;
	}
	
	private int getUnitPrice(int id) 
	{
		// TODO
		return 20 ;
	}
	
	private int getUserMoney() 
	{
		// TODO
		return 100000 ;
	}
	
	private void updateUI()
	{
		String s = text.getText() ;
		int amount = 0 ;
		try
		{
			amount = Integer.parseInt(s) ;
		}
		catch (Exception e)
		{
			// Nem szám
			btnSell.setEnabled(false) ;
			btnBuy.setEnabled(false) ;
			lblCost.setText("");
			return ;
		}

		int availableAmount = getAvailableAmount(list.getSelectionIndex()) ;
		labelAvailableAmount.setText(Integer.toString(availableAmount));
		labelAvailableAmount.pack();
		if (amount > availableAmount)
		{
			btnSell.setEnabled(false) ;
		}
		else
		{
			btnSell.setEnabled(true) ;
		}
		int unitPrice = getUnitPrice(list.getSelectionIndex()) ;		
		lblUnitCost.setText(Integer.toString(unitPrice)+money_postfix);
		lblUnitCost.pack();
	
		int price = amount * unitPrice ;
		lblCost.setText(Integer.toString(price)+money_postfix);
		lblCost.pack();
		
		int userMoney = getUserMoney() ;
		lblAvailableMoney.setText(Integer.toString(unitPrice)+money_postfix);
		lblAvailableMoney.pack() ;
		
		if (price < userMoney)
		{
			btnBuy.setEnabled(true) ;
		}
		else
		{
			btnBuy.setEnabled(false) ;			
		}
	}
	
	public StockGameComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new BorderLayout(0, 0));
		
		list = new List(this, SWT.BORDER);
		list.setLayoutData(BorderLayout.WEST);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(BorderLayout.CENTER);
		composite.setLayout(new GridLayout(2, false));
		
		Label lblAvailableMoneyTxt = new Label(composite, SWT.NONE);
		lblAvailableMoneyTxt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAvailableMoneyTxt.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.lblAvailableMoneyTxt.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		lblAvailableMoney = new Label(composite, SWT.NONE);
		
		Label lblUnitCostTxt = new Label(composite, SWT.NONE);
		lblUnitCostTxt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUnitCostTxt.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.lblUnitCostTxt.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		lblUnitCost = new Label(composite, SWT.NONE);
		
		Label lblAvailableAmountTxt = new Label(composite, SWT.NONE);
		lblAvailableAmountTxt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAvailableAmountTxt.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.lblAvailableAmountTxt.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		labelAvailableAmount = new Label(composite, SWT.NONE);
		
		Label lblAmountTxt = new Label(composite, SWT.NONE);
		lblAmountTxt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAmountTxt.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.lblAmountTxt.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblCostTxt = new Label(composite, SWT.NONE);
		lblCostTxt.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCostTxt.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.lblCostTxt.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		lblCost = new Label(composite, SWT.NONE);
		
		btnSell = new Button(composite, SWT.NONE);
		btnSell.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		btnSell.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.btnSell.text")); //$NON-NLS-1$ //$NON-NLS-2$
		
		btnBuy = new Button(composite, SWT.NONE);
		btnBuy.setText(ResourceBundle.getBundle("hu.ppke.itk.itkStock.gui.messages").getString("StockGameComposite.btnBuy.text")); //$NON-NLS-1$ //$NON-NLS-2$

		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				updateUI() ;
			}
		});

		updateUI() ;
		
	}


}
