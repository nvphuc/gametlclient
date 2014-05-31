package cnpm.processor;

import java.awt.Point;

import javax.swing.ImageIcon;

import cnpm.connector.Connector;
import cnpm.game.Game;
import cnpm.gui.Gui;
import cnpm.player.Player;

public abstract class Processor {

	protected Gui gui;

	public Processor(Gui gui) {
		this.gui = gui;
	}

	public Gui getGui() {
		return gui;
	}

	public Game getGame() {
		return gui.getGame();
	}

	public Player getPlayer() {
		return gui.getGame().getPlayer();
	}

	public Point getGuiLocation(){
		return gui.getLocationOnScreen();
	}
	
	public Connector getConnector() {
		return gui.getGame().getConnector();
	}
}
