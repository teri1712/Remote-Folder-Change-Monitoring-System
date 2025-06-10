package com.teri.systemtracking.Server.presenter;

import javax.swing.*;

public interface Presenter {

      //TODO: migrate to lifecycle aware for Message Sender
      void start();

      default void restart() {
            stop();
            start();
      }

      void stop();

      JComponent getView();
}
