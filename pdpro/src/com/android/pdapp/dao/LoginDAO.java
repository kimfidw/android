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
 * ��¼ģ����ص����ݿ����
 * @author Yang Hong
 *
 */
public class LoginDAO {
	
	DateExcBYWCF duwcf = new DateExcBYWCF();
	private SoapObject soapObject = null;
	private String workerno = "";
	/**
	 * �����û����������������û���Ϣ
	 * @param �û���
	 * @param ����
	 * @return
	 * @throws XmlPullParserException 
	 */
	public UserInfo FindUser(String username,String password) throws IOException, XmlPullParserException
	{
		soapObject = DateExcBYWCF.GetUserValid(username, password);
		return SoapObjectToUserinfo(soapObject);
	}
	
	/**
	 * ��SoapObject����ת��ΪUserAuth�б�
	 */
	private List<UserAuth> SoapOjbectToUserAuth(SoapObject soapObject)
	{
		if(soapObject == null)
			return null;
		List<UserAuth> date = new ArrayList<UserAuth>();
		if(soapObject.getName().equals("anyType"))
		{
			
			//����Web Service��õļ���
	        for(int i=0;i<soapObject.getPropertyCount();i++)
	        {
	        	//��ȡ������¼
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
	 * ���ݹ������鿴Ȩ�ź�
	 * @param userid ����
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<UserAuth> FindUserAuth(String userid)
	{
		List<UserAuth> date = SoapOjbectToUserAuth(DateExcBYWCF.WebsGetUserAuth(userid,APPCommonValue.AuthNum));
		return date;
	}
	
	/***
	 * ��soapObject����ת��ΪUserinfo
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
