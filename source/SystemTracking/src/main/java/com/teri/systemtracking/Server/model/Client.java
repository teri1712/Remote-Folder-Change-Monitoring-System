package com.teri.systemtracking.Server.model;

import java.util.Date;
import java.util.Objects;

public class Client {
      private final int id;
      private final Date createdDate;

      public Client(int id, Date createdDate) {
            this.id = id;
            this.createdDate = createdDate;
      }

      public int getId() {
            return id;
      }

      public Date getCreatedDate() {
            return createdDate;
      }

      @Override
      public boolean equals(Object o) {
            if (!(o instanceof Client)) return false;
            Client client = (Client) o;
            return id == client.id;
      }

      @Override
      public int hashCode() {
            return Objects.hashCode(id);
      }
}
