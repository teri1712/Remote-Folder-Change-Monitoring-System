/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import com.jhlabs.image.GaussianFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.image.BufferedImage;

/**
 * @author MinhTri
 */
public class SearchBar extends javax.swing.JPanel {

      private Dimension round;
      private boolean isEmpty;
      private String thumbnail;
      private int sz;
      // Variables declaration - do not modify//GEN-BEGIN:variables
      private javax.swing.Box.Filler filler1;
      private javax.swing.Box.Filler filler2;
      private javax.swing.Box.Filler filler3;
      private javax.swing.Box.Filler filler4;
      private javax.swing.Box.Filler filler5;
      private javax.swing.JPanel jPanel1;
      private javax.swing.JTextField textField;
      private com.teri.systemtracking.Server.GUI.OvalButton ovalButton1;

      public SearchBar() {
            initComponents();
            textField.setOpaque(false);
            isEmpty = true;
            sz = 16;
            textField.setForeground(new Color(150, 150, 150));
            textField.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
            textField.addFocusListener(new FocusListener() {
                  @Override
                  public void focusGained(FocusEvent e) {
                        if (isEmpty) {
                              textField.setText("");
                              textField.setForeground(Color.BLACK);
                              textField.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
                        }
                  }

                  @Override
                  public void focusLost(FocusEvent e) {
                        isEmpty = textField.getText().equals("");
                        if (isEmpty) {
                              textField.setForeground(new Color(100, 100, 100));
                              textField.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
                              textField.setText(thumbnail);
                        }
                  }

            });
      }

      public static void applyQualityProperties(Graphics2D g2) {
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
      }


      @Override
      public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            applyQualityProperties(g2);

            BufferedImage mask = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D m = mask.createGraphics();
            applyQualityProperties(m);

            m.setColor(Color.BLACK);
            m.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 8, 8);

            m.dispose();

            BufferedImage vcl = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D cc = vcl.createGraphics();
            applyQualityProperties(cc);

            cc.drawImage(mask, 0, 0, null);

            cc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.6f));

            cc.setColor(Color.BLACK);
            cc.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 8, 8);
            cc.dispose();

            GaussianFilter gg = new GaussianFilter(5);
            vcl = gg.filter(vcl, null);

            g2.drawImage(vcl, 0, 0, null);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight()
                        - 6, (round == null) ? 8 : (int) round.getWidth(),
                  (round == null) ? 8 : (int) round.getHeight());
            g2.dispose();
            super.paintComponent(g);

      }

      protected Dimension getRound() {
            return round;
      }

      protected void setRoundX(int w) {
            if (round == null) {
                  round = new Dimension(8, 8);
            }
            round.setSize(w, (int) round.getHeight());
            repaint();
      }

      protected void setRoundY(int h) {
            if (round == null) {
                  round = new Dimension(8, 8);
            }
            round.setSize((int) round.getWidth(), h);
            repaint();
      }

      public void setFontSize(int sz) {
            this.sz = sz;
            textField.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
      }

      public void setText(String val) {
            textField.setText(val);
            thumbnail = val;
      }

      public JTextField getTextField() {
            return textField;
      }

      public void addListener(ActionListener actionListener) {
            textField.addActionListener(actionListener);
            ovalButton1.addActionListener(actionListener);
      }

      /**
       * This method is called from within the constructor to initialize the form.
       * WARNING: Do NOT modify this code. The content of this method is always
       * regenerated by the Form Editor.
       */
      @SuppressWarnings("unchecked")
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated
      // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
      private void initComponents() {

            filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20));
            filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20));
            jPanel1 = new javax.swing.JPanel();
            filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 3), new java.awt.Dimension(5, 3), new java.awt.Dimension(5, 3));
            textField = new javax.swing.JTextField();
            filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 5), new java.awt.Dimension(5, 5), new java.awt.Dimension(500, 5));
            ovalButton1 = new com.teri.systemtracking.Server.GUI.OvalButton();
            filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 20));

            setMaximumSize(new java.awt.Dimension(307, 40));
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(307, 40));
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));
            add(filler1);
            add(filler2);

            jPanel1.setMaximumSize(new java.awt.Dimension(30000, 30000));
            jPanel1.setOpaque(false);
            jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
            jPanel1.add(filler4);

            textField.setFont(new java.awt.Font("Segoe UI Variable", 0, 16)); // NOI18N
            textField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
            textField.setText("type here");
            textField.setBorder(null);
            jPanel1.add(textField);
            jPanel1.add(filler5);

            add(jPanel1);

            ovalButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
            ovalButton1.setInfo("search (Enter)");
            ovalButton1.setMaximumSize(new java.awt.Dimension(30, 30));
            ovalButton1.setMinimumSize(new java.awt.Dimension(30, 30));
            ovalButton1.setPreferredSize(new java.awt.Dimension(30, 30));
            add(ovalButton1);
            add(filler3);
      }// </editor-fold>//GEN-END:initComponents
      // End of variables declaration//GEN-END:variables
}
