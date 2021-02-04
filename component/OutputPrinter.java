package component;

import java.awt.Font;
import java.awt.FontMetrics;
import java.text.SimpleDateFormat;  
import java.util.Date;  

import java.awt.print.Printable;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.ArrayList;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
//import java.io.File;

import java.io.ByteArrayOutputStream;

public class OutputPrinter implements Printable {
    private static final long serialVersionUID = 0x7645L;

    private Date date;
    private List<String> titles = new ArrayList<String>();
    private List<List<CustomerSelection>> arrayOfContent = new ArrayList<>();
    private String postscript = "";
    private String stayOrToGo = "";
    private int catalogueCounter = 0;
    
    public OutputPrinter(Date date) {
        this.date = date;
    }

    public void addContent(String catalogue ,List<CustomerSelection> content) {
        this.titles.add(catalogue);
        
        this.arrayOfContent.add(new ArrayList<CustomerSelection>());

        for(CustomerSelection selection : content) {
            this.arrayOfContent.get(catalogueCounter).add(selection);
        }
        catalogueCounter++;
    }

    public void setStayOrToGo(String stayOrToGoString) {
        this.stayOrToGo = stayOrToGoString;
    }

    public void setPostscript(String postscriptString) {
        this.postscript = postscriptString;
    }
    public void RawWriteToESC () {
        Path printerPath = Paths.get("/dev/usb/lp0");
        ByteArrayOutputStream escCommands = new ByteArrayOutputStream();
        try {
            escCommands.write(0x1b);
            escCommands.write('@'); // init
            escCommands.write('\n');
    
            escCommands.write(0x1b);
            escCommands.write('C');//JIS
            escCommands.write(0);
            escCommands.write('\n');
    
            escCommands.write(0x1c);
            escCommands.write('&');//kanji on
            escCommands.write('\n');

            int totalPrice;
            totalPrice = 0;
            String title;
            List<CustomerSelection> content = new ArrayList<CustomerSelection>();
            for(int index = 0; index < this.titles.size() ;index++) {
                content.clear();
                title = this.titles.get(index);
                content.addAll(arrayOfContent.get(index));

                escCommands.write(0x1d);
                escCommands.write('!');//Double on
                escCommands.write(0x11);
                escCommands.write('\n');
    
                escCommands.write(title.getBytes("Big5"));
                escCommands.write("\n".getBytes("Big5"));
    
                escCommands.write(0x1d);
                escCommands.write('!');//Double off
                escCommands.write(0);
                escCommands.write('\n');

                escCommands.write("品項\t\t\t數量\t\t金額\n".getBytes("Big5"));
                //escCommands.write("品項                      數量              金額\n".getBytes("Big5"));
                escCommands.write("------------------------------------------------\n".getBytes("Big5"));

                for(CustomerSelection selection : content) {
                    String transation2Printer;
                    int subTotal;

                    subTotal = selection.amount * selection.price;
                    totalPrice += subTotal;

                    //transation2Printer = selection.title+"("+String.valueOf(selection.price)+")"+",數量: "+String.valueOf(selection.amount)+",小計: "+String.valueOf(subTotal);
                    switch(selection.title.length()) {
                        case 6:
                        case 7:
                            transation2Printer = selection.title+"\t\t"+String.valueOf(selection.amount)+"\t\t"+String.valueOf(subTotal);
                            break;
                        case 4:
                        case 5:
                            transation2Printer = selection.title+"\t\t"+String.valueOf(selection.amount)+"\t\t"+String.valueOf(subTotal);
                            break;
                        default:
                            transation2Printer = selection.title+"\t\t\t"+String.valueOf(selection.amount)+"\t\t"+String.valueOf(subTotal);
                            break;
                    }

                    escCommands.write(transation2Printer.getBytes("Big5"));
                    escCommands.write("\n".getBytes("Big5"));
                }
                escCommands.write("------------------------------------------------\n".getBytes("Big5"));
            }

            String totalPriceString = "總價\t\t\t\t\t"+ String.valueOf(totalPrice)+"\n";
            escCommands.write(totalPriceString.getBytes("Big5"));
            escCommands.write(this.stayOrToGo.getBytes("Big5"));
            escCommands.write("\n".getBytes("Big5"));
            escCommands.write("------------------------------------------------\n".getBytes("Big5"));
            String[] postscriptArray = this.postscript.split("\n");
            if (postscriptArray.length > 0) {
                for(String aString : postscriptArray) {
                    if (aString.length() > 0) {
                        escCommands.write(aString.getBytes("Big5"));
                        escCommands.write("\n".getBytes("Big5"));
                    }
                }   
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss"); 
            String dateTimeString = formatter.format(date);
            escCommands.write(dateTimeString.getBytes("Big5"));
            escCommands.write("\n".getBytes("Big5"));
            escCommands.write("謝謝光臨\n歡迎再來".getBytes("Big5"));

            escCommands.write(0x1c);
            escCommands.write('.');//kanji off
            escCommands.write('\n');
    
            escCommands.write(27);
            escCommands.write('d');
            escCommands.write(5); // 5 lines forward
            escCommands.write('\n');
    
            escCommands.write(0x1b);
            escCommands.write(0x69);//cut paper
            escCommands.write('\n');
   
            escCommands.close();

            Files.write(printerPath ,escCommands.toByteArray());
        }
        catch (Exception e) {

        }
        finally {
        }

    }
/*
    private String title = "";
    private List<String> content = new ArrayList<>();
    private Date date;

    public OutputPrinter(String title, List<String> content, Date date) {
        this.title = title;
        this.content.addAll(content);
        this.date = date;
    }

    public void RawWriteToESC () {
        Path printerPath = Paths.get("/dev/usb/lp0");
        ByteArrayOutputStream escCommands = new ByteArrayOutputStream();

        try {
            escCommands.write(0x1b);
            escCommands.write('@'); // init
            escCommands.write('\n');
    
            escCommands.write(0x1b);
            escCommands.write('C');//JIS
            escCommands.write(0);
            escCommands.write('\n');
    
            escCommands.write(0x1c);
            escCommands.write('&');//kanji on
            escCommands.write('\n');

            escCommands.write(0x1d);
            escCommands.write('!');//Double on
            escCommands.write(0x11);
            escCommands.write('\n');

            escCommands.write(this.title.getBytes("Big5"));
            escCommands.write("\n".getBytes("Big5"));
            escCommands.write("------------------------".getBytes("Big5"));

            escCommands.write(0x1d);
            escCommands.write('!');//Double off
            escCommands.write(0);
            escCommands.write('\n');

            for(String item : this.content) {
                escCommands.write(item.getBytes("Big5"));
                escCommands.write("\n".getBytes("Big5"));
            }

            escCommands.write("**************************\n".getBytes("Big5"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm分ss秒"); 
            String dateTimeString = formatter.format(date);
            escCommands.write(dateTimeString.getBytes("Big5"));

            escCommands.write(0x1c);
            escCommands.write('.');//kanji off
            escCommands.write('\n');
    
            escCommands.write(27);
            escCommands.write('d');
            escCommands.write(5); // 5 lines forward
            escCommands.write('\n');
    
            escCommands.write(0x1b);
            escCommands.write(0x69);//cut paper
            escCommands.write('\n');
   
            escCommands.close();

            Files.write(printerPath ,escCommands.toByteArray());
        }
        catch (Exception e) {

        }
        finally {
        }
    }
*/
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
        FontMetrics metrics;
        Graphics2D g2d = (Graphics2D)g;
        int x = (int) pf.getImageableX();
        int y = (int) pf.getImageableY();        
        g2d.translate(x, y); 
        
        if(page == 0) {
            Font titleFont = new Font("Serif", Font.PLAIN, 24);
            g2d.setFont(titleFont);
            metrics = g.getFontMetrics(titleFont);
            int lineHeight = metrics.getHeight();

            y += lineHeight;
            //g2d.drawString(this.title, x, y);

            return PAGE_EXISTS;
        }

        return NO_SUCH_PAGE;
    }
}

