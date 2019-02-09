package com.niemiec.objects;

import java.net.Socket;

import com.niemiec.connections.InputOutputStream;
import com.niemiec.games.battleship.manager.BattleshipManagementServer;
import com.niemiec.objects.managers.ClientThreadManager;
import com.niemiec.objects.managers.MessagesManagementServer;

public class ClientThread extends Thread {
	private String nick;
	private InputOutputStream inputOutputStream;
	private MessagesManagementServer messagesManagement;

	public ClientThread(Socket socket, ClientThreadManager clientThreadManager, BattleshipManagementServer battleshipManagement) {
		this.nick = null;
		this.inputOutputStream = new InputOutputStream(socket);
		this.messagesManagement = new MessagesManagementServer(this, clientThreadManager, battleshipManagement);
	}

	@Override
	public void run() {
		Object object = null;
		while (true) {
			object = inputOutputStream.receiveTheObject();
			if (!messagesManagement.recieveTheObject(object)) {
				break;
			}
		}
		interrupt();
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}
	
	public void sendTheObject(Object object) {
		inputOutputStream.sendTheObject(object);
	}
	
	public void interrupt() {
		inputOutputStream.closeInputOutputStream();
		super.interrupt();
	}
}
