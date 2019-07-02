package client.frame.utility;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import client.frame.Theme;
import client.frame.ui.RoundedRecBorder;
import client.user.UserClient;
import nbt.INBTSerializable;
import nbt.NBTTagCompound;
import resmgt.UserResource;

/** 对话气泡 */
public class ChatBubblePanel extends JPanel implements INBTSerializable<NBTTagCompound> {
	private static final long serialVersionUID = 1L;
	private boolean userID;
	// CBPID格式：1_00001或1_
	private String CBPID = "";
	private String userIcon;
	private String userName;
	private String userDialog;
	private String userFile = "";
	private String userExtension = "";
	private String userTime = "";
	private Boolean userMessageStatus = false;
	private BubbleType type = BubbleType.NULL;
	private ImageIcon imageIcon;
	private JButton Icon;
	// private JButton dialog;
	private Bubble dialog;
	private JLabel displayedName;
	private JLabel messageStatus;

	/** 构造函数，生成一个对话气泡，显示信息的参数待定！ */
	public ChatBubblePanel(boolean isMySelf, String info, String localName, BubbleType type, String time, String ID) {
		if (type == BubbleType.LINE) {
			JLabel lineLabel = new JLabel("--------以下为全部消息--------");
			lineLabel.setVisible(true);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(lineLabel);
			this.setVisible(true);
			this.setSize(this.getWidth(), 80);
		} else if (type == BubbleType.TIME) {
			JLabel lineLabel = new JLabel("--------" + time + "--------");
			lineLabel.setVisible(true);
			this.setLayout(new FlowLayout(FlowLayout.CENTER));
			this.add(lineLabel);
			this.setVisible(true);
			this.setSize(this.getWidth(), 80);
		} else {
			this.CBPID = ID;
			this.userTime = time;
			this.type = type;
			this.userID = isMySelf;
			if (this.userID) {
				this.userName = UserClient.getClientUsername();
			} else {
				this.userName = localName;
			}
			initalUI(info);
			if (type == BubbleType.WORD) {
				this.setSize(this.getWidth(), this.dialog.getH() + 40);
			} else if (type == BubbleType.FILE) {
				this.setSize(this.getWidth(), 80);
			} else if (type == BubbleType.MEME) {
				this.setSize(this.getWidth(), 140);
			}

			if (type == BubbleType.NULL) {
				this.setVisible(false);
			} else {
				this.setVisible(true);
			}
		}

		this.setBackground(Theme.COLOR0);
		this.setOpaque(false);

	}

	public String getCBPID() {
		return CBPID;
	}

	public Bubble getDialog() {
		return dialog;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("isMyself", this.userID);
		nbt.setBoolean("Status", this.userMessageStatus);
		nbt.setInteger("Type", this.type.ordinal());
		System.out.println("type:" + this.type.ordinal());
		nbt.setString("UserIcon", this.userIcon);
		nbt.setString("CBPID", this.CBPID);
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
		this.type = BubbleType.values()[nbt.getInteger("Type")];
		System.out.println("type:" + this.type.ordinal());
		this.userID = nbt.getBoolean("isMyself");
		this.CBPID = nbt.getString("CBPID");
		this.userIcon = nbt.getString("UserIcon");
		this.userName = nbt.getString("Username");
		this.userDialog = nbt.getString("Word");
		this.userFile = nbt.getString("File");
		this.userExtension = nbt.getString("Extension");
		this.userTime = nbt.getString("Time");
		this.userMessageStatus = nbt.getBoolean("Status");
		this.reConstructComponents();
	}

	public void initalUI(String info) {
		switch (this.type) {
		case WORD:
			this.userDialog = info;
			this.dialog = new Bubble(info, BubbleType.WORD);
			break;
		case EXTENSION:
			break;
		case FILE:
			this.dialog = new FileBubble(info, this.userID);
			break;
		case PICTURE:
			this.dialog = new PictureBubble(info, BubbleType.PICTURE);
			break;
		case MEME:
			this.dialog = new PictureBubble(info, BubbleType.MEME);
			break;
		default:
			this.dialog = new Bubble("", BubbleType.WORD);
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
		imageIcon = UserResource.getHeadIcon("guest", UserResource.HeadIconSize.SMALL);
		Icon = new JButton();
		Icon.setIcon(imageIcon);
		this.Icon.setOpaque(false);
		if (this.userID) {
			this.setLayout(new FlowLayout(FlowLayout.RIGHT));
			displayedName.setText(":" + userName);
			if (this.userMessageStatus) {
				this.messageStatus = new JLabel("已读");
				this.messageStatus.setForeground(Color.BLACK);
			} else {
				this.messageStatus = new JLabel("未读");
				this.messageStatus.setForeground(Theme.COLOR9);
			}
			this.messageStatus.setOpaque(false);
			this.add(messageStatus);
			this.add(dialog);
			this.add(displayedName);
			this.add(Icon);
		} else {
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
		this.remove(length - 1);
		this.remove(length - 2);
		this.remove(length - 3);
		this.remove(length - 4);
		initalUI(this.userDialog);
	}

	/** 获取时间 */
	public String getTime() {
		return userTime;
	}

	public void setRead() {
		// TODO Auto-generated method stub
		if (this.userID && this.type != BubbleType.NULL && this.type != BubbleType.TIME
				&& this.type != BubbleType.LINE) {
			this.userMessageStatus = true;
			this.messageStatus.setText("已读");
			this.messageStatus.setForeground(Color.BLACK);
			this.revalidate();
			this.repaint();
		}
		System.out.println("test" + this.userDialog);

	}

}
