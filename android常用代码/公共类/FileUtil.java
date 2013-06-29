package com.android.pdapp.dateutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;
import android.content.Context;

import com.android.pdapp.app.AppContext;
import com.android.pdapp.model.*;

/**
 * �ļ��Ķ�ȡ�뱣��
 * @author ��ΰ
 *
 */
public class FileUtil {
	AppContext appContext = new AppContext();
	// �ļ�·��
	private final static String File_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/pdtemp/";
	
	private Context context;
	
	public FileUtil(Context cont)
	{
		this.context = cont;
	}
	
	/***
	 * �����ļ����Ͷ��󱣴滺��
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = context.openFileOutput(file, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/***
	 * ɾ�������ļ�
	 * @param filename
	 * @return
	 */
	public boolean deleteCacheFile(String filename)
	{
		boolean flag = false;
		File file = context.getFileStreamPath(filename);
		if(file.isFile())
		{
			file.delete();
			flag = true;
		}
		return flag;
	}
	
	/**
	 * �жϻ����ļ��Ƿ����
	 */
	public boolean CacheExist(String filename)
	{
		File file = context.getFileStreamPath(filename);
		if(file!=null)
		{
			return file.exists();
		}
		return false;
	}
	
	
	/***
	 * ����userid���̵��Ŷ�ȡ����,�����ַΪĬ�ϵ�ַ
	 */
	public Serializable readObject(String file)
	{
		if(!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = context.openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
			//�����л�ʧ�� - ɾ�������ļ�
			if(e instanceof InvalidClassException){
				File data = context.getFileStreamPath(file);
				data.delete();
			}
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}
	
	/***
	 * �жϻ����Ƿ����
	 * @param file
	 * @param context
	 * @return
	 */
	private boolean isExistDataCache(String file) {
		boolean exist = false;
		File cacheFile = context.getFileStreamPath(file);
		if(cacheFile.exists())
		{
			exist = true;
		}
		return exist;
	}
	
	
	/***
	 * ��ȡ�̵��ܱ���Ϣ
	 * @throws IOException 
	 * @throws XmlPullParserException 
	 */
	public List<pdHD> getPMK(InputStream inputStream)
	{
		List<pdHD> pmks = null;
		pdHD pmk = null;
		try
		{
			
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(inputStream, "UTF-8");
			
			int event = parser.getEventType();
			while(event!=XmlPullParser.END_DOCUMENT)
			{
				switch(event)
				{
				case XmlPullParser.START_DOCUMENT:
					pmks = new ArrayList<pdHD>();
					break;
				case XmlPullParser.START_TAG:
					if("pmk".equals(parser.getName()))
					{
						pmk = new pdHD();
						pmk.setPdnum(parser.getAttributeName(0));
					}
					if(pmk!=null)
					{
						if("pdtime".equals(parser.getName()))
						{
							pmk.setPdtime(parser.getAttributeName(1));
						}
						if("plant".equals(parser.getName()))
						{
							pmk.setPlant(parser.getAttributeName(2));
						}
						if("memo".equals(parser.getName()))
						{
							pmk.setMemo(parser.getAttributeName(3));
						}
						if("whodo".equals(parser.getName()))
						{
							pmk.setWhodo(parser.getAttributeName(4));
						}
						if("whoname".equals(parser.getName()))
						{
							pmk.setWhoname(parser.getAttributeName(5));
						}
						if("whendo".equals(parser.getName()))
						{
							pmk.setWhendo(parser.getAttributeName(6));
						}
					}
					break;
				case XmlPullParser.END_TAG:
					if("pmk".equals(parser.getName()))
					{
						pmks.add(pmk);
						pmk = null;
					}
					break;
				}
				event = parser.next();
			}
			return pmks;
		}
		catch(XmlPullParserException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
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
	 * д��XML�ļ�
	 * @param hdList
	 * @param writer
	 */
	public MsgInfo writeXML(List<pdHD> hdList,OutputStream writer)
	{
		XmlSerializer xmlSer = Xml.newSerializer();
		MsgInfo msg = new MsgInfo();
		try
		{
			xmlSer.setOutput(writer, "UTF-8");
			
			xmlSer.startDocument("UTF-8", true);
			xmlSer.startTag(null, "ympmks");
			for(pdHD ymk : hdList)
			{
				xmlSer.startTag(null, "pmk");
				xmlSer.attribute(null, "pdnum", ymk.getPdnum());
				xmlSer.startTag(null, "pdtime");
				xmlSer.text(ymk.getPdtime());
				xmlSer.endTag(null, "pdtime");
				xmlSer.startTag(null, "plant");
				xmlSer.text(ymk.getPlant());
				xmlSer.endTag(null, "plant");
				xmlSer.startTag(null, "memo");
				xmlSer.text(ymk.getMemo());
				xmlSer.endTag(null, "memo");
				xmlSer.startTag(null, "whodo");
				xmlSer.text(ymk.getWhodo());
				xmlSer.endTag(null, "whodo");
				xmlSer.startTag(null, "whoname");
				xmlSer.text(ymk.getWhoname());
				xmlSer.endTag(null, "whoname");
				xmlSer.startTag(null, "whendo");
				xmlSer.text(ymk.getWhendo());
				xmlSer.endTag(null, "whendo");
				xmlSer.endTag(null, "pmk");
			}
			xmlSer.endTag(null, "ympmks");
			xmlSer.endDocument();
			msg.setMsg("д��ɹ�");
			msg.setMsgtyp("S");
			writer.flush();
			writer.close();
		}catch(IllegalArgumentException e)
		{
			msg.setMsg("д��ʧ��");
			msg.setMsgtyp("F");
			e.printStackTrace();
		}catch(IllegalStateException e)
		{
			msg.setMsg("д��ʧ��");
			msg.setMsgtyp("F");
			e.printStackTrace();
		}catch (IOException e) {
			msg.setMsg("д��ʧ��");
			msg.setMsgtyp("F");
			e.printStackTrace();
		}
		return msg;
	}
	
}
