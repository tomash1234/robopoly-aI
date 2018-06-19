package game_mechanics;

public class Card {
	
	private String text;
	private int value1;
	private int value2;
	private CardType cardType;
	private boolean chance;
	
	public enum CardType{
		GIFT_FROM_BANK, PAID_TO_BANK, MOVE_TO_FIELD, PAID_FOR_HOUSE_TO_BANK, GO_TO_JAIL, GET_OF_JAIL;
	};
	protected Card(String text, CardType cardType, int val, int val2){
		this.text= text;
		this.value1 = val;
		this.value2 = val2;
		this.cardType= cardType;
	}
	public String getText() { 
		return text;
	}
	public int getValue1() {
		return value1;
	}
	public int getValue2() {
		return value2;
	}
	public CardType getCardType() {
		return cardType;
	}
	public void setChance(boolean chance) {
		this.chance = chance;
	}
	public boolean isChance() {
		return chance;
	}

}
