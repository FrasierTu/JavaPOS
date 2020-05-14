import javax.swing.*;
import component.*;

//import java.nio.file.Path;
//import java.nio.file.Files;
import java.io.File;

import java.beans.PropertyChangeListener;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Component;
//import java.io.FileNotFoundException;
import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.text.SimpleDateFormat;  
import java.util.Date;  

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;


final class Item {
    public String title;
    public List<Integer> prices;
    public List<Integer> itemCount;
};

final public class JavaPOS implements PropertyChangeListener { 
    //BigTab tab;  

    List<JPanel> itemPanels = new ArrayList<JPanel>();
    List<CustomerSelection> customerSelections = new ArrayList<CustomerSelection>();
    JTable table;
    JLabel confirmButton;
    public static void main(final String[] args) {
        /*
        try{
            final Path tempFile = Files.createTempFile("tempfiles", ".tmp");
            System.out.println("tempFile = " + tempFile);
        } catch (final Exception e) {
            e.printStackTrace();
        }
*/
        String desktopFolder = System.getProperty("user.home") + "/Desktop";

        String csvFilePath = desktopFolder+"/1.csv";
        File tmpDir = new File(csvFilePath);
        if(false == tmpDir.exists()) { 
            // do something
            JOptionPane.showMessageDialog(null, "1.csv 不存在桌面上");
            return;
        }
    
        Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");  
        String yearString = formatter.format(date);  

        String desktopDataFolder = desktopFolder+"/"+yearString;
        tmpDir = new File(desktopDataFolder);
        if(false == tmpDir.exists()) { 
            boolean bool = tmpDir.mkdirs();
            // do something
            if(false == bool){
                JOptionPane.showMessageDialog(null, "磁碟問題，程式結束");
                return;
            }
        }

        new JavaPOS();
    }

    public JavaPOS() {
        final JFrame demo = new JFrame("花開富貴");
        demo.setSize(1280, 900);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.setLayout(null);

        List<Item> items = new ArrayList<Item>();

        try {
            String desktopFolder = System.getProperty("user.home") + "/Desktop";
            String csvFilePath = desktopFolder+"/1.csv";
            InputStreamReader csvFile = new InputStreamReader(new FileInputStream(csvFilePath),"UTF-8");
            BufferedReader reader = new BufferedReader(csvFile);

            String itemTitles = reader.readLine();
            String titles[] = itemTitles.split(",");
            int count = titles.length;
            Item item;
            for (int index = 0 ; index < count; index++) {
                item = new Item();
                item.title = titles[index];
                item.prices = new ArrayList<Integer>();
                items.add(item);

                //System.out.println(itemInfo.title);
            }

            String line = null;
            while((line=reader.readLine())!=null){
                String prices[] = line.split(",");
                count = prices.length;

                for (int index = 0; index < count; index++) {
                    int value = 0;
                    try {
                        value = Integer.parseInt(prices[index]);
                        items.get(index).prices.add(value);
                    } catch (NumberFormatException e) {
                    }    
                }
            }
            
            reader.close();

        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        for (Item item : items) {
            System.out.println(item.title);
            if(item.prices.size() < 1) {
                JOptionPane.showMessageDialog(null, item.title+" 怪怪的喔！");
                System.exit(1);
            }
        }
        
        int tabCount = 0;
        final int itemWidth = 160;
        final int itemHeight = 150;
        final int panelWidth = 995;
        final int panelHeight = 800;
        
        final int xGap = 5;
        final int yGap = 5;
        
        int x=0,y=yGap;
        int itemCount = 0;

        Color[] colors = {Color.red ,Color.orange ,Color.yellow ,Color.green ,Color.blue ,Color.cyan ,Color.pink};
        JPanel panel = null;
        for (Item item : items) {
            if (panel == null) {
                panel = new JPanel();
                panel.setLayout(null);
                panel.setBounds(5, 55, panelWidth, panelHeight);
                panel.setBackground(colors[tabCount]);
                panel.setVisible(false);
                tabCount++;
            }

            x += xGap;

            if ((x+itemWidth)> panelWidth) {
                x = xGap;
                y += yGap;
                y += itemHeight;
                if ((y+itemHeight) > panelHeight) {
                    itemPanels.add(panel);
                    demo.add(panel);

                    panel = new JPanel();
                    panel.setLayout(null);
                    panel.setBounds(5, 55, 995, 800);
                    panel.setBackground(colors[tabCount]);
                    panel.setVisible(false);

                    tabCount++;
                    y = yGap;
                }
            }
            
            CustomerSelection selection = new CustomerSelection(item.title, item.prices);
            selection.ID = itemCount;
            itemCount++;
            selection.setBounds(x, y, itemWidth, itemHeight);
            selection.addPropertyChangeListener(this);
            //this.customerSelections.add(selection);
            panel.add(selection);

            x += itemWidth;
        }
        itemPanels.add(panel);
        demo.add(panel);
        
        /*
        for(int index = 0;index < tabNumber; index++) {
            panel = new JPanel();
            panel.setLayout(null);
            panel.setBounds(5, 55, 995, 800);
            panel.setBackground(colors[index]);

            int[] prices = {140,150};
            ItemInfoObject item = new ItemInfoObject("滷大腸", prices);
            item.setBounds(1, 1, itemWidth, itemHeight);
            panel.add(item);

            //panel.setVisible(false);

            demo.add(panel);
            itemPanels.add(panel);
        }
*/
        //int tabNumber = this.itemInfos.size();

        final BigTab tab  = new BigTab(tabCount);
        tab.setBounds(5, 5, 995, 45);
        tab.addPropertyChangeListener(this);
        demo.add(tab);

        String[] columnNames = {"刪除", "品名 單價 數量", "小計"};
        this.table = new JTable() {
            private static final long serialVersionUID = 0x276L;

            public boolean isCellEditable(int nRow, int nCol) {
                return false;
            }
        };

        DefaultTableModel contactTableModel = (DefaultTableModel) table.getModel();
        contactTableModel.setColumnIdentifiers(columnNames);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 0x3476L;

            final Font cellFont = new Font("Serif", Font.PLAIN, 23);
        
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                setFont(cellFont);
                return this;
            }
        };

        cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);

        cellRenderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 0x3479L;

            final Font cellFont = new Font("Serif", Font.PLAIN, 17);
        
            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                setFont(cellFont);
                return this;
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);

        JScrollPane scrollPane = new JScrollPane( table );
        scrollPane.setBounds(1005,5,255,720);
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

        int firsColumntWidth = table.getWidth() * 20 / 100;
        int secondColumnWidth = table.getWidth() - firsColumntWidth * 2;

        TableColumnModel columnModel = table.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(firsColumntWidth);
        columnModel.getColumn(1).setPreferredWidth(secondColumnWidth);
        columnModel.getColumn(2).setPreferredWidth(firsColumntWidth);

        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(table.getRowHeight() *2);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = (JTable) e.getSource();
                int column = table.columnAtPoint(e.getPoint());
                int row = table.rowAtPoint(e.getPoint());
                System.out.println("Column: "+column);
                System.out.println("Row: "+row);
            }
        });
        itemPanels.get(0).setVisible(true);

        JLabel cencelButton = new JLabel("\u274C"); 
        Rectangle spicyLevelBounds = spicyLevel.getBounds();
        cencelButton.setBounds(spicyLevelBounds.x ,spicyLevelBounds.y + spicyLevelBounds.height + 15,65, 65);
        final Border blackline = BorderFactory.createLineBorder(Color.black);
        cencelButton.setBorder(blackline);
        cencelButton.setHorizontalAlignment(JLabel.CENTER);
        cencelButton.setFont(new Font("Serif", Font.PLAIN, 37));
        cencelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new Thread(() -> {
                    ClearTransation();
                }).start();
            }
        });

        demo.add(cencelButton);

        this.confirmButton = new JLabel("0(0)"); 
        this.confirmButton.setBounds(spicyLevelBounds.x + 70 ,spicyLevelBounds.y + spicyLevelBounds.height + 15,spicyLevelBounds.width - 70, 65);
        this.confirmButton.setBorder(blackline);
        this.confirmButton.setHorizontalAlignment(JLabel.CENTER);
        this.confirmButton.setFont(new Font("Serif", Font.PLAIN, 37));
        this.confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Boolean toPrint = false;

                for(JPanel panel: itemPanels) {
                    Component[] components = panel.getComponents();
                    int componentNumber = components.length;
                    for(int index = 0; index < componentNumber; index++) {
                        CustomerSelection selection = (CustomerSelection)components[index];
    
                        for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                            int number = selection.itemCount.get(itemIndex);
                            if( number > 0 ) {
                                toPrint = true;
                                break;
                            }
                        }
                        if(toPrint) {
                            break;
                        }
                    }
                    if(toPrint) {
                        break;
                    }
                }

                if(false == toPrint) {
                    return;
                }

                new Thread(() -> {
                    ClearTransation();
                }).start();
            }
        });
        
        demo.add(confirmButton);
    }

    private void ClearTransation() {
        new Thread(() -> {
            for(JPanel panel: itemPanels) {
                Component[] components = panel.getComponents();
                int componentNumber = components.length;
                for(int index = 0; index < componentNumber; index++) {
                    CustomerSelection selection = (CustomerSelection)components[index];
                    selection.clearAll();
                }
            }
    
            DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
            tableModel.setRowCount(0);
            tableModel.fireTableDataChanged();
            this.confirmButton.setText("0(0)"); 
        }).start();
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

        if (evt.getPropertyName() == "SelectionChanged") {
            new Thread(() -> {
                DefaultTableModel tableModel = (DefaultTableModel) this.table.getModel();
                tableModel.setRowCount(0);
                String[] data = new String[3];
                int selectedItems = 0;
                int totalMoney = 0;

                for(JPanel panel: this.itemPanels) {
                    Component[] components = panel.getComponents();
                    int componentNumber = components.length;
                    for(int index = 0; index < componentNumber; index++) {
                        CustomerSelection selection = (CustomerSelection)components[index];
    
                        for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                            int number = selection.itemCount.get(itemIndex);
                            if( number > 0 ) {
                                data[0] = "\u274C";
                                data[1] = selection.title+"("+ String.valueOf(selection.priceList.get(itemIndex))+")"+" * "+String.valueOf(number);
                                data[2] = String.valueOf(selection.priceList.get(itemIndex)*number);
    
                                tableModel.addRow(data);
                                selectedItems++;
                                totalMoney += (selection.priceList.get(itemIndex)*number);
                            }
                        }
                    }

                }
                tableModel.fireTableDataChanged();
                String theString = String.valueOf(totalMoney)+"("+String.valueOf(selectedItems)+")";
                this.confirmButton.setText(theString);
            }).start();
        }
    }
}
