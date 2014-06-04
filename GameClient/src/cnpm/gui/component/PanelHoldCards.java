package cnpm.gui.component;

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

	public synchronized void addCards(Card[] arrCards) {
		for(Card card : arrCards) {
			this.cards.add(card);
		}
		showCards();
	}

	public synchronized void addCards(Vector<Card> arrCards) {
		for(Card card : arrCards) {
			this.cards.add(card);
		}
		showCards();
	}
	
	public synchronized void removeCards(Card[] arrCards) {
		for(Card card : arrCards) {
			this.cards.remove(card);
		}
		showCards();
	}
	
	public synchronized void removeCards(int[] arrCards) {
		for(int card : arrCards) {
			this.cards.remove(card);
		}
		showCards();
	}
	
	public synchronized void showCards() {
		// Cac bien luu toa do, vi tri cua tung card trong panel
		int x = 0, y = 0, deltaX = 0, deltaY = 0;

		// Tuy panel thuoc loai nao(0, 1, 2, 3) de sap vi tri cho cac card
		switch (type) {
		case 0:
			x = 12 * 20;
			y = 10;
			deltaX = 20;
			deltaY = 0;
			break;

		case 2:
			x = 0;
			y = 10;
			deltaX = -20;
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
		for (Card card : cards) {
			card.setLocation(x, y);
			add(card);
			x -= deltaX;
			y -= deltaY;
		}
		repaint();
	}
}
