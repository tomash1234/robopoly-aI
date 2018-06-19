package game_mechanics;

public interface GameReferee {
	
	public static int OK = 1;
	public static int ERROR_INVALID_USER_OR_CODE = 2;
	public static int ERROR_NO_CARD_FOUND = 3;
	public static int ERROR_CURRENT_DEAL_WASNT_FINISHED = 4;
	public static int ERROR_NOT_ENOUGH_MONEY = 5;
	public static int ERROR_INVALID_STATUS = 6;
	public static int ERROR_DEAL_PROPERTY_DOESNT_OWN = 7;
	public static int ERROR_DEAL_IS_NULL = 8;
	
	/**
	 * Referee asks visitor for payment
	 * @param owner 
	 * @return true if command was valid
	 */
	boolean pleasePayMe(String code, int owner);
	
	/**¨
	 * Player toss the dice 
	 * @param code identify the player
	 * @return true if command was valid
	 */
	boolean tossTheDice(String code, Player player);
	
	/**
	 * Are accepting the deal
	 * @param code
	 * @param deal yes or no
	 * @return true if command was valid
 	 */
	boolean answerToDeal(String code, boolean deal);
	boolean iHaveReadTheCard(String code);
	

	boolean payMoneyToBank(String code);
	boolean payRent(String code, Player payer, boolean money);
	
	/**
	 * To bid , auction 
	 * @param code
	 * @param value
	 * @return true if command was valid
	 */
	boolean bid(String code,Player player, int value);
	boolean createAuction(int player, String code, Property propterty, int price);
	

	
	
	
	boolean getOutOfJail(String code, boolean card);
	
	boolean sellToBank(String code, Player player, Property property);
	boolean sellToBank(String code,Player player, Property property, int houses);
	boolean mortgage(String code,Player player,Property property, boolean sell);
	boolean buyHouse(String code, Player player,int propertyId);
	
	 boolean createDeal(String code, int owner, Deal deal);
	 boolean privateDealAnswe(String code, int demander, boolean answer);
	 
	 
	 
	 boolean quit(String code, int player);
	
	 boolean noChecking(String code, int player);
	 
	 
	int getPlayerIdDemandFrom();
	int getPlayerIdDemandTo();
	
	int getRentPrice(int id);
	int getPlayerToGo();
	int getGameRound();
	
	
	Game getGame();

	int getMonayDamand();
	
	
}
