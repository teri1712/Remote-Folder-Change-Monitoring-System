package com.teri.systemtracking.Server.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 *
 * @author MinhTri
 */
public class PopupWindow {

    private Component base;
    private Point buff;
    private JPanel pane;
    private JWindow popup;
    private boolean isVisible;
    private boolean enableShowUpEffect;

    public PopupWindow() {
        super();
        enableShowUpEffect = true;
        buff = new Point(0, 0);
        base = null;
        popup = null;
        isVisible = false;
    }

    public void setOpacity(float o) {
        if (popup != null) {
            popup.setOpacity(o);
        }

    }

    public void setEnableShowUpEffect(boolean enableShowUpEffect) {
        this.enableShowUpEffect = enableShowUpEffect;
    }

    public void setBaseLocation(JComponent base) {
        this.base = base;
        base.addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorAdded(AncestorEvent event) {
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                updateLocation();

            }

        });

    }

    public void setBuffLocation(int x, int y) {
        buff.setLocation(x, y);
    }

    public void setSize(Dimension sz) {
        pane.setSize(sz);
        if (popup != null && popup.isVisible()) {
            popup.setSize(sz);
        }
    }

    public void setPane(JPanel pane) {
        this.pane = pane;
    }

    public JPanel getPane() {
        return pane;
    }

    public void setVisible(boolean isVisible) {
        if (this.isVisible == isVisible) {
            return;
        }
        if (isVisible) {
            show();
        } else {
            hide();
        }
        this.isVisible = isVisible;
    }

    private void show() {
        popup = new JWindow(SwingUtilities.getWindowAncestor(base));
        popup.setFocusableWindowState(false);
        popup.setBackground(new Color(0, 0, 0, 0));
        popup.setType(JWindow.Type.POPUP);
        popup.setLocationRelativeTo(null);
        popup.setAutoRequestFocus(false);
        popup.setSize(new Dimension(0, 0));
        popup.setVisible(true);
        if (enableShowUpEffect) {

            popup.setOpacity(0);
            popup.setOpacity(0.5f);
            final JWindow pop = popup;
            new Thread(new Runnable() {

                @Override
                public void run() {

                    for (int i = 1; i <= 5; i++) {
                        try {
                            Thread.sleep(100 / 5);
                        } catch (InterruptedException e) {
                        }
                        final float op = (float) i / 10 + 0.5f;
                        java.awt.EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                if (pop == popup) {
                                    pop.setOpacity(op);
                                }
                            }
                        });
                    }
                }

            }).start();

        }
        updateLocation();

        popup.setSize(pane.getSize());
        popup.getContentPane().add(pane);

    }

    private void hide() {
        if (popup != null) {
            popup.setVisible(false);
            popup.getContentPane().removeAll();
            popup.dispose();
            popup = null;
        }

    }

    public void updateLocation() {
        if (popup != null && popup.isVisible()) {
            Point locaton = base.getLocationOnScreen();
            Point newLocation = new Point((int) (locaton.getX() + buff.getX()), (int) (locaton.getY() + buff.getY()));
            if (!newLocation.equals(popup.getLocationOnScreen()))
                popup.setLocation((int) (locaton.getX() + buff.getX()), (int) (locaton.getY() + buff.getY()));
        }
    }
}
