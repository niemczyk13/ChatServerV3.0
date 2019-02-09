package com.niemiec.chat.messages.condition;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ExitMessage implements Serializable, ConditionMessage {
	private String nick;
	
	public ExitMessage() {
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
}
