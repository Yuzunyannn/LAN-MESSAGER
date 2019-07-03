package client.frame.utility;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;

import javax.swing.JButton;

import client.event.EventSendPicture;
import client.event.EventsBridge;
import resmgt.UserResource;

public class EmojiButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3874379476366961194L;
	private int ID = 0;
	public EmojiButton(int id, String toUser, boolean isReal) {
		ID = id;
		this.setBackground(Color.white);
		if (isReal) {
			String path = "meme-" + Integer.toString(ID);
			this.setIcon(UserResource.getMeme(path));
			this.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if (e.getSource() instanceof JButton) {
						System.out.println(getParent().getParent().getParent().getParent().getParent().getParent().getClass());
						EventsBridge.frontendEventHandle
								.post(new EventSendPicture(Integer.toString(ID) + ".jpg", BubbleType.MEME, toUser));
						getParent().getParent().getParent().getParent().getParent().getParent().setVisible(false);
					
					} else {
						System.out.println("Fail");
					}
				}
			});
		}
	}
}
