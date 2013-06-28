package com.android.pdapp.common;

/**
 * 设定app常用的变量
 * @author Yang Hong
 *
 */
public class APPCommonValue {
	
	//myzhp访问地址
	public final static String LOGIN_URL = "http://myzhp.app.com.cn/pm/sap/pubunit.asmx";
	//webservice命名空间
	public final static String  SERVICE_NS = "http://tempuri.org/";
	//webservice用户登录方法
	public final static String LoginMethod = "GetUserValid";
	//pmk添加方法
	public final static String PmkMethod = "YmpmkAdd";
	//pmp添加方法
	public final static String PmpMethod = "Ympmpadd";
	//根据userid、pdnum和工厂名来盘点
	public final static String pmpGetByUser = "YmpmpGetListbyuser";
	//用户权限控制方法
	public final static String LoadUserAuthList = "LoadUserAuthList";
	//获取ympmk方法
	public final static String GetympmkMethod = "YmpmkGetbyid";
	//zhpMES用到的webservice
	public final static String ZHP_URL = "http://172.18.255.6/mesw/WebService.asmx";
	//NAPPMES用到的webservice
	public final static String NAPP_URL = "http://172.18.255.6/mesw/WebService.asmx";
	//缓存文件名
	public final static String File_Name= "pd.xml";
	//缓存SD卡内的地址
	public final static String File_Path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/pdtemp/";
	//项目权信号
	public final static String AuthNum = "6000";
	//月末盘点编号编辑
	public final static String AuthEdit = "6100";
	//月末盘点作业
	public final static String AuthDo = "6101";
	//webservice返回空值时默认返回的形式
	public final static String WCFResultisnull = "anyType{}";
	//设置实际重量的默认值
	public final static String amactByDefault = "0";

}
