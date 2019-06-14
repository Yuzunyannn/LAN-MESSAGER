package client.frame.utility;

import java.awt.Color;
import java.awt.FontMetrics;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sun.prism.Texture;

import client.frame.Theme;
import client.frame.ui.RoundedRecBorder;

public class Bubble extends JPanel {
	
	private RButton words;
	public Bubble(String words) {
//		//this.words.setText(words);
//		this.words.setSize(300, 0);
//		this.JlabelSetText(this.words, words);
//		RoundedRecBorder border = new RoundedRecBorder(Theme.COLOR3, 10, 20);
//		this.words.setBorder(border);
//		this.words.setOpaque(true);
//		this.words.setBackground(Theme.COLOR0);
//		this.words.setVisible(true);
//		//this.setBackground(Theme.COLOR0);
		this.words = new RButton(words);
		this.words.setBackground(Theme.COLOR0);
		this.add(this.words);
		this.setOpaque(false);
		this.setVisible(true);
		
	}
	
	public void JlabelSetText(JLabel jLabel, String longString) {
        StringBuilder builder = new StringBuilder("<html>");
        char[] chars = longString.toCharArray();
        FontMetrics fontMetrics = jLabel.getFontMetrics(jLabel.getFont());
        int start = 0;
        int len = 0;
        while (start + len < longString.length()) {
            while (true) {
                len++;
                if (start + len > longString.length())break;
                if (fontMetrics.charsWidth(chars, start, len) 
                        > jLabel.getWidth()) {
                    break;
                }
            }
            builder.append(chars, start, len-1).append("<br/>");
            start = start + len - 1;
            len = 0;
        }
        builder.append(chars, start, longString.length()-start);
        builder.append("</html>");
        jLabel.setText(builder.toString());
    }

}
