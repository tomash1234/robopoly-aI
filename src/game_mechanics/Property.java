package game_mechanics;

public class Property {
	
	//public static final int MONEY = 0;
	public static final int REALTY = 1;
	public static final int CARD = 3;
	public static final int GROUPS_ORDER[] = new int[]{2,1,3,4,6,7,8,9,10,5};

	//private int realtyPosition;
	private final int type;
	private int moneyValue;
	private Realty realtyCard;
	private int numberOfHouse;
	private Card card;
	private boolean mortage;
	private int owner= -1;
/*	
	/**
	 * Constructor for creating money property
	 * @param value money
	 */
	/*protected Property(int value) {
		// TODO Auto-generated constructor stub
		this.type = MONEY;
		this.moneyValue = value;
	}*/
	
	public int getType() {
		return type;
	}
	protected void addHouses(){
		numberOfHouse++;
	}
	protected void removeHouses(int i){
		if(i>numberOfHouse){
			i = numberOfHouse;
		}
		numberOfHouse-=i;
	}
	
	protected Property(Realty realty){
		type = REALTY;
		this.realtyCard = realty;
		numberOfHouse = 0;
	}
	
	protected Property(Card card){
		this.card = card;
		type= CARD;
	}
	protected void setOwner(int owner) {
		this.owner = owner;
	}
	public int getOwner() {
		return owner;
	}
	
	public int getMoney() {
		return moneyValue;
	}
	
	public Realty getRealty() {
		return realtyCard;
	}
	public int getNumberOfHouse() {
		return numberOfHouse;
	}
	protected Card getCard() {
		return card;
	}
	public String getCardText(){
		if(card!=null){
			return card.getText();
		}
		return null;
	}

	public boolean isMortage() {
		return mortage;
	}
	protected void setMortage(boolean mortage) {
		this.mortage = mortage;
	}
	/*protected int addMoney(int money, Property from){
		if(type== MONEY){
			return 0;
		}
		if(money<0){
			return 0;
		}
		int myMoney = from.payMoney(money);
		return myMoney;
	}
	protected int payMoney(int money){
		if(type!= MONEY){
			return 0;
		}
		int m = moneyValue-money;
		if(m<0){
			int a = money;
			money = 0;
			return a;
		}
		moneyValue-=money;
		return money;
	}*/
	protected void setNumberOfHouse(int numberOfHouse) {
		this.numberOfHouse = numberOfHouse;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(type==CARD){
			return getCardText();
		}
		return getRealty().getName();
	}
	
}
