package client.frame.utility;

import java.awt.Button;
import java.awt.FlowLayout;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;
import client.word.Word;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;

/** 对话气泡 */
public class ChatBubblePanel extends JPanel implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
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
	private JButton dialog;
	private JLabel displayedName;
	
	/** 构造函数，生成一个对话气泡，显示信息的参数待定！*/
	public ChatBubblePanel(boolean isMySelf, String info, String localName, Type type) {
		// TODO Auto-generated constructor stub
		this.type = type;
		this.userID = isMySelf;
		this.userName = localName;
		switch (this.type) {
		case WORD:
			this.userDialog = info;
			break;
		case PICTURE:
			break;
		case EXTENSION:
			break;
		case FILE:
			break;
		default:
			break;
		}
		
		this.dialog = new JButton(this.userDialog);
		this.displayedName = new JLabel();
		this.setIcon(this.userName);
		this.setVisible(true);
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
		this.type = Type.values()[nbt.getInteger("Type")];
		this.userID = nbt.getBoolean("isMyself");
		this.userIcon = nbt.getString("UserIcon");
		this.userName = nbt.getString("Username");
		this.userDialog = nbt.getString("Word");
		this.userFile = nbt.getString("File");
		this.userExtension = nbt.getString("Extension");
		this.userTime = nbt.getString("Time");
	}
	
	/** 设置用户图标 */
	private void setIcon(String userName) {
		imageIcon = new ImageIcon("src/img/1.png");
		imageIcon.setImage(imageIcon.getImage().getScaledInstance(40, 40, 40));
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
		this.dialog = new JButton(this.userDialog);
		this.displayedName = new JLabel();
		this.setIcon(this.userName);
		if (this.userID) {
			displayedName.setText(":" + userName);
		}else {
			displayedName.setText(userName + ":");	
		}
	}

}

/** 对话气泡类型 */
enum Type {
	WORD, FILE, PICTURE, EXTENSION, NULL
}
