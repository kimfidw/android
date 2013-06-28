package com.android.pdapp.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import com.android.pdapp.app.AppContext;
import com.android.pdapp.common.APPCommonValue;
import com.android.pdapp.dateutil.DateExcBYWCF;
import com.android.pdapp.dateutil.FileUtil;
import com.android.pdapp.model.MsgInfo;
import com.android.pdapp.model.pdHD;
public class PdymkDAO {

	DateExcBYWCF duwcf = new DateExcBYWCF();
	AppContext appContext = new AppContext();
	
	
	private Context context;
	public PdymkDAO(Context context)
	{
		this.context = context;
	}
	FileUtil fUtil = new FileUtil(context);
	/***
	 * 添加盘点总表
	 * @param plant 工厂名
	 * @param pdnum 盘点编号
	 * @param pdtime 盘点时间
	 * @param memo 备注
	 * @param whodo
	 * @param whodoname
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public MsgInfo AddYMK(String plant, String pdnum, String pdtime, String memo, String whodo, String whodoname) throws IOException, XmlPullParserException
	{	
		return SoapObjectToMsg(DateExcBYWCF.YmpmkAdd(plant,pdnum,pdtime,memo,whodo,whodoname));
	}
	
	/**
	 * 根据盘点编号和工厂名来判断是否存在此盘点编号
	 * @param pdnum
	 * @param plant
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public Boolean FindPDnum(String pdnum,String plant) throws IOException, XmlPullParserException
	{
		boolean flag = false;
		SoapObject soapObject = DateExcBYWCF.getYmpmkGetbyid(pdnum, plant);
		if(soapObject!=null)
		{
			flag = true;
		}
		return flag;
	}
	
	/***
	 * SoapObject转为MsgInfo
	 * @param object
	 * @return
	 */
	private static MsgInfo SoapObjectToMsg(SoapObject object)
	{
		if (object==null) return null;

	    MsgInfo m=new MsgInfo();
	    m.setMsgtyp(object.getProperty(0).toString().trim());
	    m.setMsg(object.getPropertyAsString(1).toString());
	    return m;
	}
	
	/***
	 * 读取文件
	 * @param context
	 * @return
	 */
	public List<pdHD> ReadXMLHC(Context context)
	{
		File dir = null;
		File file = null;
		InputStream inputStream = null;
		List<pdHD> ymkList = null;
		try
		{
			if(appContext.hasSDcard())
			{
				String path = APPCommonValue.File_Path;
				dir = new File(path);
				if(!dir.exists())
				{
					dir.mkdir();
				}
				file = new File(path+APPCommonValue.File_Name);
				if(!file.exists())
				{
					file.createNewFile();
				}
				inputStream = new FileInputStream(file);
			}else
			{
				inputStream = context.openFileInput(APPCommonValue.File_Name);
			}
			ymkList = fUtil.getPMK(inputStream);
			return ymkList;
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				inputStream.close();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	/***
	 * 写入XML文件
	 * @param hdList
	 * @param context
	 * @return
	 */
	public MsgInfo WriteHC(List<pdHD> hdList,Context context)
	{
		MsgInfo msg = new MsgInfo();
		File dir = null;
		File file = null;
		OutputStream writer = null;
		List<pdHD> ymkList = null;
		try
		{
			if(appContext.hasSDcard())
			{
				String path = APPCommonValue.File_Path;
				dir = new File(path);
				if(!dir.exists())
				{
					dir.mkdir();
				}
				file = new File(path+APPCommonValue.File_Name);
				if(!file.exists())
				{
					file.createNewFile();
				}
				writer = new FileOutputStream(file);
			}else
			{
				writer = context.openFileOutput(APPCommonValue.File_Name,Context.MODE_PRIVATE);
			}
			msg = fUtil.writeXML(hdList, writer);
			return msg;
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				writer.close();
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
}
