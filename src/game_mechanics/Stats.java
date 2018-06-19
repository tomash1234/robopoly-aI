package game_mechanics;

import java.util.Arrays;

public class Stats {
	
	private int[][] platVisited;//
	private int[] moneyPaidToBank;//
	private int[] moneyGotFromBank;//
	private int[] moneyPaidToPlayer;//rents
	private int[] moneyGotFromPlayer;//rents
	private int[] doubles;//
	private int[] prison;//
	private int[] rounds;//
	private int[] totalMoney;
	private int[] moneyHouses;
	private int[] moneySellHouses;
	private int[] dealsOffer;
	
	private int[] playerEnded;
	private int[] moneyErned;
	private int[] wins;
	private int[] score;
	private int[] gamesPlayed;
	private int[] eliminations;
	private int[][] reasonQuit = new int[3][]; //0 - banka, 1- hráč, 2- jiný 
	
	
	public Stats(int players){
		platVisited = new int[players][];
		for(int i = 0;i<platVisited.length; i++){
			platVisited[i]= new int[40];
		}
		moneyPaidToBank = new int[players];
		moneyGotFromBank = new int[players];
		moneyPaidToPlayer = new int[players];
		moneyGotFromPlayer = new int[players];
		doubles = new int[players];
		prison = new int[players];
		rounds = new int[players];
		totalMoney = new int[players];
		playerEnded= new int[players];
		wins= new int[players];
		score= new int[players];
		gamesPlayed= new int[players];
		eliminations= new int[players];
		moneyHouses= new int[players];
		moneySellHouses= new int[players];
		dealsOffer= new int[players];
		moneyErned = new int[40];
		for(int i = 0;i<reasonQuit.length; i++){
			reasonQuit[i]= new int[players];
		}
		
	}
	protected void setElemination(int player){
		eliminations[player]++;
	}
	public int getEliminations(int player) {
		return eliminations[player];
	}
	
	protected void playerWon(int player) {
		wins[player]++;
	}
	public int getDoubles(int player) {
		return doubles[player];
	}
	public int getMoneyHouses(int player) {
		return moneyHouses[player];
	}
	public int getMoneySellHouses(int player) {
		return moneySellHouses[player];
	}
	public int getPlatVisited(int plat){
		int sum = 0;
		for(int i = 0; i<platVisited.length; i++){
			sum+=platVisited[i][plat];
		}
		return sum;
	}
	public int getMoneyGotFromBank(int player) {
		return moneyGotFromBank[player];
	}
	public int getMoneyGotFromPlayer(int player) {
		return moneyGotFromPlayer[player];
	}
	public int getMoneyPaidToBank(int player) {
		return moneyPaidToBank[player];
	}
	public int getMoneyPaidToPlayer(int player) {
		return moneyPaidToPlayer[player];
	}
	public int getRounds(int player) {
		return rounds[player];
	}
	public int getPlatVisitedTotal(){
		int sum = 0;
		for(int i = 0; i<platVisited.length; i++){
			for(int j = 0; j<platVisited[i].length; j++){
				sum+=platVisited[i][j];
			}
		}
		return sum;
	}
	public int getPlatVisited(int plat, int player){
		return platVisited[player][plat];
	}
	
	
	
	
	protected void stepOnPlat(int player, int plat){
		platVisited[player][plat]++;
	}
	protected void moneyPaidToBank(int player, int monay){
		moneyPaidToBank[player]+=monay;
	}
	protected void moneyGotFromBank(int player, int monay){
		moneyGotFromBank[player]+=monay;
	}
	protected void moneyPaidToPlayer(int player, int monay){
		moneyPaidToPlayer[player]+=monay;
	}
	protected void moneyGotFromPlayer(int player, int monay){
		moneyGotFromPlayer[player]+=monay;
	}
	protected void buyHousesValue(int player, int house){
		moneyHouses[player] +=house;
	}
	protected void sellHousesValue(int player, int house){
		moneySellHouses[player] +=house;
	}
	
	protected void doubles(int player){
		doubles[player]++;
	}
	protected void prison(int player){
		prison[player]++;
	}
	protected void makeDeal(int player){
		dealsOffer[player]++;
	}
	
	protected void rounds(int player){
		rounds[player]++;
	}
	protected void setTotalMoney(int player, int money){
		totalMoney[player]= money;
	}
	public int getTotalMoney(int player) {
		return totalMoney[player];
	}
	protected void playerQuit(int player, int reason){
		reasonQuit[reason][player]++;
	}
	protected void endGame(int player, int place){
		
		score[player]+=score.length-place;
		gamesPlayed[player]++;
	}
	public int getReasonQuit(int player, int quitCode) {
		return reasonQuit[quitCode][player];
	}
	public int getGamesPlayed(int player) {
		return gamesPlayed[player];
	}
	public int getScore(int player) {
		return score[player];
	}
	
	public int getDealsOffer(int player) {
		return dealsOffer[player];
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "";
		s+="Penize placeny hračům " + Arrays.toString(moneyPaidToPlayer);
		
		return s;
	}

	protected void playerEnd(int player, int round) {
		// TODO Auto-generated method stub
		playerEnded[player] = round;
	}
	public int getPlayerEnded(int player) {
		return playerEnded[player];
	}
	
	public int getPlayerWins(int player){
		return wins[player];
	}
	public void merge(Stats stats){	
		int players=  platVisited.length;

		for(int j = 0; j<platVisited[0].length; j++){
			moneyErned[j]+=stats.getMoneyErned(j);
		}
		for(int i = 0; i<players; i++){
			for(int j = 0; j<platVisited[i].length; j++){
				platVisited[i][j] += stats.getPlatVisited(j, i);
			}
			moneyPaidToBank[i] += stats.moneyPaidToBank[i]; 
			moneyGotFromBank[i] += stats.moneyGotFromBank[i]; 
			moneyPaidToPlayer[i] += stats.moneyPaidToPlayer[i]; 
			moneyGotFromPlayer[i] += stats.moneyGotFromPlayer[i]; 
			doubles[i] += stats.doubles[i]; 
			prison[i] += stats.prison[i]; 
			rounds[i] += stats.rounds[i]; 
			totalMoney[i] += stats.totalMoney[i]; 
			playerEnded[i] += stats.playerEnded[i]; 
			wins[i] += stats.wins[i]; 
			gamesPlayed[i] += stats.gamesPlayed[i]; 
			score[i] += stats.getScore(i);
			eliminations[i] += stats.getEliminations(i);
			moneyHouses[i] += stats.getMoneyHouses(i);
			moneySellHouses[i] += stats.getMoneySellHouses(i);
			dealsOffer[i] += stats.getDealsOffer(i);
			for(int j = 0; j<reasonQuit.length; j++){
				reasonQuit[j][i] += stats.reasonQuit[j][i];
			}
			
		}
		
	}
	
	protected void addMoneyErned(int plat, int amount){
		moneyErned[plat] += amount;
	}
	public int getMoneyErned(int plat) {
		return moneyErned[plat];
	}
	
	public int getFinished(int player) {
		// TODO Auto-generated method stub
		return (gamesPlayed[player] - reasonQuit[0][player] - reasonQuit[1][player] - reasonQuit[2][player]);
	}
}
