package org.tuc.interfaces;
import java.util.List;

public interface SearchInsert {
	/**
	 * Inserts key into the data structure
	 * @param key
	 */
	public void insert(int key);
	
	/**
	 * Searches for given key.
	 * @param key
	 * @return true if key is found, false otherwise
	 */
	public boolean searchKey(int key);


	/**
	 * Searches for keys between low and high (including low and high)
	 * @param low
	 * @param high
	 * @return List<Integer> of found keys
	 */
	public List<Integer> rangeQuery(int low, int high);
}
