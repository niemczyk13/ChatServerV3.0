package com.niemiec.games.battleship.game;

import java.util.HashMap;

import com.niemiec.games.battleship.game.logic.ShotShip;
import com.niemiec.games.battleship.game.objects.Player;
import com.niemiec.games.battleship.manager.BattleshipGame;

public class Battleship {
	private final int FIRST_PLAYER = 0;
	private final int SECOND_PLAYER = 1;
	
	private int index;
	private HashMap<String, Player> players;
	private String nicks[]; // dzielenie bez reszty do usytalania kolejki
	private ShotShip shotShip;
	private int theNumberOfPlayersWhoAddedShips;

	public Battleship(int index) {
		this.index = index;
		this.nicks = new String[2];
		this.players = new HashMap<>();
		this.shotShip = new ShotShip();
		this.theNumberOfPlayersWhoAddedShips = 0;
	}
	
	public BattleshipGame receivePlayTheGame(BattleshipGame battleshipGame) {
		if (shotShip.shot(battleshipGame.getShotCoordinates())) {
			battleshipGame.setGameStatus(BattleshipGame.END_GAME);
			battleshipGame.setWinnerNick(shotShip.getWinner());
		}
		battleshipGame.setNickWhoseTourn(nicks[shotShip.getTurn()]);
		return battleshipGame;
	}

	public int getIndex() {
		return index;
	}

	public void addPlayer(Player player) {
		if (players.size() < 2) {
			nicks[players.size()] = player.getNick();
			players.put(player.getNick(), player);
		}
		if (players.size() == 2) {
			shotShip.addPlayers(players.get(nicks[0]), players.get(nicks[1]));
		}
	}
	
	public String getNickFirstPlayer() {
		return nicks[FIRST_PLAYER];
	}
	
	public String getNickSecondPlayer() {
		return nicks[SECOND_PLAYER];
	}

	public boolean checkIfStart() {
		theNumberOfPlayersWhoAddedShips++;
		if (theNumberOfPlayersWhoAddedShips == 2) {
			shotShip.addPlayers(players.get(nicks[0]), players.get(nicks[1]));
			return true;
		}
		return false;
	}
	
	public void updatePlayer(Player player) {
		players.put(player.getNick(), player);
	}

	public BattleshipGame generateBattleshipGameForStart(BattleshipGame battleshipGame) {
		battleshipGame.setGameStatus(BattleshipGame.PLAY_THE_GAME);
		battleshipGame.setNickWhoseTourn(nicks[shotShip.getTurn()]);
		
		return battleshipGame;
	}

	public Object sendToTheFirstPlayer(BattleshipGame b) {
		b.setPlayer(shotShip.getPlayer(FIRST_PLAYER));
		b.setOpponentPlayerNick(nicks[SECOND_PLAYER]);
		return b;
	}

	public Object sendToTheSecondPlayer(BattleshipGame b) {
		b.setPlayer(shotShip.getPlayer(SECOND_PLAYER));
		b.setOpponentPlayerNick(nicks[FIRST_PLAYER]);
		return b;
	}

	public BattleshipGame createBattleshipGameForOpponentPlayer(BattleshipGame battleshipGame) {
		Player player = (Player) players.get(battleshipGame.getOpponentPlayerNick()).clone();
		String nick = battleshipGame.getPlayer().getNick();
		
		battleshipGame.setPlayer(player);
		battleshipGame.setOpponentPlayerNick(nick);
		
		return battleshipGame;
	}
	
//	private Player getOpponentPlayer(Player player) {
//		if (player.getNick().equals(nicks[0])) {
//			return players.get(nicks[0]);
//		}
//		return players.get(nicks[1]);
//	}
	
	public Player getFirstPlayer() {
		return players.get(nicks[0]);
	}
	
	public String getFirstNick() {
		return nicks[0];
	}
	
	public Player getSecondPlayer() {
		return players.get(nicks[1]);
	}
	
	public String getSecondNick() {
		return nicks[1];
	}
}
