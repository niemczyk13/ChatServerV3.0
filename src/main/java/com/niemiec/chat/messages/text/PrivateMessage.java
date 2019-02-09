package com.niemiec.chat.messages.text;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PrivateMessage implements Serializable, TextMessage {
	private String senderNick;
	private String recipientNick;
	private String message;
	
	public PrivateMessage(String message) {
		this.message = message;
	}

	public void setSenderNick(String senderNick) {
		this.senderNick = senderNick;
	}

	public void setRecipientNick(String recipientNick) {
		this.recipientNick = recipientNick;
	}

	public String getSenderNick() {
		return senderNick;
	}

	public String getRecipientNick() {
		return recipientNick;
	}

	public String getMessage() {
		return message;
	}
}
