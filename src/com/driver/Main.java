package com.driver;

import java.io.IOException;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;

import com.util.FetchRecords;
import com.util.URLConnector;

/**
 * 
 * @author shahid
 * Driver Class simulating the restaurant	
 */

public class Main {

	static ArrayList<String> excludeList=new ArrayList<String>();
	
	public static void main(String[] args) throws IOException, JSONException, ParseException {
		// TODO Auto-generated method stub
		Scanner scan=new Scanner(System.in);
		URLConnector instance=URLConnector.getInstance();
		URLConnection yc=instance.getConnection();
		System.out.println("Enter a sample date:(YYYY-MM-DD)");
        String nDate = scan.next();
        double budget=0.0;
        
        if(args.length>1)
        {
        	for(int i=1;i<args.length;i++)
        		excludeList.add(args[i]);
        }
        
        System.out.println("Item(s) you want to be exluded:");
        for(String exclude:excludeList)
        	System.out.println(exclude);
        
        if(args.length>0)
        	budget=Double.parseDouble(args[0]);
        else
        	budget=0.0;
        
        FetchRecords fRec=new FetchRecords(yc);
		fRec.fetchFromURL();
		fRec.readJSONData(nDate, excludeList, budget);
		
        scan.close();
	}

}
