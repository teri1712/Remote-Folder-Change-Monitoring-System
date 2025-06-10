package com.teri.systemtracking.Server.core;

import com.teri.systemtracking.Server.model.Client;

public interface ClientListener {

      void onClientConnected(Client client);

      void onClientDisconnected(Client client);
}
