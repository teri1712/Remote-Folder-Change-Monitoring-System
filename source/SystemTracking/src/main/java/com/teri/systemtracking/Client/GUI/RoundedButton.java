/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teri.systemtracking.Client.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author MinhTri
 */
public class RoundedButton extends JButton {

      private Color cl;
      private Color normal;
      private Color enter;
      private Color press;

      public RoundedButton() {
            super();
            this.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            this.setOpaque(false);
            this.setFocusPainted(false);
            this.setBorderPainted(false);
            this.setBorder(null);
            this.setContentAreaFilled(false);
            normal = new Color(255, 255, 255, 100);
            enter = new Color(255, 255, 255, 150);
            press = new Color(255, 255, 255, 200);
            cl = normal;

            addMouseListener(new MouseAdapter() {
                  @Override
                  public void mouseEntered(MouseEvent e) {
                        cl = enter;
                        repaint();
                  }

                  @Override
                  public void mouseExited(MouseEvent e) {
                        cl = normal;
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

      @Override
      public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cl);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
      }
}
