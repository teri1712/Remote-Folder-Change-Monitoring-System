package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.BellPanel;
import com.teri.systemtracking.Server.GUI.ContentPanel;
import com.teri.systemtracking.Server.GUI.NotifyListPanel;
import com.teri.systemtracking.Server.GUI.NotifyPanel;
import com.teri.systemtracking.Server.core.ClientListener;
import com.teri.systemtracking.Server.core.ClientRepository;
import com.teri.systemtracking.Server.model.Client;

import javax.swing.*;
import java.util.Date;

public class NotificationPresenter implements Presenter {

      private final ClientRepository clientRepository;
      private final NotifyPanel notifyPanel;
      private final NotifyListPanel notifyListPanel;
      private final BellPanel bellPanel;

      private final ClientListener clientListener = new ClientListener() {
            @Override
            public void onClientConnected(Client client) {
                  final String notification = "Client#" + client.getId() + " has been connected";
                  final Date now = new Date(System.currentTimeMillis());
                  setNotification(1, 0, notification, now);
            }

            @Override
            public void onClientDisconnected(Client client) {
                  final String notification = "Client#" + client.getId() + " has been closed";
                  final Date now = new Date(System.currentTimeMillis());
                  setNotification(0, 1, notification, now);
            }
      };

      public NotificationPresenter(ContentPanel contentPanel, ClientRepository clientRepository) {
            this.clientRepository = clientRepository;
            this.bellPanel = contentPanel.getConnectionPanel();
            this.notifyPanel = contentPanel.getNotifyPanel();
            this.notifyListPanel = contentPanel.getNotifyListPanel();
      }

      private void setNotification(int opened, int closed, String content, Date createdDate) {
            notifyPanel.setAnnouncement(opened, closed);
            notifyListPanel.updateAnnouncement(content, createdDate);
            bellPanel.updateBellCntNotification();
      }

      @Override
      public void start() {
            clientRepository.addClientListener(clientListener);
      }

      @Override
      public void stop() {
            clientRepository.removeClientListener(clientListener);
      }

      @Override
      public JComponent getView() {
            return null;
      }
}
