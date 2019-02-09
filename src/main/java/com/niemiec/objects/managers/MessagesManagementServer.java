package com.niemiec.objects.managers;

import com.niemiec.chat.messages.condition.CheckNickMessage;
import com.niemiec.chat.messages.condition.ExitMessage;
import com.niemiec.chat.messages.condition.ReadyToWorkMessage;
import com.niemiec.chat.messages.text.GroupMessage;
import com.niemiec.chat.messages.text.PrivateMessage;
import com.niemiec.games.battleship.manager.BattleshipGame;
import com.niemiec.games.battleship.manager.BattleshipManagementServer;
import com.niemiec.objects.ClientThread;

public class MessagesManagementServer {
	private ClientThread clientThread;
	private ClientThreadManager clientThreadManager;
	private BattleshipManagementServer battleshipManagement;

	public MessagesManagementServer(ClientThread clientThread, ClientThreadManager clientThreadManager,
			BattleshipManagementServer battleshipManagement) {
		this.clientThread = clientThread;
		this.clientThreadManager = clientThreadManager;
		this.battleshipManagement = battleshipManagement;
	}

	public boolean recieveTheObject(Object object) {
		if (object instanceof GroupMessage) {
			sendGroupMessage((GroupMessage) object);
		} else if (object instanceof PrivateMessage) {
			sendPrivateMessage((PrivateMessage) object);
		} else if (object instanceof CheckNickMessage) {
			checkNick((CheckNickMessage) object);
		} else if (object instanceof ReadyToWorkMessage) {
			updateUsersList();
		} else if (object instanceof BattleshipGame) {
			receiveBattleshipGame((BattleshipGame) object);
		} else if (object instanceof ExitMessage) {
			deleteClientThread((ExitMessage) object);
			return false;
		}
		return true;
	}

	private void receiveBattleshipGame(BattleshipGame battleshipGame) {
		battleshipManagement.receiveBattleshipGame(battleshipGame);
	}

	private void updateUsersList() {
		clientThreadManager.sendAllClientThreadNickList();
	}

	private void deleteClientThread(ExitMessage exitMessage) {
		clientThreadManager.removeClientThread(exitMessage.getNick());
	}

	private void sendPrivateMessage(PrivateMessage privateMessage) {
		clientThreadManager.sendTheObject(privateMessage.getRecipientNick(), privateMessage);
	}

	private void sendGroupMessage(GroupMessage groupMessage) {
		clientThreadManager.sendTheObjectAll(groupMessage);
	}

	private void checkNick(CheckNickMessage checkNickMessage) {
		if (clientThreadManager.add(checkNickMessage.getNick(), clientThread)) {
			checkNickMessage.nickNotExist();
		}
		sendTheObject(checkNickMessage);
	}

	private void sendTheObject(Object object) {
		clientThread.sendTheObject(object);
	}

}
