package com.teri.systemtracking.common.model;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

      private final String path;
      private final String action;
      private final Date date;

      public Event(String path, String action, Date date) {
            this.path = path;
            this.action = action;
            this.date = date;
      }

      public String getPath() {
            return path;
      }

      public String getAction() {
            return action;
      }

      public Date getDate() {
            return date;
      }
}
