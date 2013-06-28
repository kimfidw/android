package com.android.pdapp.dao;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.os.AsyncTask;
import com.android.pdapp.dateutil.DateExcBYWCF;
import com.android.pdapp.dateutil.FileUtil;
import com.android.pdapp.model.*;

public class PdmxDAO {
	
	DateExcBYWCF duwcf = new DateExcBYWCF();
	
	private Context context;
	
	private FileUtil fileUtil;
	private List<Barinfo>  barinfoList = null;
	private String key = "";
	public PdmxDAO(Context context)
	{
		this.context = context;
	}
	
	public MsgInfo AddPDMX(String plant,String pdnum, String trkno, String whodo, String whodoname, String amact) throws IOException, XmlPullParserException
	{
		return SoapObjectToMsg(DateExcBYWCF.Ympmpadd(plant, pdnum,trkno,whodo,whodoname,amact));
	}
	
	/***
	 * ����barinfo�б�
	 * @param plant
	 * @param trkno
	 * @param bartyp
	 * @return
	 */
	/*
	public List<Barinfo> getBarInfoListByTrkno(String plant,String trkno,int bartyp)
	{
		fileUtil = new FileUtil(context);
		key = plant+"-"+trkno+"-"+bartyp;
		if(fileUtil.readObject(key)!=null)
		{
			barinfoList = (ArrayList<Barinfo>) fileUtil.readObject(key);
		}else
		{
			//Log.d("db", "dbsuccess");
			//�������ݿ� ��ȡ������Ϊ����
			SoapObject details = DateExcBYWCF.GetBarInfoS(plant, trkno, bartyp);
			//Log.d("detail", details.toString());
			if(details!=null)
			{
				barinfoList = SoapObjectToBarList(details);
				if(barinfoList!=null)
				{
					fileUtil.saveObject((Serializable)barinfoList, key);
				}
			}
		}
		return SoapObjectToBarList(DateExcBYWCF.GetBarInfoS(plant, trkno, bartyp));
	}
	*/
	
	/***
	 * SoapObjectת��ΪMSG
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
	 * �����û������̵��Ų�ѯ����
	 * @param plant
	 * @param pdnum
	 * @param userid
	 * @return
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 */
	public List<Barinfo> getPMPDateByUser(String plant,String pdnum,String userid) throws IOException, XmlPullParserException
	{
		SoapObject details = null;
		//�������ݿ� ��ȡ������Ϊ����
		details = DateExcBYWCF.YmpmpGetListbyuser(plant, pdnum, userid);
		if(details!=null)
		{
			barinfoList = SoapObjectToBarList(details);
		}
		
		return barinfoList;
	}
	
	// AsyncTask�첽����
	class AddCacheTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			// ��data��ǰ�������
			fileUtil.saveObject((Serializable)barinfoList, key);
			
			
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
	
	/***
	 * �����������ض���ת��ΪList
	 * @param object
	 * @return
	 */
	private static List<Barinfo> SoapObjectToBarList(SoapObject object)
	{   
		SoapObject Us=object;
		if (Us==null) return null;
		List<Barinfo> barinfoList=new ArrayList<Barinfo> ();
		if(Us.getName()=="anyType")
	    {
	        //����Web Service��õļ���
	        for(int i=0;i<Us.getPropertyCount();i++){
	             
	        	Barinfo m=new Barinfo();
	            //��ȡ����������
	            SoapObject soapChilds =(SoapObject)Us.getProperty(i);
	            m.setPlant(soapChilds.getProperty("Plant").toString());
	            m.setTrkno(soapChilds.getProperty("Trkno").toString());
	            m.setAmount(Double.parseDouble(soapChilds.getProperty("Amcal").toString()));
	            m.setBartyp(soapChilds.getProperty("Bartyp").toString());
	            m.setEqno(soapChilds.getProperty("Eqno").toString());
	            m.setEqtyp(soapChilds.getProperty("Eqtyp").toString());
	            m.setLenth(Double.parseDouble(soapChilds.getProperty("Lencal").toString()));
	            m.setMemo(soapChilds.getProperty("Memo").toString());
	            m.setNpmno(soapChilds.getProperty("Npmno").toString());
	            m.setPdcno(soapChilds.getProperty("Pdcno").toString());
	            m.setPdgno(soapChilds.getProperty("Pdgno").toString());
	            m.setPdlno(soapChilds.getProperty("Pdlno").toString());
	            m.setPdpkno(soapChilds.getProperty("Pdpkno").toString());
	            m.setPsqwt(Double.parseDouble(soapChilds.getProperty("Psqwt").toString()));
	            m.setReams(Double.parseDouble(soapChilds.getProperty("Reams").toString()));
	            m.setRollno(soapChilds.getProperty("Rollno").toString());
	            m.setRolltyp(soapChilds.getProperty("Rolltyp").toString());
	            m.setSetno(soapChilds.getProperty("Setno").toString());
	            m.setSftdat(soapChilds.getProperty("Sftdat").toString());
	            m.setShift(soapChilds.getProperty("Shift").toString());
	            m.setShiftt(soapChilds.getProperty("Shiftt").toString());
	            m.setState(soapChilds.getProperty("State").toString());
	            m.setWhodo(soapChilds.getProperty("Whodo").toString());
	            m.setWhodoname(soapChilds.getProperty("Whodoname").toString());
	            m.setWidth(Double.parseDouble(soapChilds.getProperty("Widcal").toString()));
	            m.setAmact(Double.parseDouble(soapChilds.getProperty("Amact").toString()));
	            barinfoList.add(m);
	        }
	    }
		return barinfoList;

	}
}
