package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.util.FetchRecords;

/**
 * 
 * @author shahid
 * Helper class for main
 */
public class MainHelper {

	HashMap<String, Double> combMap;
	HashMap<String, Double> sumMap;
	ArrayList<String> freeItemsList;
	double sum;
	double budget;
	String freeItem;
	
	/**
	 * constructor
	 * @param budget
	 */
	public MainHelper(double budget)
	{
		this.combMap=new HashMap<String, Double>();
		this.sumMap=new HashMap<String, Double>();
		this.freeItemsList=new ArrayList<String>();
		this.sum=0.0;
		this.budget=budget;
		this.freeItem="";
	}
	
	/**
	 * 
	 * @param itemMap
	 * @param futureDateMap
	 * Finding items to buy within the given budget
	 */
	public void findItemsToBuy(HashMap<String, HashMap<String, Double>> itemMap, HashMap<String, Integer> futureDateMap)
	{
		System.out.println("So your Budget is:"+budget);
	        for(Map.Entry<String, HashMap<String, Double>> ent: itemMap.entrySet())
	        {
	        	Double sum=0.0;
	        	int flag=0;
	        	freeItemsList.clear();
	        	if(futureDateMap.containsKey(ent.getKey()))
	        	{
	        		System.out.println("for Date:"+ent.getKey());
	        		
	        		HashMap<String, Double> gMap=ent.getValue();//all items for a single date
	        		double leastAmount=999.99;
	        		
	        		//Finding the sume total of all prices on menu...... and the items that go for free
	        		for(Map.Entry<String, Double> ten:gMap.entrySet())
	        		{
	        			sum=sum+ten.getValue();
	        			if(ten.getValue()<leastAmount && ten.getValue()>0.0)
	        				leastAmount=ten.getValue();
	        			if(ten.getValue()==0.0)
	        			{
	        				freeItem=ten.getKey();
	        				freeItemsList.add(ten.getKey());
	        			}
	        		}
	        		if(budget==0.0)
	        		{
	        			System.out.println("You Can't buy anything as you have no budget");
	        			continue;
	        		}
	        		else if(sum<=0.0)
					{
						System.out.println("You Can't buy anything as Nothing is being sold this day");
		        			continue;
					}
	        		else if(sum<=budget)
	        		{
	        			flag=1;
	        		}
	        		if(flag==1)
	        		{
	        			System.out.println("You have enough bucks to buy 'em all!:");
	        			double sumTotal=0.0;
	        			for(Map.Entry<String, Double> ten:gMap.entrySet())
		        		{
	        				System.out.println(ten.getKey()+":"+ten.getValue());
	        				sumTotal=sumTotal+ten.getValue();
		        		}
	        			System.out.println("Total Amount spent:"+sumTotal);
	        			System.out.println("You have "+(budget-sum)+" dollars left after purchases on:"+ent.getKey());
	        		}
	        		else
	        		{
	        			double val=0;
	        			if(budget<leastAmount)
		        		{
		        			System.out.println("You don't have sufficient amount to buy even the least expensive item being sold!");
		        			val=0.0;
		        		}
	        			else
	        			{	
		        			System.out.println("The best combination of items you can buy in the given budget is:");
		        			sumMap.clear();
		        			permute("", "", gMap, 4);
		        			for(Map.Entry<String, Double> lent:sumMap.entrySet())
		        			{
		        				if(lent.getValue()>val && lent.getValue()<=budget)
		        				{
		        					val=lent.getValue();
		        				}
		        			}
		        			
		        			combMap.clear();
		        			for(Map.Entry<String, Double> lent:sumMap.entrySet())
		        			{
		        				
		        				if(lent.getValue()==val)
		        				{
		        					if(!checkCombo(lent.getKey()))
		        					{
		        						System.out.println(lent.getKey());
		        						for(String freeIt:freeItemsList)
		        							System.out.println("FREE:"+freeIt);
		        						
		        						System.out.println("Total Amount spent:"+lent.getValue());
		        					}
		        				}
		        				
		        			}
	        			}
	        			System.out.println("You have "+(budget-val)+" dollars left after purchases on:"+ent.getKey());
	        		}
	        		System.out.println("----------");
	        	}
	        }
	}
	
	/**
	 * 
	 * @param sentItem
	 * @param list
	 * @return
	 * Chcking if item is excluded
	 */
	public boolean isExcluded(String sentItem,ArrayList<String> list)
	{
		for(String exclude:list)
		{
			if(sentItem.toLowerCase().contains(exclude.toLowerCase()))
				return true;
		}
		return false;
	}
	
	/**
	 * Checking best combination
	 * @param key
	 * @return
	 */
	public boolean checkCombo(String key)
	{
		if(combMap.isEmpty())
		{
			combMap.put(key, 0.0);
			return false;
		}
		else
		{
			for(Map.Entry<String, Double> ent:combMap.entrySet())
			{
				String mapKey[]=ent.getKey().split(",");
				if(modPermKey(mapKey, key))
				{
					return true;
				}

			}
			combMap.put(key, 0.0);
		}
		return false;
	}
	
	
	public boolean modPermKey(String[] str,String key)
	{
		String keySplit[]=key.split(",");
		HashMap<String, Integer> strMap=new HashMap<String, Integer>();
		HashMap<String, Integer> keyMap=new HashMap<String, Integer>();
		for(int i=0;i<str.length;i++)
		{
			if(!strMap.containsKey(str[i]))
			{
				strMap.put(str[i], 1);
			}
			else
			{
				strMap.put(str[i], strMap.get(str[i])+1);
			}
		}
		for(int i=0;i<keySplit.length;i++)
		{
			if(!keyMap.containsKey(keySplit[i]))
			{
				keyMap.put(keySplit[i], 1);
			}
			else
			{
				keyMap.put(keySplit[i], keyMap.get(keySplit[i])+1);
			}
		}
		if(strMap.size()!=keyMap.size())
			return false;
		else
		{
			for(Map.Entry<String, Integer> ent:strMap.entrySet())
			{
				if(keyMap.containsKey(ent.getKey()))
				{
					if(ent.getValue()!=keyMap.get(ent.getKey()))
						return false;
				}
			}
			
			return true;
		}
		
	}
	
	/**
	 * finds of different combinations of items and their prices
	 * @param pref
	 * @param keys
	 * @param map
	 * @param n
	 */
	public void permute(String pref, String keys,HashMap<String, Double> map,int n)
	{
		if(sumDigits(pref,keys) || pref.split(",").length==map.size())
		{
			
			if(sumDigits(pref,keys) && checkDuplicate(keys))
			{
				sumMap.put(keys, sum);
				FetchRecords.cnt++;
			}
			return;
		}
		else
		{
			for(Map.Entry<String, Double> ent:map.entrySet())
			{
				if(pref.equalsIgnoreCase("") && keys.equalsIgnoreCase(""))
				{
					if(ent.getValue()!=0.0)
						permute(pref+ent.getValue(),keys+ent.getKey()+":"+ent.getValue(),map,n-1);
				}
					
				else
				{
					if(ent.getValue()!=0.0)
						permute(pref+","+ent.getValue(),keys+","+ent.getKey()+":"+ent.getValue(),map,n-1);
				}
			}
		}
	}
	
	/**
	 * Checks if same combination of items already bought
	 * @param str
	 * @return
	 */
	public static boolean checkDuplicate(String str)
	{
		HashMap<String, Integer> map=new HashMap<String, Integer>();
		String strSplit[]=str.split(",");
		for(int i=0;i<strSplit.length;i++)
		{
			if(!map.containsKey(strSplit[i]))
				map.put(strSplit[i], 1);
			else
				return false;
		}
		return true;
	}
	
	/**
	 * calculates the sum of prices
	 * @param str
	 * @param keys
	 * @return
	 */
	public boolean sumDigits(String str,String keys)
	{
		if(str.equalsIgnoreCase(""))
			return false;
		String strSplit[]=str.split(",");
		sum=0;
		for(int i=0;i<strSplit.length;i++)
		{
			sum=sum+Double.parseDouble(strSplit[i]);
		}

		if(sum==budget)
		{
			return true;
		}
		else
		{
			if(checkDuplicate(keys))
				sumMap.put(keys, sum);
			return false;
		}
	}
	
}
