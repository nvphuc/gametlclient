package cnpm.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cnpm.game.Game;
import cnpm.gui.component.MyPanel;
import cnpm.gui.component.PanelHoldCards;
import cnpm.gui.component.PlayerDesk;
import cnpm.gui.component.TheBai;
import cnpm.processor.ProcessorGuiPlay;

public class GuiPlay extends Gui {

	public int tableSize;
	
	public PlayerDesk[] playerDesk;

	public PanelHoldCards[] pnTableCards;
	
	public JPanel pnTableCard1, pnTableCard3, pnTableCard4, pnTableCard2;

	// Nut danh bai, bo luot, san sang
	public JButton btHitCards, btSkipTurn, btReady;

	

	public JPanel pnChat;
	public JScrollPane scrollPaneChat;
	public JTextArea txtContent;
	public JTextField txtChat;
	public JButton btSendChat;
	public JLabel lbTable;
	
	public GuiPlay(Game game, Point location, int tableSize) {
		super(game, location, "BackGround1");
		setTitle("Play Game");
		this.tableSize = tableSize;
		//processor = new ProcessorGuiPlay(this);
		setGui();
		//Thread thread = new Thread((ProcessorGuiPlay) processor);
		//thread.start();
	}

	@Override
	public void setGui() {
		
		// Tao tabledesk
		playerDesk = new PlayerDesk[tableSize];
		
		for(int i = 0; i < tableSize; i++) {
			playerDesk[i] = new PlayerDesk(i*(4/tableSize));
			pnMain.add(playerDesk[i]);
		}
		
		// tablecard
		pnTableCards = new PanelHoldCards[tableSize];
		for(int i = 0; i < tableSize; i++) {
			
			switch(i*(4/tableSize)) {
			case 0:
				pnTableCards[i] = new PanelHoldCards(411, 300, 0);
				pnMain.add(pnTableCards[i]);
				break;
				
			case 2:
				pnTableCards[i] = new PanelHoldCards(301, 180, 0);
				pnMain.add(pnTableCards[i]);
				break;
				
			case 1:
				pnTableCards[i] = new PanelHoldCards(481, 120, 0);
				pnMain.add(pnTableCards[i]);
				break;
				
			case 3:
				pnTableCards[i] = new PanelHoldCards(231, 120, 0);
				pnMain.add(pnTableCards[i]);
				break;
			}
		}
		
		
		// nut danh bai
		btHitCards = new JButton("Ä�Ã¡nh BÃ i");
		btHitCards.addActionListener(this);
		btHitCards.setEnabled(false);
		btHitCards.setBounds(800, 521, 90, 25);
		pnMain.add(btHitCards);

		// nut bo luot
		btSkipTurn = new JButton("Bá»� LÆ°á»£t");
		btSkipTurn.addActionListener(this);
		btSkipTurn.setEnabled(false);
		btSkipTurn.setBounds(800, 557, 90, 25);
		pnMain.add(btSkipTurn);

		// nut san sang
		btReady = new JButton("Sáºµn SÃ ng");
		btReady.addActionListener(this);
		btReady.setEnabled(true);
		btReady.setBounds(800, 486, 90, 25);
		pnMain.add(btReady);
		

		// Tao khung chat ==================================
		pnChat = new MyPanel("BackGround2");
		pnChat.setLayout(null);
		pnChat.setOpaque(false);
		pnChat.setBounds(2, 440, 280, 185);
		pnMain.add(pnChat);

		scrollPaneChat = new JScrollPane();
		scrollPaneChat.setBounds(6, 12, 268, 136);
		pnChat.add(scrollPaneChat);

		txtContent = new JTextArea();
		txtContent.setEditable(false);
		scrollPaneChat.setViewportView(txtContent);

		txtChat = new JTextField();
		txtChat.addActionListener(this);
		txtChat.setColumns(10);
		txtChat.setBounds(6, 153, 218, 25);
		pnChat.add(txtChat);

		btSendChat = new JButton("Gửi");
		btSendChat.addActionListener(this);
		btSendChat.setMargin(new Insets(0, 0, 0, 0));
		btSendChat.setAlignmentY(0.0f);
		btSendChat.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btSendChat.setBounds(233, 153, 40, 25);
		pnChat.add(btSendChat);
		
		lbTable = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon image = new ImageIcon("images/TablePlay.png");
				g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		lbTable.setBounds(250, 150, 500, 280);
		pnMain.add(lbTable);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ProcessorGuiPlay processor = (ProcessorGuiPlay) this.processor;

		if (e.getSource() == btHitCards) {
			//processor.hitCards();
		}
		if (e.getSource() == btSkipTurn) {
			//processor.skipTurn();
		}
		if (e.getSource() == btReady) {
			for(int i = 0; i < tableSize; i++) {
				playerDesk[i].initCards();
			}
			//processor.ready();
		}
		if (e.getSource() == txtChat) {
			//processor.sendChat();
		}
		if (e.getSource() == btSendChat) {
			//processor.sendChat();
		}
	}
}
