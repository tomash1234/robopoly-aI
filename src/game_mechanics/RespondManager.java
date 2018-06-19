package game_mechanics;
/**
 * Tato přídá má na starosti zpracovávat vstup a výstup od uživatele a synchronizovat herní smyčku
 * Třída zpracovávat čekání a obchody
 * @author Tomas
 *
 */
public class RespondManager implements GameReferee{
	
	private int waitFor = 0;
	private int requester = 0;
	
	private Deal deal;
	private Auction auction;
	
	private String codes[];
	private Player players[];
	private PlayerInteraction playerIteracion;
	
	private Object lock = new Object();

	public static int PLAYER_OK  = 0;
	public static int PLAYER_LEAVE_GAME  = 1;
	public static int PLAYER_NO_RESPOND  = 2;
	public static int PLAYER_NO = 3;
	public static int PLAYER_QUIT = 4;
	

	public static final int TOSS_THE_DICE = 1;
	public static final int ASKING_IF_BUY_BANK = 2;
	public static final int ASKING_IF_CARD_READ = 3;
	public static final int ASKING_FOR_MONEY_TO_BANK = 4;
	public static final int ASKING_FOR_RENT = 5;
	private static final int WAIT_REACH = 6;
	public static final int ASKING_AUCTION = 7;
	
	private boolean responded = false;
	private int respondedInt = 0;
	
	private int moneyPaid = 0;
	private int moneyOwner = 0;
	private boolean dealWait = false;
	private  boolean quit = false;
	private GameSession gameSession;
	

	public RespondManager( Player[] players, PlayerInteraction playerIteracion, GameSession gameSession) {
		super();
		this.gameSession =gameSession;
		this.players = players;
		this.playerIteracion = playerIteracion;
		
	
	}
	
	protected void start(String[] codes){
		this.codes = codes;
		for(int i= 0; i<players.length; i++){
			final int a = i;
			if(playerIteracion.needOwnThread(i)){
				startNewThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerIteracion.setGameReferee(RespondManager.this, a);
						playerIteracion.onStartGame(players[a], codes[a]);
						
					}
				});
			}else{
				playerIteracion.setGameReferee(RespondManager.this, i);
				playerIteracion.onStartGame(players[a], codes[a]);
				
			}
		}
	}
	protected void startTurn(){
		moneyPaid = 0;
		 quit = false;
	}
	
	protected int waitForTosting(int playerToGo){
		waitFor = TOSS_THE_DICE;
		responded= false;
		requester = playerToGo;
		deal = null;
		quit = false;

		if(playerIteracion.needOwnThread(playerToGo)){
			
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playerIteracion.onTurnStart(players[playerToGo]);
				}
			});
			waitFor = 0;
			if(quit){
				quit = false;
				return PLAYER_QUIT;
			}
			if(responded){
				return PLAYER_OK;
			}	
		}else{
			boolean toss = playerIteracion.onTurnStart(players[playerToGo]);
			if(quit){
				quit = false;
				return PLAYER_QUIT;
			}
			return toss?PLAYER_OK:PLAYER_NO_RESPOND;
		}

		return PLAYER_NO_RESPOND;
	}
	protected int waitForBuying(int playerToGo, Realty property){
		waitFor = ASKING_IF_BUY_BANK;
		respondedInt= 0;
		requester = playerToGo;
		quit = false;
 
		if(playerIteracion.needOwnThread(playerToGo)){
			
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playerIteracion.doYouWantBuyIt(players[playerToGo], property);
				}
			});
			waitFor = 0;
			if(quit){
				return PLAYER_QUIT; 
			} 
				
			if(respondedInt==1){
				return PLAYER_OK;
			}else if(respondedInt==2){
				return PLAYER_NO;
			}
		}else{
			boolean buy = playerIteracion.doYouWantBuyIt(players[playerToGo], property);
			if(quit){
				return PLAYER_QUIT;
			}
			return buy?PLAYER_OK:PLAYER_NO;
		}
		
		
		return PLAYER_NO_RESPOND;
	}
	
	
	protected int waitToRead(int playerToGo, String text){
		waitFor = ASKING_IF_CARD_READ;
		responded= false;
		requester = playerToGo;
		quit = false;
		if(playerIteracion.needOwnThread(playerToGo)){
	
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playerIteracion.haveYouReadTheCard(players[playerToGo], text);
				}
			});
		
			waitFor = 0;
			if(quit){
				return PLAYER_QUIT;
			}
			requester = -1;
			if(responded){
				return PLAYER_OK;
			}
		}else{
			boolean read = playerIteracion.haveYouReadTheCard(players[playerToGo], text);
			if(quit){
				return PLAYER_QUIT;
			}
			return read?PLAYER_OK:PLAYER_NO_RESPOND;
		}

		return PLAYER_NO_RESPOND;
	}
	
	protected int waitToPayToBank(int playerToGo, int money){
		waitFor = ASKING_FOR_MONEY_TO_BANK;
		requester = playerToGo;
		moneyPaid = money;
		quit = false;
		if(playerIteracion.needOwnThread(playerToGo)){
	
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playerIteracion.youHaveToPayToBank(players[playerToGo], money);
				}
			});
		
			waitFor = 0;
			requester = -1;

		//	int mon = players[playerToGo].getWallet().payMoneyTo(gameReferee.getGame().getBank().getBankWallet(), money);
			//platba musí být zaplaceno
			if(quit){
				return PLAYER_QUIT;
			}
			if(moneyPaid == 0){
				
				return PLAYER_OK;
			}
			
				
		}else{
			playerIteracion.youHaveToPayToBank(players[playerToGo], money);
			if(quit){
				return PLAYER_QUIT;
			}
			int mon = players[playerToGo].getWallet().payMoneyTo(gameSession.getGame().getBank().getBankWallet(), money);
			
			if(mon==Wallet.TRANSACTIONS_OK){

				getGame().getStats().moneyPaidToBank(playerToGo, moneyPaid);
				moneyPaid = 0;
				return PLAYER_OK;
			}
		}

		return PLAYER_NO_RESPOND;
	}
	
	protected int waitToPayToPlayer(int playerToGo, int to,  int money){
		moneyOwner = to;
		requester = playerToGo;
		moneyPaid = money;
		quit = false;
		waitFor = ASKING_FOR_RENT;
		if(playerIteracion.needOwnThread(playerToGo)){
			/*requester = playerToGo;
			moneyOwner = to;
			moneyPaid = money;*/
	
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					playerIteracion.youHavetToPayTo(players[requester],players[moneyOwner], money);
				}
			});
		
			waitFor = 0;
			requester = -1;

			//int mon = players[playerToGo].getWallet().payMoneyTo(players[to].getWallet(), money);
			//platba musí být zaplaceno
			if(quit){
				return PLAYER_QUIT;
			}
			
			if(moneyPaid==0){
				
				return PLAYER_OK;
			}
				
		}else{
			boolean p = playerIteracion.youHavetToPayTo(players[playerToGo],players[to], money);
			if(quit){
				return PLAYER_QUIT;
			}
			if(!p){
				if(moneyPaid==0){
					return PLAYER_OK;
				}
			}
			
			int mon = players[playerToGo].getWallet().payMoneyTo(players[to].getWallet(), money);
			if(mon==Wallet.TRANSACTIONS_OK){
				getGame().getStats().moneyPaidToPlayer(playerToGo, moneyPaid);
				getGame().getStats().moneyGotFromPlayer(to, moneyPaid);
				moneyPaid = 0;
				waitFor = 0;
				requester = -1;
				return PLAYER_OK;
			}else{

				return PLAYER_QUIT;
			}
		}

		return PLAYER_NO_RESPOND;
	}
	
	private void waitToRespond(Runnable runnable){
		synchronized (lock) {
			startNewThread(runnable);
			try {
				lock.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	@Override
	public boolean pleasePayMe(String code, int owner) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tossTheDice(String code, Player player) {
		// TODO Auto-generated method stub
		if(code==null){
			if(!codes[requester].equals(code)){
				
			System.err.println("Bad player , wait for " + requester + ", not to" + player.getPlayerId());
			}else{
				System.err.println("Code is null");
			}
			return false;
		}
		if(waitFor!=TOSS_THE_DICE){
		//	return GameReferee.ERROR_INVALID_STATUS;
			System.err.println("Wait for another events");
			return false;
		}
		synchronized (lock) {
			responded = true;
			lock.notify();
		}
		//return GameReferee.OK;
		return true;
	}

	@Override
	public boolean answerToDeal(String code, boolean deal) {
		// TODO Auto-generated method stub
		if(code==null || !codes[requester].equals(code)){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			return false;
		}
		if(waitFor!=ASKING_IF_BUY_BANK){
		//	return GameReferee.ERROR_INVALID_STATUS;
			return false;
		}
		synchronized (lock) {
			respondedInt = deal?1:2;
			lock.notify();
		}
		//return GameReferee.OK;
		return true;
	}

	@Override
	public boolean iHaveReadTheCard(String code) {
		// TODO Auto-generated method stub
		if(code==null || !codes[requester].equals(code)){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			return false;
		}
		if(waitFor!=ASKING_IF_CARD_READ){
		//	return GameReferee.ERROR_INVALID_STATUS;
			return false;
		}
		
		synchronized (lock) {
			responded = true;
			lock.notify();
		}
		//return GameReferee.OK;
		return true;
	}

	@Override
	public boolean payMoneyToBank(String code) {
		// TODO Auto-generated method stub
		if(code==null || !codes[requester].equals(code)){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			return false;
		}
		if(waitFor!=ASKING_FOR_MONEY_TO_BANK){
		//	return GameReferee.ERROR_INVALID_STATUS;
			return false;
		}
		
		synchronized (lock) {
			int m = players[requester].getWallet().payMoneyTo(getGame().getBank().getBankWallet(), moneyPaid);
			if(m==Wallet.TRANSACTIONS_OK){
				getGame().getStats().moneyPaidToBank(requester, moneyPaid);
				moneyPaid = 0;
				moneyOwner = -1;
				lock.notify();
			}else{
				//return GameReferee.ERROR_NOT_ENOUGH_MONEY;
				return false;
			}
		}
		//return GameReferee.OK;
		
		return true;
	}

	@Override
	public boolean payRent(String code, Player payer, boolean money) {
		System.out.println("Paying rent" );
		// TODO Auto-generated method stub
		if(code==null || !codes[requester].equals(code)){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			System.out.println("Bad code" );
			return false;
		}
		if(waitFor!=ASKING_FOR_RENT){
		//	return GameReferee.ERROR_INVALID_STATUS;
			System.out.println("Bad code 2 " );
			return false;
		}
		
		synchronized (lock) {
			int m = players[requester].getWallet().payMoneyTo(players[moneyOwner].getWallet(), moneyPaid);
			if(m==Wallet.TRANSACTIONS_OK){
				getGame().getStats().moneyPaidToPlayer(requester, moneyPaid);
				getGame().getStats().moneyGotFromPlayer(moneyOwner, moneyPaid);
				lock.notify();
			}else{
				//return GameReferee.ERROR_NOT_ENOUGH_MONEY;
				System.out.println("Bad code 3 " );
				return false;
			}
		}
		//return GameReferee.OK;
		moneyPaid = 0;
		return true;
	}

	@Override
	public boolean bid(String code, Player player, int value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createAuction(int player, String code, Property propterty, int price) {
		// TODO Auto-generated method stub
		if(player==-1){
			if(propterty.getOwner()!=-1){
				return false;
			}
			auction = new Auction(propterty, price, players.length);
			requstAllPlayersAuction();
			return true;
		}
		return false;
	}
	
	private void requstAllPlayersAuction(){
		for(int i = 0; i<players.length; i++){
			if(playerIteracion.needOwnThread(i)){
				final int a = i;
				startNewThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						playerIteracion.onAuctionStarts(a, auction.getProperty(), auction.getMoney(), true, -1);  
					}
				});
			}else{
				playerIteracion.onAuctionStarts(i, auction.getProperty(), auction.getMoney(), true, -1);  
			}
		}
	}

	@Override
	public boolean getOutOfJail(String code, boolean card) {
		// TODO Auto-generated method stub
		
		
		return gameSession.getOutOfJail(code, card);
	}

	@Override
	public boolean sellToBank(String code, Player player, Property property) {
		// TODO Auto-generated method stub
		return gameSession.sellToBank(code, player, property);
	}

	@Override
	public boolean sellToBank(String code, Player player, Property property, int houses) {
		// TODO Auto-generated method stub
		return gameSession.sellToBank(code, player, property, houses);
	}

	@Override
	public boolean mortgage(String code, Player player, Property property, boolean sell) {
		// TODO Auto-generated method stub
		return gameSession.mortgage(code, player, property, sell);
	}

	@Override
	public boolean buyHouse(String code, Player player, int propertyId) {
		// TODO Auto-generated method stub
		return gameSession.buyHouse(code, player, propertyId);
	}

	@Override
	public boolean createDeal(String code, int owner, Deal deal) {
		// TODO Auto-generated method stub
		if(deal==null){
			//ERROR_DEAL_IS_NULL;
			System.err.println("Deal is null");
			return false;
		}
		if(code==null || !code.equals(codes[owner])|| deal.getPlayerWhoOffer()!=owner||!players[owner].isActive() ||!players[deal.getDealTo()].isActive() || deal.getDealTo()==deal.getPlayerWhoOffer()){ 
			//ERROR_INVALID_USER_OR_CODE
			System.err.println("Deal is made invalid  " + ( !code.equals(codes[owner])) + ", " + ( deal.getPlayerWhoOffer()!=owner) + ", " + (!players[owner].isActive()) + ","
					+ ( deal.getDealTo()==deal.getPlayerWhoOffer()));
			return false;
		}
		
		if( this.deal!=null){
		//ERROR_CURRENT_DEAL_WASNT_FINISHED	;
			System.err.println("Deal isnt allready made");
			return false;
		}
		if(deal.getMoneyOffer()>players[deal.getPlayerWhoOffer()].getMoney()&&deal.getMoneyDemand()>players[deal.getDealTo()].getMoney()){
			//ERROR_NOT_ENOUGH_MONEY;
			System.err.println("Deal not engouh money");
		
			return false;
		}
		
		for(int i = 0;i<deal.getOfferLength(); i++){
			if(deal.getPlayerWhoOffer()!=deal.getOffer(i).getOwner() || (deal.getOffer(i).getRealty()!=null && deal.getOffer(i).getNumberOfHouse()>0)){
				//ERROR_DEAL_PROPERTY_DOESNT_OWN;
				System.err.println("Deal owner hasnt it");
				return false;
			}
		}
		for(int i = 0;i<deal.getDemandLength(); i++){
			if(deal.getDealTo()!=deal.getDemand(i).getOwner() || deal.getDemand(i).getNumberOfHouse()>0){
				//ERROR_DEAL_PROPERTY_DOESNT_OWN;
				System.err.println("Deal demander hasnt it");
				return false;
			}
		}
		if(deal.getRent()==1){
			if(moneyOwner==deal.getDealTo() && requester==deal.getPlayerWhoOffer()){
				deal.setRentValue(moneyPaid);
			}
		}
		if(deal.getRent()==-1){
			if(requester==deal.getDealTo() && moneyOwner==deal.getPlayerWhoOffer()){
				deal.setRentValue(moneyPaid);
			}
		}
		this.deal = deal;
		deal.confirmO();
		///
		if(playerIteracion.needOwnThread(deal.getDealTo())){
			if(playerIteracion.needOwnThread(owner)){
				boolean a = playerIteracion.dealWasOffered(players[deal.getDealTo()], deal);
				return true;
			}
			dealWait = true;
			waitToRespond(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					boolean a = playerIteracion.dealWasOffered(players[deal.getDealTo()], deal);
				}
			});
		}else{
			boolean a = playerIteracion.dealWasOffered(players[deal.getDealTo()], deal);
			//if(a){
				resolveDealAnswer(a);
			//}
		}
		//
		return true;
	}


	
	@Override
	public boolean privateDealAnswe(String code, int demander, boolean answer) {
		// TODO Auto-generated method stub
		if(code==null || !codes[demander].equals(code) || deal==null){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			return false;
		}
		/*if(answer){
			if(waitFor==ASKING_FOR_RENT){
				if(deal.getRent()==-1 && deal.getPlayerWhoOffer()==moneyOwner  && deal.getDealTo()==requester){
					System.out.println("OK rent odpusteno1");
					rentPaid();
					
				}else if(deal.getRent()==1 && deal.getPlayerWhoOffer()==requester  && deal.getDealTo()==moneyOwner){
					System.out.println("OK rent odpusteno");
					rentPaid();
				}else{

					System.out.println("OK rent neeeodpusteno");
				}
			}
			gameSession.commitDeal(deal);
			
		}
		this.deal = null;*/
		resolveDealAnswer(answer);
		return true;
	}
	
	private void resolveDealAnswer(boolean answer){
		playerIteracion.dealAnswer(players[deal.getPlayerWhoOffer()], answer, deal);
		if(answer){
			if(waitFor==ASKING_FOR_RENT){
				if(deal.getRent()==-1 && deal.getPlayerWhoOffer()==moneyOwner  && deal.getDealTo()==requester){
					System.out.println("OK rent odpusteno1");
					rentPaid();
					
				}else if(deal.getRent()==1 && deal.getPlayerWhoOffer()==requester  && deal.getDealTo()==moneyOwner){
					System.out.println("OK rent odpusteno");
					rentPaid();
				}else{

					System.out.println("OK rent neeeodpusteno");
				}
			}
			gameSession.commitDeal(deal);
			
		}
		
		if(dealWait){
			dealWait = false;
			synchronized (lock) {
				
				lock.notify();
			}
		}
		this.deal = null;
		
	}
	private void rentPaid(){
		synchronized (lock) {
			moneyPaid = 0;
			lock.notify();
		}
	}

	@Override
	public boolean quit(String code, int player) {
		// TODO Auto-generated method stub
		if(code==null || !codes[player].equals(code)){
			//return GameReferee.ERROR_INVALID_USER_OR_CODE;
			return false;
		}
		if (waitFor != 0 && requester == player) {
			quit = true;
			synchronized (lock) {
				lock.notify();
			}

		} else {
			System.out.println("Quit Game from else");
			gameSession.quit(player, waitFor==ASKING_FOR_RENT, moneyOwner);
		}
		return true;
	}

	@Override
	public boolean noChecking(String code, int player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getPlayerIdDemandFrom() {
		// TODO Auto-generated method stub
		return requester;
	}

	@Override
	public int getPlayerIdDemandTo() {
		// TODO Auto-generated method stub
		return moneyOwner;
	}

	@Override
	public int getRentPrice(int id) {
		// TODO Auto-generated method stub
		return gameSession.getRentPrice(id);
	}

	@Override
	public int getPlayerToGo() {
		// TODO Auto-generated method stub
		return gameSession.getPlayerToGo();
	}

	@Override
	public int getGameRound() {
		// TODO Auto-generated method stub
		return gameSession.getGameRound();
	}

	@Override
	public Game getGame() {
		// TODO Auto-generated method stub
		return gameSession.getGame();
	}

	@Override
	public int getMonayDamand() {
		// TODO Auto-generated method stub
		return moneyPaid;
	}
	
	private void startNewThread(Runnable runable){
		Thread t = new Thread(runable);
		t.start();
	}

}
