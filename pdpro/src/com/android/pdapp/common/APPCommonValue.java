package com.android.pdapp.common;

/**
 * �趨app���õı���
 * @author Yang Hong
 *
 */
public class APPCommonValue {
	
	//myzhp���ʵ�ַ
	public final static String LOGIN_URL = "http://myzhp.app.com.cn/pm/sap/pubunit.asmx";
	//webservice�����ռ�
	public final static String  SERVICE_NS = "http://tempuri.org/";
	//webservice�û���¼����
	public final static String LoginMethod = "GetUserValid";
	//pmk��ӷ���
	public final static String PmkMethod = "YmpmkAdd";
	//pmp��ӷ���
	public final static String PmpMethod = "Ympmpadd";
	//����userid��pdnum�͹��������̵�
	public final static String pmpGetByUser = "YmpmpGetListbyuser";
	//�û�Ȩ�޿��Ʒ���
	public final static String LoadUserAuthList = "LoadUserAuthList";
	//��ȡympmk����
	public final static String GetympmkMethod = "YmpmkGetbyid";
	//zhpMES�õ���webservice
	public final static String ZHP_URL = "http://172.18.255.6/mesw/WebService.asmx";
	//NAPPMES�õ���webservice
	public final static String NAPP_URL = "http://172.18.255.6/mesw/WebService.asmx";
	//�����ļ���
	public final static String File_Name= "pd.xml";
	//����SD���ڵĵ�ַ
	public final static String File_Path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/pdtemp/";
	//��ĿȨ�ź�
	public final static String AuthNum = "6000";
	//��ĩ�̵��ű༭
	public final static String AuthEdit = "6100";
	//��ĩ�̵���ҵ
	public final static String AuthDo = "6101";
	//webservice���ؿ�ֵʱĬ�Ϸ��ص���ʽ
	public final static String WCFResultisnull = "anyType{}";
	//����ʵ��������Ĭ��ֵ
	public final static String amactByDefault = "0";

}
