package com.niemiec.chat.messages.condition;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ReadyToWorkMessage implements Serializable, ConditionMessage {
	private String nick;
	
	public ReadyToWorkMessage(String nick) {
		this.nick = nick;
	}
	
	public String getNick() {
		return nick;
	}
}
