package cnpm.gui.component;

import java.awt.Color;
import java.awt.Point;
import java.util.Vector;

import javax.swing.JPanel;

public class PanelHoldCards extends JPanel {

	private int type;
	public Vector<Card> cards;
	public Point coordinate;
	
	public PanelHoldCards(int x, int y, int type) {
		super();
		
		this.setLayout(null);
		this.setOpaque(false);
		
		this.coordinate = new Point(x, y);
		this.type = type;
		
		if(type == 0) {
			this.setBounds(coordinate.x, coordinate.y, 320, 110);
		}
		else {
			this.setBounds(coordinate.x, coordinate.y, 80, 280);
		}
		
		this.cards = new Vector<Card>();
	}

	public synchronized void addCard(final Card card) {
		cards.add(card);
		add(card);
		repaint();	
	}
	
}
