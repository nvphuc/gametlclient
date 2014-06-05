package cnpm.processor;

import javax.swing.ImageIcon;

import cnpm.gui.Gui;
import cnpm.gui.GuiPlay;
import cnpm.gui.GuiWaitingRoom;
import cnpm.gui.component.Avatar;
import cnpm.gui.component.Card;
import cnpm.gui.component.MyDialog;
import cnpm.gui.component.PanelHoldCards;

public class ProcessorGuiPlay extends Processor implements Runnable {

	private boolean isRunning;
	private int orderNumber;

	private boolean isPlaying;

	public ProcessorGuiPlay(Gui gui, int orderNumber) {
		super(gui);
		isRunning = true;
		isPlaying = false;
		this.orderNumber = orderNumber;
	}

	@Override
	public void run() {

		while (isRunning) {
			String message = getConnector().receiveMessage();
			System.out.println("PlayNhan: " + message);
			String[] args = message.split("@");
			String[] data;
			int location;
			GuiPlay gui = (GuiPlay) this.gui;
			
			switch (args[0]) {

			case "InforTable":
				displayPlayers(Integer.parseInt(args[1]));
				break;
				
			case "LeaveTable":
				location = (gui.tableSize + Integer.parseInt(args[1]) - orderNumber) % gui.tableSize;
				gui.playerDesk[location].avatarPlayer.reset();
				break;

			case "Ready":
				displayReady(Integer.parseInt(args[1]));
				break;

			case "RSHitCards":
				if (!args[1].equals("OK")) {
					String[] bts = { "XÁC NHẬN" };
					System.out.println("sound 1");
					gui.getSoundManager().playSound("hitcards");
					new MyDialog().showMessage(gui, "", "LỖI ĐÁNH BÀI", bts);
				}
				else {
					PanelHoldCards pnCards = gui.playerDesk[0].pnHandCards;
					for(int i = 0; i < pnCards.getCardsNumber(); i++) {
						Card card = pnCards.cards.get(i);
						if(card.isClicked) {
							pnCards.cards.remove(card);					
						}
					}
					pnCards.repaint();
					gui.btHitCards.setEnabled(false);
					gui.btSkipTurn.setEnabled(false);
				}
				break;
				
			case "Turn":
				showTurn(Integer.parseInt(args[1]));
				break;
				
			case "NewTurn":
				for(int i = 0; i < gui.tableSize; i++) {
					gui.pnTableCards[i].clearPanel();
				}
				showTurn(Integer.parseInt(args[1]));
				gui.btSkipTurn.setEnabled(false);
				break;
				
			case "HitCards":
				showHitCards(args[1]);
				break;

			case "Chat":
				// de de phong truong hop nhap co chu @
				for (int i = 1; i < args.length; i++) {
					gui.txtContent.append(args[i]);
				}
				gui.txtContent.append("\n");
				break;

			case "StartGame":
				isPlaying = true;
				hideReady();
				gui.showStartGame();
				initGame();
				break;
				
			case "GameResult":
				isPlaying = false;
				for(int i = 0; i < gui.tableSize; i++) {
					gui.playerDesk[i].pnHandCards.clearPanel();
					gui.pnTableCards[i].clearPanel();
				}
				gui.btReady.setEnabled(true);
				gui.btHitCards.setEnabled(false);
				gui.btSkipTurn.setEnabled(false);
				break;
				
			case "Report":
				data = args[1].split(":");
				for(int i = 0; i < data.length; i++) {
					if(data[i].equals(orderNumber +"")) {
						gui.showWinner(i);
						break;
					}
				}
				break;
				
			case "Finish":
				isPlaying = false;
				for(int i = 0; i < gui.tableSize; i++) {
					gui.playerDesk[i].pnHandCards.clearPanel();
					gui.pnTableCards[i].clearPanel();
				}
				gui.btReady.setEnabled(true);
				gui.btHitCards.setEnabled(false);
				gui.btSkipTurn.setEnabled(false);
				data = args[1].split(":");
				for(int i = 0; i < data.length; i++) {
					if(data[i].equals(orderNumber +"")) {
						gui.showWinner(i+1);
						break;
					}
				}
				break;
			/*case "HideReady":
				hideReady();
				break;*/

			case "DealCards":
				showDealCards(args[1]);
				break;

			default:
				isRunning = false;
				break;
			}
		}
	}

	private void showTurn(int index) {
		GuiPlay gui = (GuiPlay) this.gui;
		if(index == orderNumber) {
			gui.getSoundManager().playSound("hitcards");
			gui.btHitCards.setEnabled(true);
			gui.btSkipTurn.setEnabled(true);
		}		
	}

	private void showDealCards(String msg) {
		GuiPlay gui = (GuiPlay) this.gui;
		PanelHoldCards pnCards = gui.playerDesk[0].pnHandCards;

		Card[] cards = parseCards(msg);
		sortCards(cards);

		for (int i = 0; i < 13; i++) {
			Card card = pnCards.cards.get(i);
			card.setImage(cards[i].value);
		}
	}

	public void sortCards(Card[] cards) {
		Card temp;
		for (int i = 0; i < cards.length; i++) {
			for (int j = 0; j <= i; j++) {
				if (cards[j].value < cards[i].value) {
					temp = cards[i];
					cards[i] = cards[j];
					cards[j] = temp;
				}
			}
		}
	}

	private void hideReady() {
		GuiPlay gui = (GuiPlay) this.gui;
		for (int i = 0; i < gui.tableSize; i++) {
			Avatar avatar = gui.playerDesk[i].avatarPlayer;
			avatar.setReady(false);
		}
	}

	private void initGame() {
		isPlaying = true;
		GuiPlay gui = (GuiPlay) this.gui;
		for (int i = 0; i < gui.tableSize; i++) {
			gui.playerDesk[i].initCards();
		}
	}

	private void displayReady(int index) {
		GuiPlay gui = (GuiPlay) this.gui;
		if (index == orderNumber) {
			gui.btReady.setEnabled(false);
		}
		int location = (gui.tableSize + index - orderNumber) % gui.tableSize;
		Avatar avatar = gui.playerDesk[location].avatarPlayer;
		avatar.setReady(true);
	}

	private void displayPlayers(int index) {
		GuiPlay gui = (GuiPlay) this.gui;
		int location = (gui.tableSize + index - orderNumber) % gui.tableSize;
		String userName = getConnector().receiveMessage();
		gui.playerDesk[location].avatarPlayer.setUserName(userName);
		int credits = Integer.parseInt(getConnector().receiveMessage());
		gui.playerDesk[location].avatarPlayer.setCredit(credits + "");
		ImageIcon img = getConnector().receiveImage();
		gui.playerDesk[location].avatarPlayer.setImgAvatar(img);
		String ready = getConnector().receiveMessage();
		if (ready.equals(true + "")) {
			gui.playerDesk[location].avatarPlayer.setReady(true);
		} else {
			gui.playerDesk[location].avatarPlayer.setReady(false);
		}
	}

	private void showHitCards(String msg) {
		GuiPlay gui = (GuiPlay) this.gui;
		// Tach chuoi
		String[] data = msg.split(":");
		// Tim vi tri cua nguoi danh
		int location = (gui.tableSize + Integer.parseInt(data[0]) - orderNumber) % gui.tableSize;
		// Lay danh sach cac la bai
		Card[] cards = parseCards(data[1]);
		System.out.println("Test showHitCards 3: " +location );
		// Clear table
		clearTableCards();
		// Lay panelHoldCards
		PanelHoldCards pnCards = gui.playerDesk[location].pnHandCards;

		// Hien thi bai ra ban va xoa cac la tren tay nguoi choi
		if (location == 0) {
			pnCards.removeCards(cards);
			showTableCards(0, cards);
			for(int i = 0; i < pnCards.getCardsNumber(); i++) {
				Card card = pnCards.cards.get(i);
				if(card.isClicked) {
					pnCards.cards.remove(card);					
				}
			}
			pnCards.repaint();
		} else {
			gui.playerDesk[location].pnHandCards.removeCards(cards.length);
			showTableCards(location, cards);
		}
	}
	
	private void showTableCards(int location, Card[] cards) {
		GuiPlay gui = (GuiPlay) this.gui;
		gui.pnTableCards[location].addCards(cards);
	}

	public Card[] parseCards(String cards) {
		cards = cards.trim();
		String[] tmp = cards.split("_");
		Card[] arrCards = new Card[tmp.length];
		for (int i = 0; i < arrCards.length; i++) {
			arrCards[i] = new Card(tmp[i]);
		}
		return arrCards;
	}

	private void clearTableCards() {
		GuiPlay gui = (GuiPlay) this.gui;
		for (int i = 0; i < gui.tableSize; i++) {
			gui.pnTableCards[i].clearPanel();
		}
	}

	public void hitCards() {
		GuiPlay gui = (GuiPlay) this.gui;
		PanelHoldCards pnCards = gui.playerDesk[0].pnHandCards;
		String strCards = "";
		for (int i = 0; i < pnCards.getCardsNumber(); i++) {
			Card card = pnCards.cards.get(i);
			if (card.isClicked == true) {
				if (!strCards.equals("")) {
					strCards += "_";
				}
				strCards += card.value;
			}
		}
		if (strCards.equals("")) {
			System.out.println("sound 1");
			gui.getSoundManager().playSound("hitcards");
			return;
		}
		System.out.println("hitCards: " + strCards);
		getConnector().sendMessage("HitCards@" + strCards);
	}

	public void sendChat(String content) {
		String msg = "Chat@" + content;
		getConnector().sendMessage(msg);
	}

	public void ready() {
		getConnector().sendMessage("Ready");
	}

	public void skipTurn() {
		getConnector().sendMessage("SkipTurn");
		GuiPlay gui = (GuiPlay) this.gui;
		gui.btHitCards.setEnabled(false);
		gui.btSkipTurn.setEnabled(false);
	}

	public void deselectAll() {
		GuiPlay gui = (GuiPlay) this.gui;
		gui.playerDesk[0].pnHandCards.downAllCards();
	}

	public void leaveTable() {
		if(isPlaying) {
			String[] bts = {"XÁC NHẬN"};
			new MyDialog().showMessage(gui, "", "BẠN KHÔNG THỂ RỜI PHÒNG LÚC NÀY", bts);
		}
		else {
			getConnector().sendMessage("LeaveTable");
			isRunning = false;
			new GuiWaitingRoom(getGame(), getGuiLocation());
			gui.dispose();
		}
	}
}
