package component;

import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
//import javax.swing.JPanel;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SisBroTradeName extends JLabel implements ComponentListener {
    private static final long serialVersionUID = 0x2433L;
    private int spicyLevel = 0;
    String levels[] = {"柯黑壓","柯大姊"};

    public SisBroTradeName() {
        this.setLayout(null);
        final Font font = new Font("Serif", Font.PLAIN, 29);
        final Border borderLine = BorderFactory.createLineBorder(Color.darkGray);
        setFont(font);
        setBorder(borderLine);

        this.addComponentListener(this);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                spicyLevel++;
                if (spicyLevel >= levels.length) {
                    spicyLevel = 0;
                }
                setText(levels[spicyLevel]);
            }
        });
    }

    public SisBroTradeName(String input[]) {
        this.levels = input;
        this.setLayout(null);
        final Font font = new Font("Serif", Font.PLAIN, 29);
        final Border blueline = BorderFactory.createLineBorder(Color.cyan);
        setFont(font);
        setBorder(blueline);

        this.addComponentListener(this);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                spicyLevel++;
                if (spicyLevel >= levels.length) {
                    spicyLevel = 0;
                }
                setText(levels[spicyLevel]);
            }
        });
    }

    public void componentResized(final ComponentEvent e) {
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setText(this.levels[this.spicyLevel]);
    }
    
    /*
    public void componentResized(final ComponentEvent e) {
        // Perform calculation here
        // System.out.println("componentResized");
        final int width = getWidth();
        final int height = getHeight();
        final JLabel lLabel = new JLabel("\u25C0");// +"\t" + levels[0]+ "\t" + "\u25B6");
        final Font labelFont = new Font("Serif", Font.PLAIN, 29);
        final JLabel dummyLabel = new JLabel("我");
        final int charWidth = dummyLabel.getFontMetrics(labelFont).stringWidth(dummyLabel.getText());
        final int charHeight = dummyLabel.getFontMetrics(labelFont).getHeight();

        dummyLabel.setFont(labelFont);
        lLabel.setFont(labelFont);

        final Border greenline = BorderFactory.createLineBorder(Color.green);
        lLabel.setBorder(greenline);
        lLabel.setSize(charWidth+5, charHeight);
        lLabel.setLocation(2, 4);
        lLabel.setHorizontalAlignment(JLabel.CENTER);
        //rightArrow.setBounds( width - buttonWidth, 0, buttonWidth, height);
        this.add(lLabel);
        lLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                spicyLevel--;
                if (spicyLevel < 0) {
                    spicyLevel = levels.length - 1;
                }
                levelLabel.setText(levels[spicyLevel]);
            }
        });

        final JLabel rLabel = new JLabel("\u25B6");
        rLabel.setFont(labelFont);
        rLabel.setBorder(greenline);
        rLabel.setSize(charWidth+5, charHeight);
        rLabel.setLocation(width - charWidth - 8, 4);
        this.add(rLabel);
        rLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                spicyLevel++;
                if (spicyLevel >= levels.length) {
                    spicyLevel = 0;
                }
                levelLabel.setText(levels[spicyLevel]);
            }
        });

        //this.levelLabel = new JLabel(this.levels[0]);
        levelLabel.setFont(labelFont);
        levelLabel.setSize(width - lLabel.getWidth() - rLabel.getWidth() - 8, height);
        levelLabel.setLocation(lLabel.getLocation().x+lLabel.getWidth()+2, 2);
        //final Border blueline = BorderFactory.createLineBorder(Color.blue);
        //levelLabel.setBorder(blueline);
        levelLabel.setHorizontalAlignment(JLabel.CENTER);

        this.add(levelLabel);
    }
*/
    public void componentHidden(final ComponentEvent ce) {
    };

    public void componentShown(final ComponentEvent ce) {
        System.out.println("componentShown");
    };

    public void componentMoved(final ComponentEvent ce) {
        System.out.println("componentMoved");
    };

    public String get() {
        return levels[spicyLevel];
    }

    public void set(int level) {
        this.spicyLevel = level;
        setText(levels[spicyLevel]);
    }
}