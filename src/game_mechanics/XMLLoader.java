package game_mechanics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import game_mechanics.Card.CardType;
import javafx.scene.paint.Color;

public class XMLLoader {
	
	private Document document;
	private String text;
	private Color color;
	private int value, group, housePrice;
	private String image;
	private int[] rents;
	//private int cardType, val1, val2;
	private String root;
	
	public XMLLoader(){
	load(getClass().getResourceAsStream("/plats.xml"), null);
	
		
	}
	
	public XMLLoader(String file){
			File fi = new File(file);
			try {
				load(new FileInputStream(file), fi.getParentFile().getAbsolutePath());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
		}
	public void load(InputStream file, String dir){
		if(dir!=null){
			root = dir;
		}
		DocumentBuilder builder;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			 document = builder.parse(file);
			 
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception exception){
			
		}
	}
	  
	public void loadPlat(int id){
		text = null;
		value= 0;
		housePrice = 0;
		group = 0;
		image=null;
		color=null;
		rents = new int[6];
		NodeList nodes = document.getElementsByTagName("plat");
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			Element el = (Element) node;
			if(Integer.valueOf(el.getAttribute("id"))==id){
				NodeList nameNL = el.getElementsByTagName("name");
				if(nameNL.getLength()>0){
					text = nameNL.item(0).getTextContent();
					/*if(text.length()>15){ 
						text = text.substring(0, 15) + "\n" + text.substring(15);
					}*/
				}
				NodeList groupG = el.getElementsByTagName("group");
				if(groupG.getLength()>0){
					group= Integer.valueOf(groupG.item(0).getTextContent());
				}
				NodeList colorNL = el.getElementsByTagName("color");
				if(colorNL.getLength()>0){
					color = Color.web(colorNL.item(0).getTextContent());
				}
				NodeList valueNL = el.getElementsByTagName("value");
				if(valueNL.getLength()>0){
					value = Integer.valueOf(valueNL.item(0).getTextContent());
				}
				
				NodeList imageNL = el.getElementsByTagName("img");
				if(imageNL.getLength()>0){
					String urlImg =  imageNL.item(0).getTextContent();
					if(root==null){
						image = /*new Image(ClassLoader.getSystemResourceAsStream(*/urlImg;//));
					}else{

						image =/**/root + File.separator + urlImg;//);
					}
				}
				NodeList housePrices = el.getElementsByTagName("housePrice");
				if(housePrices.getLength()>0){
					housePrice = Integer.valueOf(housePrices.item(0).getTextContent());
				}
				NodeList re = el.getElementsByTagName("rent");
				for(int a = 0; a<rents.length; a++){
					for(int j = 0; j<re.getLength(); j++){
						Element element = (Element) re.item(j);
						String s = element.getAttribute("h");
						try{
							Integer ida = Integer.valueOf(s);
							if(ida==a){
								rents[a] =  Integer.valueOf(element.getTextContent());
							}
						}catch(Exception exception){}
					}
					
				}
				break;  
			} 
		}

	}
	
	public CardDeck loadCardDeck(int type){
		CardDeck cardDeck = new CardDeck(type);

		NodeList nodes = document.getElementsByTagName(type==CardDeck.CHANCE?"chance":"financy");
		for(int i = 0; i< nodes.getLength(); i++){
			Element element = (Element) nodes.item(i);
			String text = element.getTextContent();
			int cardType = Integer.valueOf(element.getAttribute("type"));
			int val1 = 0;
			if(element.hasAttribute("val")){
				val1 = Integer.valueOf(element.getAttribute("val"));
			}
			int val2 = 0;

			if(element.hasAttribute("val2")){
				val2 = Integer.valueOf(element.getAttribute("val2"));
			}
			Card c = new Card(text, CardType.values()[cardType-1], val1, val2);
			c.setChance(type==CardDeck.CHANCE);
			cardDeck.addCard(c);
			
			
		}
		cardDeck.shuffle();
		
		return cardDeck;
		
	}
	
	
	
	public int getHousePrice() {
		return housePrice;
	}
	public int getGroup() {
		return group;
	}
	public String getImage() {
		return image;
	}
	public String getText() {
		return text;
	}
	public int getValue() {
		return value;
	}
	public Color getColor() {
		return color;
	}
	
	public int[] getRents() {
		return rents;
	}
}
