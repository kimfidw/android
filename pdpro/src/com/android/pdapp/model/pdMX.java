package com.android.pdapp.model;

import java.io.Serializable;

/**
 * �̵���ϸ��
 * 
 * @author Fu Dongwei
 *
 */
public class pdMX implements Serializable {
	//�̵���
	private String pdnum;
	//�����
	private String trkno;
	//��������
	private String bartyp;
	//Ʒ��
	private String pdcno;
	//��̨
	private String npmno;
	//����
	private Double psqwt;
	//���۷���
	private Double widcal;
	//�����߳�
	private Double lencal;
	//��������
	private Double amcal;
	//ʵ������
	private Double amact;
	private String whodo;
	private String whoname;
	private String whendo;
	
	public String getPdnum() {
		return pdnum;
	}
	public void setPdnum(String pdnum) {
		this.pdnum = pdnum;
	}
	public String getTrkno() {
		return trkno;
	}
	public void setTrkno(String trkno) {
		this.trkno = trkno;
	}
	public String getBartyp() {
		return bartyp;
	}
	public void setBartyp(String bartyp) {
		this.bartyp = bartyp;
	}
	public String getPdcno() {
		return pdcno;
	}
	public void setPdcno(String pdcno) {
		this.pdcno = pdcno;
	}
	public String getNpmno() {
		return npmno;
	}
	public void setNpmno(String npmno) {
		this.npmno = npmno;
	}
	public Double getPsqwt() {
		return psqwt;
	}
	public void setPsqwt(Double psqwt) {
		this.psqwt = psqwt;
	}
	public Double getWidcal() {
		return widcal;
	}
	public void setWidcal(Double widcal) {
		this.widcal = widcal;
	}
	public Double getLencal() {
		return lencal;
	}
	public void setLencal(Double lencal) {
		this.lencal = lencal;
	}
	public Double getAmcal() {
		return amcal;
	}
	public void setAmcal(Double amcal) {
		this.amcal = amcal;
	}
	public Double getAmact() {
		return amact;
	}
	public void setAmact(Double amact) {
		this.amact = amact;
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
