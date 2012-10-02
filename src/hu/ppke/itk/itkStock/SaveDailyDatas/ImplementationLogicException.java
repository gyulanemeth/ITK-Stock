package hu.ppke.itk.itkStock.SaveDailyDatas;

/**
 * Implementáció és a valőság különbözőségéből adódó kivétel.
 * 
 * @see BusinessObject
 * @see AbstractManager
 */

public class ImplementationLogicException extends Exception
{
	private static final long serialVersionUID = 3724533177325530271L;
	public ImplementationLogicException()
	{
		this.doLog(""); //dummy
		// TODO Auto-generated constructor stub
	}
	
	public ImplementationLogicException(String msg)
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
	

}
