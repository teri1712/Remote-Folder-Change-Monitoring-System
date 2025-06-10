package com.teri.systemtracking.Server.model;

import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.Packet;

public class ReceivedMessage extends Message {
      private Packet rawPacket;

      public ReceivedMessage(Message message) {
            super(message.getCommand(), message.getContent());
      }

      public void setRawPacket(Packet rawPacket) {
            this.rawPacket = rawPacket;
      }

      public Packet getRawPacket() {
            return rawPacket;
      }
}
