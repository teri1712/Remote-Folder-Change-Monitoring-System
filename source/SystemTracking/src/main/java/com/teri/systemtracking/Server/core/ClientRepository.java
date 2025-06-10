package com.teri.systemtracking.Server.core;


import com.teri.systemtracking.Server.model.Client;

public interface ClientRepository {

      FolderTracker getFolderTracker(Client client);

      MessageSender getSender(Client client);

      void remove(Client client);

      void addClientListener(ClientListener listener);

      void removeClientListener(ClientListener listener);
}
