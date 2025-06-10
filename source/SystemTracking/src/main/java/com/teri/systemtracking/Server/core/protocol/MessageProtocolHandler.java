package com.teri.systemtracking.Server.core.protocol;

import com.teri.systemtracking.Server.core.MessageReceiver;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.Server.model.ReceivedMessage;
import com.teri.systemtracking.common.Packet;
import com.teri.systemtracking.common.utils.SerializeUtils;

public class MessageProtocolHandler implements ProtocolHandler {

      public MessageProtocolHandler() {
      }

      @Override
      public boolean supports(Packet packet) {
            byte[] data = packet.getPayload();
//            try {
//                  Message message = SerializeUtils.deserialize(data);
//                  return message != null;
//            } catch (ClassCastException e) {
//                  return false;
//            }
            return data != null && data.length >= 2
                  && (data[0] & 0xFF) == 0xAC
                  && (data[1] & 0xFF) == 0xED; // Serializable Magic bytes

      }


      @Override
      public final void handle(Client client, Packet packet, MessageReceiver receiver) {
            byte[] payload = packet.getPayload();
            ReceivedMessage message = new ReceivedMessage(SerializeUtils.deserialize(payload));
            message.setRawPacket(packet);
            receiver.onMessage(message);
      }
}
