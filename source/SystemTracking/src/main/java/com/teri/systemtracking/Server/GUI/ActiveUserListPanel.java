/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author MinhTri
 */
public class ActiveUserListPanel extends javax.swing.JPanel {

    /**
     * Creates new form ActiveUserListPanel
     */
    private int effectTurn;
    private int cntW;

    public ActiveUserListPanel() {
        initComponents();
        effectTurn = 0;
        cntW = 0;
        listScrollPane1.updateContentComponent(listEntityPanel);
        for (int i = 0; i < 11; i++) {
            listEntityPanel
                    .add(new javax.swing.Box.Filler(new java.awt.Dimension(250, 200), new java.awt.Dimension(250, 200),
                            new java.awt.Dimension(250, 200)));
        }
        // searchByIdTextField.getTextField().addFocusListener(new FocusListener() {

        //     @Override
        //     public void focusGained(FocusEvent e) {
        //         final int thisThreadTurn = ++effectTurn;
        //         new Thread(new Runnable() {

        //             @Override
        //             public void run() {

        //                 for (int i = 1; i <= 50; i++) {
        //                     final int dif = i * 3;
        //                     try {
        //                         Thread.sleep(3);
        //                     } catch (InterruptedException e) {
        //                     }
        //                     if (thisThreadTurn != effectTurn)
        //                         return;
        //                     java.awt.EventQueue.invokeLater(new Runnable() {
        //                         public void run() {
        //                             if (thisThreadTurn != effectTurn)
        //                                 return;

        //                             cntW = dif / 3;
        //                             searchByIdTextField.setPreferredSize(
        //                                     new Dimension(200 + dif, searchByIdTextField.getHeight()));

        //                             searchByIdTextField.revalidate();
        //                             searchByIdTextField.repaint();

        //                             jPanel2.revalidate();
        //                             jPanel2.repaint();

        //                         }
        //                     });
        //                 }
        //             }
        //         }).start();
        //     }

        //     @Override
        //     public void focusLost(FocusEvent e) {
        //         final int thisThreadTurn = ++effectTurn;
        //         final int cur = cntW;
        //         Thread th = new Thread(new Runnable() {

        //             @Override
        //             public void run() {

        //                 for (int i = cur - 1; i >= 0; i--) {
        //                     final int dif = i * 3;

        //                     try {
        //                         Thread.sleep(3);
        //                     } catch (InterruptedException e) {
        //                     }
        //                     if (thisThreadTurn != effectTurn)
        //                         return;
        //                     java.awt.EventQueue.invokeLater(new Runnable() {
        //                         public void run() {
        //                             if (thisThreadTurn != effectTurn)
        //                                 return;

        //                             cntW = dif / 3;
        //                             searchByIdTextField.setPreferredSize(
        //                                     new Dimension(200 + dif, searchByIdTextField.getHeight()));

        //                             jPanel2.revalidate();
        //                             jPanel2.repaint();

        //                         }
        //                     });
        //                 }
        //             }
        //         });
        //         th.setPriority(10);
        //         th.start();
        //     }

        // });
    }

    public OvalButton getShowAllButton() {
        return showAllButton;
    }

    public void addConnectionPanel(JPanel pnl) {
        listEntityPanel.add(pnl, 0);
        listEntityPanel.repaint();
        listEntityPanel.revalidate();
    }

    public void replaceAllInList(ArrayList<JPanel> l) {
        listEntityPanel.removeAll();
        for (int i = 0; i < 11; i++) {
            listEntityPanel
                    .add(new javax.swing.Box.Filler(new java.awt.Dimension(250, 200), new java.awt.Dimension(250, 200),
                            new java.awt.Dimension(250, 200)));
        }

        for (JPanel i : l) {
            listEntityPanel.add(i, 0);
        }

        listEntityPanel.repaint();
        listEntityPanel.revalidate();

    }

    public void removeConnectionPanel(JPanel pnl) {
        listEntityPanel.remove(pnl);
        listEntityPanel.repaint();
        listEntityPanel.revalidate();
    }

    public SearchBar getSearchBar() {
        return searchByIdTextField;
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

        listEntityPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        searchByIdTextField = new com.teri.systemtracking.Server.GUI.SearchBar();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(15, 15), new java.awt.Dimension(15, 15),
                new java.awt.Dimension(15, 15));
        showAllButton = new com.teri.systemtracking.Server.GUI.OvalButton();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 15), new java.awt.Dimension(50, 15),
                new java.awt.Dimension(50, 15));
        listScrollPane1 = new com.teri.systemtracking.Server.GUI.ListScrollPane();
        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 15), new java.awt.Dimension(50, 15),
                new java.awt.Dimension(50, 15));

        listEntityPanel.setOpaque(false);
        listEntityPanel.setLayout(new java.awt.GridLayout(0, 4, 10, 10));

        setMaximumSize(new java.awt.Dimension(32817, 32817));
        setMinimumSize(new java.awt.Dimension(930, 650));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(930, 640));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 50));
        jPanel1.setMinimumSize(new java.awt.Dimension(708, 50));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(598, 50));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 5));

        searchByIdTextField.setMaximumSize(new java.awt.Dimension(30000, 45));
        searchByIdTextField.setPreferredSize(new java.awt.Dimension(500, 45));
        searchByIdTextField.setText("search by date");
        jPanel2.add(searchByIdTextField);
        jPanel2.add(filler2);

        showAllButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/all.png"))); // NOI18N
        showAllButton.setInfo("show all connection");
        showAllButton.setMaximumSize(new java.awt.Dimension(35, 35));
        showAllButton.setPreferredSize(new java.awt.Dimension(35, 35));
        jPanel2.add(showAllButton);

        jPanel1.add(jPanel2);

        add(jPanel1);
        add(filler1);

        listScrollPane1.setBorder(null);
        listScrollPane1.setOpaque(false);
        add(listScrollPane1);
        add(filler3);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel listEntityPanel;
    private com.teri.systemtracking.Server.GUI.ListScrollPane listScrollPane1;
    private com.teri.systemtracking.Server.GUI.SearchBar searchByIdTextField;
    private com.teri.systemtracking.Server.GUI.OvalButton showAllButton;
    // End of variables declaration//GEN-END:variables
}
