package cnpm.gui.component;

import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card extends JLabel {
	public String cardnumber;
	public Point coordinate;
	public int width = 80, height = 100;
	public boolean isClicked = false;

	public Card() {
		setImage("100");
	}
	// ve quan bai o vi tri moi
	public void select() {
		this.setBounds(coordinate.x, coordinate.y - 10, width, height);
	}

	// ve lai quan bai tai vi tri cu
	public void deselect() {
		this.setBounds(coordinate.x, coordinate.y, width, height);
	}

	public void setLocation(int x, int y) {
		this.coordinate = new Point(x, y);
		this.setBounds(coordinate.x, coordinate.y, width, height);
	}
	
	// hien thi hinh cua the bai nay
	public void setImage(String cardnumber) {
		this.cardnumber = cardnumber;
		this.setIcon(new ImageIcon("images/cards/" + cardnumber + ".png"));
	}
}
