package tienlen.gui.component;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelEffect extends JPanel {

	private int valueReturn;
	private Container container;
	
	public PanelEffect(Container container) {
		this.container = container;
		setLayout(null);
		setOpaque(false);
		setBounds(0, 0, container.getWidth(), container.getHeight());
		this.container.add(this);
		valueReturn = -1;
	}
	
	public void setValueReturn(int value) {
		valueReturn = value;
	}
	
	public int getValueReturn() {
		return valueReturn;
	}
	
	public void runEffect() {	
		this.repaint();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for(Component component : container.getComponents()){
					if (component instanceof JButton) {
						component.setEnabled(false);
					}
				}
				while(valueReturn == -1) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				for(Component component : container.getComponents()){
					if (component instanceof JButton) {
						component.setEnabled(true);
					}
				}	
			}
		}).start();
	}
}
