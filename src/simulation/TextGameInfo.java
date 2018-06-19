package simulation;

import java.util.List;

import game_mechanics.Deal;
import game_mechanics.GameInfo;
import game_mechanics.GameReferee;
import game_mechanics.GraphicsRespond;
import game_mechanics.Plat;
import game_mechanics.Player;
import game_mechanics.Property;
import game_mechanics.Realty;
import game_mechanics.Stats;

public class TextGameInfo implements GameInfo{
	private GraphicsRespond graphicsRespond;

	@Override
	public void onGameStarted(String info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGraphicsRespond(GraphicsRespond graphicsRespond) {
		// TODO Auto-generated method stub
		this.graphicsRespond = graphicsRespond;
	}

	@Override
	public void onStartMovingFigure(int playerId, int destination) {
		// TODO Auto-generated method stub
		graphicsRespond.onFigureReachedRightPlace();
	}

	@Override
	public void onDiceStoppedRolling(int val1, int val2, int doubleRows) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArriveOnPlat(Plat plat) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPropertySold(Realty realty, int player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerPassStart(int player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardFlip(int type, String text) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setGameReferee(GameReferee gameReferee) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerTurnStart(int player) {
		// TODO Auto-generated method stub
		
		
	}
	@Override
	public void onEndOfGame(Stats stats, List<Player> players) {
		// TODO Auto-generated method stub
		System.out.println("---- END OF GAME----");
		int p = players.size();
		for(Player pl: players){
			System.out.println("" + p + ". \t" + pl.getName() + "\t" +  stats.getTotalMoney(pl.getPlayerId()) + " BTC");
			p--;
		}
		System.out.println(stats);
		
	}

	@Override
	public boolean gameInfoAll() {
		// TODO Auto-generated method stub
		return false;
	}
}
