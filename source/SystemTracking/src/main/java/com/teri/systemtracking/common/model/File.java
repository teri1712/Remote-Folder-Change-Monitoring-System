package com.teri.systemtracking.common.model;

import java.io.Serializable;

public class File implements Serializable {
      private final String name;
      private final String path;

      public File(String name, String path) {
            this.name = name;
            this.path = path;
      }

      public boolean isFile() {
            return true;
      }

      public String getName() {
            return name;
      }

      public String getPath() {
            return path;
      }
}
