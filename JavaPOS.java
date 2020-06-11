import component.*;

import javax.swing.*;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
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
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.text.SimpleDateFormat;  
import java.util.Date;  

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Font;
//import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

//import java.awt.print.PrinterJob;
//import java.awt.print.PrinterException;

import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.ByteArrayOutputStream;

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
    SpicyLevel spicyLevel;
    public static void main(final String[] args) {
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

        date = null;
        formatter = null;
        System.gc();

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
            csvFile.close();

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
        final int itemHeight = 151;
        final int panelWidth = 995;
        final int panelHeight = 800;
        
        final int xGap = 5;
        final int yGap = 5;
        
        int x=0,y=yGap;
        //int itemCount = 0;

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
            //itemCount++;
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

        String[] columnNames = {"刪除", "品項 單價 數量", "小計"};
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

        Rectangle tableBounds = scrollPane.getBounds();
        
        this.spicyLevel = new SpicyLevel();
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
                int tableColumn = table.columnAtPoint(e.getPoint());
                int tableRow = table.rowAtPoint(e.getPoint());

                if (tableColumn != 0) {
                    return;
                }

                int count = 0;
                for(JPanel panel: itemPanels) {
                    Component[] components = panel.getComponents();
                    int componentNumber = components.length;
                    for(int index = 0; index < componentNumber; index++) {
                        CustomerSelection selection = (CustomerSelection)components[index];
    
                        for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                            if (selection.itemCount.get(itemIndex) != 0) {
                                if(count == tableRow) {
                                    selection.clearItem(itemIndex);
                                    new Thread(() -> {
                                        SelectionChanged();
                                    }).start();
                                    return;
                                }
                                count++;
                            }
                       }
                    }
                }
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
        this.confirmButton.setFont(new Font("Serif", Font.PLAIN, 39));
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

                final Date date = new Date();
                
                SaveToFile(date);
                DoPrinting(date);

                ClearTransation();
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
            this.spicyLevel.set(0);
        }).start();
    }

    private void DoPrinting(Date date) {
        //final int printWidthDots = 80 / 25.4 * 203 = 639;
        // font size 128 ,MaxDescent 26

        //final int printWidthDots = 630;
        /*
        final int titleFontSize = 32;
        final int contentFontSize = 24;

        //title image
        this.StringToESC("柯黑鴨", "標楷體" , titleFontSize);

        int number,price ,subTotal ;
        String transation2Printer = "";
        
        for(JPanel panel: itemPanels) {
            Component[] components = panel.getComponents();
            int componentNumber = components.length;
            for(int index = 0; index < componentNumber; index++) {
                CustomerSelection selection = (CustomerSelection)components[index];
                for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                    number = selection.itemCount.get(itemIndex);
                    if( number > 0 ) {
                        price = selection.priceList.get(itemIndex);
                        subTotal = number * price;

                        transation2Printer = selection.title+"("+String.valueOf(price)+")"+",數量:"+String.valueOf(number)+",小計:"+String.valueOf(subTotal);
                        this.StringToESC(transation2Printer, "標楷體" , contentFontSize);
                    }
                }
            }
        }

        String spyceLevel = this.spicyLevel.get();
        this.StringToESC(spyceLevel, "標楷體" , contentFontSize);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm分ss秒"); 
        String dateTimeString = formatter.format(date);
        this.StringToESC(dateTimeString, "標楷體" , contentFontSize);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
*/
        int number,price ,subTotal ,totalPrice ;
        String transation2Printer = "";
        List<String> content = new ArrayList<>();

        totalPrice = 0;
        for(JPanel panel: itemPanels) {
            Component[] components = panel.getComponents();
            int componentNumber = components.length;
            for(int index = 0; index < componentNumber; index++) {
                CustomerSelection selection = (CustomerSelection)components[index];
                for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                    number = selection.itemCount.get(itemIndex);
                    if( number > 0 ) {
                        price = selection.priceList.get(itemIndex);
                        subTotal = number * price;

                        transation2Printer = selection.title+"("+String.valueOf(price)+")"+",數量:"+String.valueOf(number)+",小計:"+String.valueOf(subTotal)+"元";
                        content.add(transation2Printer);
                        totalPrice += subTotal;
                    }
                }
            }
        }
        content.add("總價:"+ String.valueOf(totalPrice)+"元");

        content.add(this.spicyLevel.get());

        OutputPrinter printer = new OutputPrinter("柯黑鴨", content ,date);
        printer.RawWriteToESC();
    }

    private void StringToESC(String string, String fontName , int fontSize) {
        //final int printWidthDots = 80 / 25.4 * 203 = 639;
        // font size 128 ,MaxDescent 26
        Font font = new Font(fontName, Font.PLAIN, fontSize);
        BufferedImage offScreenCanvas = new BufferedImage(string.length() * fontSize , fontSize + fontSize * 26 / 128 + 1, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = offScreenCanvas.createGraphics();
        g2d.translate(0, 0); 

        g2d.setFont(font);

        g2d.drawString(string, 0 , fontSize);
        g2d.dispose();

        WritableRaster raster = offScreenCanvas .getRaster();
        DataBufferByte data = (DataBufferByte) raster.getDataBuffer();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-ss");  
        String fileName = formatter.format(date);

        try {
            Path path = Paths.get("D:/Source/Java/Text2Lineart/"+ fileName+".data");
            Files.write(path, data.getData());

            File outputfile = new File("D:/Source/Java/Text2Lineart/"+ fileName+".jpg");
            ImageIO.write(offScreenCanvas, "jpg", outputfile);
        }catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        offScreenCanvas.flush();

        System.gc();
    }

    private void SaveToFile(Date date) {
        String desktopFolder = System.getProperty("user.home") + "/Desktop";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");  
        String yearFolder = desktopFolder+"/"+formatter.format(date);
        File tmpFile = new File(yearFolder);
        if(false == tmpFile.exists()) { 
            boolean bool = tmpFile.mkdirs();
            // do something
            if(false == bool){
                JOptionPane.showMessageDialog(null, "磁碟問題");
                date = null;
                formatter = null;
                tmpFile = null;
                System.gc();
                return;
            }
        }

        formatter = new SimpleDateFormat("MM");  
        String mothFolder = yearFolder+"/"+formatter.format(date);
        tmpFile = new File(mothFolder);
        if(false == tmpFile.exists()) { 
            boolean bool = tmpFile.mkdirs();
            // do something
            if(false == bool){
                JOptionPane.showMessageDialog(null, "磁碟問題");
                date = null;
                formatter = null;
                tmpFile = null;
                System.gc();
                return;
            }
        }

        formatter = new SimpleDateFormat("dd");  
        String csvFilePath = mothFolder+"/"+formatter.format(date) + ".csv";  
        
        try {
            OutputStreamWriter csvFile;
            BufferedWriter csvWriter;

            tmpFile = new File(csvFilePath);
            if(false == tmpFile.exists()) {
                csvFile = new OutputStreamWriter(new FileOutputStream(csvFilePath),"UTF-8");
                csvWriter = new BufferedWriter(csvFile);
                csvWriter.write("品項(單價)"+","+"數量"+","+"小計"+"\n");
            }
            else {
                csvFile = new OutputStreamWriter(new FileOutputStream(csvFilePath, true),"UTF-8");
                csvWriter = new BufferedWriter(csvFile);
            }
            
            formatter = new SimpleDateFormat("HH點mm分ss秒");
            csvWriter.write("\n"+formatter.format(date)+"\n");

            int number,price ,subTotal ;
            String transation2csv = "";

            for(JPanel panel: itemPanels) {
                Component[] components = panel.getComponents();
                int componentNumber = components.length;
                for(int index = 0; index < componentNumber; index++) {
                    CustomerSelection selection = (CustomerSelection)components[index];
                    for(int itemIndex = 0; itemIndex<selection.itemCount.size(); itemIndex++) {
                        number = selection.itemCount.get(itemIndex);
                        if( number > 0 ) {
                            price = selection.priceList.get(itemIndex);
                            subTotal = number * price;

                            transation2csv = selection.title+"("+String.valueOf(price)+")"+","+String.valueOf(number)+","+String.valueOf(subTotal);
                            csvWriter.write(transation2csv+"\n");
                            //totalPrice += subTotal;
                        }
                    }
                }
            }
            csvWriter.write(spicyLevel.get() +"\n");

            csvWriter.close();
            csvFile.close();
        } catch(Exception e1){
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        tmpFile = null;
        formatter = null;
        System.gc();
    }

    private void SelectionChanged() {
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
                SelectionChanged();
            }).start();
        }
    }
}
