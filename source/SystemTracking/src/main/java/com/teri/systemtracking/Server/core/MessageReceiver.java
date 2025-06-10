package com.teri.systemtracking.Server.core;

import com.teri.systemtracking.Server.model.ReceivedMessage;

public interface MessageReceiver {
      void onMessage(ReceivedMessage message);
}
