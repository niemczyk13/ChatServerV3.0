package com.niemiec.chat.messages.text;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GroupMessage implements Serializable, TextMessage {
	private String senderNick;
	private String message;
	
	public GroupMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getSenderNick() {
		return senderNick;
	}
	
	public void setSenderNick(String senderNick) {
		this.senderNick = senderNick;
	}
}
