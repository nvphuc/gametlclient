package tienlen.gui;

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

import tienlen.game.Game;
import tienlen.gui.component.Effect;
import tienlen.gui.component.MyPanel;
import tienlen.gui.component.PanelHoldCards;
import tienlen.gui.component.PlayerDesk;
import tienlen.processor.ProcessorGuiPlay;

public class GuiPlay extends Gui {

	public int tableSize;

	// hien thi vi tri ghe cua nguoi choi
	public PlayerDesk[] playerDesk;

	// vung danh cac la bai xuong ban
	public PanelHoldCards[] pnTableCards;

	// hien thi mat ban
	public JLabel lbTable;

	// Nut danh bai, bo luot, san sang
	public JButton btHitCards, btSkipTurn, btDeselectAll, btReady, btInvite,
			btLeaveTable;

	// Khung chat
	public JPanel pnChat;
	public JScrollPane scrollPaneChat;
	public JTextArea txtContent;
	public JTextField txtChat;
	public JButton btSendChat;

	public GuiPlay(Game game, Point location, int tableSize, int orderNumber) {
		super(game, location, "BackGround0");
		setTitle("Play Game");
		this.tableSize = tableSize;

		processor = new ProcessorGuiPlay(this, orderNumber);
		setGui();
		Thread thread = new Thread((ProcessorGuiPlay) processor);
		thread.start();
	}

	@Override
	public void setGui() {

		// Tao tabledesk
		playerDesk = new PlayerDesk[tableSize];

		for (int i = 0; i < tableSize; i++) {
			playerDesk[i] = new PlayerDesk(i * (4 / tableSize));
			pnMain.add(playerDesk[i]);
		}

		// tablecard
		pnTableCards = new PanelHoldCards[tableSize];
		for (int i = 0; i < tableSize; i++) {

			switch (i * (4 / tableSize)) {
			case 0:
				pnTableCards[i] = new PanelHoldCards(370, 300, 0);
				pnMain.add(pnTableCards[i]);
				break;

			case 2:
				pnTableCards[i] = new PanelHoldCards(320, 180, 0);
				pnMain.add(pnTableCards[i]);
				break;

			case 1:
				pnTableCards[i] = new PanelHoldCards(390, 300, 2);
				pnMain.add(pnTableCards[i]);
				break;

			case 3:
				pnTableCards[i] = new PanelHoldCards(290, 240, 2);
				pnMain.add(pnTableCards[i]);
				break;
			}
		}

		// nut san sang
		btReady = new JButton("SẴN SÀNG");
		btReady.addActionListener(this);
		btReady.setBounds(310, 580, 100, 25);
		pnMain.add(btReady);

		// nut san sang
		btDeselectAll = new JButton("BỎ CHỌN");
		btDeselectAll.addActionListener(this);
		btDeselectAll.setBounds(440, 580, 100, 25);
		pnMain.add(btDeselectAll);

		// nut danh bai
		btHitCards = new JButton("�?�?NH BÀI");
		btHitCards.addActionListener(this);
		btHitCards.setBounds(570, 580, 100, 25);
		pnMain.add(btHitCards);

		// nut bo luot
		btSkipTurn = new JButton("BỎ LƯỢT");
		btSkipTurn.addActionListener(this);
		btSkipTurn.setBounds(700, 580, 90, 25);
		pnMain.add(btSkipTurn);
		
		// nut moi nguoi choi
		btInvite = new JButton("MỜI");
		btInvite.addActionListener(this);
		btInvite.setBounds(900, 490, 90, 50);
		//pnMain.add(btInvite);

		// roi ban
		btLeaveTable = new JButton("RỜI BÀN");
		btLeaveTable.addActionListener(this);
		btLeaveTable.setBounds(900, 560, 90, 50);
		pnMain.add(btLeaveTable);

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

		// Tao mat ban
		lbTable = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				ImageIcon image = new ImageIcon("images/TablePlay.png");
				g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(),
						null);
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
			processor.hitCards();
		}
		if (e.getSource() == btSkipTurn) {
			processor.skipTurn();
		}
		if (e.getSource() == btReady) {
			processor.ready();
		}
		if (e.getSource() == btDeselectAll) {
			processor.deselectAll();
		}
		if (e.getSource() == btLeaveTable) {
			processor.leaveTable();
		}
		if (e.getSource() == txtChat || e.getSource() == btSendChat) {
			processor.sendChat(txtChat.getText());
			txtChat.setText("");
		}
	}

	public void showStartGame() {
		getSoundManager().playSound("startgame");
		btHitCards.setEnabled(false);
		btSkipTurn.setEnabled(false);
		String notice = "BAÉT ÑAÀU GAME";
		Effect effect = new Effect(notice);
		effect.setBounds((pnEffect.getWidth() - notice.length() * 60) / 2,
				(pnEffect.getHeight() - 130) / 2, notice.length() * 60, 130);
		pnEffect.add(effect);
		pnEffect.repaint();
	}
	
	public void showWinner(int result) {
		String notice = "VEÀ THÖÙ " + result;
		Effect effect = new Effect(notice);
		effect.setBounds((pnEffect.getWidth() - notice.length() * 60) / 2,
				(pnEffect.getHeight() - 130) / 2, notice.length() * 60, 130);
		pnEffect.add(effect);
		pnEffect.repaint();
	}
}
