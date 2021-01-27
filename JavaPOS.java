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

//import java.io.ByteArrayOutputStream;

final class Item {
    public String title;
    public Integer price;
    public String catalogue;
};

final public class JavaPOS implements PropertyChangeListener { 
    //BigTab tab;  

    List<JPanel> itemPanels = new ArrayList<JPanel>();
    List<CustomerSelection> customerSelections = new ArrayList<CustomerSelection>();
    
    JTable table;
    JLabel confirmButton;
    //SpicyLevel spicyLevel;
    SisBroTradeName sisBroTradeName;
    ToGoOrNot toGoOrNot;
    public static void main(final String[] args) {
        String desktopFolder = System.getProperty("user.home") + "/Desktop";

        String csvFilePath = desktopFolder+"/sister.csv";
        File tmpDir = new File(csvFilePath);
        if(false == tmpDir.exists()) { 
            // do something
            JOptionPane.showMessageDialog(null, "sister.csv 不存在桌面上");
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
        final JFrame mainFrame = new JFrame("花開富貴");
        mainFrame.setSize(1280, 900);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);

        List<Item> sisItems = this.OpenCSV("sister");
        List<Item> broItems = this.OpenCSV("brother");
        List<JPanel> sisPanels = this.ItemsToPanel(sisItems);
        List<JPanel> broPanels = this.ItemsToPanel(broItems);

        this.itemPanels.addAll(sisPanels);
        this.itemPanels.addAll(broPanels);
        //final int itemWidth = 160;
        for(JPanel panel : this.itemPanels) {
            mainFrame.add(panel);
        }
        this.itemPanels.get(0).setBackground(new  Color(53,0,0));
       
        this.itemPanels.get(0).setVisible(true);
        //demo.add(panel);

        final BigTab tab  = new BigTab(sisPanels.size()+broPanels.size());
        tab.setBounds(5, 5, 995, 45);
        tab.addPropertyChangeListener(this);
        mainFrame.add(tab);
        
        //
        this.table= this.CreateTable();
        JScrollPane scrollPane = new JScrollPane( this.table );
        scrollPane.setBounds(1005,5,255,720);
        //scrollPane.setBounds(1005,5,255,660);
        mainFrame.add(scrollPane);

        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

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
    
                        if (selection.amount != 0) {
                            if(count == tableRow) {
                                selection.clearItem();
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
        });

        Rectangle tableBounds = scrollPane.getBounds();
        this.toGoOrNot = new ToGoOrNot();
        toGoOrNot.setBounds(tableBounds.x,tableBounds.y + tableBounds.height + 5,tableBounds.width, 45);
        mainFrame.add(toGoOrNot);

        JLabel cencelButton = new JLabel("\u274C"); 
        //Rectangle spicyLevelBounds = spicyLevel.getBounds();
        Rectangle toGoOrNotBounds = toGoOrNot.getBounds();
        cencelButton.setBounds(toGoOrNotBounds.x ,toGoOrNotBounds.y + toGoOrNotBounds.height + 15,65, 65);
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
        mainFrame.add(cencelButton);

        this.confirmButton = new JLabel("0(0)"); 
        this.confirmButton.setBounds(toGoOrNotBounds.x + 70 ,toGoOrNotBounds.y + toGoOrNotBounds.height + 15,toGoOrNotBounds.width - 70, 65);
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
    
                        int number = selection.amount;
                            if( number > 0 ) {
                                toPrint = true;
                                break;
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
        
        mainFrame.add(confirmButton);
        /*
        


        
        this.sisBroTradeName = new SisBroTradeName();
        sisBroTradeName.setBounds(tableBounds.x,tableBounds.y + tableBounds.height + 5,tableBounds.width, 45);
        demo.add(sisBroTradeName);


        demo.setResizable(false);
        demo.setLocationRelativeTo(null);
        demo.setVisible(true);

        
        itemPanels.get(0).setVisible(true);

        
        */
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
            this.toGoOrNot.set(0);
        }).start();
    }

    private List<Item> OpenCSV(String csvName) {
        List<Item> items = new ArrayList<Item>();

        try {
            String desktopFolder = System.getProperty("user.home") + "/Desktop";
            String csvFilePath = desktopFolder+"/"+csvName+".csv";
            InputStreamReader csvFile = new InputStreamReader(new FileInputStream(csvFilePath),"UTF-8");
            BufferedReader reader = new BufferedReader(csvFile);

            String catalogueString = reader.readLine().split(",")[0];

            Item item;
            String line = null;
            int lineItemCount = 0;
            while((line=reader.readLine())!=null){
                String namePrice[] = line.split(",");
                lineItemCount = namePrice.length;

                if (lineItemCount != 2) {
                    continue;
                }
                item = new Item();
                item.title = namePrice[0];
                item.price = Integer.parseInt(namePrice[1]);
                item.catalogue = catalogueString;
                items.add(item);
            }            
            reader.close();
            csvFile.close();

        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return items;
    }

    final private List<JPanel> ItemsToPanel(List<Item> items) {
        final int xGap = 8;
        final int yGap = 8;

        final int itemWidth = 320;
        final int itemHeight = 151;
        final int panelWidth = 995;
        final int panelHeight = 800;

        int x,y ;

        List<JPanel> panels = new ArrayList<JPanel>();

        JPanel aPanel = null;
        x = xGap ; y = panelHeight;
        for (Item item : items) {
            if ((y+itemHeight) > panelHeight) {
                aPanel = new JPanel();
                aPanel.setLayout(null);
                aPanel.setBounds(5, 55, 995, 800);
                aPanel.setVisible(false);
                aPanel.setName(item.catalogue);
                panels.add(aPanel);
                y = yGap;
                x = xGap;
            }
            CustomerSelection selection = new CustomerSelection(item.title, item.price ,item.catalogue);
            //itemCount++;
            selection.setBounds(x, y, itemWidth, itemHeight);
            selection.addPropertyChangeListener(this);
            //this.customerSelections.add(selection);
            aPanel.add(selection);

            x += itemWidth;
            x += xGap;

            if ( (x+itemWidth) > panelWidth ) {
                x = xGap;
                y += yGap;
                y += itemHeight;
            }
        }

        return panels;
    }

    private JTable CreateTable () {
        String[] columnNames = {"刪除", "品項 單價 數量", "小計"};
        JTable table;

        table = new JTable() {
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

        return table;
    }

    private void DoPrinting(Date date) {
        List<String> content = new ArrayList<>();
        int subTotal ,totalPrice ;
        String transation2Printer = "";
        List<String> catalogues = new ArrayList<String>();
        
        for(JPanel panel: itemPanels) {
            Component[] components = panel.getComponents();
            int componentNumber = components.length;
            for(int index = 0; index < componentNumber; index++) {
                CustomerSelection selection = (CustomerSelection)components[index];
                if( selection.amount > 0 ) {
                    if (catalogues.contains(selection.catalogue)) {
                        continue;
                    }
                    catalogues.add(selection.catalogue);
                }
            }
        }

        totalPrice = 0;
        OutputPrinter printer;
        for(String subCatalogue : catalogues) {
            content.clear();
            for(JPanel panel: itemPanels) {
                Component[] components = panel.getComponents();
                int componentNumber = components.length;
                for(int index = 0; index < componentNumber; index++) {
                    CustomerSelection selection = (CustomerSelection)components[index];
                    if(selection.catalogue != subCatalogue) {
                        continue;
                    }
                    if( selection.amount > 0 ) {   
                        subTotal = selection.amount * selection.price;
    
                        transation2Printer = selection.title+"("+String.valueOf(selection.price)+")"+",數量:"+String.valueOf(selection.amount)+",小計:"+String.valueOf(subTotal);
                        content.add(transation2Printer);
                        totalPrice += subTotal;
                    }
                }
            }
            if(content.size() < 1) {
                continue;
            }
            content.add("總價:"+ String.valueOf(totalPrice));
            content.add(this.toGoOrNot.get());

            printer = new OutputPrinter(subCatalogue, content ,date);
            printer.RawWriteToESC();
        }
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

        totalPrice = 0;
        for(JPanel panel: itemPanels) {
            Component[] components = panel.getComponents();
            int componentNumber = components.length;
            for(int index = 0; index < componentNumber; index++) {
                CustomerSelection selection = (CustomerSelection)components[index];
                number = selection.amount;
                if( number > 0 ) {
                    price = selection.price;
                    subTotal = number * price;

                    transation2Printer = selection.title+"("+String.valueOf(price)+")"+",數量:"+String.valueOf(number)+",小計:"+String.valueOf(subTotal);
                    content.add(transation2Printer);
                    totalPrice += subTotal;
                }
        }
        }
        content.add("總價:"+ String.valueOf(totalPrice));

        //content.add(this.spicyLevel.get());
        content.add(this.toGoOrNot.get());
        
        OutputPrinter printer = new OutputPrinter("柯黑鴨", content ,date);
        printer.RawWriteToESC();
        */
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
                    number = selection.amount;
                    if( number > 0 ) {
                        price = selection.price;
                        subTotal = number * price;

                        transation2csv = selection.title+"("+String.valueOf(price)+")"+","+String.valueOf(number)+","+String.valueOf(subTotal);
                        csvWriter.write(transation2csv+"\n");
                        //totalPrice += subTotal;
                    }
            }
            }
            //csvWriter.write(spicyLevel.get() +"\n");
            csvWriter.write(toGoOrNot.get() +"\n");

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

                int number = selection.amount;
                if( number > 0 ) {
                    data[0] = "\u274C";
                    data[1] = selection.title+"("+ String.valueOf(selection.price)+")"+" * "+String.valueOf(number);
                    data[2] = String.valueOf(selection.price*number);

                    tableModel.addRow(data);
                    selectedItems++;
                    totalMoney += (selection.price*number);
                }
            }

        }
        tableModel.fireTableDataChanged();
        String theString = String.valueOf(totalMoney)+"("+String.valueOf(selectedItems)+")";
        this.confirmButton.setText(theString);
    }
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Color[] colors = {new Color(53,0,0) ,new Color(0,0,53) ,new Color(0,53,0), new Color(102,102,102) ,Color.orange ,Color.yellow ,Color.green , Color.pink};

        if (evt.getPropertyName() == "TabSelectedIndex") {
            int tabIndex = (int)evt.getNewValue();

            for (JPanel panel : this.itemPanels) {
                panel.setBackground(colors[tabIndex]);
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
