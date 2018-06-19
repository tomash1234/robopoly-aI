package game_mechanics;

import java.util.Arrays;

public class Realty{
	
	
	
	private int[] rent = new int[6];
	
	private int mortgage;
	private int houseCost;
	private int price;
	private int realtyID;
	private int group;
	private String name;
	
	/**
	 * @param name name;
	 * @param realtyID it is same as plat id
	 * @param price printed price
	 * @param rents 6 numbers
	 * @param rentWithHotel
	 * @param mortagePrice
	 * @param houseCost price for 1 house or hotel
	 * @param group group of realties
	 */
	protected Realty(String name, int realtyID, int price, int[] rents, int mortagePrice, int houseCost, int group) {
		//super(realtyID, false);
		this.name = name;
		this.realtyID = realtyID;
		this.price = price;
		this.rent[0] = rents[0];
		this.rent[1] = rents[1];
		this.rent[2] = rents[2];
		this.rent[3] = rents[3];
		this.rent[4] = rents[4];
		this.rent[5] = rents[5];
		this.mortgage = mortagePrice;
		this.houseCost = houseCost;
		this.group = group;
		
	}
	public int getRealtyID() {
		return realtyID;
	}
	public int getPrice() {
		return price;
	}
	public int getRentPrice(int houses) {
		return rent[houses];
	}
	public int getMortgage() {
		return mortgage;
	}
	public int getHouseCost() {
		return houseCost;
	}
	public String getName() {
		return name;
	}
	public int getGroup() {
		return group;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[" + group + "] " + name;
	}
}
