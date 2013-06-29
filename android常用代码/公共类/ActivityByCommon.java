//在Activity中常用到的方法
public class CommonUserByActivity
{
	/***
	 * 清空登录信息
	 */
	private void clearLogin() {
		//对sharedPreference的编辑
		SharedPreferences.Editor editor = sharedPreference.edit();
		editor.putBoolean(AppConfig.Is_Login, false);
		editor.putString(AppConfig.Login_time, "");
		editor.putString(AppConfig.Pd_Auth1,"");
		editor.putString(AppConfig.Pd_Auth2,"");
		editor.commit();
	}
	
	/***
	 * 菜单选择
	 * 
	 * @param item
	 */
	public void MenuSelected(MenuItem item) {
		// 得到当前选中的menuItem的ID
		int item_id = item.getItemId();
		switch (item_id) {
		// 退出当前应用
		case R.id.exit:
			clearLogin();
			AppManager.getAppManager().finishAllActivity();
			break;

		}
	}

	/**
	 * 点击两次返回按钮退出，结束进程
	 */
	public Boolean ExitByReturnBtn(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				UIHelper.ToastMessage(this, "再按一次退出程序");
				mExitTime = System.currentTimeMillis();
			} else {
				//清空登录信息
				clearLogin();
				AppManager.getAppManager().finishAllActivity();
				//结束当前应用的进程
				//android.os.Process.killProcess(android.os.Process.myPid());
				AppManager.getAppManager().AppExit(getApplicationContext());
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	// AsyncTask异步任务
	class AddCacheTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected String doInBackground(Integer... params) {
			// 在data最前添加数据
			fileUtil.saveObject((Serializable)date, key);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
	
	final Handler handler = new Handler(){
	@Override
	public void handleMessage(Message msg)
	{
		MsgInfo msginfo = (MsgInfo)msg.obj;
		if(msginfo.getMsgtyp().equals("S"))
		{
			myApp = MyAPP.getMyAPP();
		    //记录盘点编号
			myApp.setPdnumApp(pdnum);
			//记录已盘点数量
			//记录盘点编号
			sharedPreference = getSharedPreferences(AppConfig.App_Config, 0);
			SharedPreferences.Editor editor = sharedPreference.edit();
			editor.putString("pdnum", pdnum);
			editor.commit();
			//插入数据					
			InsertHCTask ihcTask = new InsertHCTask();
			ihcTask.execute(2000);			
			UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.save_success));
		}else if(msginfo.getMsgtyp().equals("E"))
		{
			UIHelper.ToastMessage(AddMXActivity.this, msginfo.getMsg());
			return;
		}else if(msg.what==2)
		{
			UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.network_slow));
			return;
		}
		else if(msg.what==3)
		{
			UIHelper.ToastMessage(AddMXActivity.this, getString(R.string.network_timeout));
			return;
		}
	    }			
	};	
	new Thread(){
	@Override
	public void run() {
		Message msg = new Message();					
		PdmxDAO mxDao = new PdmxDAO(AddMXActivity.this);
		//保存数据到数据库
		MsgInfo msginfo;
		try {
			msginfo = mxDao.AddPDMX(plant, pdnum, trkno, whodo, whoname, amact);
			if(msginfo!=null)
			{
				String msgtyp = msginfo.getMsgtyp();
				msg.obj = msginfo;
				if(msgtyp.equals("S"))
				{
					msg.what=1;
				}else if(msgtyp.equals("E"))
				{
					msg.what = 0;
				}
			}
		}catch(SocketTimeoutException e)
		{
			msg.what=3;
			e.printStackTrace();
		}
		catch (IOException e) {
			msg.what=2;
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.sendMessage(msg);
		}
		}.start();
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
	
	/***
	 * 将服务器返回对象转换为List
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
	        //遍历Web Service获得的集合
	        for(int i=0;i<Us.getPropertyCount();i++){
	             
	        	Barinfo m=new Barinfo();
	            //获取单条的数据
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