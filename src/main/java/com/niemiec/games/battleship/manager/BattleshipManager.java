package com.niemiec.games.battleship.manager;

import java.util.ArrayList;

import com.niemiec.games.battleship.game.Battleship;
import com.niemiec.games.battleship.game.objects.Player;
import com.niemiec.games.battleship.game.objects.PlayerImpl;

public class BattleshipManager {
	private ArrayList<Battleship> battleships;
	private int index;

	public BattleshipManager() {
		this.battleships = new ArrayList<>();
		this.index = 0;
	}

	public Battleship getBattleship(int index) {
		for (int i = 0; i < battleships.size(); i++) {
			if (battleships.get(i).getIndex() == index)
				return battleships.get(i);
		}
		return null;
	}
	
	public Battleship getBattleship(BattleshipGame battleshipGame) {
		return getBattleship(battleshipGame.getGameIndex());
	}

	public void deleteBattleship(BattleshipGame battleshipGame) {
		int index = battleshipGame.getGameIndex();
		for (int i = 0; i < battleships.size(); i++) {
			if (battleships.get(i).getIndex() == index)
				battleships.remove(i);
		}
	}

	public void showStatistic() {
		System.out.println("*********BATTLESHIP STATISTIC*************");
		for (int i = 0; i < battleships.size(); i++) {
			Battleship b = battleships.get(i);
			System.out.println("Battleship index: " + b.getIndex() + ", player1: " + b.getNickFirstPlayer()
					+ ", player2: " + b.getNickSecondPlayer());
		}
		System.out.println("******************************************");
	}

	public BattleshipGame createNewBattleship(BattleshipGame battleshipGame) {
		index++;
		Battleship battleship = new Battleship(index);
		battleship.addPlayer(battleshipGame.getPlayer());
		battleship.addPlayer(new PlayerImpl(Player.SECOND_PLAYER, battleshipGame.getOpponentPlayerNick()));
		battleshipGame.setGameIndex(index);
		battleships.add(battleship);

		return battleshipGame;
	}
}
