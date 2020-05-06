//import java.awt.*;
import javax.swing.*;
import component.*;

import java.nio.file.Path;
import java.nio.file.Files;

import java.beans.PropertyChangeListener;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.util.List;
import java.util.ArrayList;

import java.awt.Color;

final public class JavaPOS implements PropertyChangeListener { 
    //BigTab tab;  
    List<JPanel> itemPanels = new ArrayList<JPanel>();

    public static void main(final String[] args) {
        try{
            final Path tempFile = Files.createTempFile("tempfiles", ".tmp");
            System.out.println("tempFile = " + tempFile);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        
        new JavaPOS();
    }

    public JavaPOS() {
        final JFrame demo = new JFrame("花開富貴");
        demo.setSize(1280, 900);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.setLayout(null);

        final int tabNumber = 1;

        final BigTab tab  = new BigTab(tabNumber);
        //JButton rightLabel = new JButton("Right");
        tab.setBounds(5, 5, 995, 45);
        //rightPanel.setSize(280,900);
        //rightPanel.setResizable(false);
        tab.addPropertyChangeListener(this);
        //demo.add(rightPanel, BorderLayout.EAST);
        demo.add(tab);
        
        Color[] colors = {Color.red ,Color.orange ,Color.yellow ,Color.green ,Color.blue ,Color.cyan ,Color.pink};
        JPanel panel;
        for(int index = 0;index < tabNumber; index++) {
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBounds(5, 55, 995, 800);
            panel.setBackground(colors[index]);

            int[] prices = {100,150};
            ItemInfoObject item = new ItemInfoObject("滷大腸", prices);
            item.setBounds(1, 1, 100, 120);
            panel.add(item);

            //panel.setVisible(false);

            demo.add(panel);
            itemPanels.add(panel);
        }

        String[] columnNames = {"刪除", "品名 單價 數量 小計"};
        final JTable table = new JTable() {
            private static final long serialVersionUID = 0x276L;

            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };

        DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
        contactTableModel.setColumnIdentifiers(columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane( table );
        scrollPane.setBounds(1005,5,255,730);
        demo.add(scrollPane);

/*
        ItemInfoObject itemInfo;
        for (int index = 0; index < 4; index ++) {
            itemInfo = new ItemInfoObject();
            itemInfo.setLocation((10 + itemInfo.getWidth()) * index + 5, 55);
            demo.add(itemInfo);
        }
  */      
        Rectangle tableBounds = scrollPane.getBounds();
        
        SpicyLevel spicyLevel = new SpicyLevel();
        spicyLevel.setBounds(tableBounds.x,tableBounds.y + tableBounds.height + 5,tableBounds.width, 45);
        demo.add(spicyLevel);

        demo.setResizable(false);
        demo.setLocationRelativeTo(null);
        demo.setVisible(true);

        int firstWidth = table.getWidth() * 25 / 100;
        int secondWidth = table.getWidth() - firstWidth;

        TableColumnModel columnModel = table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(firstWidth);
        columnModel.getColumn(1).setPreferredWidth(secondWidth);

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        
        itemPanels.get(0).setVisible(true);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName() == "TabSelectedIndex") {
            int tabIndex = (int)evt.getNewValue();

            for (JPanel panel : this.itemPanels) {
                panel.setVisible(false);
            }

            itemPanels.get(tabIndex).setVisible(true);
            return;
        }

        if (evt.getPropertyName() == "GoodsList") {
        }
    }
}
