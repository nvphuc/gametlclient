package tienlen.processor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import tienlen.gui.Gui;
import tienlen.gui.GuiEdit;
import tienlen.gui.GuiLogin;
import tienlen.gui.GuiPlay;
import tienlen.gui.GuiWaitingRoom;
import tienlen.gui.component.MyDialog;
import tienlen.gui.component.MyInputDialog;
import tienlen.gui.component.Table;


public class ProcessorGuiWaitRoom extends Processor implements Runnable {

	private boolean isRunning;
	
	public ProcessorGuiWaitRoom(Gui gui) {
		super(gui);
		isRunning = true;
	}

	@Override
	public void run() {
		while (isRunning) {
			String message = getConnector().receiveMessage();
			System.out.println("WaitNhan: " + message);
			String[] args = message.split("@");
			String[] data;
			
			switch (args[0]) {
			
			case "RSEdit":
				isRunning = false;
				break;
				
			case "RSJoinTable":
				/*
				 * msg nhan: "RSJoinTable@OK:size|ERROR"
				 */
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					int tableSize = Integer.parseInt(data[1]);
					int orderNumber = Integer.parseInt(data[2]);
					new GuiPlay(getGame(), getGuiLocation(), tableSize, orderNumber);
					gui.dispose();
					isRunning = false;
				} else {
					String[] bt = {"XÁC NHẬN"};
					new MyDialog().showMessage(gui, "ERROR", "Không thể vào bàn", bt);
				}
				break;
				
			case "RSUpdateTables":
				/* 
				 * msg nhan: "RSUpdateTables@NONE|name1:bet#name2:bet#..#namen:bet"
				 */
				String[] tables;
				if (args[1].equals("NONE")) {
					tables = new String[0];
				} else {
					tables = args[1].split("#");
				}
				refreshTables(tables);
				break;

			case "RSPlayRight":
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					int tableSize = Integer.parseInt(data[1]);
					int orderNumber = Integer.parseInt(data[2]);
					new GuiPlay(getGame(), getGuiLocation(), tableSize, orderNumber);
					gui.dispose();
					isRunning = false;
				} else {
					String[] bt = {"XÁC NHẬN"};
					new MyDialog().showMessage(gui, "ERROR", "Không thể vào bất kỳ bàn nào hiện tại", bt);
				}
				break;

			case "RSCreateTable":
				/*
				 * msg gui: "CreateTable@TableName:bet:2|4"
				 * msg nhan: "RSCreateTable@OK:size|ERROR"
				 */
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					int tableSize = Integer.parseInt(data[1]);
					int orderNumber = Integer.parseInt(data[2]);
					new GuiPlay(getGame(), getGuiLocation(), tableSize, orderNumber);
					gui.dispose();
					isRunning = false;
				} else {
					String[] bt = {"XÁC NHẬN"};
					new MyDialog().showMessage(gui, "ERROR", "Không thể tạo bàn", bt);
				}
				break;
				
			default:
				isRunning = false;
				//System.out.println("mat ket noi voi server");
				break;
			}
		}
	}
	
	public void createTable() {
		MyInputDialog inputDialog = new MyInputDialog();
		String TableName = inputDialog.showMessage(gui,0 ,"Inform", "Nhập tên bàn:");
		if (checkString(TableName)) {
			String[] args = {"OK"};
			new MyDialog().showMessage(gui, "", "Tên bàn không được chứa ký tự đặc biệt", args);
			return;
		}
		if (TableName != null) {
			while (TableName.equals("") || TableName.length() > 10) {
				String input = inputDialog.showMessage(gui,0 ,"Inform", "Nhập tên bàn (Từ 1 đến 10 ký tự):");
				TableName = input;
				if (TableName == null)
					break;
			}
		}
		if (TableName != null) {
			MyDialog myDialog = new MyDialog();
			String[] buttons = { "ĐẤU 2", "ĐẤU 4", "HỦY" };
			
			int roomSize = myDialog.showMessage(gui, "", "Chọn loại phòng", buttons);
			if (roomSize != -1 && roomSize != 2) {
				/*
				 * Gui message tao phong len server Cau truc message:
				 * "CreateTable@TenPhong:bet:songuoichoi"
				 */
				System.out.println("hghgh");
				getConnector().sendMessage("CreateTable@" + TableName + ":100:" + (roomSize * 2 + 2));
			}
		}
	}

	public void editAccount() {
		getConnector().sendMessage("Edit");
		new GuiEdit(getGame(), getGuiLocation());
		gui.dispose();
		isRunning = false;
	}
	
	public void logout() {
		getConnector().disconnect();
		getGame().reset();
		new GuiLogin(getGame(), getGuiLocation());
		gui.dispose();
		isRunning = false;
	}
	
	public void playRight() {
		getConnector().sendMessage("PlayRight");
	}
	
	public void updateTables() {
		getConnector().sendMessage("UpdataTables");
	}
	
	public void refreshTables(String[] tableNames) {
		// Toa do x, y de ve cac phong len pnTables
		int x = 0;
		int y = 0;

		GuiWaitingRoom gui = (GuiWaitingRoom) this.gui;
		/* Xoa het cac phong da ve len pnTables */
		gui.scrollPaneTable.getContainer().removeAll();
		gui.scrollPaneTable.setSizeContainer(new Dimension(500,
				(tableNames.length / 4) * 100 + 100));
		/* Duyet va ve cac phong luu trong roomnames[] */
		for (int i = 0; i < tableNames.length; i++) {
			String[] data = tableNames[i].split(":");
			/* Khoi tao 1 phong tai toa do x,y voi ten phong la roomnames[i] */
			gui.tables[i] = new Table(data[0], Integer.parseInt(data[1]));

			/* Gan su kien click chuot cho phong vua tao */
			final Table tmp = gui.tables[i];
			tmp.addActionListener(new ActionListener() {			
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getConnector().sendMessage("JoinTable@" + tmp.getName());
				}
			});

			/* Ve phong len pnTables */
			tmp.setBounds(x, y, tmp.getPreferredSize().width,
					tmp.getPreferredSize().height);
			gui.scrollPaneTable.getContainer().add(tmp);

			/* Chinh lai toa do ve */
			if ((i + 1) % 4 != 0) { // Moi dong ve toi da 5 room
				x += tmp.getPreferredSize().width + 5;
			} else {
				x = 0;
				y += tmp.getPreferredSize().height + 5;
			}
		}

		/* Repaint lai pnTables */
		gui.scrollPaneTable.getContainer().repaint();
	}

	public void settingSound() {
		// TODO Auto-generated method stub
		
	}
	private boolean checkString(String str) {
		Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		return (m.find());
	}
}
