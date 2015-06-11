package com.jawaly.senders;

import java.io.Serializable;

public class SenderName implements Serializable{
	
	String senderName;
	int senderId;
	String state;
	String activeState;
	int isDefault;
	
	public SenderName(){
		
	}
	
	public SenderName(String senderName, int senderId, String state,
			String activeState, int isDefault) {
		super();
		this.senderName = senderName;
		this.senderId = senderId;
		this.state = state;
		this.activeState = activeState;
		this.isDefault = isDefault;
	}
	
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public int getSenderId() {
		return senderId;
	}
	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getActiveState() {
		return activeState;
	}
	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}
	public int isDefault() {
		return isDefault;
	}
	public void setDefault(int isDefault) {
		this.isDefault = isDefault;
	}

}
