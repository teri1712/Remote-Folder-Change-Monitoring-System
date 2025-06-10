package com.teri.systemtracking.common;

import java.io.Serializable;

public class Packet implements Serializable {
      public static final String INITIAL_REQUEST_PACKET = "DRIVES";
      public static final String EVENT_PACKET = "EVENT";
      private final String id;
      private final byte[] payload;

      public Packet(String id, byte[] payload) {
            this.id = id;
            this.payload = payload;
      }

      public String getId() {
            return id;
      }

      public byte[] getPayload() {
            return payload;
      }
}
