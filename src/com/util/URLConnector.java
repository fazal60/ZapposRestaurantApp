package com.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;


/**
 * 
 * @author shahid
 * URLConnector is a singleton class which establishes a connection
 * to the URL specified. It then gives the URLConnection instance 
 * to the calling class.
 */

public class URLConnector {

	URL nileWeb;
	URLConnection yc;
	private static URLConnector instance = new URLConnector();
	private URLConnector() {
		// TODO Auto-generated constructor stub
	}
	
	public static URLConnector getInstance(){
	      return instance;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public URLConnection getConnection() throws IOException
	{
		this.nileWeb = new URL("http://codapi.zappos.biz/menus");
        this.yc = this.nileWeb.openConnection();
        return this.yc;
	}
}
	