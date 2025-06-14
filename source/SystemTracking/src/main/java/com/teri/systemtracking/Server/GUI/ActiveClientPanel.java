/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import com.jhlabs.image.GaussianFilter;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.Server.presenter.ActiveClientPresenter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * @author MinhTri
 */
public class ActiveClientPanel extends javax.swing.JPanel {

      /**
       * Creates new form ActiveUserEntityPanel
       */
      private BufferedImage vcl;
      // Variables declaration - do not modify//GEN-BEGIN:variables
      private com.teri.systemtracking.Server.GUI.DateRegistryPanel connectedDatePanel;
      private com.teri.systemtracking.Server.GUI.OvalButton disconnectButton;
      private com.teri.systemtracking.Server.GUI.OvalButton editButton;
      private javax.swing.Box.Filler filler1;
      private javax.swing.Box.Filler filler2;
      private javax.swing.Box.Filler filler3;
      private javax.swing.Box.Filler filler4;
      private javax.swing.Box.Filler filler5;
      private javax.swing.Box.Filler filler6;
      private javax.swing.Box.Filler filler7;
      private javax.swing.Box.Filler filler9;
      private javax.swing.JLabel id;
      private javax.swing.JLabel jLabel1;
      private javax.swing.JPanel jPanel1;
      private javax.swing.JPanel jPanel2;
      private javax.swing.JPanel jPanel3;
      private javax.swing.JPanel jPanel4;
      private javax.swing.JTextArea trackingFile;

      private ActiveClientPresenter presenter;

      public ActiveClientPanel() {
            initComponents();
            BufferedImage mask = new BufferedImage((int) getPreferredSize().getWidth(),
                  (int) getPreferredSize().getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D m = mask.createGraphics();
            applyQualityProperties(m);

            m.setColor(Color.BLACK);
            m.fillRoundRect(10, 10, (int) getPreferredSize().getWidth() - 20, (int) getPreferredSize().getHeight() - 20, 15,
                  15);

            m.dispose();

            vcl = new BufferedImage((int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight(),
                  BufferedImage.TYPE_INT_ARGB);

            Graphics2D cc = vcl.createGraphics();
            applyQualityProperties(cc);

            cc.drawImage(mask, 0, 0, null);

            cc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.05f));

            cc.setColor(Color.BLACK);
            cc.fillRoundRect(10, 10, (int) getPreferredSize().getWidth() - 20, (int) getPreferredSize().getHeight() - 20,
                  15, 15);
            cc.dispose();

            GaussianFilter gg = new GaussianFilter(10);
            vcl = gg.filter(vcl, null);
            vcl = gg.filter(vcl, null);
            vcl = gg.filter(vcl, null);

            initActions();
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

      private void initActions() {

            editButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        if (presenter != null)
                              presenter.navigateToClient();
                  }
            });

            disconnectButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        if (presenter != null)
                              presenter.deleteSelf();
                  }
            });
      }

      @Override
      public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            applyQualityProperties(g2);

            g2.drawImage(vcl, 0, 0, null);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 15, 15);
            g2.dispose();
            super.paintComponent(g);
      }

      public void setPresenter(final ActiveClientPresenter presenter) {
            this.presenter = presenter;
      }

      public void initView(Client client) {
            connectedDatePanel.setDate(client.getCreatedDate());
            this.id.setText("Client#" + client.getId());
      }

      public void setTrackingFile(String file) {
            trackingFile.setText(file);
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
      // Code">//GEN-BEGIN:initComponents
      private void initComponents() {

            filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 10), new java.awt.Dimension(25, 10),
                  new java.awt.Dimension(25, 10));
            jPanel1 = new javax.swing.JPanel();
            filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            jLabel1 = new javax.swing.JLabel();
            filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(10, 25),
                  new java.awt.Dimension(10, 25));
            id = new javax.swing.JLabel();
            filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            jPanel2 = new javax.swing.JPanel();
            filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            connectedDatePanel = new com.teri.systemtracking.Server.GUI.DateRegistryPanel();
            jPanel3 = new javax.swing.JPanel();
            filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            trackingFile = new javax.swing.JTextArea();
            filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 0), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            jPanel4 = new javax.swing.JPanel();
            filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(25, 25), new java.awt.Dimension(25, 25),
                  new java.awt.Dimension(25, 25));
            disconnectButton = new com.teri.systemtracking.Server.GUI.OvalButton();
            editButton = new com.teri.systemtracking.Server.GUI.OvalButton();

            setAlignmentX(0.0F);
            setMaximumSize(new java.awt.Dimension(250, 233));
            setMinimumSize(new java.awt.Dimension(250, 233));
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(250, 233));
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

            filler9.setAlignmentX(0.0F);
            add(filler9);

            jPanel1.setAlignmentX(0.0F);
            jPanel1.setMaximumSize(new java.awt.Dimension(285, 60));
            jPanel1.setMinimumSize(new java.awt.Dimension(285, 60));
            jPanel1.setOpaque(false);
            jPanel1.setPreferredSize(new java.awt.Dimension(285, 60));
            jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

            filler1.setAlignmentX(0.0F);
            jPanel1.add(filler1);

            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user.png"))); // NOI18N
            jPanel1.add(jLabel1);

            filler2.setAlignmentX(0.0F);
            jPanel1.add(filler2);

            id.setFont(new java.awt.Font("Fira Code Medium", 0, 20)); // NOI18N
            id.setText("User#abcd1234");
            id.setToolTipText("");
            id.setMaximumSize(new java.awt.Dimension(182, 40));
            id.setMinimumSize(new java.awt.Dimension(0, 40));
            id.setPreferredSize(new java.awt.Dimension(182, 40));
            jPanel1.add(id);

            filler4.setAlignmentX(0.0F);
            jPanel1.add(filler4);

            add(jPanel1);

            jPanel2.setAlignmentX(0.0F);
            jPanel2.setMaximumSize(new java.awt.Dimension(285, 60));
            jPanel2.setMinimumSize(new java.awt.Dimension(285, 40));
            jPanel2.setOpaque(false);
            jPanel2.setPreferredSize(new java.awt.Dimension(285, 40));
            jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

            filler3.setAlignmentX(0.0F);
            jPanel2.add(filler3);
            jPanel2.add(connectedDatePanel);

            add(jPanel2);

            jPanel3.setAlignmentX(0.0F);
            jPanel3.setMaximumSize(new java.awt.Dimension(285, 50));
            jPanel3.setMinimumSize(new java.awt.Dimension(285, 40));
            jPanel3.setOpaque(false);
            jPanel3.setPreferredSize(new java.awt.Dimension(285, 40));
            jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

            filler5.setAlignmentX(0.0F);
            jPanel3.add(filler5);

            trackingFile.setEditable(false);
            trackingFile.setColumns(20);
            trackingFile.setFont(new java.awt.Font("Segoe UI Variable", 0, 15)); // NOI18N
            trackingFile.setLineWrap(true);
            trackingFile.setRows(5);
            trackingFile.setText("tracking : none");
            trackingFile.setWrapStyleWord(true);
            trackingFile.setBorder(null);
            trackingFile.setMaximumSize(new java.awt.Dimension(230, 2147483647));
            trackingFile.setMinimumSize(new java.awt.Dimension(0, 18));
            trackingFile.setOpaque(false);
            jPanel3.add(trackingFile);

            filler7.setAlignmentX(0.0F);
            jPanel3.add(filler7);

            add(jPanel3);

            jPanel4.setAlignmentX(0.0F);
            jPanel4.setMaximumSize(new java.awt.Dimension(285, 35));
            jPanel4.setMinimumSize(new java.awt.Dimension(285, 35));
            jPanel4.setOpaque(false);
            jPanel4.setPreferredSize(new java.awt.Dimension(285, 35));
            jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.LINE_AXIS));

            filler6.setAlignmentX(0.0F);
            jPanel4.add(filler6);

            disconnectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bin.png"))); // NOI18N
            disconnectButton.setInfo("diconnect");
            disconnectButton.setMaximumSize(new java.awt.Dimension(35, 35));
            disconnectButton.setMinimumSize(new java.awt.Dimension(35, 35));
            disconnectButton.setPreferredSize(new java.awt.Dimension(35, 35));
            jPanel4.add(disconnectButton);

            editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.png"))); // NOI18N
            editButton.setInfo("show client session");
            editButton.setMaximumSize(new java.awt.Dimension(35, 35));
            editButton.setMinimumSize(new java.awt.Dimension(35, 35));
            editButton.setPreferredSize(new java.awt.Dimension(35, 35));
            jPanel4.add(editButton);

            add(jPanel4);
      }// </editor-fold>//GEN-END:initComponents
      // End of variables declaration//GEN-END:variables
}
