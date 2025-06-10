package com.teri.systemtracking.common.model;

import java.util.List;

public class Folder extends File {
      private final List<File> files;

      public Folder(String path, String name, List<File> files) {
            super(name, path);
            this.files = files;
      }

      public List<File> getFiles() {
            return files;
      }

      @Override
      public boolean isFile() {
            return false;
      }
}
