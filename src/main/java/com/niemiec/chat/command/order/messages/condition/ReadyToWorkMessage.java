package com.niemiec.chat.command.order.messages.condition;

import java.io.Serializable;

import com.niemiec.chat.command.type.messages.condition.ConditionMessage;

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
