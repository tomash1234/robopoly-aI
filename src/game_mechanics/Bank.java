package game_mechanics;

import java.util.ArrayList;
import java.util.List;

public class Bank {
	
	
	private Wallet bankWallet;
	private List<Property> realties = new ArrayList<>();
	private List<Property>  oldRealties;
	//private List<Property> cards= new ArrayList<>();
	
	
	public Bank(int startMoney, List<Property> realties){
		bankWallet = new Wallet(startMoney, null, null);
		oldRealties = new ArrayList<>(realties);
		this.realties.addAll(realties);
		
	}
	
	public boolean isAvailable(int realtyID){
		for(Property property: realties){
			if(realtyID==property.getRealty().getRealtyID()){
				return true;
			}
		}
		return false;
	}
	protected void passStart(Wallet wallet){
		bankWallet.payMoneyTo(wallet, 4000);
	}
	protected Property getProperty(int id){
		for(Property property: realties){
			if(id==property.getRealty().getRealtyID()){
				return property;
			}
		}
		return null;
	}
	protected Wallet getBankWallet() {
		return bankWallet;
	}
	protected Property sellRealty(Realty toBuy, Wallet  wallet){
		if (wallet.payMoneyTo(bankWallet,toBuy.getPrice()) == Wallet.TRANSACTIONS_OK) {
			for (Property property : realties) {
				if (toBuy.getRealtyID() == property.getRealty().getRealtyID()) {
					realties.remove(property);
					return property;
				}
			}
		} else {
			// System.err.println("Nedostatek penez");
		}
		
		return null;
	}
	protected boolean returnProperty(Property property){
		if(property.getRealty()!=null){
			realties.add(property);
			property.setOwner(-1);
			return true;
		}
		return false;
	}
	public List<Property> getAllProperties(){
		return new ArrayList<>(oldRealties);
	}
	

}
