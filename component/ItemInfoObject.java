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

public class ItemInfoObject extends JPanel implements ComponentListener {
    private static final long serialVersionUID = 0x97645L;
    public String title = "";
    private int[] priceList;
    private int[] itemCount;
    int order = 0;
    private int itemIndex = 0;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public ItemInfoObject() {
        this.addComponentListener(this);
    }

    public ItemInfoObject(final String title, final int[] priceList) {
        this.title = title;
        this.priceList = new int[priceList.length];
        System.arraycopy(priceList, 0, this.priceList, 0, priceList.length);
        this.itemCount = new int[priceList.length];

        for(int index = 0 ; index < this.itemCount.length; index++) {  
            this.itemCount[index] = 0;
        }

        this.setLayout(null);
        this.setBackground(new  Color(240, 240, 240));
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
        final Font labelFont = new Font("Serif", Font.PLAIN, 27);
        final JLabel dummyLabel = new JLabel("我");
        final JLabel itemPriceLabel = new JLabel(String.valueOf(this.priceList[0]));// +"\t" + levels[0]+ "\t" + "\u25B6");
        final JLabel itemCountLabel = new JLabel(String.valueOf(this.itemCount[0]));// +"\t" + levels[0]+ "\t" + "\u25B6");

        dummyLabel.setFont(labelFont);
        
        final int charWidth = dummyLabel.getFontMetrics(labelFont).stringWidth(dummyLabel.getText());
        int charHeight = dummyLabel.getFontMetrics(labelFont).getHeight();

        int labelX,labelY,labelWidth,labelHeight;

        labelX = 0;
        labelY = 0;
        labelWidth = width;
        labelHeight = height/3;
        final JLabel titleLabel = new JLabel( this.title);
        titleLabel.setFont(labelFont);
        titleLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        this.add(titleLabel);

        labelX = 5;
        labelY = titleLabel.getHeight() + 5;
        labelWidth = charWidth * 3/2;
        labelHeight = charHeight;
        final JLabel lChoice = new JLabel("\u25C0");// +"\t" + levels[0]+ "\t" + "\u25B6");
        lChoice.setFont(labelFont);
        lChoice.setBounds(labelX, labelY, labelWidth, labelHeight);
        lChoice.setHorizontalAlignment(JLabel.CENTER);
        lChoice.setBorder(blackline);
        this.add(lChoice);
        lChoice.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                itemIndex--;
                if (itemIndex < 0) {
                    itemIndex = priceList.length - 1;
                }
                itemPriceLabel.setText(String.valueOf(priceList[itemIndex]));
                itemCountLabel.setText(String.valueOf(itemCount[itemIndex]));
            }
        });

        labelX = width - 5 - charWidth * 3/2;
        labelY = titleLabel.getHeight() + 5;
        labelWidth = charWidth * 3/2;
        labelHeight = charHeight;
        final JLabel rChoice = new JLabel("\u25B6");// +"\t" + levels[0]+ "\t" + "\u25B6");
        rChoice.setFont(labelFont);
        rChoice.setBounds(labelX, labelY, labelWidth, labelHeight);
        rChoice.setHorizontalAlignment(JLabel.CENTER);
        rChoice.setBorder(blackline);
        this.add(rChoice);
        rChoice.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                itemIndex++;
                if (itemIndex >= priceList.length) {
                    itemIndex = 0;
                }
                itemPriceLabel.setText(String.valueOf(priceList[itemIndex]));
                itemCountLabel.setText(String.valueOf(itemCount[itemIndex]));
            }
        });

        labelX = lChoice.getLocation().x + lChoice.getWidth();
        labelY = titleLabel.getHeight() + 5;
        labelWidth = width - (lChoice.getWidth() + rChoice.getWidth() + 10);
        labelHeight = charHeight;
        itemPriceLabel.setFont(labelFont);
        itemPriceLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        itemPriceLabel.setHorizontalAlignment(JLabel.CENTER);
        //itemPriceLabel.setBorder(blackline);
        this.add(itemPriceLabel);

        labelX = 5;
        labelY = lChoice.getLocation().y + lChoice.getHeight() + 15;
        labelWidth = charWidth * 3/2;
        labelHeight = charHeight;
        final JLabel minus = new JLabel("\u2212");// +"\t" + levels[0]+ "\t" + "\u25B6");
        minus.setFont(labelFont);
        minus.setBounds(labelX, labelY, labelWidth, labelHeight);
        minus.setHorizontalAlignment(JLabel.CENTER);
        minus.setBorder(blackline);
        this.add(minus);
        minus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int count = itemCount[itemIndex];

                if (count < 1) {
                    return;
                }
                itemCount[itemIndex]--;
                itemCountLabel.setText(String.valueOf(itemCount[itemIndex]));
                changes.firePropertyChange("ItemQuantityChanged", count,itemCount[itemIndex]);
            }
        });

        labelX = width - 5 - charWidth * 3/2;
        labelY = lChoice.getLocation().y + lChoice.getHeight() + 15;
        labelWidth = charWidth * 3/2;
        labelHeight = charHeight;
        final JLabel plus = new JLabel("\uFF0B");// +"\t" + levels[0]+ "\t" + "\u25B6");
        plus.setFont(labelFont);
        plus.setBounds(labelX, labelY, labelWidth, labelHeight);
        plus.setHorizontalAlignment(JLabel.CENTER);
        plus.setBorder(blackline);
        this.add(plus);
        plus.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int count = itemCount[itemIndex];
                itemCount[itemIndex]++;
                itemCountLabel.setText(String.valueOf(itemCount[itemIndex]));
                changes.firePropertyChange("ItemQuantityChanged", count,itemCount[itemIndex]);
            }
        });

        labelX = minus.getLocation().x + minus.getWidth();
        labelY = lChoice.getLocation().y + lChoice.getHeight() + 15;
        labelWidth = width - (minus.getWidth() + plus.getWidth() + 10);
        labelHeight = charHeight;
        itemCountLabel.setFont(labelFont);
        itemCountLabel.setBounds(labelX, labelY, labelWidth, labelHeight);
        itemCountLabel.setHorizontalAlignment(JLabel.CENTER);
        //itemCountLabel.setBorder(blackline);
        this.add(itemCountLabel);
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

