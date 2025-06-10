package com.teri.systemtracking.Server.GUI;

import com.jhlabs.image.GaussianFilter;
import com.teri.systemtracking.Server.presenter.SearchPresenter;
import com.teri.systemtracking.common.model.File;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileSearchBar extends SearchBar {
      private SearchPresenter presenter;
      private PopupWindow popup;
      private JPanel rcmList;

      public FileSearchBar() {
            super();
            popup = new PopupWindow();
            popup.setBaseLocation(this);
            popup.setBuffLocation(0, (int) this.getPreferredSize().getHeight());

            setRoundX(8);
            setRoundY(8);
            JPanel rcmPane = new JPanel() {

                  @Override
                  public void paintComponent(Graphics g) {
                        Dimension round = getRound();
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
            getTextField().addFocusListener(new FocusListener() {

                  @Override
                  public void focusGained(FocusEvent e) {
                  }

                  @Override
                  public void focusLost(FocusEvent e) {
                        popup.setVisible(false);
                  }
            });
            getTextField().addKeyListener(new KeyAdapter() {
                  @Override
                  public void keyReleased(KeyEvent e) {
                        popup.setVisible(true);
                  }
            });

            addComponentListener(new ComponentListener() {
                  @Override
                  public void componentResized(ComponentEvent e) {
                        Dimension d = FileSearchBar.this.getSize();
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
            getTextField().addHierarchyListener(new HierarchyListener() {

                  @Override
                  public void hierarchyChanged(HierarchyEvent e) {
                  }

            });
            initActions();
      }

      public void setPresenter(SearchPresenter presenter) {
            this.presenter = presenter;
      }

      private void initActions() {
            getTextField().getDocument().addDocumentListener(new DocumentListener() {

                  @Override
                  public void insertUpdate(DocumentEvent e) {
                        String path = getTextField().getText();
                        if (path.equals("search by path") || path.isEmpty()) {
                              setRecommendationList(Collections.emptyList());
                              return;
                        }
                        try {
                              if (presenter != null)
                                    presenter.searchByPath(path);
                        } catch (InterruptedException e1) {
                              // TODO Auto-generated catch block
                              e1.printStackTrace();
                        }
                  }

                  @Override
                  public void removeUpdate(DocumentEvent e) {

                        String path = getTextField().getText();
                        if (path.isEmpty()) {
                              setRecommendationList(Collections.emptyList());
                              return;
                        }
                        try {
                              presenter.searchByPath(path);
                        } catch (InterruptedException e1) {
                              // TODO Auto-generated catch block
                              e1.printStackTrace();
                        }

                  }

                  @Override
                  public void changedUpdate(DocumentEvent e) {

                  }

            });
            addListener(new ActionListener() {
                  @Override
                  public void actionPerformed(ActionEvent e) {
                        requestFocusInWindow();
                        if (presenter != null)
                              presenter.getNavigator().navigateToFolder(getTextField().getText());
                  }

            });
      }

      public void setRecommendationList(List<File> files) {
            List<JComponent> searchResults = new ArrayList<>();
            for (File file : files) {
                  String path = file.getPath();
                  SearchEntityPanel e = new SearchEntityPanel();
                  e.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                              presenter.getNavigator().navigateToFolder(path);
                              requestFocusInWindow();
                        }
                  });
                  e.setContent(path);
                  searchResults.add(e);
            }
            setRecommendationComponents(searchResults);
      }

      private void setRecommendationComponents(List<JComponent> recommendations) {
            rcmList.removeAll();
            int h = 30;
            h *= recommendations.size();
            for (JComponent rcm : recommendations) {
                  rcm.setAlignmentX(0);
                  rcmList.add(rcm);
            }
            h = Math.min(230, h);
            popup.setSize(new Dimension(popup.getPane().getWidth(), h + 20));
            rcmList.repaint();
            rcmList.revalidate();
      }

}
