/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.jhlabs.image.GaussianFilter;

/**
 *
 * @author MinhTri
 */
public class SearchBar extends javax.swing.JPanel {

    /**
     * Creates new form SearchBar
     */
    private PopupWindow popup;
    private JPanel rcmList;
    private boolean enableRcm;
    private boolean isEmpty;
    private String thumbnail;
    private Dimension round;
    private int sz;

    public SearchBar() {
        initComponents();
        jTextField1.setOpaque(false);
        enableRcm = false;
        isEmpty = true;
        round = null;
        sz = 16;
        jTextField1.setForeground(new Color(150, 150, 150));
        jTextField1.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
        jTextField1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (isEmpty) {
                    jTextField1.setText("");
                    jTextField1.setForeground(Color.BLACK);
                    jTextField1.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                isEmpty = jTextField1.getText().equals("");
                if (isEmpty) {
                    jTextField1.setForeground(new Color(100, 100, 100));
                    jTextField1.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
                    jTextField1.setText(thumbnail);
                }
            }

        });
    }

    public void enablePopupRcm(boolean rcm) {
        this.enableRcm = rcm;
        if (rcm) {
            popup = new PopupWindow();
            popup.setBaseLocation(this);
            popup.setBuffLocation(0, (int) this.getPreferredSize().getHeight());
            JPanel rcmPane = new JPanel() {

                @Override
                public void paintComponent(Graphics g) {

                    Graphics2D g2 = (Graphics2D) g.create();
                    applyQualityProperties(g2);

                    BufferedImage mask = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D m = mask.createGraphics();
                    applyQualityProperties(m);

                    m.setColor(Color.BLACK);
                    m.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10,
                            (round == null) ? 8 : (int) round.getWidth(),
                            (round == null) ? 8 : (int) round.getHeight());

                    m.dispose();

                    BufferedImage vcl = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

                    Graphics2D cc = vcl.createGraphics();
                    applyQualityProperties(cc);

                    cc.drawImage(mask, 0, 0, null);

                    cc.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.1f));

                    cc.setColor(Color.BLACK);
                    cc.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10,
                            (round == null) ? 8 : (int) round.getWidth(),
                            (round == null) ? 8 : (int) round.getHeight());
                    cc.dispose();

                    GaussianFilter gg = new GaussianFilter(5);
                    vcl = gg.filter(vcl, null);
                    vcl = gg.filter(vcl, null);
                    vcl = gg.filter(vcl, null);

                    g2.drawImage(vcl, 0, 0, null);

                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10,
                            (round == null) ? 8 : (int) round.getWidth(),
                            (round == null) ? 8 : (int) round.getHeight());
                    g2.dispose();
                    super.paintComponent(g);

                }

            };

            rcmPane.setLayout(new BoxLayout(rcmPane, BoxLayout.Y_AXIS));
            rcmPane.setOpaque(false);

            ListScrollPane scroll = new ListScrollPane();

            rcmList = new JPanel();
            rcmList.setLayout(new BoxLayout(rcmList, BoxLayout.Y_AXIS));
            rcmList.setOpaque(false);

            scroll.setOpaque(false);
            scroll.updateContentComponent(rcmList);

            rcmPane.add(new javax.swing.Box.Filler(new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 5),
                    new java.awt.Dimension(50, 10)));

            rcmPane.add(scroll);

            rcmPane.add(new javax.swing.Box.Filler(new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 5),
                    new java.awt.Dimension(50, 10)));

            popup.setPane(rcmPane);
            popup.setSize(this.getPreferredSize());
            jTextField1.addFocusListener(new FocusListener() {

                @Override
                public void focusGained(FocusEvent e) {
                }

                @Override
                public void focusLost(FocusEvent e) {
                    popup.setVisible(false);
                }
            });
            jTextField1.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    popup.setVisible(true);
                }
            });

            addComponentListener(new ComponentListener() {
                @Override
                public void componentResized(ComponentEvent e) {
                    Dimension d = SearchBar.this.getSize();
                    popup.setSize(new Dimension((int) d.getWidth(), popup.getPane().getHeight()));
                    popup.setBuffLocation(0, (int) d.getHeight() + 5);
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
            addAncestorListener(new AncestorListener() {

                @Override
                public void ancestorAdded(AncestorEvent event) {
                }

                @Override
                public void ancestorRemoved(AncestorEvent event) {
                    popup.setVisible(false);

                }

                @Override
                public void ancestorMoved(AncestorEvent event) {
                }

            });
            jTextField1.addHierarchyListener(new HierarchyListener() {

                @Override
                public void hierarchyChanged(HierarchyEvent e) {
                }

            });
        }
    }

    public void setRcmList(ArrayList<JPanel> rcms) {
        if (!enableRcm) {
            return;
        }
        rcmList.removeAll();
        int h = 30;
        h *= rcms.size();
        for (JPanel rcm : rcms) {
            rcm.setAlignmentX(0);
            rcmList.add(rcm);
        }
        h = Math.min(230, h);
        popup.setSize(new Dimension(popup.getPane().getWidth(), h + 20));
        rcmList.repaint();
        rcmList.revalidate();
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

    public void setFontSize(int sz) {
        this.sz = sz;
        jTextField1.setFont(new java.awt.Font("Segoe UI Variable", 0, sz));
    }

    public void setRoundX(int w) {
        if (round == null) {
            round = new Dimension(8, 8);
        }
        round.setSize(w, (int) round.getHeight());
        repaint();
    }

    public void setRoundY(int h) {
        if (round == null) {
            round = new Dimension(8, 8);
        }
        round.setSize((int) round.getWidth(), h);
        repaint();
    }

    public void setText(String val) {
        jTextField1.setText(val);
        thumbnail = val;
    }

    public JTextField getTextField() {
        return jTextField1;
    }

    public void addHandlerActionListener(ActionListener actionListener) {
        jTextField1.addActionListener(actionListener);
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
        jTextField1 = new javax.swing.JTextField();
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

        jTextField1.setFont(new java.awt.Font("Segoe UI Variable", 0, 16)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jTextField1.setText("type here");
        jTextField1.setBorder(null);
        jPanel1.add(jTextField1);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    private javax.swing.Box.Filler filler4;
    private javax.swing.Box.Filler filler5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private com.teri.systemtracking.Server.GUI.OvalButton ovalButton1;
    // End of variables declaration//GEN-END:variables
}
