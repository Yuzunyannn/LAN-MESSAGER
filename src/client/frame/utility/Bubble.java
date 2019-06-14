package client.frame.utility;

import java.awt.FontMetrics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Bubble extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel words;
	private RButton rbutton;
	public Bubble(String words) {
		this.words = new JLabel(words);
		this.words.setSize(300, 100);
		this.JlabelSetText(this.words, words);
		System.out.println(this.words.getText());
		this.rbutton = new RButton(this.words.getText());
		//this.add(this.words);
		this.add(rbutton);
		this.setOpaque(false);
		this.setVisible(true);
	}
	
	/** 文字换行 */
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
        //Button.setText(builder.toString());
    }

}
