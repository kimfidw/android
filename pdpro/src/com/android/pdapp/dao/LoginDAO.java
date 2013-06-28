package com.android.pdapp.dao;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import com.android.pdapp.model.*;
import com.android.pdapp.common.APPCommonValue;
import com.android.pdapp.dateutil.*;
/**
 * 登录模块相关的数据库操作
 * @author Yang Hong
 *
 */
public class LoginDAO {
	
	DateExcBYWCF duwcf = new DateExcBYWCF();
	private SoapObject soapObject = null;
	private String workerno = "";
	/**
	 * 根据用户名、密码来返回用户信息
	 * @param 用户名
	 * @param 密码
	 * @return
	 * @throws XmlPullParserException 
	 */
	public UserInfo FindUser(String username,String password) throws IOException, XmlPullParserException
	{
		soapObject = DateExcBYWCF.GetUserValid(username, password);
		return SoapObjectToUserinfo(soapObject);
	}
	
	/**
	 * 将SoapObject对象转化为UserAuth列表
	 */
	private List<UserAuth> SoapOjbectToUserAuth(SoapObject soapObject)
	{
		if(soapObject == null)
			return null;
		List<UserAuth> date = new ArrayList<UserAuth>();
		if(soapObject.getName().equals("anyType"))
		{
			
			//遍历Web Service获得的集合
	        for(int i=0;i<soapObject.getPropertyCount();i++)
	        {
	        	//获取单条记录
	        	SoapObject soapChilds =(SoapObject)soapObject.getProperty(i);
	        	UserAuth userAuth = new UserAuth();
	        	userAuth.setUserid(soapChilds.getPropertyAsString("Userid"));
	        	userAuth.setAuthno(soapChilds.getPropertyAsString("Authno"));
	        	date.add(userAuth);
	        }
		}
		
		return date;
	}
	
	/**
	 * 根据工号来查看权信号
	 * @param userid 工号
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<UserAuth> FindUserAuth(String userid)
	{
		List<UserAuth> date = SoapOjbectToUserAuth(DateExcBYWCF.WebsGetUserAuth(userid,APPCommonValue.AuthNum));
		return date;
	}
	
	/***
	 * 将soapObject对象转化为Userinfo
	 * @param soapObject
	 * @return
	 */
	private UserInfo SoapObjectToUserinfo(SoapObject soapObject)
	{
		if(soapObject==null)
			return null;
		
		UserInfo userinfo = new UserInfo();
		userinfo.setUsername(soapObject.getPropertyAsString("Userid"));
		userinfo.setCode(soapObject.getPropertyAsString("C_code"));
		userinfo.setWhoname(soapObject.getPropertyAsString("Us_name"));
		
		return userinfo;
	}
}
