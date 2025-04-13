package com.teri.systemtracking.Server.Server;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.teri.systemtracking.Server.ClientWorker.Client;
import com.teri.systemtracking.Server.GUI.ContentPanePanel;

public class CentralWorker {
   private ContentPanePanel contentPane;
   private int cnt;
   private ServerSocket server;
   private HashMap<String, ArrayList<Client>> connectionRepo;
   private Client isChosenToEdit;
   private Thread centralChannel;
   private BlockingQueue<Runnable> centralHandlerQueue;

   public CentralWorker(ContentPanePanel contentPanePanel) throws IOException {
      this.contentPane = contentPanePanel;
      isChosenToEdit = null;
      server = new ServerSocket(6969);
      connectionRepo = new HashMap<>();
      cnt = 0;

      /* set triggered events */
      contentPane.getShowAllButton().addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               ArrayList<JPanel> l = new ArrayList<>();
               synchronized (connectionRepo) {
                  for (Map.Entry<String, ArrayList<Client>> i : connectionRepo.entrySet()) {
                     for (Client cl : i.getValue()) {
                        l.add(cl.getActiveUserEntityPanel());
                     }
                  }
               }
               contentPane.replaceAllInList(l);
            } catch (Exception ex) {
               JOptionPane.showMessageDialog(null, "invalid date");
            }

         }
      });
      contentPane.getSearchByDate().addHandlerActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            try {
               Date date = Date.valueOf(contentPane.getSearchByDate().getTextField().getText());
               String d = date.toString();
               ArrayList<JPanel> l = new ArrayList<>();
               synchronized (connectionRepo) {
                  ArrayList<Client> take = connectionRepo.get(d);
                  if (take != null) {
                     for (Client cl : take)
                        l.add(cl.getActiveUserEntityPanel());
                  }
               }
               contentPane.replaceAllInList(l);
            } catch (Exception ex) {
               JOptionPane.showMessageDialog(null, "invalid date");
            }

         }

      });

      /* central worker channel */
      centralHandlerQueue = new ArrayBlockingQueue<Runnable>(100, true);
      centralChannel = new Thread(new Runnable() {

         @Override
         public void run() {
            while (true) {
               try {
                  centralHandlerQueue.take().run();
               } catch (InterruptedException e) {
               }
            }
         }

      });
      centralChannel.start();
      /* tcp client listener */
      new Thread(new Runnable() {
         @Override
         public void run() {
            while (true) {
               try {
                  Socket client = server.accept();

                  final Date today = new Date(System.currentTimeMillis());
                  final Client cl = new Client(++cnt, client, CentralWorker.this, today);

                  final String contentNotification = "Client#" + Integer.toString(cl.getId()) + " has been connected";
                  String createdDate = today.toString();
                  java.awt.EventQueue.invokeLater(new Runnable() {
                     public void run() {
                        contentPane.setNotification(1, 0, contentNotification, today);
                     }
                  });
                  synchronized (connectionRepo) {
                     if (!connectionRepo.containsKey(createdDate)) {
                        connectionRepo.put(createdDate, new ArrayList<Client>());
                     }
                     connectionRepo.get(createdDate).add(cl);
                  }
                  java.awt.EventQueue.invokeLater(new Runnable() {
                     public void run() {
                        contentPane.addConnectionEntity(cl.getActiveUserEntityPanel());
                     }
                  });

               } catch (Exception e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }

         }

      }).start();

   }

   /* central worker api */

   public void removeConnection(final Client cl) throws InterruptedException {
      centralHandlerQueue.put(new Runnable() {
         @Override
         public void run() {
            String createdDate = cl.getCreatedDate().toString();

            synchronized (connectionRepo) {
               connectionRepo.get(createdDate).remove(cl);

               if (connectionRepo.get(createdDate).isEmpty()) {
                  connectionRepo.remove(createdDate);
               }
            }
            final String contentNotification = "Client#" + Integer.toString(cl.getId()) + " has been closed";
            final Date cur = new Date(System.currentTimeMillis());
            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  if (isChosenToEdit == cl) {
                     isChosenToEdit = null;
                     contentPane.showConnectionList();
                  }
                  contentPane.setNotification(0, 1, contentNotification, cur);
                  contentPane.removeConnectionEntity(cl.getActiveUserEntityPanel());
               }
            });
         }
      });

   }

   public void showClientSession(final JPanel pnl, final Client owner) throws InterruptedException {
      centralHandlerQueue.put(new Runnable() {
         @Override
         public void run() {
            java.awt.EventQueue.invokeLater(new Runnable() {
               public void run() {
                  isChosenToEdit = owner;
                  contentPane.showEditSession(pnl);
               }
            });
         }
      });
   }

}
