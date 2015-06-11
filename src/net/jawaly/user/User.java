package net.jawaly.user;

import java.io.Serializable;

public class User implements Serializable{
	
	private String fullName;
	private String userName;
	private String passWord;
	int credit;
	String email;
	private int user_id;
	private boolean email_verification;
	private String mobile_number;
	private boolean mobile_active_state;
	private int group_id;
	private boolean user_state;
	private int user_open_sender_name;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public boolean isEmail_verification() {
		return email_verification;
	}
	public void setEmail_verification(boolean email_verification) {
		this.email_verification = email_verification;
	}
	public String getMobile_number() {
		return mobile_number;
	}
	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}
	public boolean isMobile_active_state() {
		return mobile_active_state;
	}
	public void setMobile_active_state(boolean mobile_active_state) {
		this.mobile_active_state = mobile_active_state;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public boolean isUser_state() {
		return user_state;
	}
	public void setUser_state(boolean user_state) {
		this.user_state = user_state;
	}
	public int getUser_open_sender_name() {
		return user_open_sender_name;
	}
	public void setUser_open_sender_name(int user_open_sender_name) {
		this.user_open_sender_name = user_open_sender_name;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	
}
