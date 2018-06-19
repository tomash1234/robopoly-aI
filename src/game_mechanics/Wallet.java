package game_mechanics;

public class Wallet {
	
	public static final int TRANSACTIONS_OK = 0;
	public static final int TRANSACTIONS_FAIL_INVALID_AMOUNT = 1;
	public static final int TRANSACTIONS_FAIL_NOT_ENOUGH_MONEY = 2;
	
	private int money;
	private Player player;
	private PlayerInteraction interaction;
	
	public int getMoney() {
		return money;
	}
	
	/**
	 * Creates wallet with some money
	 * @param money
	 */
	public Wallet(int money, PlayerInteraction interaction, Player player) {
		// TODO Auto-generated constructor stub
		this.money = money;
		this.interaction = interaction;
		this.player = player;
	}
	
	
	private void receivePayment(int money){
		this.money += money;
		if(interaction!=null){
			interaction.walletUsed(player, this.money, +money);
		}
	}
	/**
	 * Pays money from this wallet to wallet in params
	 * @param wallet receiver
	 * @param money amount of money
	 * @return {@link Wallet.TRANSACTIONS_OK} if it is ok OR:
	 * <br> {@link Wallet.TRANSACTIONS_FAIL_INVALID_AMOUNT}
	 * <br> {@link Wallet.TRANSACTIONS_FAIL_INVALID_MONEY}
	 */
	public int payMoneyTo(Wallet wallet, int money){
		if(money<0){
			return TRANSACTIONS_FAIL_INVALID_AMOUNT;
		}
		if(this.money<money){
			return TRANSACTIONS_FAIL_NOT_ENOUGH_MONEY;
		}
		this.money-= money;
		wallet.receivePayment(money);
		if(interaction!=null){
			interaction.walletUsed(player,this.money, -money);
		}
		
		return TRANSACTIONS_OK;
	}

}
