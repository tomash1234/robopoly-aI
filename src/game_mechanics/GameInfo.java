package game_mechanics;

import java.util.List;

public interface GameInfo {
	
	void onGameStarted(String info);
	void setGraphicsRespond(GraphicsRespond graphicsRespond);
	
	void onStartMovingFigure(int playerId, int destination);
	
	void onDiceStoppedRolling(int val1, int val2, int doubleRows);
	
	void onArriveOnPlat(Plat plat);
	
	void onPropertySold(Realty realty, int player);
	
	void onPlayerPassStart(int player);
	 
	void onCardFlip(int type, String text);
	
	/*void onDealAccepted(Deal deal);
	void onDealRefused(Deal dael); */
	
	void setGameReferee(GameReferee gameReferee);
	
	void onPlayerTurnStart(int player);
	boolean gameInfoAll();
	void onEndOfGame(Stats stats, List<Player> players);

}
