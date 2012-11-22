package hu.ppke.itk.itkStock.server.id;

import hu.ppke.itk.itkStock.SaveDailyDatas.StockDataObserver;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO: Gábor, légyszíves implementáld az árak kezdeti feltöltését és későbbi frissítését. A StockDataManage osztály
// szól, amikor kell frissíteni, ekkor fut le a notify fv.

/**
 * 2 hashmap-et használó osztály, tárolja a kulcs-érték és az érték-kulcs
 * párokat Az initializeIdentifiers metódussal kell beállítani a kezdőértékeket
 * A put és getId metódusokkal is lehet új kulcs-érték párokat eltárolni
 */
public class StockId implements StockDataObserver {

	private static Map<Short, String> keyToValue = new HashMap<Short, String>();
	private Map<String, Short> valueToKey = new HashMap<String, Short>();
	private short key = 1;
	private boolean filled = false;

	/**
	 * Default constructor Call initializeIdentifiers/put/getId to add values
	 */
	public StockId() {

	}

	/**
	 * Constructor with String array, fills up the hashmaps
	 * 
	 * @param names
	 *            Name of stocks
	 */
	public StockId(String[] names) {
		initializeIdentifiers(names);
	}

	/**
	 * Initializes the maps with stock names
	 * 
	 * @param names
	 *            Stock names, every name will get a unique 2 byte (short) key
	 * @return Returns a set of keys
	 */
	public Set<Short> initializeIdentifiers(String[] names) {
		keyToValue.clear();
		valueToKey.clear();
		key = 1;
		for (String value : names) {
			put(value);
		}
		return keyToValue.keySet();
	}

	/**
	 * Gets the identifier of a stock name
	 * 
	 * @param value
	 *            Name of stock
	 * @param putNew
	 *            Flag to put new value if it wasn't found
	 * @return Returns the key assigned to the stock Return 0 if value was not
	 *         found and putNew is false
	 */
	public short getId(String value, boolean putNew) {
		if (valueToKey.containsKey(value)) {
			return valueToKey.get(value);
		} else if (putNew) {
			return put(value);
		}
		return 0;
	}

	/**
	 * Gets identifier of a stock name
	 * 
	 * @param value
	 *            Name of stock
	 * @return Returns the key assigned of the stock Returns 0 if value was not
	 *         found
	 */
	public short getId(String value) {
		return getId(value, false);
	}

	/**
	 * Stores a stock name (if not stored)
	 * 
	 * @param value
	 *            Name of stock
	 * @return Returns the key assigned to the stock
	 */
	public short put(String value) {
		if (keyToValue.containsValue(value) == false) {
			keyToValue.put(key, value);
			return valueToKey.put(value, key++);
		}
		return 0;
	}

	/**
	 * Returns stock name
	 * 
	 * @param key
	 *            Key identifying the stock name
	 * @return Name of stock
	 */
	public String getValue(short key) {
		return keyToValue.get(key);
	}

	@Override
	public void notify(List<String> updatedStocks) {
		// TODO: stock names, ids and prices should be updated when this
		// function is invoked
		
		if(!filled) {
			initializeIdentifiers((String[]) updatedStocks.toArray());
			// TODO: árak feltöltése
			filled = true;
		} else {
			// TODO: ha nem kezdeti feltöltés, nevek, árak frissítése
		}
		
	}
	
	public static Collection<String> getStocks(){
		return keyToValue.values();
	}

}
