package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.ActiveClientPanel;
import com.teri.systemtracking.Server.GUI.ContentPanel;
import com.teri.systemtracking.Server.GUI.EditClientPanel;
import com.teri.systemtracking.Server.core.ClientListener;
import com.teri.systemtracking.Server.core.ClientRepository;
import com.teri.systemtracking.Server.core.FolderTracker;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.Server.core.filter.CompositeClientFilter;
import com.teri.systemtracking.Server.core.filter.DateFilter;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.Server.navigate.Navigator;

import javax.swing.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ContentPresenter implements Navigator, Presenter {

      private final ContentPanel contentPanel;
      private final CompositeClientFilter clientFilter = new CompositeClientFilter();
      private final Map<Client, ActiveClientPresenter> clientPresenters;
      private final NotificationPresenter notificationPresenter;
      private final ClientRepository clientRepository;
      private EditClientPresenter currentEditClientPresenter = null;

      private final ClientListener clientListener = new ClientListener() {

            @Override
            public void onClientConnected(Client client) {
                  ActiveClientPresenter clientPresenter = new ActiveClientPresenter(client, ContentPresenter.this, clientRepository);
                  clientPresenters.put(client, clientPresenter);
                  contentPanel.getActiveUserListPanel().addConnectionPanel(client.getId(), clientPresenter.getView());
                  clientPresenter.start();
            }

            @Override
            public void onClientDisconnected(Client client) {
                  ActiveClientPresenter clientPresenter = clientPresenters.remove(client);
                  if (clientPresenter != null) {
                        clientPresenter.stop();
                        contentPanel.getActiveUserListPanel().removeConnectionPanel(client.getId());
                        if (currentEditClientPresenter != null && currentEditClientPresenter.getClient().equals(client)) {
                              currentEditClientPresenter.stop();
                              currentEditClientPresenter = null;
                              navigateToHome();
                        }
                  }
            }
      };


      public ContentPresenter(ContentPanel contentPanel, ClientRepository clientRepository) {
            this.clientRepository = clientRepository;
            this.contentPanel = contentPanel;
            this.clientPresenters = new HashMap<>();
            this.notificationPresenter = new NotificationPresenter(contentPanel, clientRepository);
      }

      @Override
      public void start() {
            this.notificationPresenter.start();
            this.contentPanel.setPresenter(this);
            this.clientRepository.addClientListener(clientListener);
      }

      @Override
      public void stop() {
            this.notificationPresenter.stop();
            this.contentPanel.setPresenter(null);
            this.clientRepository.removeClientListener(clientListener);

            if (currentEditClientPresenter != null) {
                  currentEditClientPresenter.stop();
                  currentEditClientPresenter = null;
            }

            for (ActiveClientPresenter clientPresenter : clientPresenters.values()) {
                  clientPresenter.stop();
            }

      }

      private void showClientList() {
            Map<Integer, ActiveClientPanel> activeClientPanelMap = new TreeMap<>();
            for (Map.Entry<Client, ActiveClientPresenter> clientEntry : clientPresenters.entrySet()) {
                  Client client = clientFilter.filter(clientEntry.getKey());
                  if (client == null)
                        continue;
                  activeClientPanelMap.put(client.getId(), clientEntry.getValue().getView());
            }
            this.contentPanel.showClientList(activeClientPanelMap);
      }

      public void filterByDate(Date date) {
            clientFilter.addFilter(new DateFilter(date));
            showClientList();
      }

      @Override
      public void navigateTo(Client client) {
            if (
                  (currentEditClientPresenter != null &&
                        client.equals(currentEditClientPresenter.getClient()))
                        || !clientPresenters.containsKey(client))
                  return;
            if (currentEditClientPresenter != null) {
                  currentEditClientPresenter.stop();
            }
            MessageSender sender = clientRepository.getSender(client);
            FolderTracker folderTracker = clientRepository.getFolderTracker(client);

            EditClientPresenter presenter = new EditClientPresenter(client, folderTracker, sender);
            contentPanel.showClientPanel((EditClientPanel) presenter.getView());
            presenter.start();

            currentEditClientPresenter = presenter;
      }


      @Override
      public void navigateToHome() {
            if (currentEditClientPresenter != null) {
                  currentEditClientPresenter.stop();
                  currentEditClientPresenter = null;
            }
            clientFilter.clearFilters();
            showClientList();
      }


      @Override
      public JComponent getView() {
            return contentPanel;
      }
}
