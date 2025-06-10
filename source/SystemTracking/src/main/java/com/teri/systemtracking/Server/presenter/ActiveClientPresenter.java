package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.ActiveClientPanel;
import com.teri.systemtracking.Server.core.ClientRepository;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.Server.navigate.Navigator;

public class ActiveClientPresenter implements Presenter {

      private final Client client;
      private final Navigator navigator;
      private final ActiveClientPanel activeClientPanel;
      private final ClientRepository clientRepository;

      public ActiveClientPresenter(Client client, Navigator navigator, ClientRepository clientRepository) {
            this.clientRepository = clientRepository;
            this.navigator = navigator;
            this.client = client;
            this.activeClientPanel = new ActiveClientPanel();
      }

      public void navigateToClient() {
            navigator.navigateTo(client);
      }

      public void deleteSelf() {
            clientRepository.remove(client);
      }

      public Client getClient() {
            return client;
      }

      @Override
      public void start() {
            activeClientPanel.setPresenter(this);
            activeClientPanel.initView(client);
      }

      @Override
      public void stop() {
            activeClientPanel.setPresenter(null);
      }

      public ActiveClientPanel getView() {
            return activeClientPanel;
      }
}
