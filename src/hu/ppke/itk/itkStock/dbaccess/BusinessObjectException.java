package hu.ppke.itk.itkStock.dbaccess;

/**
 * 'BusinessObject'-ekre, es azok 'Manager' osztalyaira vonatkozo kivetelosztaly.
 * 
 * @see BusinessObject
 * @see AbstractManager
 */

public class BusinessObjectException extends Exception
{

	public BusinessObjectException()
	{
		this.doLog(""); //dummy
		// TODO Auto-generated constructor stub
	}
	
	public BusinessObjectException(String msg)
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
