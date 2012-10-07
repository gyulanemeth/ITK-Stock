package hu.ppke.itk.itkStock.server.id;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StockId {
	
	private static Map<Short,String> keyToValue = new HashMap<>();
	private static Map<String,Short> valueToKey = new HashMap<>();
	private static short key = 1;
	
	/**
	 * Initializes the maps with stock names
	 * @param names
	 * Stock names, every name will get a unique 2 byte (short) key
	 * @return
	 * Returns a set of keys
	 */
	public static Set<Short> initializeIdentifiers(String[] names){
		for(String value : names){
			put(value);
		}
		return keyToValue.keySet();
	}
	
	/**
	 * Gets the identifier of a stock name
	 * 
	 * @param value
	 * Name of stock
	 * @return
	 * Returns the key assigned to the stock
	 */
	public static short getID(String value){
		if(valueToKey.containsKey(value)){
			return valueToKey.get(value);
		}else{
			return put(value);
		}
	}
	
	/**
	 * Stores a stock name (if not stored)
	 * @param value
	 * Name of stock
	 * @return
	 * Returns the key assigned to the stock
	 */
	public static short put(String value){
		if(keyToValue.containsValue(value) == false){
			keyToValue.put(key, value);
			return valueToKey.put(value, key++);
		}
		return 0;
	}
	
	/**
	 * Returns stock name
	 * @param key
	 * Key identifying the stock name
	 * @return
	 * Name of stock
	 */
	public static String getValue(short key){
		return keyToValue.get(key);
	}
	
}
