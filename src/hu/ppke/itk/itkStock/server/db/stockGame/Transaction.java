package hu.ppke.itk.itkStock.server.db.stockGame;

import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObject;
import hu.ppke.itk.itkStock.server.db.dbAccess.BusinessObjectException;

import java.sql.SQLException;

public class Transaction extends BusinessObject {

	public Transaction(TransactionManager manager, int id) {
		super(manager, id);
	}

	@Override
	public boolean get() throws SQLException, BusinessObjectException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void update() throws SQLException, BusinessObjectException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void create() throws SQLException, BusinessObjectException {
		// TODO Auto-generated method stub
		
	}

}
