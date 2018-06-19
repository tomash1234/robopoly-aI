package game_mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardDeck {
	private int type;
	
	public static final int CHANCE = 0;
	public static final int FINANCY = 1;


	public CardDeck(int type) {
		this.type = type;
	}
	public int getType() {
		return type;
	}
	private List<Card> cards = new ArrayList<>();
	
	
	protected void addCard(Card card){
		cards.add(card);
	}
	
	protected Card takeCard(){
		return cards.remove(0);
	}
	protected Card showCard(){
		cards.add(cards.remove(0));
		return cards.get(cards.size()-1);
	}
	
	protected void shuffle(){
		Collections.shuffle(cards);
	}
	
}

