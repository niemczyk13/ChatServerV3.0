package com.niemiec.chat.command.order.messages.condition;

import java.io.Serializable;

import com.niemiec.chat.command.type.messages.condition.ConditionMessage;

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
