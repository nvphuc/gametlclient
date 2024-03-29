package tienlen.processor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;

import tienlen.gui.Gui;
import tienlen.gui.GuiEdit;
import tienlen.gui.GuiWaitingRoom;
import tienlen.gui.component.MyDialog;
import tienlen.gui.component.MyInputDialog;

public class ProcessorGuiEdit extends Processor {

	private String strFilePath;

	public ProcessorGuiEdit(Gui gui) {
		super(gui);
		strFilePath = null;
	}

	private String showDialog() {
		FileDialog fd = new FileDialog(new Frame(), "Select File...",
				FileDialog.LOAD);
		fd.setFile("*.png");
		fd.setVisible(true);
		if (fd.getFile() != null) {
			return fd.getDirectory() + fd.getFile();
		}
		return null;
	}

	public void getFileImage() {
		GuiEdit gui = (GuiEdit) this.gui;
		strFilePath = showDialog();
		if (strFilePath != null) {
			gui.image = new ImageIcon(strFilePath);
			gui.repaintAvatar();
		}
	}

	public void sendUpdateAvatar() {
		if (strFilePath != null) {
			getConnector().sendMessage("UpdateAvatar");
			getConnector().sendImage(strFilePath);
			String result = getConnector().receiveMessage();
			if (result.equals("OK")) {
				GuiEdit gui = (GuiEdit) this.gui;
				String[] bt = { "X�?C NHẬN" };
				new MyDialog()
						.showMessage(gui, "", "�?ỔI AVATAR THÀNH CÔNG", bt);
				getGame().getPlayer().setAvatar(gui.image);
			} else {
				String[] bt = { "X�?C NHẬN" };
				new MyDialog().showMessage(gui, "", "�?ỔI AVATAR THẤT BẠI", bt);
			}
			strFilePath = null;
		}
	}

	public void sendBack() {
		getConnector().sendMessage("BackWaitingRoom");
		String result = getConnector().receiveMessage();
		if (result.equals("RSBack")) {
			new GuiWaitingRoom(getGame(), getGuiLocation());
			gui.dispose();
		} else {
			String[] bt = { "X�?C NHẬN" };
			new MyDialog().showMessage(gui, "", "CÓ LỖI", bt);
		}
	}

	public void sendUpdateUserName(String newUserName) {
		if (checkString(newUserName)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "UserName không được chứa ký tự đặc biệt", args);
			return;
		}
		if (!newUserName.equals(getPlayer().getUsername())) {
			MyInputDialog inputDialog = new MyInputDialog();
			String pass2 = inputDialog.showMessage(gui, 1, "Inform",
					"Nhập mật khẩu bảo mật (Pass 2): ");
			
			if (pass2 != null) {
				getConnector().sendMessage(
						"UpdateUserName@" + newUserName + ":" + pass2);
				String result = getConnector().receiveMessage();
				if (result.equals("OK")) {
					getPlayer().setUsername(newUserName);
					String[] bt = { "X�?C NHẬN" };
					new MyDialog().showMessage(gui, "",
							"�?ỔI USERNAME THÀNH CÔNG", bt);

				} else {
					String[] bt = { "X�?C NHẬN" };
					new MyDialog().showMessage(gui, "",
							"�?ỔI USERNAME THẤT BẠI", bt);
				}
			}
		}
	}

	public void sendUpdatePassword(String newPass) {
		if (checkString(newPass)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Pass không được chứa ký tự đặc biệt", args);
			return;
		}
		MyInputDialog inputDialog = new MyInputDialog();
		String pass2 = inputDialog.showMessage(gui, 1, "Inform",
				"Nhập mật khẩu bảo mật (Pass 2): ");
		if (pass2 != null) {
			getConnector().sendMessage("UpdatePass@" + newPass + ":" + pass2);
			String result = getConnector().receiveMessage();
			if (result.equals("OK")) {
				String[] bt = { "X�?C NHẬN" };
				new MyDialog().showMessage(gui, "", "�?ỔI PASSWORD THÀNH CÔNG",
						bt);

			} else {
				String[] bt = { "X�?C NHẬN" };
				new MyDialog()
						.showMessage(gui, "", "�?ỔI PASSWORD THẤT BẠI", bt);
			}
		}
	}
	private boolean checkString(String str) {
		Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return (m.find());
	}
}
