package com.teri.systemtracking.Server;

import com.teri.systemtracking.Server.GUI.MainFrame;
import com.teri.systemtracking.Server.presenter.ContentPresenter;
import com.teri.systemtracking.Server.server.Server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ServerApp {
      public static void main(String[] args) throws IOException {
            try {
                  for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        System.out.println(info.getName());
                        if ("Windows".equals(info.getName())) {
                              javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        }
                  }
            } catch (ClassNotFoundException ex) {
                  java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                  java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                  java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                  java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            Server server = new Server();
            try {
                  java.awt.EventQueue.invokeAndWait(new Runnable() {
                        public void run() {
                              MainFrame mainFrame = new MainFrame();
                              mainFrame.setVisible(true);
                              new ContentPresenter(mainFrame.getContentPanePanel(), server).start();
                        }
                  });
            } catch (InvocationTargetException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
            } catch (InterruptedException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
            }
            server.startServer();
      }
}
