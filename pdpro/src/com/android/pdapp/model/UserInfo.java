package com.android.pdapp.model;

/**
 * ��¼�û���Ϣ
 * @author Fu Dongwei
 *
 */
public class UserInfo {
	//�û���
	private String username;
	//����
	private String password;
	//������
	private String code;
	//�û�����
	private String whoname;
	//Ȩ�޿���
	private String AuthNum;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getWhoname() {
		return whoname;
	}
	public void setWhoname(String whoname) {
		this.whoname = whoname;
	}
	public String getAuthNum() {
		return AuthNum;
	}
	public void setAuthNum(String authNum) {
		AuthNum = authNum;
	}
}
