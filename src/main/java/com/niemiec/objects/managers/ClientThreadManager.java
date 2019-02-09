package com.niemiec.objects.managers;

import java.util.ArrayList;

import com.niemiec.chat.messages.condition.UsersListMessage;
import com.niemiec.games.battleship.manager.BattleshipGame;
import com.niemiec.objects.ClientThread;

public class ClientThreadManager {
	private ArrayList<ClientThread> clientThreadList;
	private ArrayList<String> clientThreadNickList;
	
	public ClientThreadManager() {
		clientThreadList = new ArrayList<>();
		clientThreadNickList = new ArrayList<>();
	}
	
	public synchronized void sendTheObject(String nick, Object object) {
		getClientThread(nick).sendTheObject(object);
	}
	
	public synchronized void sendTheObject(Object battleshipGame) {
		getClientThread(((BattleshipGame) battleshipGame).getPlayer().getNick()).sendTheObject(battleshipGame);
	}
	
	public synchronized void sendAllClientThreadNickList() {
		sendTheObjectAll(new UsersListMessage(clientThreadNickList.clone()));
	}
	
	public synchronized void sendTheObjectAll(Object object) {
		for (ClientThread c : clientThreadList) {
			c.sendTheObject(object);
		}
	}

	public synchronized void removeClientThread(String nick) {
		ClientThread c = getClientThread(nick);
		clientThreadList.remove(c);
		clientThreadNickList.remove(nick);
		sendAllClientThreadNickList();
	}

	public synchronized boolean add(String nick, ClientThread clientThread) {
		if (getClientThread(nick) != null)
			return false;
		clientThread.setNick(nick);
		clientThreadList.add(clientThread);
		clientThreadNickList.add(nick);
		return true;
	}
	
	private synchronized ClientThread getClientThread(String nick) {
		for (ClientThread c : clientThreadList) {
			if (c.getNick().equals(nick))
				return c;
		}
		return null;
	}
	
	public synchronized void shutdown() {
		try {
			for (int i = 0; i < clientThreadList.size(); i++)
				clientThreadList.get(i).interrupt();
		} catch (Exception e) {
			
		}
	}
}
