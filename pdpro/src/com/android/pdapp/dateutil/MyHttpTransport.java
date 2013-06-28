package com.android.pdapp.dateutil;

import java.io.IOException;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

public class MyHttpTransport extends HttpTransportSE {

	//Ä¬ÈÏ³¬Ê±20s
	private int timeout= 20000; 
	public MyHttpTransport(String url) {
		super(url);
		
	}

	public MyHttpTransport(String url, int timeout) {
		super(url, timeout);
		this.timeout = timeout;
	}
	
	public ServiceConnection getServiceConnection() throws IOException
	{
		ServiceConnectionSE serviceConnection = new ServiceConnectionSE(this.url,timeout);
		return serviceConnection;
		
	}

}
