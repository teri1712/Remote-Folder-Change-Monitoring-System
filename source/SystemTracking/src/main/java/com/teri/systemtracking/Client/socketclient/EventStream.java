package com.teri.systemtracking.Client.socketclient;

import com.teri.systemtracking.common.Packet;

import java.io.IOException;

public class EventStream {
      private final PacketSender sender;
      private boolean running = true;

      public EventStream(PacketSender sender) {
            this.sender = sender;
      }

      public synchronized void send(Packet packet) throws IOException {
            if (!running)
                  throw new IllegalStateException("tracking path no longer tracked");
            sender.send(packet);
      }

      synchronized void stop() {
            running = false;
      }
}
