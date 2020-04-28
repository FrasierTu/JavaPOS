//import java.awt.*;
import javax.swing.*;
import component.*;

import java.nio.file.Path;
import java.nio.file.Files;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import javax.swing.table.DefaultTableModel;

final public class JavaPOS implements PropertyChangeListener { 
    //BigTab tab;  
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

        final int tabNumber = 5;

        final BigTab tab  = new BigTab(tabNumber);
        //JButton rightLabel = new JButton("Right");
        tab.setBounds(5, 5, 1000, 45);
        //rightPanel.setSize(280,900);
        //rightPanel.setResizable(false);
        tab.addPropertyChangeListener(this);
        //demo.add(rightPanel, BorderLayout.EAST);
        demo.add(tab);
        
        String[] columnNames = {"刪除", "品名", "單價", "數量", "小計"};
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
        scrollPane.setBounds(1005,5,255,760);
        demo.add(scrollPane);

        demo.setResizable(false);
        demo.setLocationRelativeTo(null);
        demo.setVisible(true);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (evt.getPropertyName() == "TabSelectedIndex") {
            System.out.println(evt.getNewValue());
            return;
        }

        if (evt.getPropertyName() == "GoodsList") {
        }
    }
}
