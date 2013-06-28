package com.android.pdapp.model;

/**
 * 登录用户信息
 * @author Fu Dongwei
 *
 */
public class UserInfo {
	//用户名
	private String username;
	//密码
	private String password;
	//工厂名
	private String code;
	//用户姓名
	private String whoname;
	//权限控制
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
