package me.xiaozhangup.race;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CountdownGUI extends JFrame {
    public static final Color CLOSE_BUTTON_TEXT_COLOR = new Color(246, 236, 217);
    public static final Color CLOSE_BACKGROUND = new Color(235, 164, 157);
    public static final Color BACKGROUND = new Color(243, 231, 206);
    public static final Color FONT_COLOR = new Color(239, 159, 118);
    public static final Color TIME_DONE_COLOR = new Color(231, 130, 132);
    public static Font font;
    public static Image img = null;
    private final Timer timer;
    private final JLabel label;
    private int seconds;
    private int mouseX;
    private int mouseY;

    public CountdownGUI(int time) {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds--;
                label.setText("剩余 " + seconds + " 秒");
                if (seconds == 0) {
                    timer.stop();
                    label.setForeground(TIME_DONE_COLOR);
                    label.setText("时间已到");
                }
            }
        });
        //TimerTask

        //Setup Start
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        setSize((int) (width / 2.5), height / 5);
        setTitle(time + " 秒倒计时");


        label = new JLabel();

        label.setBackground(BACKGROUND);
        label.setOpaque(true);
        label.setForeground(FONT_COLOR);
        label.setText("剩余 " + time + " 秒");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(font.deriveFont(Font.BOLD, getHeight() >> 1));
        add(label);
        //Setup End


        //Close Start
        JButton closeButton = new JButton("单击关闭计时器");
        closeButton.setSize(getWidth() / 5, getHeight() / 4);
        closeButton.setFont(font.deriveFont(Font.BOLD, getHeight() >> 3));
        closeButton.setForeground(CLOSE_BUTTON_TEXT_COLOR);
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> {
            timer.stop();
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setBackground(CLOSE_BACKGROUND);
        panel.add(closeButton);
        add(panel, BorderLayout.SOUTH);
        //Close END

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        // add a mouse motion listener to update the frame location when dragged
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // get the current position of the mouse on the screen
                int screenX = e.getXOnScreen();
                int screenY = e.getYOnScreen();

                // calculate the new position of the frame
                int frameX = screenX - mouseX;
                int frameY = screenY - mouseY;

                // set the frame location
                setLocation(frameX, frameY);
            }
        });
        //Mouse move

        //Show UI
        setLocation(width / 2 - (getWidth() / 2), height / 2 - (getHeight() / 2));
        seconds = time;
        timer.start();
    }

    public static void main(String[] args) throws ParseException {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        //Jar resources load
        URL url = CountdownGUI.class.getResource("/icon.png");
        URL fontURL = CountdownGUI.class.getResource("/SourceHanSansSC-Normal-2.otf");
        if (url == null || fontURL == null) {
            System.exit(0);
            return;
        }
        try {
            InputStream fontStream = fontURL.openStream();
            InputStream input = url.openStream();
            img = ImageIO.read(input);
            font = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            font = font.deriveFont(Font.BOLD);
            input.close();
            fontStream.close();
        } catch (IOException | FontFormatException ignored) {
        }

        setUpTask(12, 39, 0);
        //Add more at there

        if (args.length > 2) {
            setUpTask(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        //Just for some test
    }

    private static void setUpTask(int a, int b, int c) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, a, b, c);//设置要执行的日期时间
        Date defaultdate = calendar.getTime();
        java.util.Timer dTimer = new java.util.Timer();
        dTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                showTimer(30);
            }
        }, defaultdate , 24* 60* 60 * 1000);//24* 60* 60 * 1000
    }

    private static void showTimer(int time) {
        Thread thread = new Thread(() -> {
            CountdownGUI frame = new CountdownGUI(time);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setBackground(BACKGROUND);

            Shape shape = new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 32, 32);
            frame.setShape(shape);

            frame.setIconImage(img);
            frame.setCursor(new Cursor(Cursor.MOVE_CURSOR));

            //Show the frame
            frame.setVisible(true);
        });

        thread.start();
    }
}