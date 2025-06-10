/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author MinhTri
 */
public class OvalButton extends JButton {

      private Color cl;
      private Color normal;
      private Color enter;
      private Color press;
      private volatile boolean isEnter;
      private JPanel info;
      private PopupWindow popup;

      public OvalButton() {
            super();
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            this.setOpaque(false);
            this.setFocusPainted(false);
            this.setBorderPainted(false);
            this.setBorder(null);

            this.setContentAreaFilled(false);
            normal = new Color(0, 0, 0, 0);
            enter = new Color(0, 0, 0, 15);
            press = new Color(0, 0, 0, 30);
            cl = normal;
            isEnter = false;
            info = null;
            popup = new PopupWindow();
            popup.setBaseLocation(this);
            addAncestorListener(new AncestorListener() {

                  @Override
                  public void ancestorAdded(AncestorEvent event) {
                  }

                  @Override
                  public void ancestorRemoved(AncestorEvent event) {
                        popup.setVisible(false);
                        isEnter = false;
                        cl = normal;
                        repaint();
                  }

                  @Override
                  public void ancestorMoved(AncestorEvent event) {
                  }

            });
            addComponentListener(new ComponentListener() {
                  @Override
                  public void componentResized(ComponentEvent e) {
                        Dimension sz = OvalButton.this.getSize();
                        int x = (int) (sz.getWidth() / 2 - popup.getPane().getWidth() / 2);
                        int y = (int) (-popup.getPane().getHeight() - 10);
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
                        popup.setVisible(false);
                        isEnter = false;
                        cl = normal;
                        repaint();
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
                                                      if (isEnter) {
                                                            popup.setVisible(true);
                                                      }
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

                        g2.setColor(new Color(0, 0, 0, 25));
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

      @Override
      public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cl);
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.dispose();
            super.paintComponent(g);
      }
}
