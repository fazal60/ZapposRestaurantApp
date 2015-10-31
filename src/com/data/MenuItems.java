package com.data;

import java.util.HashMap;

/**
 * 
 * @author shahid
 * Class holding data for all items
 */

public class MenuItems {

	HashMap<String, Double> item;
	
	public MenuItems()
	{
		this.item=new HashMap<String, Double>();
	}
	
	public void setItem(String key, double val)
	{
		this.item.put(key, val);
	}
	
	public HashMap<String, Double>  getItem()
	{
		return this.item;
	}
	
}
