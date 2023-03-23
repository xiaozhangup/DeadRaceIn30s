package me.xiaozhangup.race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

public class CountdownGUI extends JFrame {
    private final Timer timer;
    private final JLabel label;
    private int seconds;
    public static boolean showing = false;
    public static Date date = new Date();

    public CountdownGUI(int time) {
        showing = true;
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds--;
                label.setText("剩余 " + seconds + " 秒");
                if (seconds == 0) {
                    timer.stop();
                    label.setForeground(new Color(231, 130, 132));
                    label.setText("时间已到");
                }
            }
        });
        //TimerTask

        //Setup Start
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        setSize(width / 3, height / 5);
        setTitle(time + " 秒倒计时");
        label = new JLabel();
        label.setBackground(new Color(243, 231, 206));
        label.setOpaque(true);
        label.setForeground(new Color(239, 159, 118));
        label.setText("剩余 " + time + " 秒");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, getHeight() / 2));
        add(label);
        //Setup End


        //Close Start
        JButton closeButton = new JButton("单击关闭"); //创建关闭按钮
        closeButton.setBackground(new Color(243, 231, 206));
        closeButton.setSize(getWidth() / 5, getHeight() / 4);
        closeButton.setFont(new Font("Arial", Font.BOLD, getHeight() / 8));
        closeButton.setForeground(new Color(65, 69, 89));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> {
            timer.stop();
            showing = false;
            dispose();
        });

        JPanel panel = new JPanel();
        panel.setBackground(new Color(243, 231, 206));
        panel.add(closeButton);
        add(panel, BorderLayout.SOUTH);
        //Close END

        //Show UI
        setLocation(width / 2 - (getWidth() / 2), height / 2 - (getHeight() / 2));
        seconds = time;
        timer.start();
    }

    public static void main(String[] args) {
        java.util.Timer show = new java.util.Timer();
        show.schedule(new TimerTask() {
            @Override
            public void run() {
                showTimer(30);
//                if () {
//                    showTimer(30);
//                } else if () {
//                    showTimer(30);
//                } else if () {
//                    showTimer(30);
//                }
            }
        }, 0, 800);
    }

    private static void showTimer(int time) {
        if (showing) return;
        Thread thread = new Thread(() -> {

            CountdownGUI frame = new CountdownGUI(time);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setAlwaysOnTop(true);
            frame.setUndecorated(true);
            frame.setBackground(new Color(243, 231, 206));

            Shape shape = new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 64, 64);
            frame.setShape(shape);

            frame.setVisible(true);
        });

        thread.setName("Timer");
        thread.start();
    }
}