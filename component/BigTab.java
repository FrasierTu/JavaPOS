package component;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.util.ArrayList;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;

public class BigTab extends JPanel implements MouseListener , ComponentListener {
    private static final long serialVersionUID = 0x346L;
    private int selectedIndex = 0;
    private int itemCount = 0;
    private FontMetrics metrics;
    
    int panelWidth = 0;//getWidth();
    int panelHeight = 0;//getHeight();
    int tabWidth = 0;
    
    ArrayList<Integer> list = new ArrayList<>();
    
    final int gap = 3;

    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public BigTab(final int itemNumber) {
        this.itemCount = itemNumber;
        //final Border blackline = BorderFactory.createLineBorder(Color.black);
        //this.setBorder(blackline);
        this.addMouseListener(this);

        this.addComponentListener(this);

        final Font defaultFont = this.getFont();
        final Font newFont = defaultFont.deriveFont(defaultFont.getSize() * 1.4F);
        this.setFont(newFont);
        this.metrics = this.getFontMetrics(newFont);
        // System.out.println(this.getWidth());
        // this.addMouseListener(new MyMouseListener());
    }

    public BigTab() {
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        this.setBorder(blackline);
        // this.addMouseListener(new MyMouseListener());
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
    
    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void componentResized(final ComponentEvent e) {
        // Perform calculation here
        // System.out.println("componentResized");
        list.clear();
        this.panelWidth = getWidth();
        this.panelHeight = getHeight();

        this.tabWidth = (panelWidth - (this.itemCount - 1) * gap) / this.itemCount;

        for (int i = 0; i < this.itemCount; i++) {
            list.add((i * gap + i * this.tabWidth + this.tabWidth));
        }
    }

    public void componentHidden(final ComponentEvent ce) {
    };

    public void componentShown(final ComponentEvent ce) {
        System.out.println("componentShown");
    };

    public void componentMoved(final ComponentEvent ce) {
        //System.out.println("componentMoved");
    };

    @Override
    public void mouseClicked(final MouseEvent event) {
    }

    public void mouseEntered(final MouseEvent event) {
        //System.out.println("entered");
    }

    public void mouseExited(final MouseEvent event) {
        //System.out.println("exited");
    }

    public void mousePressed(final MouseEvent event) {
        new Thread(() -> {
            final int x = event.getX();
            //final int y = event.getY();
    
            final int oldIndex = selectedIndex;
    
            for (int i = 0; i < this.itemCount; i++) {
                if (x < list.get(i)) {
                    selectedIndex = i;
                    break;
                }
            }
    
            /*
             * for(int i = 0; i < this.itemCount; i++) { if (x < (i*gap+i*this.tabWidth +
             * this.tabWidth)) { indexSelected = i; break; } }
             */
    
            this.repaint();
            changes.firePropertyChange("TabSelectedIndex", oldIndex,selectedIndex);
            }
        ).start();
        // System.out.println("pressed");
    }

    public void mouseReleased(final MouseEvent event) {
        // System.out.println("released");

    }

    /*
     * private class MyMouseListener implements MouseListener {
     * 
     * }
     */
    protected void paintComponent(final Graphics g) {
        // System.out.println("paintComponent");
        super.paintComponent(g);

        final int stringHeight = metrics.getHeight();
        int stringWidth = 0;

        final int stringY = (panelHeight - stringHeight) / 2 + stringHeight - 5;
        
        int stringX = 0;
        
        int rectX = 0;
        
        Color[] colors = {Color.red ,Color.orange ,Color.yellow ,Color.green ,Color.blue ,Color.cyan ,Color.pink};
        
        for(int i = 0; i < this.itemCount; i++) {
            stringWidth = metrics.stringWidth(Integer.toString(i+1));
            stringX = (tabWidth - stringWidth) / 2;
            
            rectX = i*gap+i*tabWidth;
            g.setColor(Color.black);
            g.drawRect(rectX, 0 , tabWidth, this.panelHeight-1);
            
            if (i == this.selectedIndex) {
                g.setColor(colors[i]);
                g.fillRect(rectX + 1, 1 , tabWidth-1, panelHeight-2);
            }
            
            g.setColor(Color.black);
            stringX += rectX;
            g.drawString(Integer.toString(i+1), stringX ,stringY);
        }
        
    }
}


