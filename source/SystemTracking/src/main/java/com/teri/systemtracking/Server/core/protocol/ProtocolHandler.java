package com.teri.systemtracking.Server.core.protocol;


import com.teri.systemtracking.Server.core.MessageReceiver;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.common.Packet;

public interface ProtocolHandler {

      boolean supports(Packet packet);

      void handle(Client client, Packet packet, MessageReceiver receiver);

}
