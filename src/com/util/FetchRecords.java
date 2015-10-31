package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.data.MenuItems;
import com.driver.MainHelper;

/**
 * 
 * @author shahid
 * Fetching data from the URL and processing it
 */

public class FetchRecords
{
	
	public static int cnt=1;
	URLConnection yc;
	String str="";
	HashMap<String, Integer> futureDateMap;
	HashMap<String, MenuItems> map;
	HashMap<String, HashMap<String, Double>> itemMap;
	
	/**
	 * 
	 * @param yc
	 * Constructor initializing all members
	 */
	
	public FetchRecords(URLConnection yc)
	{
		this.yc=yc;
		this.futureDateMap=new HashMap<String, Integer>();
		this.map=new HashMap<String, MenuItems>();
		this.itemMap=new HashMap<String, HashMap<String,Double>>();
	}
	
	/**
	 * 
	 * @throws IOException
	 * Taking URL connection as a parameter and fetching records
	 */
	public void fetchFromURL() throws IOException
	{
		String inputLine;
		BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
		while ((inputLine = in.readLine()) != null) 
        {
            this.str=this.str+inputLine;
        }
		in.close();
		
	}
	
	
	/**
	 * 
	 * @param nDate
	 * @param excludeList
	 * @param budget
	 * @throws JSONException
	 * @throws ParseException
	 * 
	 * reading and processing the json data
	 */
	public void readJSONData(String nDate, ArrayList<String> excludeList, double budget) throws JSONException, ParseException
	{
		MainHelper mainObj=new MainHelper(budget);
		//reading JSON data from the given URL
        JSONObject first = new JSONObject(this.str);
        // contains one object
        JSONArray locArr = first.getJSONArray("menus");
        for(int i=0;i<locArr.length();i++)
        {
        	JSONObject locArrObj = locArr.getJSONObject(i); // cotains one "out" array
        	String dateObj = locArrObj.getString("date");
        	String menuId = locArrObj.getString("menuId");
        	JSONArray conferenceLocArr = locArrObj.getJSONArray("items");
	        // this array has two objects and each object has array
	        JSONObject o = null;
	        
	        
	        String dateInString=dateObj.substring(0,10);
	        if(dateInString.compareTo(nDate)>=0)
	        	futureDateMap.put(dateInString, 1);
	        
	        MenuItems miObj=new MenuItems();
	        
	        for (int j = 0; j < conferenceLocArr.length(); j++)
	        {
	           o = conferenceLocArr.getJSONObject(j); // it has one array

	           if(!o.getString("price").trim().replace("\"", "").equalsIgnoreCase("Free") && !o.getString("price").replace("$", "").equalsIgnoreCase("nothing"))
	           {
	        	   if(!mainObj.isExcluded(o.getString("name"),excludeList))
	        		     miObj.setItem(o.getString("name"), Double.parseDouble(o.getString("price").replace("$", "")));;
	           }
	           else
	           {
	        	   if(!mainObj.isExcluded(o.getString("name"),excludeList))
	        		   miObj.setItem(o.getString("name"), 0.0);
	           }
	        }
	        
	        if(!map.containsKey(dateInString+","+menuId))
	        		map.put(dateInString+","+menuId, miObj);
	        else
	        	System.out.println("date already present:"+dateObj);
	        cnt++;
        }
        
        for(Map.Entry<String, MenuItems> ent: map.entrySet())
        {
        	String dt=ent.getKey().split(",")[0];
        	if(!itemMap.containsKey(dt))
        	{
        		MenuItems gObj=ent.getValue();
	        	HashMap<String, Double> iMap=gObj.getItem();
	        	HashMap<String, Double> newMap=new HashMap<String, Double>();
	        	for(Map.Entry<String, Double> val: iMap.entrySet())
		        {
	        		newMap.put(val.getKey(), val.getValue());
		        }
	        	
	        	itemMap.put(dt, newMap);
        	}
        	else
        	{
        		HashMap<String, Double> getMap=itemMap.get(dt);
        		MenuItems gObj=ent.getValue();
	        	HashMap<String, Double> iMap=gObj.getItem();
	        	for(Map.Entry<String, Double> val: iMap.entrySet())
		        {
	        		getMap.put(val.getKey(), val.getValue());
		        }
        	}
        }
        
        mainObj.findItemsToBuy(itemMap, futureDateMap);
	}

}
