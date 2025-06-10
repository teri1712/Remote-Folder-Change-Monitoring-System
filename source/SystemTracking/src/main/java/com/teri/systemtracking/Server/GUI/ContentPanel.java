/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import com.teri.systemtracking.Server.presenter.ContentPresenter;
import com.teri.systemtracking.Server.presenter.NotificationPresenter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author MinhTri
 */
public class ContentPanel extends javax.swing.JPanel {

      private ContentPresenter presenter;
      private NotificationPresenter notificationPresenter;

      private NotifyPanel notifyPanel;
      private NotifyListPanel notifyListPanel;
      private BellPanel bellPanel;
      private HelpPanel helpPanel;
      private boolean notifyListPanelIsVisible;
      // Variables declaration - do not modify//GEN-BEGIN:variables
      private javax.swing.Box.Filler filler1;
      private javax.swing.Box.Filler filler4;
      private javax.swing.Box.Filler filler5;
      private javax.swing.Box.Filler filler7;
      private javax.swing.Box.Filler filler9;
      private javax.swing.JPanel cardLayout;
      private javax.swing.JPanel jPanel2;
      private javax.swing.JPanel jPanel3;
      private javax.swing.JPanel jPanel4;
      private javax.swing.JPanel jPanel5;
      private com.teri.systemtracking.Server.GUI.RoundedButton contentButton;


      public ContentPanel() {
            initComponents();
            notifyPanel = new NotifyPanel();

            helpPanel = new HelpPanel();
            HelpPanel.setHelp(helpPanel);

            notifyListPanel = new NotifyListPanel();
            notifyListPanelIsVisible = false;
            notifyListPanel.getPopupWindow().setEnableShowUpEffect(false);

            final PopupWindow notifyWindow = notifyPanel.getPopupWindow();
            final PopupWindow helpWindow = helpPanel.getPopupWindow();
            final PopupWindow notifyListPopupWindow = notifyListPanel.getPopupWindow();

            notifyWindow.setBaseLocation(this);
            notifyWindow.setBuffLocation(10, (int) this.getPreferredSize().getHeight() - 170 - 20);

            helpWindow.setBaseLocation(this);
            helpWindow.setBuffLocation((int) this.getPreferredSize().getWidth() / 2 - (int) helpPanel
                        .getPreferredSize().getWidth() / 2,
                  (int) this.getPreferredSize().getHeight() / 2
                        - (int) helpPanel.getPreferredSize().getHeight() / 2);

            notifyListPopupWindow.setBaseLocation(bellPanel.getNotifyButton());
            notifyListPopupWindow.setBuffLocation(-(int) notifyListPanel.getPreferredSize().getWidth() + 60, 40);
            bellPanel.getNotifyButton().addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent e) {
                        notifyListPanelIsVisible = !notifyListPanelIsVisible;
                        notifyListPopupWindow.setVisible(notifyListPanelIsVisible);
                  }

            });

            bellPanel.getNotifyButton().addAncestorListener(new AncestorListener() {

                  @Override
                  public void ancestorAdded(AncestorEvent event) {
                  }

                  @Override
                  public void ancestorRemoved(AncestorEvent event) {
                        notifyListPopupWindow.setVisible(false);
                        notifyListPanelIsVisible = false;
                  }

                  @Override
                  public void ancestorMoved(AncestorEvent event) {
                  }

            });

            notifyPanel.setSize(notifyPanel.getPreferredSize());
            helpPanel.setSize(helpPanel.getPreferredSize());
            notifyListPanel.setSize(notifyListPanel.getPreferredSize());

            notifyWindow.setPane(notifyPanel);
            helpWindow.setPane(helpPanel);
            notifyListPopupWindow.setPane(notifyListPanel);

            addComponentListener(new ComponentListener() {
                  @Override
                  public void componentResized(ComponentEvent e) {
                        int h = ContentPanel.this.getHeight();
                        notifyWindow.setBuffLocation(10, h - 170 - 20);
                        helpWindow.setBuffLocation((int) ContentPanel.this.getWidth() / 2 - (int) helpPanel.getWidth() / 2,
                              (int) ContentPanel.this.getHeight() / 2
                                    - (int) helpPanel.getHeight() / 2);

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

            initActions();
      }

      public void setPresenter(ContentPresenter presenter) {
            this.presenter = presenter;
      }

      public NotifyListPanel getNotifyListPanel() {
            return notifyListPanel;
      }

      public NotifyPanel getNotifyPanel() {
            return notifyPanel;
      }

      public BellPanel getConnectionPanel() {
            return bellPanel;
      }

      private void initActions() {
            bellPanel.getShowAllButton().addActionListener(new ActionListener() {

                  @Override
                  public void actionPerformed(ActionEvent actionEvent) {
                        if (presenter != null)
                              presenter.navigateToHome();
                  }
            });

            contentButton.addActionListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        if (presenter != null)
                              presenter.navigateToHome();
                  }
            });

            getSearchBar().addListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        try {

                              Date date = new SimpleDateFormat("yyyy-MM-dd").parse(getSearchBar().getTextField().getText());
                              if (presenter != null)
                                    presenter.filterByDate(date);
                        } catch (Exception ex) {
                              JOptionPane.showMessageDialog(null, "invalid date");
                        }

                  }
            });
      }

      @Override
      public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                  RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
      }


      private SearchBar getSearchBar() {
            return bellPanel.getSearchBar();
      }

      public ActiveUserListPanel getActiveUserListPanel() {
            return bellPanel.getActiveUserListPanel();
      }

      public void showClientPanel(EditClientPanel clientPanel) {
            CardLayout layout = ((CardLayout) cardLayout.getLayout());
            jPanel5.removeAll();
            jPanel5.add(clientPanel);
            layout.show(cardLayout, "edit session");
      }

      public void showClientList(Map<Integer, ActiveClientPanel> activeClientPanels) {
            getActiveUserListPanel().replaceAll(activeClientPanels);
            ((CardLayout) cardLayout.getLayout()).show(cardLayout, "connection list");
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
      // Code">//GEN-BEGIN:initComponents
      private void initComponents() {

            jPanel2 = new javax.swing.JPanel() {

                  @Override
                  public void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(0, 0, 0, 5));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                  }

            };
            jPanel3 = new JPanel() {

                  @Override
                  public void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                              RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.WHITE);
                        g2.fillRect(0, 0, getWidth() - 1, getHeight());

                        g2.setColor(new Color(0, 0, 0, 10));
                        g2.fillRect(getWidth() - 1, 0, 1, getHeight());

                        g2.dispose();
                        super.paintComponent(g);
                  }

            };
            filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 37), new java.awt.Dimension(50, 37),
                  new java.awt.Dimension(50, 37));
            contentButton = new com.teri.systemtracking.Server.GUI.RoundedButton();
            filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 1150),
                  new java.awt.Dimension(10, 1150));
            jPanel4 = new javax.swing.JPanel();
            filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20),
                  new java.awt.Dimension(20, 20));
            cardLayout = new javax.swing.JPanel();
            bellPanel = new BellPanel();
            jPanel5 = new javax.swing.JPanel();
            filler5 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20),
                  new java.awt.Dimension(20, 20));
            filler9 = new javax.swing.Box.Filler(new java.awt.Dimension(5, 20), new java.awt.Dimension(5, 1150),
                  new java.awt.Dimension(5, 1150));

            setMaximumSize(new java.awt.Dimension(1060, 32877));
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(1060, 700));
            setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

            jPanel2.setAlignmentX(0.0F);
            jPanel2.setOpaque(false);
            jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

            jPanel3.setAlignmentX(0.0F);
            jPanel3.setMaximumSize(new java.awt.Dimension(60, 32767));
            jPanel3.setMinimumSize(new java.awt.Dimension(60, 810));
            jPanel3.setOpaque(false);
            jPanel3.setPreferredSize(new java.awt.Dimension(70, 810));
            jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
            jPanel3.add(filler1);

            contentButton.setBorder(null);
            contentButton.setForeground(new java.awt.Color(255, 255, 255));
            contentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/share.png"))); // NOI18N
            contentButton.setAlignmentX(0.5F);
            contentButton.setBorderPainted(false);
            contentButton.setContentAreaFilled(false);
            contentButton.setFocusPainted(false);
            contentButton.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
            contentButton.setIconTextGap(12);
            contentButton.setInfo("show connections");
            contentButton.setMaximumSize(new java.awt.Dimension(40, 40));
            contentButton.setMinimumSize(new java.awt.Dimension(40, 40));
            contentButton.setPreferredSize(new java.awt.Dimension(40, 40));
            contentButton.addActionListener(new java.awt.event.ActionListener() {
                  public void actionPerformed(java.awt.event.ActionEvent evt) {
                        roundedButton1ActionPerformed(evt);
                  }
            });
            jPanel3.add(contentButton);

            jPanel2.add(jPanel3);

            filler7.setAlignmentX(0.0F);
            jPanel2.add(filler7);

            jPanel4.setAlignmentX(0.0F);
            jPanel4.setMaximumSize(new java.awt.Dimension(30000, 30000));
            jPanel4.setMinimumSize(new java.awt.Dimension(500, 810));
            jPanel4.setOpaque(false);
            jPanel4.setPreferredSize(new java.awt.Dimension(980, 810));
            jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

            filler4.setAlignmentX(0.0F);
            jPanel4.add(filler4);

            cardLayout.setAlignmentX(0.0F);
            cardLayout.setMinimumSize(new java.awt.Dimension(1030, 690));
            cardLayout.setOpaque(false);
            cardLayout.setPreferredSize(new java.awt.Dimension(1030, 100));
            cardLayout.setLayout(new java.awt.CardLayout());

            bellPanel.setAlignmentY(0.0F);
            bellPanel.setMinimumSize(new java.awt.Dimension(500, 690));
            cardLayout.add(bellPanel, "connection list");

            jPanel5.setOpaque(false);
            jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));
            cardLayout.add(jPanel5, "edit session");

            jPanel4.add(cardLayout);

            filler5.setAlignmentX(0.0F);
            jPanel4.add(filler5);

            jPanel2.add(jPanel4);

            filler9.setAlignmentX(0.0F);
            jPanel2.add(filler9);

            add(jPanel2);
      }// </editor-fold>//GEN-END:initComponents

      private void roundedButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_roundedButton1ActionPerformed
            // TODO add your handling code here:
      }// GEN-LAST:event_roundedButton1ActionPerformed
      // End of variables declaration//GEN-END:variables
}
