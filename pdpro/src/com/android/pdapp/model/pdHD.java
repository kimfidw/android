package com.android.pdapp.model;

/**
 * 盘点主表
 * @author Fu Dongwei
 *
 */
public class pdHD {
	//盘点编号
	private String pdnum;
	//年月
	private String pdtime;
	//工厂代码
	private String plant;
	//备注
	private String memo;
	private String whodo;
	private String whoname;
	private String whendo;
	
	public String getPdnum() {
		return pdnum;
	}
	public void setPdnum(String pdnum) {
		this.pdnum = pdnum;
	}
	public String getPdtime() {
		return pdtime;
	}
	public void setPdtime(String pdtime) {
		this.pdtime = pdtime;
	}
	public String getPlant() {
		return plant;
	}
	public void setPlant(String plant) {
		this.plant = plant;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getWhodo() {
		return whodo;
	}
	public void setWhodo(String whodo) {
		this.whodo = whodo;
	}
	public String getWhoname() {
		return whoname;
	}
	public void setWhoname(String whoname) {
		this.whoname = whoname;
	}
	public String getWhendo() {
		return whendo;
	}
	public void setWhendo(String whendo) {
		this.whendo = whendo;
	}
	
	
}
