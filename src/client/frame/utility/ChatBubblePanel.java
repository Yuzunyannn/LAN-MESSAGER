package client.frame.utility;

import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ButtonUI;

import client.frame.Theme;
import client.frame.ui.RoundedRecBorder;
import client.user.UserClient;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;

/** 对话气泡 */
public class ChatBubblePanel extends JPanel implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
	private final int USERICONSIZE = 40;
	private boolean userID;
	private String userIcon;
	private String userName;
	private String userDialog;
	private String userFile = "";
	private String userExtension = "";
	private String userTime = "";
	private Type type = Type.NULL;
	private ImageIcon imageIcon;
	private JButton Icon;
	//private JButton dialog;
	private Bubble dialog; 
	private JLabel displayedName;
	private JLabel readStatus;
	
	/** 构造函数，生成一个对话气泡，显示信息的参数待定！*/
	public ChatBubblePanel(boolean isMySelf, String info, String localName, Type type, String time) {
		// TODO Auto-generated constructor stub
		if (type == Type.LINE) {
			JLabel lineLabel = new JLabel("--------以下为全部消息--------");
			lineLabel.setVisible(true);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(lineLabel);
			this.setVisible(true);
		} else if (type == Type.TIME) {
			JLabel lineLabel = new JLabel("--------"+time+"--------");
			lineLabel.setVisible(true);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(lineLabel);
			this.setVisible(true);
		} else {
			this.userTime = time;
			this.type = type;
			this.userID = isMySelf;
			if (this.userID) {
				this.userName = UserClient.getClientUsername();
			} else {
				this.userName = localName;
			}
			initalUI(info);
			if (type == Type.NULL) {
				this.setVisible(false);
			} else {
				this.setVisible(true);
			}
		}
		this.setBackground(Theme.COLOR0);
		this.setOpaque(false);
		this.setSize(this.getWidth(), 80);
		
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isMyself", this.userID);
		nbt.setInteger("Type", this.type.ordinal());
		nbt.setString("UserIcon", this.userIcon);
		nbt.setString("Username", this.userName);
		nbt.setString("Word", this.userDialog);
		nbt.setString("File", this.userFile);
		nbt.setString("Extension", this.userExtension);
		nbt.setString("Time", this.userTime);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt == null) {
			System.out.println("NBT为空！");
			return;
		}
		this.type = Type.values()[nbt.getInteger("Type")];
		this.userID = nbt.getBoolean("isMyself");
		this.userIcon = nbt.getString("UserIcon");
		this.userName = nbt.getString("Username");
		this.userDialog = nbt.getString("Word");
		this.userFile = nbt.getString("File");
		this.userExtension = nbt.getString("Extension");
		this.userTime = nbt.getString("Time");
		this.reConstructComponents();
	}
	
	public void initalUI(String info ) {
		switch (this.type) {
		case WORD:
			this.userDialog = info;
			this.dialog = new Bubble(info, Type.WORD);
			break;
		case PICTURE:
			break;
		case EXTENSION:
			break;
		case FILE:
			this.dialog = new FileBubble(info);
			break;
		default:
			this.dialog = new Bubble("", Type.WORD);
			break;
		}
		this.displayedName = new JLabel();
		this.setIcon(this.userName);
		RoundedRecBorder border = new RoundedRecBorder(Theme.COLOR0, 1, 10);
		this.Icon.setBorder(border);
		this.Icon.setBorderPainted(true);
		
	}
	
	/** 设置用户图标 */
	private void setIcon(String userName) {
		imageIcon = new ImageIcon("src/img/1.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(USERICONSIZE, USERICONSIZE, USERICONSIZE));
		Icon = new JButton();
		Icon.setIcon(imageIcon);
		if (this.userID) {
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			displayedName.setText(":" + userName);
				this.add(dialog);
				this.add(displayedName);
				this.add(Icon);
		}else {
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			displayedName.setText(userName + ":");	
				this.add(Icon);
				this.add(displayedName);
				this.add(dialog);
		}
		dialog.setFont(Theme.FONT1);
		displayedName.setFont(Theme.FONT1);
	}
	
	/** 重构组件 */
	public void reConstructComponents() {
		int length = this.getComponentCount();
		this.remove(length-1);
		this.remove(length-2);
		this.remove(length-3);
		initalUI(this.userDialog);
	}
	
	/** 获取时间 */
	public String getTime() {
		return userTime;
	}

}

/** 对话气泡类型 */
enum Type {
	WORD, FILE, PICTURE, EXTENSION, NULL, LINE, TIME
}