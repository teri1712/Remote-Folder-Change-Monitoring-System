package com.teri.systemtracking;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.teri.systemtracking.Server.GUI.MainFrame;
import com.teri.systemtracking.Server.Server.CentralWorker;

/**
 * Hello world!
 * 
 */
public class App {
    public static void main(String[] args) throws IOException {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
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
        final MainFrame mainFrame = new MainFrame();
        try {
            java.awt.EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    mainFrame.setVisible(true);
                }
            });
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new CentralWorker(mainFrame.getContentPanePanel());
    }
}
