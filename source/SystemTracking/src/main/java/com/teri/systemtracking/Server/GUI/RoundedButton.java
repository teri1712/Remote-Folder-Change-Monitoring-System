/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author MinhTri
 */
public class RoundedButton extends JButton {

    private Color cl;
    private Color normal;
    private Color enter;
    private Color press;
    private boolean isActive;

    private PopupWindow popup;
    boolean isEnter;
    private JPanel info;

    public RoundedButton() {
        super();
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        this.setOpaque(false);
        normal = new Color(0, 0, 0, 0);
        enter = new Color(0, 0, 0, 15);
        press = new Color(0, 0, 0, 30);
        cl = normal;
        isActive = false;
        info = null;
        isEnter = false;
        popup = new PopupWindow();
        popup.setBaseLocation(this);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension sz = RoundedButton.this.getSize();
                int x = (int) (sz.getWidth() / 2 - popup.getPane().getWidth() / 2);
                int y = (int) (-popup.getPane().getHeight() - 5);
                popup.setBuffLocation(x, y);
                popup.updateLocation();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }

        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                cl = enter;
                isEnter = true;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            java.awt.EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    if (isEnter)
                                        popup.setVisible(true);
                                }
                            });
                        } catch (InterruptedException e) {
                        }
                    }

                }).start();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cl = normal;
                isEnter = false;
                popup.setVisible(false);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                cl = press;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                cl = enter;
                repaint();
            }
        });
    }

    public void setInfo(String infoContent) {
        info = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);

                g2.setColor(new Color(0, 0, 0, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        info.setOpaque(false);
        JLabel lb = new JLabel();
        lb.setFont(new java.awt.Font("Fira Code Medium", 0, 12)); // NOI18N
        lb.setText(" " + infoContent + " ");
        lb.setSize((int) lb.getPreferredSize().getWidth(), (int) lb.getPreferredSize().getHeight() + 10);
        info.setSize(lb.getSize());
        info.add(lb);

        popup.setPane(info);

    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (isActive) {
            g2.setColor(enter);
        } else {
            g2.setColor(cl);
        }
        g2.fillRoundRect(0, 1, getWidth(), getHeight() - 2, 10, 10);
        g2.dispose();
        super.paintComponent(g);
    }
}
