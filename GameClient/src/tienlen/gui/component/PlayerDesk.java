package tienlen.gui.component;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JPanel;

import tienlen.processor.ProcessorGuiPlay;

public class PlayerDesk extends JPanel {

	public int location;
	public Avatar avatarPlayer;
	public PanelHoldCards pnHandCards;

	public PlayerDesk(int location) {
		super();
		this.setLayout(null);
		this.setOpaque(false);

		this.location = location;
		this.avatarPlayer = new Avatar("");

		switch (this.location) {
		case 0:
			this.avatarPlayer.setBounds(0, 0, 100, 130);
			this.pnHandCards = new PanelHoldCards(120, 10, location);
			this.setBounds(301, 420, 440, 130);
			break;

		case 2:
			this.avatarPlayer.setBounds(340, 0, 100, 130);
			this.pnHandCards = new PanelHoldCards(0, 10, location);
			this.setBounds(300, 30, 440, 130);
			break;

		case 1:
			this.avatarPlayer.setBounds(90, 160, 100, 130);
			this.pnHandCards = new PanelHoldCards(0, 0, location);
			this.setBounds(760, 170, 190, 300);
			break;

		case 3:
			this.avatarPlayer.setBounds(0, 0, 100, 130);
			this.pnHandCards = new PanelHoldCards(110, 10, location);
			this.setBounds(45, 120, 190, 300);
			break;
		}
		this.add(avatarPlayer);
		this.add(pnHandCards);
	}

	public int getCardsNumber() {
		return pnHandCards.cards.size();
	}

	public void initCards() {
		Vector<Card> cards = new Vector<Card>();
		for (int i = 0; i < 13; i++) {
			Card card = new Card();
			if (location == 0) {
				final Card temp = card;
				card.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						temp.isClicked = !temp.isClicked;
						if (temp.isClicked == true) {
							temp.select();
						} else {
							temp.deselect();
						}
					}
				});
			}
			cards.add(card);
		}
		pnHandCards.addCards(cards);
	}

	public void removeCard(Card card) {
		pnHandCards.cards.remove(card);
		pnHandCards.remove(card);
		pnHandCards.repaint();
	}

	public void removeCards(Card[] card) {
		pnHandCards.cards.remove(card);
		pnHandCards.removeCards(card);
		pnHandCards.repaint();
	}

	public void refreshCards() {
		int cardsNumber = pnHandCards.cards.size();
		int x = 0, y = 0, deltaX = 0, deltaY = 0;

		switch (this.location) {
		case 0:
			x = 18 * 12;
			y = 10;
			deltaX = 20;
			deltaY = 0;
			break;

		case 2:
			x = 220;
			y = 10;
			deltaX = 20;
			deltaY = 0;
			break;

		case 1:
			x = 0;
			y = 180;
			deltaX = 0;
			deltaY = 20;
			break;

		case 3:
			x = 0;
			y = 20 * 12;
			deltaX = 0;
			deltaY = 20;
			break;
		}

		for (int i = 0; i < cardsNumber; i++) {
			Card card = pnHandCards.cards.get(i);
			card.setLocation(x, y);
			x -= 18;
		}
	}

	public Card getCard(int i) {
		return pnHandCards.cards.get(i);
	}

	public Vector<Card> getAllCards() {
		return pnHandCards.cards;
	}
}
