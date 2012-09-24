package hu.ppke.itk.itkStock.dbaccess;

public class DatabaseException extends Exception
{

	public DatabaseException()
	{
		this.doLog(""); //dummy
		// TODO Auto-generated constructor stub
	}
	
	public DatabaseException(String msg)
	{
		super(msg);
		this.doLog(msg);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 */
	///TODO: ...
	private void doLog(String msg)
	{
		///TODO: implement logging
	}
	
	private static final long serialVersionUID = 2890161709971001598L;

}
