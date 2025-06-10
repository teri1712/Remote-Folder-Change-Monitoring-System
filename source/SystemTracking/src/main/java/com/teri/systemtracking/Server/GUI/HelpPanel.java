/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import com.jhlabs.image.GaussianFilter;

import javax.sound.sampled.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * @author MinhTri
 */
public class HelpPanel extends javax.swing.JPanel {

      /**
       * Creates new form NotifyPanel
       */
      private static HelpPanel helpPanel;

      public static void showHelp() {
            helpPanel.getPopupWindow().setVisible(true);
      }

      public static void setHelp(HelpPanel helpPanel) {
            HelpPanel.helpPanel = helpPanel;
      }

      private PopupWindow popup;
      private volatile int cntNewConnection = 0;
      private volatile int cntClosedConnection = 0;
      private Object cc = new Object();

      public HelpPanel() {
            initComponents();
            ovalButton1.setInfo("close");
            ovalButton1.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        popup.setVisible(false);
                  }
            });
            popup = new PopupWindow();
            new Thread(new Runnable() {
                  @Override
                  public void run() {
                        while (true) {

                              int x = 0, y = 0;
                              boolean con = false;
                              synchronized (cc) {
                                    if (cntNewConnection == 0 && cntClosedConnection == 0) {
                                          try {
                                                cc.wait();
                                                con = true;
                                          } catch (InterruptedException e) {
                                                e.printStackTrace();
                                          }
                                    } else {
                                          x = cntNewConnection;
                                          cntNewConnection = 0;
                                          y = cntClosedConnection;
                                          cntClosedConnection = 0;
                                    }
                              }
                              if (con) {
                                    continue;
                              }
                              final int xx = x;
                              final int yy = y;
                              java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                          jLabel2.setText(Integer.toString(xx) + " has been connected");
                                          jLabel3.setText(Integer.toString(yy) + " has been closed");
                                          popup.setVisible(true);
                                    }
                              });
                              try {
                                    AudioInputStream audioStream = AudioSystem
                                          .getAudioInputStream(getClass().getResource("/sound-effect.wav"));

                                    final Clip audioClip = (Clip) AudioSystem.getClip();

                                    audioClip.open(audioStream);

                                    audioClip.start();

                                    audioClip.addLineListener(new LineListener() {

                                          @Override
                                          public void update(LineEvent event) {
                                                if (event.getType() == LineEvent.Type.STOP) {
                                                      audioClip.close();
                                                }
                                          }

                                    });

                              } catch (Exception ex) {
                                    ex.printStackTrace();
                              }
                              try {
                                    Thread.sleep(3000);
                              } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                              }

                              for (int i = 9; i >= 0; i--) {
                                    try {
                                          Thread.sleep(100);
                                          final float o = (float) i / 10;
                                          java.awt.EventQueue.invokeLater(new Runnable() {
                                                public void run() {
                                                      popup.setOpacity(o);
                                                }
                                          });
                                    } catch (InterruptedException e) {
                                    }
                              }
                              java.awt.EventQueue.invokeLater(new Runnable() {
                                    public void run() {
                                          popup.setVisible(false);
                                    }
                              });
                        }
                  }

            }).start();

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

      public PopupWindow getPopupWindow() {
            return popup;
      }

      public void setAnnouncement(final int opened, final int closed) {
            new Thread(new Runnable() {

                  @Override
                  public void run() {
                        synchronized (cc) {
                              cntClosedConnection += closed;
                              cntNewConnection += opened;
                              cc.notify();
                        }
                  }

            }).start();
      }

      @Override
      public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            applyQualityProperties(g2);

            BufferedImage mask = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D m = mask.createGraphics();
            applyQualityProperties(m);

            m.setColor(Color.BLACK);
            m.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 8, 8);

            m.dispose();

            BufferedImage vcl = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D cc = vcl.createGraphics();
            applyQualityProperties(cc);

            cc.drawImage(mask, 0, 0, null);

            cc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.1f));

            cc.setColor(Color.BLACK);
            cc.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 8, 8);
            cc.dispose();

            GaussianFilter gg = new GaussianFilter(10);
            vcl = gg.filter(vcl, null);
            vcl = gg.filter(vcl, null);
            vcl = gg.filter(vcl, null);

            g2.drawImage(vcl, 0, 0, null);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 8, 8);
            g2.dispose();
            super.paintComponent(g);
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
      // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
      private void initComponents() {

            jPanel1 = new javax.swing.JPanel();
            filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 15), new java.awt.Dimension(30, 12), new java.awt.Dimension(30, 12));
            jPanel3 = new javax.swing.JPanel();
            filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20));
            jLabel1 = new javax.swing.JLabel();
            filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 30000), new java.awt.Dimension(50, 30000), new java.awt.Dimension(300, 30000));
            ovalButton1 = new com.teri.systemtracking.Server.GUI.OvalButton();
            filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20));
            jPanel2 = new javax.swing.JPanel();
            filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20));
            jPanel4 = new javax.swing.JPanel();
            filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 10), new java.awt.Dimension(20, 10), new java.awt.Dimension(20, 10));
            jLabel2 = new javax.swing.JLabel();
            filler6 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5));
            jLabel3 = new javax.swing.JLabel();
            filler8 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5));
            jLabel4 = new javax.swing.JLabel();
            filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5));
            jLabel5 = new javax.swing.JLabel();
            filler11 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 5), new java.awt.Dimension(20, 15));
            jLabel6 = new javax.swing.JLabel();
            filler10 = new javax.swing.Box.Filler(new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20), new java.awt.Dimension(30, 20));

            setMinimumSize(new java.awt.Dimension(360, 250));
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(335, 300));
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

            jPanel1.setAlignmentX(0.0F);
            jPanel1.setAlignmentY(0.0F);
            jPanel1.setMaximumSize(new java.awt.Dimension(30000, 30000));
            jPanel1.setMinimumSize(new java.awt.Dimension(335, 76));
            jPanel1.setOpaque(false);
            jPanel1.setPreferredSize(new java.awt.Dimension(335, 76));
            jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
            jPanel1.add(filler7);

            jPanel3.setMaximumSize(new java.awt.Dimension(32767, 40));
            jPanel3.setMinimumSize(new java.awt.Dimension(10, 40));
            jPanel3.setOpaque(false);
            jPanel3.setPreferredSize(new java.awt.Dimension(100, 40));
            jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));
            jPanel3.add(filler1);

            jLabel1.setFont(new java.awt.Font("Segoe UI Variable", 0, 15)); // NOI18N
            jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/question-mark.png"))); // NOI18N
            jLabel1.setText("Help");
            jLabel1.setIconTextGap(8);
            jPanel3.add(jLabel1);
            jPanel3.add(filler2);

            ovalButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cancel.png"))); // NOI18N
            ovalButton1.setMaximumSize(new java.awt.Dimension(30, 30));
            ovalButton1.setMinimumSize(new java.awt.Dimension(30, 30));
            ovalButton1.setPreferredSize(new java.awt.Dimension(30, 30));
            ovalButton1.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                        ovalButton1ActionPerformed(evt);
                  }
            });
            jPanel3.add(ovalButton1);
            jPanel3.add(filler3);

            jPanel1.add(jPanel3);

            jPanel2.setMaximumSize(new java.awt.Dimension(30000, 30000));
            jPanel2.setOpaque(false);
            jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));
            jPanel2.add(filler4);

            jPanel4.setMaximumSize(new java.awt.Dimension(333333, 333333));
            jPanel4.setMinimumSize(new java.awt.Dimension(0, 78));
            jPanel4.setOpaque(false);
            jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

            filler5.setAlignmentX(0.0F);
            jPanel4.add(filler5);

            jLabel2.setFont(new java.awt.Font("Segoe UI Variable", 0, 17)); // NOI18N
            jLabel2.setText("1. Select folder");
            jPanel4.add(jLabel2);

            filler6.setAlignmentX(0.0F);
            jPanel4.add(filler6);

            jLabel3.setFont(new java.awt.Font("Segoe UI Variable", 0, 17)); // NOI18N
            jLabel3.setText("3. Press submit button");
            jPanel4.add(jLabel3);

            filler8.setAlignmentX(0.0F);
            jPanel4.add(filler8);

            jLabel4.setFont(new java.awt.Font("Segoe UI Variable", 0, 17)); // NOI18N
            jLabel4.setText("4. Date valid format yyyy-mm/DD");
            jPanel4.add(jLabel4);

            filler9.setAlignmentX(0.0F);
            jPanel4.add(filler9);

            jLabel5.setFont(new java.awt.Font("Segoe UI Variable", 0, 17)); // NOI18N
            jLabel5.setText("5. Author : minh tri");
            jPanel4.add(jLabel5);

            filler11.setAlignmentX(0.0F);
            jPanel4.add(filler11);

            jLabel6.setFont(new java.awt.Font("Segoe UI Variable", 0, 17)); // NOI18N
            jLabel6.setText("20120220@student.hcmus.edu.vn");
            jPanel4.add(jLabel6);

            jPanel2.add(jPanel4);
            jPanel2.add(filler10);

            jPanel1.add(jPanel2);

            add(jPanel1);
      }// </editor-fold>//GEN-END:initComponents

      private void ovalButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ovalButton1ActionPerformed
            // TODO add your handling code here:
      }// GEN-LAST:event_ovalButton1ActionPerformed

      // Variables declaration - do not modify//GEN-BEGIN:variables
      private javax.swing.Box.Filler filler1;
      private javax.swing.Box.Filler filler10;
      private javax.swing.Box.Filler filler11;
      private javax.swing.Box.Filler filler2;
      private javax.swing.Box.Filler filler3;
      private javax.swing.Box.Filler filler4;
      private javax.swing.Box.Filler filler5;
      private javax.swing.Box.Filler filler6;
      private javax.swing.Box.Filler filler7;
      private javax.swing.Box.Filler filler8;
      private javax.swing.Box.Filler filler9;
      private javax.swing.JLabel jLabel1;
      private javax.swing.JLabel jLabel2;
      private javax.swing.JLabel jLabel3;
      private javax.swing.JLabel jLabel4;
      private javax.swing.JLabel jLabel5;
      private javax.swing.JLabel jLabel6;
      private javax.swing.JPanel jPanel1;
      private javax.swing.JPanel jPanel2;
      private javax.swing.JPanel jPanel3;
      private javax.swing.JPanel jPanel4;
      private com.teri.systemtracking.Server.GUI.OvalButton ovalButton1;
      // End of variables declaration//GEN-END:variables
}
