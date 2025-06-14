/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import com.teri.systemtracking.Server.presenter.EventObservationPresenter;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author MinhTri
 */
public class EventObservationPanel extends javax.swing.JPanel {

      private EventObservationPresenter presenter;
      private boolean onSound;
      // Variables declaration - do not modify//GEN-BEGIN:variables
      private javax.swing.Box.Filler filler1;
      private javax.swing.JLabel jLabel2;
      private javax.swing.JPanel jPanel1;
      private javax.swing.JPanel jPanel2;
      private javax.swing.JPanel listEntityPanel;
      private com.teri.systemtracking.Server.GUI.ListScrollPane listScrollPane1;
      private com.teri.systemtracking.Server.GUI.OvalButton unTrackingButton;
      private com.teri.systemtracking.Server.GUI.SearchBar searchBar2;
      private com.teri.systemtracking.Server.GUI.OvalButton showAllButton;

      public EventObservationPanel() {
            initComponents();
            onSound = false;
            listScrollPane1.updateContentComponent(listEntityPanel);

            for (int i = 0; i < 11; i++) {
                  listEntityPanel
                        .add(new javax.swing.Box.Filler(new java.awt.Dimension(250, 200), new java.awt.Dimension(250, 200),
                              new java.awt.Dimension(250, 200)));
            }
            initActions();
      }

      public void setListEvent(ArrayList<JPanel> l) {
            listEntityPanel.removeAll();
            for (int i = 0; i < 11; i++) {
                  listEntityPanel
                        .add(new javax.swing.Box.Filler(new java.awt.Dimension(250, 200),
                              new java.awt.Dimension(250, 200),
                              new java.awt.Dimension(250, 200)));
            }
            if (l != null)
                  for (JPanel i : l)
                        listEntityPanel.add(i, 0);
            listEntityPanel.repaint();
            listEntityPanel.revalidate();

      }

      public void clearEvents() {
            listEntityPanel.removeAll();
            for (int i = 0; i < 11; i++) {
                  listEntityPanel
                        .add(new javax.swing.Box.Filler(new java.awt.Dimension(250, 200),
                              new java.awt.Dimension(250, 200),
                              new java.awt.Dimension(250, 200)));
            }
            listEntityPanel.repaint();
            listEntityPanel.revalidate();

      }

      public void addEvent(JPanel e) {
            if (!onSound && isShowing()) {
                  onSound = true;
                  new Thread(new Runnable() {

                        @Override
                        public void run() {
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
                                                      java.awt.EventQueue.invokeLater(new Runnable() {
                                                            public void run() {
                                                                  onSound = false;
                                                            }
                                                      });
                                                }
                                          }

                                    });

                              } catch (Exception ex) {
                                    ex.printStackTrace();
                              }
                        }
                  }).start();
            }
            listEntityPanel.add(e, 0);
            listEntityPanel.repaint();
            listEntityPanel.revalidate();
      }

      private void initActions() {
            unTrackingButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        if (presenter != null) {
                              presenter.getFolderTracker().untrack();
                        }
                  }
            });
      }

      public void setPresenter(EventObservationPresenter presenter) {
            this.presenter = presenter;
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
      // Code">//GEN-BEGIN:initComponents
      private void initComponents() {

            listEntityPanel = new javax.swing.JPanel();
            jPanel1 = new javax.swing.JPanel();
            jLabel2 = new javax.swing.JLabel();
            jPanel2 = new javax.swing.JPanel();
            searchBar2 = new com.teri.systemtracking.Server.GUI.SearchBar();
            showAllButton = new com.teri.systemtracking.Server.GUI.OvalButton();
            unTrackingButton = new com.teri.systemtracking.Server.GUI.OvalButton();
            listScrollPane1 = new com.teri.systemtracking.Server.GUI.ListScrollPane();
            filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 30), new java.awt.Dimension(50, 30),
                  new java.awt.Dimension(50, 30));

            listEntityPanel.setOpaque(false);
            listEntityPanel.setLayout(new java.awt.GridLayout(0, 4, 10, 10));

            setMaximumSize(new java.awt.Dimension(32817, 32817));
            setMinimumSize(new java.awt.Dimension(930, 650));
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(930, 640));
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

            jPanel1.setMaximumSize(new java.awt.Dimension(32767, 70));
            jPanel1.setMinimumSize(new java.awt.Dimension(708, 70));
            jPanel1.setOpaque(false);
            jPanel1.setPreferredSize(new java.awt.Dimension(598, 70));
            jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

            jLabel2.setFont(new java.awt.Font("Fira Code Medium", 0, 16)); // NOI18N
            jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
            jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/time.png"))); // NOI18N
            jLabel2.setText(" Oberseved Events");
            jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
            jLabel2.setIconTextGap(9);
            jLabel2.setInheritsPopupMenu(false);
            jPanel1.add(jLabel2);

            jPanel2.setOpaque(false);
            jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 15, 15));

            searchBar2.setText("search by date (yyyy-MM-dd)");
            jPanel2.add(searchBar2);

            showAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/all.png"))); // NOI18N
            showAllButton.setInfo("show all events");
            showAllButton.setMaximumSize(new java.awt.Dimension(35, 35));
            showAllButton.setPreferredSize(new java.awt.Dimension(35, 35));
            jPanel2.add(showAllButton);

            unTrackingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/disconnect.png"))); // NOI18N
            unTrackingButton.setInfo("disable tracking");
            unTrackingButton.setMaximumSize(new java.awt.Dimension(35, 35));
            unTrackingButton.setPreferredSize(new java.awt.Dimension(35, 35));
            jPanel2.add(unTrackingButton);

            jPanel1.add(jPanel2);

            add(jPanel1);

            listScrollPane1.setBorder(null);
            listScrollPane1.setOpaque(false);
            add(listScrollPane1);
            add(filler1);
      }// </editor-fold>//GEN-END:initComponents
      // End of variables declaration//GEN-END:variables
}
