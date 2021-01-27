package component;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

//import java.util.List;
//import java.util.ArrayList;

public class CustomerSelection extends JPanel implements ComponentListener {
    private static final long serialVersionUID = 0x97645L;
    JLabel priceLabel;
    JLabel amountLabel;
    JLabel titleLabel;

    public String title = "";
    public Integer price;
    public Integer amount;
    public String catalogue;
    //public int ID = 0;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public CustomerSelection() {
        this.addComponentListener(this);
    }

    public CustomerSelection(final String title, final Integer price, final String catalogue) {
        this.title = title;
        this.price = price;
        this.amount = 0;
        this.catalogue = catalogue;

        this.setLayout(null);
        this.setBackground(new  Color(220, 220, 220));
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);

        this.addComponentListener(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void componentResized(final ComponentEvent e) {
        final int width = getWidth();
        final int height = getHeight();
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        final Font labelFont = new Font("Serif", Font.BOLD, 29);
        final JLabel dummyLabel = new JLabel("我");
        
        priceLabel = new JLabel(String.valueOf(this.price));// +"\t" + levels[0]+ "\t" + "\u25B6");
        amountLabel = new JLabel(String.valueOf(this.amount));// +"\t" + levels[0]+ "\t" + "\u25B6");

        dummyLabel.setFont(labelFont);
        
        final int charWidth = dummyLabel.getFontMetrics(labelFont).stringWidth(dummyLabel.getText());
        int charHeight = dummyLabel.getFontMetrics(labelFont).getHeight() -1 ;

        int labelX,labelY,labelWidth,labelHeight;

        labelX = 0;
        labelY = 0;
        labelWidth = width;
        labelHeight = height/3;
        this.titleLabel = new JLabel( this.title);
        this.titleLabel.setFont(labelFont);
        this.titleLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        this.titleLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(this.titleLabel);

        labelX = 0;
        labelY = titleLabel.getHeight() + 5;
        labelWidth = width;
        labelHeight = charHeight;
        priceLabel.setFont(labelFont);
        priceLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        priceLabel.setHorizontalAlignment(JLabel.CENTER);
        //itemPriceLabel.setBorder(blackline);
        this.add(priceLabel);

        labelX = 5;
        labelY = priceLabel.getLocation().y + priceLabel.getHeight() + 10;
        labelWidth = charWidth * 2;
        labelHeight = charHeight;
        final JLabel minus = new JLabel("\u2212");// +"\t" + levels[0]+ "\t" + "\u25B6");
        minus.setFont(labelFont);
        minus.setBounds(labelX, labelY, labelWidth, labelHeight+5);
        minus.setHorizontalAlignment(JLabel.CENTER);
        minus.setBorder(blackline);
        this.add(minus);
        minus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int oldCount = amount;

                if (oldCount < 1) {
                    return;
                }

                int count = oldCount-1;
                amount = count;
                new Thread(() -> {
                    changes.firePropertyChange("SelectionChanged", oldCount,count);
                    }
                ).start();  

                if(count < 1) {
                    titleLabel.setForeground(Color.black);
                }
                amountLabel.setText(String.valueOf(amount));
            }
        });

        labelWidth = charWidth * 2;
        labelX = width - 5 - labelWidth;
        labelY = priceLabel.getLocation().y + priceLabel.getHeight() + 10;
        labelHeight = charHeight;
        final JLabel plus = new JLabel("\uFF0B");// +"\t" + levels[0]+ "\t" + "\u25B6");
        plus.setFont(labelFont);
        plus.setBounds(labelX, labelY, labelWidth, labelHeight+5);
        plus.setHorizontalAlignment(JLabel.CENTER);
        plus.setBorder(blackline);
        this.add(plus);
        plus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int oldCount = amount;
                int count = oldCount + 1;
                amount = count;
                titleLabel.setForeground(Color.magenta);
                new Thread(() -> {
                    changes.firePropertyChange("SelectionChanged", oldCount,count);
                    }
                ).start(); 
                amountLabel.setText(String.valueOf(amount));
            }
        });

        labelX = minus.getLocation().x + minus.getWidth();
        labelY = priceLabel.getLocation().y + priceLabel.getHeight() + 10;
        labelWidth = width - (minus.getWidth() + plus.getWidth() + 10);
        labelHeight = charHeight;
        amountLabel.setFont(labelFont);
        amountLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        amountLabel.setHorizontalAlignment(JLabel.CENTER);
        //itemCountLabel.setBorder(blackline);
        this.add(amountLabel);
    }

    public void componentHidden(final ComponentEvent ce) {
    };

    public void componentShown(final ComponentEvent ce) {
        System.out.println("componentShown");
    };

    public void componentMoved(final ComponentEvent ce) {
        //System.out.println("componentMoved");
    };

    public void clearAll() {
        new Thread(() -> {
            this.amount = 0;
            this.priceLabel.setText(String.valueOf(price));
            this.amountLabel.setText(String.valueOf(amount));
            this.titleLabel.setForeground(Color.black);
        }).start();
    }

    public void clearItem() {
        new Thread(() -> {
            this.amount = 0;
            this.priceLabel.setText(String.valueOf(price));
            this.amountLabel.setText("0");
    
            this.titleLabel.setForeground(Color.black);
        }).start();
    }
}

