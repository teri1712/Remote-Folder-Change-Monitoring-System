package com.teri.systemtracking.Client.socketclient;

import com.teri.systemtracking.common.Packet;

import java.io.IOException;

public interface PacketSender {
      void send(Packet packet) throws IOException;
}
