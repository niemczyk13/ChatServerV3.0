package com.niemiec.chat.messages.condition;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UsersListMessage implements Serializable, ConditionMessage {
	private ArrayList<String> users;
	
	@SuppressWarnings("unchecked")
	public UsersListMessage(Object users) {
		this.users = (ArrayList<String>) users;
	}

	public ArrayList<String> getUsersArrayList() {
		return users;
	}
}
