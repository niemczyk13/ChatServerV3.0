package com.niemiec.chat.messages.condition;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CheckNickMessage implements Serializable, ConditionMessage{
	private String nick;
	private boolean nickNotExist;
	
	public CheckNickMessage(String nick) {
		this.nick = nick;
		this.nickNotExist = false;
	}

	public boolean isNickNotExist() {
		return nickNotExist;
	}

	public void nickNotExist() {
		this.nickNotExist = true;
	}

	public String getNick() {
		return nick;
	}
}