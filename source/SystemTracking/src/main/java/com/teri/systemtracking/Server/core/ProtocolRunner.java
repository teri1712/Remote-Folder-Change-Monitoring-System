package com.teri.systemtracking.Server.core;

import com.teri.systemtracking.Server.core.protocol.ProtocolHandler;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.common.Packet;

public class ProtocolRunner implements Runnable {
      private final ProtocolHandler protocolHandler;
      private final Packet packet;
      private final Client client;
      private final MessageReceiver receiver;

      public ProtocolRunner(ProtocolHandler protocolHandler, MessageReceiver receiver, Client client, Packet packet) {
            this.protocolHandler = protocolHandler;
            this.client = client;
            this.packet = packet;
            this.receiver = receiver;
      }

      @Override
      public void run() {
            protocolHandler.handle(client, packet, receiver);
      }
}
