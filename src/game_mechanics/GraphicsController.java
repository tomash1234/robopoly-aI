package game_mechanics;


import javafx.scene.paint.Color;

public interface GraphicsController {
	
	/**
	 * Creating game board
	 */
	void initBoard();
	/**
	 * Creating plats
	 */
	void initPlat(int id,String text1, String price, String imageUrl, Color color);
	/**
	 * Creating players
	 * @param players array of players
	 */
	void initPlayer(Player[] players);
	/**
	 * Moves with player's figure
	 * @param playerId 
	 * @param destination i>=0 AND i<40
	 */
	void moveWithFigure(int playerId, int destination);

	/**
	 * Add houses or hotel on specific plat
	 * @param platId specific plat ID
	 * @param number number of houses, 1-4, 5=hotel
	 */
	void setHouse(int platId, int number);

}
