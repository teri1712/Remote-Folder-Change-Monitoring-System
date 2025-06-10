package com.teri.systemtracking.Server.core;

import com.teri.systemtracking.common.Message;
import io.reactivex.rxjava3.core.Observable;

public interface MessageSender {
      //TODO: Offload to background thread
      Observable<Object> send(Message message);
}
