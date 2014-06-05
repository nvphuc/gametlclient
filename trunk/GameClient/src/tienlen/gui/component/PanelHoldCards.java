package tienlen.gui.component;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JPanel;

public class PanelHoldCards extends JPanel {

	// Co hai kieu la ngang va dung
	private int type;
	// Danh sach cac la bai
	public Vector<Card> cards;
	// Toa do cua panel (co ve khong can thiet)
	public Point coordinate;

	public PanelHoldCards(int x, int y, int type) {
		super();

		this.setLayout(null);
		this.setOpaque(false);

		this.coordinate = new Point(x, y);
		this.type = type;

		if (type % 2 == 0) {
			this.setBounds(coordinate.x, coordinate.y, 320, 110);
		} else {
			this.setBounds(coordinate.x, coordinate.y, 80, 280);
		}

		this.cards = new Vector<Card>();
	}

	public int getCardsNumber() {
		return cards.size();
	}

	public synchronized void addCards(int[] arrCards) {
		for (int value : arrCards) {
			Card card = new Card(value);
			cards.add(card);
		}
		showCards();
	}

	public synchronized void addCards(Card[] arrCards) {
		for (Card card : arrCards) {
			this.cards.add(card);
		}
		showCards();
	}

	public synchronized void addCards(Vector<Card> arrCards) {
		for (Card card : arrCards) {
			cards.add(card);
		}
		showCards();
	}

	public synchronized void removeCards(Card[] arrCards) {
		for (Card card : arrCards) {
			this.cards.remove(card);
		}
		showCards();
	}

	public synchronized void removeCards(int cardsNumber) {
		for (int i = 0; i < cardsNumber; i++) {
			this.cards.remove(0);
		}
		showCards();
	}

	public synchronized void clearPanel() {
		this.cards.clear();
		this.removeAll();
		this.repaint();
	}

	public synchronized void showCards() {
		// Clear het cac card truoc do
		this.removeAll();

		// Cac bien luu toa do, vi tri cua tung card trong panel
		int x = 0, y = 0, deltaX = 0, deltaY = 0;

		// Tuy panel thuoc loai nao(0, 1, 2, 3) de sap vi tri cho cac card
		int numberCards = this.cards.size();
		
		switch (type) {
		case 0:
			x = numberCards * 20;
			y = 10;
			deltaX = 20;
			deltaY = 0;
			break;

		case 2:
			x = 12*20;
			y = 10;
			deltaX = 20;
			deltaY = 0;
			break;

		case 1:
			x = 0;
			y = 0;
			deltaX = 0;
			deltaY = -15;
			break;

		case 3:
			x = 0;
			y = 180;
			deltaX = 0;
			deltaY = 15;
			break;
		}
		// Add cac la bai bai panel
		for (Card card : this.cards) {
			card.setLocation(x, y);
			this.add(card);
			x -= deltaX;
			y -= deltaY;
		}
		repaint();
	}
	
	public void downAllCards() {
		for(Card card : cards) {
			card.deselect();
		}
	}
}
