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
            escCommands.write("---------------".getBytes("Big5"));

            escCommands.write(0x1d);
            escCommands.write('!');//Double off
            escCommands.write(0);
            escCommands.write('\n');

            for(String item : this.content) {
                escCommands.write(item.getBytes("Big5"));
            }

            escCommands.write("---------------".getBytes("Big5"));
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
            g2d.drawString(this.title, x, y);

            return PAGE_EXISTS;
        }

        return NO_SUCH_PAGE;
    }
}

