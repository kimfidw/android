package com.android.pdapp.dateutil;

import java.io.IOException;

import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.transport.ServiceConnection;
import org.ksoap2.transport.ServiceConnectionSE;

/**
 * 自定义HttpTransportSE类，用于设置连接属性
 * @author 东伟
 *
 */
public class MyHttpTransport extends HttpTransportSE {

	//默认超时20s
	private int timeout= 20000; 
	public MyHttpTransport(String url) {
		super(url);
		
	}

	public MyHttpTransport(String url, int timeout) {
		super(url, timeout);
		this.timeout = timeout;
	}
	
	@Override
	public ServiceConnection getServiceConnection() throws IOException
	{
		ServiceConnectionSE serviceConnection = new ServiceConnectionSE(this.url,timeout);
		return serviceConnection;
		
	}

}
