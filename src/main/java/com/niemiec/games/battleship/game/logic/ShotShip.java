package com.niemiec.games.battleship.game.logic;

import java.io.Serializable;
import java.util.Random;

import com.niemiec.games.battleship.game.data.check.CheckData;
import com.niemiec.games.battleship.game.objects.Board;
import com.niemiec.games.battleship.game.objects.Coordinates;
import com.niemiec.games.battleship.game.objects.Player;
import com.niemiec.games.battleship.game.objects.PlayerImpl;
import com.niemiec.games.battleship.game.objects.Ship;

@SuppressWarnings("serial")
public class ShotShip implements Serializable  {
	private boolean gameEnd;
	private int winner;
	private int turn;
	private Player[] players;

	public ShotShip() {
		this.gameEnd = false;
		this.winner = -1;
		this.turn = randomTurn();
		this.players = new PlayerImpl[2];
	}

	private int randomTurn() {
		Random random = new Random();
		return random.nextInt(2);
	}

	public void addPlayers(Player player1, Player player2) {
		players[0] = player1;
		players[1] = player2;
	}
	
	public Player getPlayer(int index) {
		return (Player) players[index].clone();
	}

	public boolean shot(Coordinates coordinates) {
		if (!shotRealPlayer(coordinates) && !gameEnd) {
			changeTurn();
		}
		return gameEnd;
	}

	private boolean shotRealPlayer(Coordinates coordinates) {
		return shotMast(coordinates, turn);
	}

	private boolean shotMast(Coordinates coordinates, int activePlayer) {

		if (checkWithTheOpponentBoardIfWasHit(coordinates, activePlayer)) {
//			BorderManagement.drawOpponentBoardInOpponentBorder(players[Player.REAL_PLAYER]);
//			BorderManagement.drawBoardInMyBorder(players[Player.REAL_PLAYER]);
			return true;
		}
//		BorderManagement.drawOpponentBoardInOpponentBorder(players[Player.REAL_PLAYER]);
//		BorderManagement.drawBoardInMyBorder(players[Player.REAL_PLAYER]);

		return false;
	}

	private boolean checkWithTheOpponentBoardIfWasHit(Coordinates coordinates, int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		int box = getBoxFromPlayerBoard(coordinates, opponentPlayer);

		switch (box) {
		case Board.BOX_EMPTY:
			setBoxInToOpponentBoard(coordinates, Board.BOX_NOT_HIT, activePlayer);
			setBoxInToPlayerBoard(coordinates, Board.BOX_NOT_HIT, opponentPlayer);
			return false;
		case Board.BOX_SHIP:
			actionAfterHit(coordinates, activePlayer);
			return true;
		}
		return true;
	}

	private void actionAfterHit(Coordinates coordinates, int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		shipWasHit(opponentPlayer, coordinates);
		if (shipWasSunk(opponentPlayer, coordinates)) {
			change3To5AndInsert1Around(activePlayer, coordinates);
			ifActivePlayerIsVirtualResetTheHitData(activePlayer);
			players[opponentPlayer].increaseSunkenShips();
			checkIfWeHaveTheEndOfTheGame(activePlayer);
		} else {
			setBoxInToOpponentBoard(coordinates, Board.BOX_HIT, activePlayer);
			setBoxInToPlayerBoard(coordinates, Board.BOX_HIT, opponentPlayer);
			ifActivePlayerIsVirtualSetHitData(coordinates, activePlayer);
		}

	}

	private void checkIfWeHaveTheEndOfTheGame(int activePlayer) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		if (players[opponentPlayer].getSunkenShips() == 10) {
			gameEnd = true;
			winner = activePlayer;
		}
	}

	private void shipWasHit(int opponentPlayer, Coordinates coordinates) {
		players[opponentPlayer].getCollectionShips().shipWasHit(coordinates);

	}

	private boolean shipWasSunk(int opponentPlayer, Coordinates coordinates) {
		return players[opponentPlayer].getCollectionShips().checkIfShipIsSunk(coordinates);
	}

	private void change3To5AndInsert1Around(int activePlayer, Coordinates coordinates) {
		insetr1AroundHitShip(activePlayer, coordinates);
		check3To5InBoard(activePlayer, coordinates);		
	}

	private void insetr1AroundHitShip(int activePlayer, Coordinates coordinates) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		Ship ship = players[opponentPlayer].getCollectionShips().getShip(coordinates);
		for (int i = 1; i <= ship.getNumberOfMasts(); i++) {
			Coordinates c = ship.getCoordinates(i);
			insert1AroundMast(c, activePlayer, opponentPlayer);
		}
	}

	private void insert1AroundMast(Coordinates c, int activePlayer, int opponentPlayer) {
		for (int changeX = -1; changeX < 2; changeX++) {
			for (int changeY = -1; changeY < 2; changeY++) {
				if (CheckData.checkIfWithinThePlayingField(c, changeX, changeY)) {
					Coordinates cWithChangeXY = new Coordinates(c, changeX, changeY);
					insertOne(cWithChangeXY, activePlayer, opponentPlayer);
				}
			}
		}
	}

	private void insertOne(Coordinates cWithChangeXY, int activePlayer, int opponentPlayer) {
		int box = getBoxFromPlayerBoard(cWithChangeXY, opponentPlayer);
		if (box != Board.BOX_NOT_HIT && box != Board.BOX_HIT) {
			setBoxInToPlayerBoard(cWithChangeXY, Board.BOX_NOT_HIT, opponentPlayer);
			setBoxInToOpponentBoard(cWithChangeXY, Board.BOX_NOT_HIT, activePlayer);
		}
	}

	private void check3To5InBoard(int activePlayer, Coordinates coordinates) {
		int opponentPlayer = getIndexOpponentPlayer(activePlayer);
		Ship ship = players[opponentPlayer].getCollectionShips().getShip(coordinates);
		for (int i = 1; i <= ship.getNumberOfMasts(); i++) {
			Coordinates c = ship.getCoordinates(i);
			setBoxInToOpponentBoard(c, Board.BOX_SUNK, activePlayer);
			setBoxInToPlayerBoard(c, Board.BOX_SUNK, opponentPlayer);
		}
	}

	private void ifActivePlayerIsVirtualResetTheHitData(int activePlayer) {
		if (players[activePlayer].getInformationInThePlayerIsVirtual()) {
			PlayerImpl player = (PlayerImpl) players[activePlayer];
			player.resetHitData();
		}
	}

	private void ifActivePlayerIsVirtualSetHitData(Coordinates coordinates, int activePlayer) {
		if (players[activePlayer].getInformationInThePlayerIsVirtual()) {
			PlayerImpl player = (PlayerImpl) players[activePlayer];
			player.setHitData(coordinates);
		}
	}

	private int getIndexOpponentPlayer(int activePlayer) {
		return (activePlayer == Player.SECOND_PLAYER) ? Player.FIRST_PLAYER : Player.SECOND_PLAYER;
	}

	private void setBoxInToPlayerBoard(Coordinates coordinates, int box, int typeOfPlayer) {
		players[typeOfPlayer].getBoard().setBox(coordinates, box);
	}

	private void setBoxInToOpponentBoard(Coordinates coordinates, int box, int typeOfPlayer) {
		players[typeOfPlayer].getOpponentBoard().setBox(coordinates, box);
	}

	private int getBoxFromPlayerBoard(Coordinates coordinates, int typeOfPlayer) {
		return players[typeOfPlayer].getBoard().getBox(coordinates);
	}

//	public void firstShotInTheGame() {
//		int r = randomStart();
//
//		if (r == Player.FIRST_PLAYER) {
//			shotVirtualPlayer();
//		}
//	}

//	private int randomStart() {
//		Random random = new Random();
//		return random.nextInt(2);
//	}

//	public String getWinnerName() {
//		if (winner == Player.SECOND_PLAYER)
//			return "RealPlayer";
//		else
//			return "VirtualPlayer";
//	}

	public String getWinner() {
		return players[winner].getNick();
	}
	
	public int getTurn() {
		return turn;
	}
	
	private void changeTurn() {
		turn = (turn == 0 ? 1 : 0);
	}
}
