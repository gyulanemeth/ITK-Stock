package hu.ppke.itk.itkStock.server.id;

import java.util.HashMap;
import java.util.Map;

public class StockId {
	
	private static Map<Short,String> keyToValue = new HashMap<>();
	private static Map<String,Short> valueToKey = new HashMap<>();
	private static short key = 0;
	
	public static void initializeIdentifiers(String[] names){
		for(String value : names){
			keyToValue.put(key++, value);
			valueToKey.put(value, key);
		}
	}
	
	public static short getID(String value){
		if(valueToKey.containsKey(value)){
			return valueToKey.get(value);
		}else{
			keyToValue.put(key, value);
			return valueToKey.put(value, key++);
		}
	}
	
	public static String getValue(short key){
		return keyToValue.get(key);
	}
	
	public static void main(String[] args) {
		StockId.getID("hello");
		StockId.getID("hello1");
		StockId.getID("hello2");
		StockId.getID("hello3");
		
		for(short s = 0; s < 4; ++s){
			System.out.println(StockId.getValue(s));
		}
	}
	
}
