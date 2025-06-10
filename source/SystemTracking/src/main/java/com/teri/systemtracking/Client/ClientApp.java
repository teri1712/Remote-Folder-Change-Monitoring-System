package com.teri.systemtracking.Client;

import com.teri.systemtracking.Client.GUI.MainFrame;
import com.teri.systemtracking.Client.socketclient.Client;

public class ClientApp {
      public static void main(String args[]) {
            /* Set the Nimbus look and feel */
            // <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
            // (optional) ">
            /*
             * If Nimbus (introduced in Java SE 6) is not available, stay with the default
             * look and feel.
             * For details see
             * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            try {
                  for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                              javax.swing.UIManager.setLookAndFeel(info.getClassName());
                              break;
                        }
                  }
            } catch (ClassNotFoundException ex) {
            } catch (InstantiationException ex) {
            } catch (IllegalAccessException ex) {
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            }
            Client client = new Client();
            java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run() {
                        new MainFrame(client).setVisible(true);
                  }
            });
      }
}
