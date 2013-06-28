package com.android.pdapp.dateutil;

import java.io.IOException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

import com.android.pdapp.common.*;

/**
 * ����webService�������������
 * 
 * @author Fu Dongwei
 * 
 */
public class DateExcBYWCF {

	/**
	 * ����webservice��ȡ�û���Ϣ 
	 * @param methodName
	 * @param params
	 * @return
	 */
	public static SoapObject GetUserValid(String userid, String pwd) throws IOException,XmlPullParserException {
		String mthname = APPCommonValue.LoginMethod;
		MyHttpTransport ht = new MyHttpTransport(APPCommonValue.LOGIN_URL,4000);
		ht.debug = true;
		org.ksoap2.serialization.SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapobject = new SoapObject(APPCommonValue.SERVICE_NS,
				APPCommonValue.LoginMethod);
		soapobject.addProperty("userid", userid);
		soapobject.addProperty("pwd", pwd);
		envelope.bodyOut = soapobject;
		envelope.dotNet = true;
		ht.call(APPCommonValue.SERVICE_NS + mthname,
				envelope);
		SoapObject result = (SoapObject) envelope.bodyIn;
		if(!ht.responseDump.contains(mthname+"Result"))
    	{
    		return null;
    	}else
    	{
    		SoapObject detail =(SoapObject) result.getProperty(mthname+"Result");
    		return ExecResult(detail);
    	}
	}

	/***
	 * ����̵�������Ϣ
	 * 
	 * @param plant
	 * @param pdnum
	 * @param pdtime
	 * @param memo
	 * @param whodo
	 * @param whodoname
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static SoapObject YmpmkAdd(String plant, String pdnum,String pdtime,String memo,String whodo,String whodoname) throws IOException, XmlPullParserException {
		String mthname = APPCommonValue.PmkMethod;
		String url = GetMesUrl(plant);
		MyHttpTransport ht = new MyHttpTransport(url,4000);
		ht.debug = true;
		org.ksoap2.serialization.SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		SoapObject soapobject = new SoapObject(APPCommonValue.SERVICE_NS,
				mthname);
		soapobject.addProperty("plant", plant);
		soapobject.addProperty("pdnum", pdnum);
		soapobject.addProperty("pdtime", pdtime);
		soapobject.addProperty("memo", memo);
		soapobject.addProperty("whodo", whodo);
		soapobject.addProperty("whodoname", whodoname);
		envelope.bodyOut = soapobject;
		envelope.dotNet = true;
		ht.call(APPCommonValue.SERVICE_NS + mthname, envelope);
		SoapObject result = (SoapObject) envelope.bodyIn;
		SoapObject detail = (SoapObject) result.getProperty(mthname
				+ "Result");
		return ExecResult(detail);
	}

	/***
	 * �����ϸ�̵���Ϣ
	 * @param plant
	 * @param property
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static SoapObject Ympmpadd(String plant, String pdnum, String trkno, String whodo, String whodoname, String amact) throws IOException, XmlPullParserException
	{
			String mthname = APPCommonValue.PmpMethod;
			String url = GetMesUrl(plant);
			MyHttpTransport ht = new MyHttpTransport(url,4000);
			ht.debug = true;
			org.ksoap2.serialization.SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			SoapObject soapobject = new SoapObject(APPCommonValue.SERVICE_NS,
					mthname);
			soapobject.addProperty("pdnum", pdnum);
			soapobject.addProperty("trkno", trkno);
			soapobject.addProperty("whodo", whodo);
			soapobject.addProperty("whodoname", whodoname);
			
			soapobject.addProperty("amact",amact);
			envelope.bodyOut = soapobject;
			envelope.dotNet = true;
			ht.call(APPCommonValue.SERVICE_NS + mthname, envelope);
			//Log.e("response", String.valueOf(envelope.getResponse().equals(null)));
			SoapObject result = (SoapObject) envelope.bodyIn;
			SoapObject detail = (SoapObject) result.getProperty(mthname+ "Result");
			return ExecResult(detail);
	}

	
	/***
	 * ���ط�������ֽ�����ϸ����
	 * @param plant 
	 * @param pdnum
	 * @param userid
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static SoapObject YmpmpGetListbyuser(String plant,String pdnum,String userid) throws IOException, XmlPullParserException
	{
	    String mthname=APPCommonValue.pmpGetByUser;
	    String url=GetMesUrl(plant);
	    MyHttpTransport ht = new MyHttpTransport(url,4000);
	    ht.debug=true;
	    org.ksoap2.serialization.SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    SoapObject soapobject=new SoapObject(APPCommonValue.SERVICE_NS,mthname);
	    soapobject.addProperty("pdnum", pdnum);
	    soapobject.addProperty("userid", userid);
	    envelope.bodyOut=soapobject;
	    envelope.dotNet=true;
	  	ht.call(APPCommonValue.SERVICE_NS+mthname, envelope);
	   	SoapObject result=(SoapObject) envelope.bodyIn;
	   	SoapObject detail =(SoapObject) result.getProperty(mthname+"Result");
	   	//Log.i("pdnum-userid", pdnum+"-"+userid);
	   	//sLog.d("detail",detail.toString());
	   	
	   	return ExecResult(detail);
	}
	
	/***
	 * ��ȡֽ����Ϣ
	 * @param plant
	 * @param trkno
	 * @param bartyp
	 * @return
	 */
	public static SoapObject GetBarInfoS(String  plant,String trkno,int bartyp)
	{
	    String mthname="GetBarInfoS";
	    String url=GetMesUrl(plant);
	    //Log.i("plant", plant);
	    //Log.i("bartyp", String.valueOf(bartyp));
	    HttpTransportSE ht=new HttpTransportSE(url);
	    ht.debug=true;
	    org.ksoap2.serialization.SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    SoapObject soapobject=new SoapObject(APPCommonValue.SERVICE_NS,mthname);
	    soapobject.addProperty("plant", plant);
	    soapobject.addProperty("trkno", trkno);
	    soapobject.addProperty("barref", bartyp);
	    envelope.bodyOut=soapobject;
	    envelope.dotNet=true;
	    try
	    {
	    	ht.call(APPCommonValue.SERVICE_NS+mthname, envelope);
	    	SoapObject result=(SoapObject) envelope.bodyIn;
	    	SoapObject detail =(SoapObject) result.getProperty(mthname+"Result");
	    	//Log.i("barinfo",detail.toString());
	    	return ExecResult(detail);
	    }catch(IOException e)
	    {
	    	e.printStackTrace();
	    } catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * ��ȡָ���û���Ȩ�ź��б�
	 * @param userid ����
	 * @param authno 6000����ĩ�̵㣩
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static SoapObject WebsGetUserAuth(String  userid,String authno) 
	{
	    String mthname=APPCommonValue.LoadUserAuthList;
	    MyHttpTransport ht = new MyHttpTransport(APPCommonValue.LOGIN_URL,4000);
	    //HttpTransportSE ht=new HttpTransportSE(APPCommonValue.LOGIN_URL);
	    ht.debug=true;
	    org.ksoap2.serialization.SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    SoapObject soapobject=new SoapObject(APPCommonValue.SERVICE_NS,mthname);
	    soapobject.addProperty("userid", userid);
	    soapobject.addProperty("authnogrp", authno);
	    envelope.bodyOut=soapobject;
	    envelope.dotNet=true;
	    try
	    {
	    	ht.call(APPCommonValue.SERVICE_NS+mthname, envelope);
	    	SoapObject result=(SoapObject) envelope.bodyIn;
	    	SoapObject detail =(SoapObject) result.getProperty(mthname+"Result");
	    	return ExecResult(detail);
	    }catch(IOException e)
	    {
	    	e.printStackTrace();
	    }
	    catch(XmlPullParserException e)
	    {
	    	e.printStackTrace();
	    }
	    return null;
	}
	
	/**
	 * �����̵��ź͹����������SoapObject����
	 * @param pdnum
	 * @param plant
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public static SoapObject getYmpmkGetbyid(String pdnum,String plant) throws IOException, XmlPullParserException
	{
		String mthname = APPCommonValue.GetympmkMethod;
		String url = GetMesUrl(plant);
		MyHttpTransport ht = new MyHttpTransport(url,4000);
		//HttpTransportSE ht = new HttpTransportSE(url);
		ht.debug=true;
	    org.ksoap2.serialization.SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    SoapObject soapobject=new SoapObject(APPCommonValue.SERVICE_NS,mthname);
	    soapobject.addProperty("pdnum", pdnum);
	    envelope.bodyOut=soapobject;
	    envelope.dotNet=true;
    	ht.call(APPCommonValue.SERVICE_NS+mthname, envelope);
    	SoapObject result=(SoapObject) envelope.bodyIn;
    	if(!ht.responseDump.contains(mthname+"Result"))
    	{
    		return null;
    	}else
    	{
    		SoapObject detail =(SoapObject) result.getProperty(mthname+"Result");
    		return ExecResult(detail);
    	}
	    
	}
	
	/**
	 * ���ݹ�������������
	 * @param plant ������
	 * @return
	 */
	private static String GetMesUrl(String plant) {
		String url = "";
		if (plant.equals("ZHP")) {
			url = APPCommonValue.ZHP_URL;
		} else {
			url = APPCommonValue.NAPP_URL;
		}
		return url;
	}
	
	/**
	 * ����soapObjectΪ�յ����
	 * @param soapObject
	 * @return
	 */
	private static SoapObject ExecResult(SoapObject soapObject)
	{
		if(soapObject.toString().equals(APPCommonValue.WCFResultisnull))
		{
			return null;
		}
		return soapObject;
	}
}
