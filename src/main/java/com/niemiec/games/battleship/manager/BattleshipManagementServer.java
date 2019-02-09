package com.niemiec.games.battleship.manager;

import com.niemiec.games.battleship.game.Battleship;
import com.niemiec.objects.managers.ClientThreadManager;

public class BattleshipManagementServer {
	private ClientThreadManager clientThreadManager;
	private BattleshipManager battleshipManager;

	public BattleshipManagementServer(ClientThreadManager clientThreadManager) {
		this.clientThreadManager = clientThreadManager;
		this.battleshipManager = new BattleshipManager();
	}

	public void receiveBattleshipGame(BattleshipGame battleshipGame) {
		switch (battleshipGame.getGameStatus()) {
		case BattleshipGame.GAME_PROPOSAL:
			receiveGameProposal(battleshipGame);
			break;
		case BattleshipGame.ACCEPTING_THE_GAME:
			receiveAcceptingTheGame(battleshipGame);
			break;
		case BattleshipGame.REJECTION_GAME_PROPOSAL:
			receiveRejectionGameProposal(battleshipGame);
			break;
		case BattleshipGame.SHIPS_ADDED:
			receiveShipsAdded(battleshipGame);
			break;
		case BattleshipGame.PLAY_THE_GAME:
			receivePlayTheGame(battleshipGame);
			break;
		case BattleshipGame.GIVE_UP:
			receiveEndGame(battleshipGame);
			break;
		case BattleshipGame.END_GAME:
			receiveEndGame(battleshipGame);
			break;
		}
	}

	private void receiveEndGame(BattleshipGame battleshipGame) {
		sendToOpponentPlayer(battleshipGame);
//		sendToBothPlayers(battleshipGame);
		battleshipManager.deleteBattleship(battleshipGame);
	}

	private void receivePlayTheGame(BattleshipGame battleshipGame) {
		Battleship battleship = battleshipManager.getBattleship(battleshipGame.getGameIndex());
		battleshipGame = battleship.receivePlayTheGame(battleshipGame);
		sendToBothPlayers(battleshipGame);
		if (battleshipGame.getGameStatus() == BattleshipGame.END_GAME) {
			battleshipManager.deleteBattleship(battleshipGame);
		}
	}

	private void receiveShipsAdded(BattleshipGame battleshipGame) {

		Battleship battleship = battleshipManager.getBattleship(battleshipGame);
		battleship.updatePlayer(battleshipGame.getPlayer());
		if (battleship.checkIfStart()) {
			BattleshipGame b = battleship.generateBattleshipGameForStart(battleshipGame);
			sendToBothPlayers(b);
		}
	}

	private void receiveRejectionGameProposal(BattleshipGame battleshipGame) {
		Battleship battleship = battleshipManager.getBattleship(battleshipGame);
		battleshipGame = battleship.createBattleshipGameForOpponentPlayer(battleshipGame);
		
		clientThreadManager.sendTheObject(battleshipGame);		
		battleshipManager.deleteBattleship(battleshipGame);
	}

	private void receiveAcceptingTheGame(BattleshipGame battleshipGame) {
		battleshipGame.setGameStatus(BattleshipGame.ADD_SHIPS);
		sendToBothPlayers(battleshipGame);
	}

	private void receiveGameProposal(BattleshipGame battleshipGame) {
		battleshipGame = battleshipManager.createNewBattleship(battleshipGame);
		try {
		sendToOpponentPlayer(battleshipGame);
		} catch (Exception e) {
			battleshipGame.setGameStatus(BattleshipGame.REJECTION_GAME_PROPOSAL);
			battleshipGame = battleshipManager.getBattleship(battleshipGame).createBattleshipGameForOpponentPlayer(battleshipGame);
			sendToOpponentPlayer(battleshipGame);
		}
	}

	private void sendToBothPlayers(BattleshipGame battleshipGame) {
		Battleship battleship = battleshipManager.getBattleship(battleshipGame);
		clientThreadManager.sendTheObject(battleship.sendToTheFirstPlayer(battleshipGame));
		clientThreadManager.sendTheObject(battleship.sendToTheSecondPlayer(battleshipGame));
	}
	
	private void sendToOpponentPlayer(BattleshipGame battleshipGame) {
		Battleship battleship = battleshipManager.getBattleship(battleshipGame);
		clientThreadManager.sendTheObject(battleship.createBattleshipGameForOpponentPlayer(battleshipGame));
	}
}
