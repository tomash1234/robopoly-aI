package game_mechanics;

public class PlayerRequester implements  PlayerInteraction{
	
	private PlayerInteraction[] interactions;
	
	/*public PlayerRequester(){
		this.interactions = interactions;
		this.playersId=  playersId;
	}
	*/
	public void setInteractions(PlayerInteraction[] interactions) {
		this.interactions = interactions;
	}
	private int getPlayerIndex(int playerId){
		return playerId;
		/*for(int i = 0; i <playersId.length; i++){
			if(playerId == playersId[i]){
				return i;
			}
		}
		return -1;*/
	}

	@Override
	public void onStartGame(Player player, String code) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(player.getPlayerId())].onStartGame(player, code);
	}

	@Override
	public boolean onTurnStart(Player player) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player.getPlayerId())].onTurnStart(player);
	}

	@Override
	public void setGameReferee(GameReferee gameReferee, int playerID) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(playerID)].setGameReferee(gameReferee, playerID);
	}

	@Override
	public boolean doYouWantBuyIt(Player player, Realty property) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player.getPlayerId())].doYouWantBuyIt(player, property);
	}

	@Override
	public boolean haveYouReadTheCard(Player player, String text) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player.getPlayerId())].haveYouReadTheCard(player, text);
	}

	@Override
	public void youHaveToPayToBank(Player player, int amount) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(player.getPlayerId())].youHaveToPayToBank(player, amount);
		
	}

	@Override
	public void walletUsed(Player player, int money, int lastTranslaction) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(player.getPlayerId())].walletUsed(player, money, lastTranslaction);
		
	}

	@Override
	public boolean youHavetToPayTo(Player payer, Player player, int money) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(payer.getPlayerId())].youHavetToPayTo(payer, player, money);
		
	}
	@Override 
	public boolean dealWasOffered(Player player, Deal deal) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player.getPlayerId())].dealWasOffered(player, deal);
	}

	@Override
	public void dealAnswer(Player offer, boolean answer, Deal deal) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(offer.getPlayerId())].dealAnswer(offer, answer, deal);
	}
	
	/*@Override
	public void onAuctionStarts(Property property, int price) {
		// TODO Auto-generated method stub
		//inactive vyřešit
		for(PlayerInteraction interaction: interactions){
			interaction.onAuctionStarts(property, price);
		}
	}
	@Override
	public void onAuctionPriceRised(int player, int value) {
		// TODO Auto-generated method stub
		for(PlayerInteraction interaction: interactions){
			interaction.onAuctionPriceRised(player, value);
		}
	}*/
	
	public int onAuctionStarts(int player, Property property, int price, boolean start, int lastPlayer) {
		return interactions[getPlayerIndex(player)].onAuctionStarts(player, property, price, start, lastPlayer);
		
	};
	@Override
	public void isSomeOneTurn(int player, int playerToGo) {
		// TODO Auto-generated method stub
		interactions[getPlayerIndex(player)].isSomeOneTurn(player, playerToGo);
	}
	@Override
	public boolean needOwnThread(int player) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player)].needOwnThread(player);
	}
	@Override
	public String getPlayersType(Player player) {
		// TODO Auto-generated method stub
		return interactions[getPlayerIndex(player.getPlayerId())].getPlayersType(player);
	}

}
