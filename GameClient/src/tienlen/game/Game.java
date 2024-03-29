package tienlen.game;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import tienlen.connector.Connector;
import tienlen.gui.GuiLogin;
import tienlen.gui.GuiPlay;
import tienlen.music.SoundManager;
import tienlen.player.Player;

public class Game {

	private Connector connector;
	private Player player;
	public SoundManager soundManager;
	
	public Game() {
		connector = new Connector();
		player = new Player();
		soundManager = new SoundManager("musics/background.wav");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point location = new Point((screenSize.width - 1000)/2, (screenSize.height - 650)/2);
		new GuiLogin(this, location);
		//new GuiPlay(this, location, 4);
	}

	public Connector getConnector() {
		return connector;
	}

	public Player getPlayer() {
		return player;
	}
	
	public void reset() {
		connector = new Connector();
		player = new Player();
		soundManager = new SoundManager("musics/background.wav");
	}
	
	public static void main(String[] args) {
		Game game = new Game();
	}
}
