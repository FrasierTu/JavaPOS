package component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class ItemInfoObject extends JPanel {
    private static final long serialVersionUID = 0x97645L;

    public ItemInfoObject() {
        
        //final LayoutManager layout = new FlowLayout();
        //this.setLayout(layout);
        this.setLayout(null);
        this.add(new JLabel("Border to JPanel"));

        final Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);
    }
}

