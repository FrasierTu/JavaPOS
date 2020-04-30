package component;

import java.awt.Color;
import java.awt.Font;
//import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ItemInfoObject extends JPanel {
    private static final long serialVersionUID = 0x97645L;

    public ItemInfoObject() {

        // final LayoutManager layout = new FlowLayout();
        // this.setLayout(layout);
        this.setLayout(null);
        final JLabel label = new JLabel("這是測試");
        Font labelFont = label.getFont();
        int newFontSize = (int)(labelFont.getSize() * 2.5);
        labelFont = new Font(labelFont.getName(), Font.PLAIN, newFontSize);
        label.setFont(labelFont);
        String labelText = label.getText();
        //int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        int stringHeight = label.getFontMetrics(labelFont).getHeight();
        
        label.setBounds(0, 0, 240, stringHeight);
        this.add(label);

        this.setSize(240, 180);
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);
    }

    public ItemInfoObject(final String title, final int prices[]) {
    }
}

