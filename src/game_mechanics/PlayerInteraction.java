package game_mechanics;

import java.util.List;

public interface PlayerInteraction {
	
	/**
	 * On start of the game, player got unique code, which is used for communication with GameReferee
	 * @param code
	 */
	void onStartGame(Player player, String code);
	/**
	 * Every players is asked to check if somebody is visiting his property
	 */
	
	/**
	 * Player is asked for tossing a dice
	 */
	boolean onTurnStart(Player player);
	
	
	void setGameReferee(GameReferee gameReferee, int playerID);
	
	/**
	 * Player can buy a realty
	 * @param player
	 * @param property realty to buy
	 * @return if player works in this thread, he answers by setting true or false; 
	 */
	boolean doYouWantBuyIt(Player player,Realty property);
	
	
	int onAuctionStarts(int player, Property property, int price, boolean start, int lastPlayer);
	
	/**
	 * 
	 * @param player
	 * @param text Card text
	 * @return
	 */
	boolean haveYouReadTheCard(Player player,String text);
	
	/**
	 * Player is asked for paying money to the bank
	 * If player works in this thread, he has to pay this amount, otherwise he will bankrupt
	 * @param player
	 * @param amount money to pay
	 */
	void youHaveToPayToBank(Player player,int amount);
	/**
	 * Info
	 * Wallet was used
	 * @param player
	 * @param money money in wallet 
	 * @param lastTranslaction 
	 */
	void walletUsed(Player player,int money, int lastTranslaction);
	
	/**
	 * If player works in this thread, he has to pay this amount, otherwise he will bankrupt
	 * @param payer player who has to pay
	 * @param player player who receives money
	 * @param money money to pay
	 * @return
	 */
	boolean youHavetToPayTo(Player payer,Player player, int money);
	
	/**
	 * If player works in this thread, player answers to the by returning boolean
	 * @param player 
	 * @param deal 
	 * @return 
	 */
	boolean dealWasOffered(Player player, Deal deal);
	
	/**
	 * After player offers his deal, the answer gets by this method
	 * @param offer 
	 * @param answer true deal accepted; false deal refused
	 */
	void dealAnswer(Player offer, boolean answer, Deal deal);
	
	void isSomeOneTurn(int player, int playerToGo);
	
	/**
	 * Return false if player works in one thread, if he need own thread he returns false
	 * @param player
	 * @return
	 */
	boolean needOwnThread(int player);
	
	/**
	 * Return string name of players type. HumanPlaye OR StaticAI OR NeuralNetworkAI
	 * @param player
	 * @return string type name
	 */
	String getPlayersType(Player player);
	
	
	

}
