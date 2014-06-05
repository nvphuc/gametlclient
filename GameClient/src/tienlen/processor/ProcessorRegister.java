package tienlen.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import tienlen.gui.Gui;
import tienlen.gui.GuiLogin;
import tienlen.gui.GuiRegister;
import tienlen.gui.component.MyDialog;

public class ProcessorRegister extends Processor {

	public ProcessorRegister(Gui gui) {
		super(gui);
	}

	public void cancel() {
		new GuiLogin(((GuiRegister) gui).getGame(), getGuiLocation());
		((GuiRegister) gui).dispose();

	}

	public void register(String userName, String pass1, String cfPass1,
			String pass2) {
		GuiRegister gui = (GuiRegister) this.gui;
		if (userName.equals("") || pass1.equals("") || cfPass1.equals("")
				|| pass2.equals("")) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Phải nhập đầy đủ các trư�?ng", args);
			return;
		}
		if (checkString(userName)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "UserName không được chứa ký tự đặc biệt", args);
			return;
		}
		if (checkString(pass1)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Pass 1 không được chứa ký tự đặc biệt", args);
			return;
		}
		if (!pass1.equals(cfPass1)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Sai Comfirm Pass 1", args);
			return;
		}
		if (checkString(pass2)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Pass 2 không được chứa ký tự đặc biệt", args);
			return;
		}
		if (getConnector().connect()) {
			String message = "Register@" + userName + ":" + pass1 + ":" + pass2;
			getConnector().sendMessage(message);
			message = getConnector().receiveMessage();
			if (message.equals("OK")) {
				String[] args = {"OK"};
				new MyDialog().showMessage(gui, "", "Tạo tài khoản thành công", args);
				getConnector().disconnect();
				new GuiLogin(gui.getGame(), getGuiLocation());
				gui.dispose();
			} else {
				String[] args = {"OK"};
				new MyDialog().showMessage(gui, "", "Tạo tài khoản thất bại", args);
			}
		}
		else {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Không kết nối được với Server", args);
		}
	}

	private boolean checkString(String str) {
		Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return (m.find());
	}
}