/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.teri.systemtracking.Server.GUI;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 *
 * @author MinhTri
 */
public class ListScrollPane extends JScrollPane {

    public ListScrollPane() {
        super();
    }

    public ListScrollPane(JComponent content) {
        super(content, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        getViewport().setOpaque(false);
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getViewport(), 1);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());
        getVerticalScrollBar().setOpaque(false);
        setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;

                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;

                Rectangle vsbR = new Rectangle();
                vsbR.width = 12;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension();
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension();
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar scrollbar = (JScrollBar) c;
                if (!scrollbar.isEnabled() || r.width > r.height) {
                    return;
                } else if (isDragging) {
                    color = new Color(0x33, 0x35, 0x33, 150);
                } else if (isThumbRollover()) {
                    color = new Color(0x33, 0x35, 0x33, 100);
                } else {
                    color = new Color(0x33, 0x35, 0x33, 60);
                }
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y + 15, r.width, r.height - 30, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
    }

    public void updateContentComponent(JComponent content) {
        setViewportView(content);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        getViewport().setOpaque(false);
        setComponentZOrder(getVerticalScrollBar(), 0);
        setComponentZOrder(getViewport(), 1);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());
        getVerticalScrollBar().setOpaque(false);
        setLayout(new ScrollPaneLayout() {
            @Override
            public void layoutContainer(Container parent) {
                JScrollPane scrollPane = (JScrollPane) parent;

                Rectangle availR = scrollPane.getBounds();
                availR.x = availR.y = 0;

                Insets insets = parent.getInsets();
                availR.x = insets.left;
                availR.y = insets.top;
                availR.width -= insets.left + insets.right;
                availR.height -= insets.top + insets.bottom;

                Rectangle vsbR = new Rectangle();
                vsbR.width = 10;
                vsbR.height = availR.height;
                vsbR.x = availR.x + availR.width - vsbR.width;
                vsbR.y = availR.y;

                if (viewport != null) {
                    viewport.setBounds(availR);
                }
                if (vsb != null) {
                    vsb.setVisible(true);
                    vsb.setBounds(vsbR);
                }
            }
        });
        getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension();
                    }
                };
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return new JButton() {
                    @Override
                    public Dimension getPreferredSize() {
                        return new Dimension();
                    }
                };
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                Color color = null;
                JScrollBar scrollbar = (JScrollBar) c;
                if (!scrollbar.isEnabled() || r.width > r.height) {
                    return;
                } else if (isDragging) {
                    color = new Color(0x33, 0x35, 0x33, 150);
                } else if (isThumbRollover()) {
                    color = new Color(0x33, 0x35, 0x33, 100);
                } else {
                    color = new Color(0x33, 0x35, 0x33, 60);
                }
                g2.setPaint(color);
                g2.fillRoundRect(r.x, r.y + 15, r.width, r.height - 30, 10, 10);
                g2.dispose();
            }

            @Override
            protected void setThumbBounds(int x, int y, int width, int height) {
                super.setThumbBounds(x, y, width, height);
                scrollbar.repaint();
            }
        });
        getVerticalScrollBar().setUnitIncrement(5);

    }

}
