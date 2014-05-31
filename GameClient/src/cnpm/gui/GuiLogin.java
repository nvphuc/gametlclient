package cnpm.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cnpm.game.Game;
import cnpm.gui.component.HintPasswordField;
import cnpm.gui.component.HintTextField;
import cnpm.processor.ProcessorGuiLogin;

public class GuiLogin extends Gui {

	private JTextField tfUsername, tfPassword;	
	private JButton btLogin, btRegister;			
	private JLabel lbLogo;
	private JPanel pnLogin;

	public GuiLogin(Game game, Point location) {
		super(game, location, "BackGround1");
		setTitle("Login");
		setGui();
		processor = new ProcessorGuiLogin(this);
	}

	@Override
	public void setGui() {
		
		// Tao panel chua khung login
		pnLogin = new JPanel();
		pnLogin.setBounds(650, 75, 240, 350);
		pnLogin.setLayout(null);
		pnLogin.setBackground(Color.WHITE);
		
		// Tao textfield username
		tfUsername = new HintTextField("username");
		tfUsername.setBounds(20, 50, 200, 40);
		pnLogin.add(tfUsername);
		
		// Tao texfield password
		tfPassword = new HintPasswordField("password");
		tfPassword.setBounds(20, 150, 200, 40);
		pnLogin.add(tfPassword);

		// Tao button login
		btLogin = new JButton("Login");
		btLogin.setBounds(10, 250, 100, 30);
		btLogin.addActionListener(this);
		pnLogin.add(btLogin);

		// Tao button register
		btRegister = new JButton("Register");
		btRegister.setBounds(130, 250, 100, 30);
		btRegister.addActionListener(this);
		pnLogin.add(btRegister);
						
		// Tao anh logo
		lbLogo = new JLabel(new ImageIcon("images/LogoTienLen.gif"));
		lbLogo.setBounds(150, 100, 325, 113);
		
		// Gan cac thanh phan vao pnMain
		pnMain.add(lbLogo);
		pnMain.add(pnLogin);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		ProcessorGuiLogin processor = (ProcessorGuiLogin) this.processor;
		
		if (e.getSource() == btLogin) {
			processor.login(tfUsername.getText(), tfPassword.getText());
		}
		
		if (e.getSource() == btRegister) {
			processor.register();
		}
		
	}
}