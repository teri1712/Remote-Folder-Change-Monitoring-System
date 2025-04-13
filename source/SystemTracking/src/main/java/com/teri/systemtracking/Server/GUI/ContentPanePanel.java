/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JPanel;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author MinhTri
 */
public class ContentPanePanel extends javax.swing.JPanel {

    /**
     * Creates new form ContentPanePanel
     */
    private NotifyPanel notifyPanel;
    private HelpPanel helpPanel;
    private NotifyListPanel notifyListPanel;
    private boolean notifyListPanelIsVisible;

    public ContentPanePanel() {
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

        notifyListPopupWindow.setBaseLocation(connectionSessionPanel1.getNotifyButton());
        notifyListPopupWindow.setBuffLocation(-(int) notifyListPanel.getPreferredSize().getWidth() + 60, 40);
        connectionSessionPanel1.getNotifyButton().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                notifyListPanelIsVisible = !notifyListPanelIsVisible;
                notifyListPopupWindow.setVisible(notifyListPanelIsVisible);
            }

        });

        connectionSessionPanel1.getNotifyButton().addAncestorListener(new AncestorListener() {

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
                int h = ContentPanePanel.this.getHeight();
                notifyWindow.setBuffLocation(10, h - 170 - 20);
                helpWindow.setBuffLocation((int) ContentPanePanel.this.getWidth() / 2 - (int) helpPanel.getWidth() / 2,
                        (int) ContentPanePanel.this.getHeight() / 2
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

        roundedButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConnectionList();
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

    public void replaceAllInList(ArrayList<JPanel> l) {
        connectionSessionPanel1.replaceAllInList(l);

    }

    public OvalButton getShowAllButton() {
        return connectionSessionPanel1.getShowAllButton();
    }

    public SearchBar getSearchByDate() {
        return connectionSessionPanel1.getSearchBar();
    }

    public void addConnectionEntity(JPanel pnl) {
        connectionSessionPanel1.addEntity(pnl);
    }

    public void removeConnectionEntity(ActiveUserEntityPanel pnl) {
        connectionSessionPanel1.removeEntity(pnl);
    }

    public void showEditSession(JPanel pnl) {
        CardLayout layout = ((CardLayout) jPanel1.getLayout());
        jPanel5.removeAll();
        jPanel5.add(pnl);
        layout.show(jPanel1, "edit session");
    }

    public void showConnectionList() {
        ((CardLayout) jPanel1.getLayout()).show(jPanel1, "connection list");
    }

    public void setNotification(int opened, int closed, String content, Date createdDate) {
        notifyPanel.setAnnouncement(opened, closed);
        notifyListPanel.updateAnnouncement(content, createdDate);
        connectionSessionPanel1.updateBellCntNotification();

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
        roundedButton1 = new com.teri.systemtracking.Server.GUI.RoundedButton();
        filler7 = new javax.swing.Box.Filler(new java.awt.Dimension(10, 20), new java.awt.Dimension(10, 1150),
                new java.awt.Dimension(10, 1150));
        jPanel4 = new javax.swing.JPanel();
        filler4 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20),
                new java.awt.Dimension(20, 20));
        jPanel1 = new javax.swing.JPanel();
        connectionSessionPanel1 = new com.teri.systemtracking.Server.GUI.ConnectionSessionPanel();
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

        roundedButton1.setBorder(null);
        roundedButton1.setForeground(new java.awt.Color(255, 255, 255));
        roundedButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/share.png"))); // NOI18N
        roundedButton1.setAlignmentX(0.5F);
        roundedButton1.setBorderPainted(false);
        roundedButton1.setContentAreaFilled(false);
        roundedButton1.setFocusPainted(false);
        roundedButton1.setFont(new java.awt.Font("Segoe UI Variable", 0, 14)); // NOI18N
        roundedButton1.setIconTextGap(12);
        roundedButton1.setInfo("show connections");
        roundedButton1.setMaximumSize(new java.awt.Dimension(40, 40));
        roundedButton1.setMinimumSize(new java.awt.Dimension(40, 40));
        roundedButton1.setPreferredSize(new java.awt.Dimension(40, 40));
        roundedButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roundedButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(roundedButton1);

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

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setMinimumSize(new java.awt.Dimension(1030, 690));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(1030, 100));
        jPanel1.setLayout(new java.awt.CardLayout());

        connectionSessionPanel1.setAlignmentY(0.0F);
        connectionSessionPanel1.setMinimumSize(new java.awt.Dimension(500, 690));
        jPanel1.add(connectionSessionPanel1, "connection list");

        jPanel5.setOpaque(false);
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(jPanel5, "edit session");

        jPanel4.add(jPanel1);

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.teri.systemtracking.Server.GUI.ConnectionSessionPanel connectionSessionPanel1;
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.Box.Filler filler7;
    private javax.swing.Box.Filler filler9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private com.teri.systemtracking.Server.GUI.RoundedButton roundedButton1;
    // End of variables declaration//GEN-END:variables
}
