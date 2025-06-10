package com.teri.systemtracking.common;

import java.io.Serializable;

public class Message implements Serializable {

      private final String command;
      private final Object content;

      public Message(String command, Object content) {
            this.command = command;
            this.content = content;
      }

      public Object getContent() {
            return content;
      }

      public String getCommand() {
            return command;
      }
}
