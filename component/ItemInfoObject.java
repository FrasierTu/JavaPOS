package component;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ItemInfoObject extends JPanel implements ComponentListener {
    private static final long serialVersionUID = 0x97645L;
    private String title = "";
    private int[] priceList;

    public ItemInfoObject() {
        this.addComponentListener(this);
    }

    public ItemInfoObject(final String title, final int[] priceList) {
        this.title = title;
        this.priceList = new int[priceList.length];
        System.arraycopy(priceList, 0, this.priceList, 0, priceList.length);

        this.setLayout(null);
        this.setBackground(Color.lightGray);
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);

        this.addComponentListener(this);
    }

    public void componentResized(final ComponentEvent e) {
        final int width = getWidth();
        final int height = getHeight();
        final JLabel label = new JLabel( this.title);
        final Font labelFont = new Font("Serif", Font.PLAIN, 29);
        label.setFont(labelFont);
        label.setBounds(0, 0, width, height/3);
        //String labelText = label.getText();
        //int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
        //int stringHeight = label.getFontMetrics(labelFont).getHeight();
        
        this.add(label);
    }

    public void componentHidden(final ComponentEvent ce) {
    };

    public void componentShown(final ComponentEvent ce) {
        System.out.println("componentShown");
    };

    public void componentMoved(final ComponentEvent ce) {
        //System.out.println("componentMoved");
    };

}

