package com.android.pdapp.model;

public class MsgInfo {
	private String Msgtyp;
	private String Msg;
	
	
	
	public String getMsgtyp() {
		return Msgtyp;
	}
	public void setMsgtyp(String msgtyp) {
		Msgtyp = msgtyp;
	}
	public String getMsg() {
		return Msg;
	}
	public void setMsg(String msg) {
		Msg = msg;
	}
	
	public MsgInfo()
	{}
	public MsgInfo(String msgtyp,String msg)
	{
		Msgtyp=msgtyp;
		Msg=msg;
	}
}
