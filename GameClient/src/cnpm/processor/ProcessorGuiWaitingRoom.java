package cnpm.processor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import cnpm.gui.Gui;
import cnpm.gui.GuiEdit;
import cnpm.gui.GuiLogin;
import cnpm.gui.GuiPlay;
import cnpm.gui.GuiWaitingRoom;
import cnpm.gui.component.MyDialog;
import cnpm.gui.component.MyInputDialog;
import cnpm.gui.component.Table;

public class ProcessorGuiWaitingRoom extends Processor implements Runnable {

	private boolean isRunning;
	
	public ProcessorGuiWaitingRoom(Gui gui) {
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
				
			case "RSCreateTable":
				/*
				 * msg gui: "CreateTable@TableName:bet:2|4"
				 * msg nhan: "RSCreateTable@OK:size|ERROR"
				 */
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					new GuiPlay(getGame(), getGuiLocation(), Integer.parseInt(data[1]));
					gui.dispose();
					isRunning = false;
				} else {
					String[] bt = {"XÁC NHẬN"};
					new MyDialog().showMessage(gui, "ERROR", "Không thể tạo bàn", bt);
				}
				break;
				
			case "RSJoinTable":
				/*
				 * msg nhan: "RSJoinTable@OK:size|ERROR"
				 */
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					new GuiPlay(getGame(), getGuiLocation(), Integer.parseInt(data[1]));
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
					tables = args[1].split(":");
				}
				refreshTables(tables);
				break;

			case "RSPlayRight":
				data = args[1].split(":");
				if (data[0].equals("OK")) {
					isRunning = false;
					new GuiPlay(getGame(), getGuiLocation(), Integer.parseInt(data[1]));
					gui.dispose();
					isRunning = false;
				} else {
					String[] bt = {"XÁC NHẬN"};
					new MyDialog().showMessage(gui, "ERROR", "Không thể vào bất kỳ bàn nào hiện tại", bt);
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
			int roomSize = myDialog.showMessage(gui, "Xác nhận", "Chọn loại phòng", buttons);
			if (roomSize != -1) {
				/*
				 * Gui message tao phong len server Cau truc message:
				 * "CreateTable@TenPhong:bet:songuoichoi"
				 */
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

			/* Khoi tao 1 phong tai toa do x,y voi ten phong la roomnames[i] */
			gui.tables[i] = new Table(tableNames[i]);

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
}
