package com.teri.systemtracking.Server.server;

import com.teri.systemtracking.Server.core.FolderTracker;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.model.Event;
import com.teri.systemtracking.common.model.Folder;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.util.Date;
import java.util.List;

public class ClientFolderTracker implements FolderTracker {

      BehaviorSubject<Event> eventStream = BehaviorSubject.create();
      private final MessageSender sender;
      private final List<Folder> drives;
      private String tracking;
      private boolean hasRequest;

      public ClientFolderTracker(MessageSender sender, List<Folder> drives) {
            this.drives = drives;
            this.sender = sender;
      }

      @Override
      public List<Folder> getDrives() {
            return drives;
      }

      @Override
      public void track(String path) {
            if (path.equals(tracking) || hasRequest)
                  return;
            hasRequest = true;
            tracking = path;
            Message message = new Message("track", path);
            sender.send(message).subscribe(new Consumer<Object>() {
                  @Override
                  public void accept(Object o) throws Throwable {
                        int status = (int) o;
                        if (status == -1) {
                              tracking = null;
                              eventStream.onComplete();
                        } else {
                              Event event = new Event(path, "track", new Date());
                              eventStream.onNext(event);
                        }
                        hasRequest = false;
                  }
            });
      }

      @Override
      public void untrack() {
            if (tracking == null || hasRequest)
                  return;
            hasRequest = true;
            Message message = new Message("untrack", tracking);
            sender.send(message).subscribe(new Consumer<Object>() {
                  @Override
                  public void accept(Object o) throws Throwable {
                        int status = (int) o;
                        if (status != -1) {
                              Event event = new Event(tracking, "untrack", new Date());
                              eventStream.onNext(event);
                        }
                        tracking = null;
                        hasRequest = false;
                  }
            });
      }

      @Override
      public Observable<Event> getEventStream() {
            return eventStream;
      }
}
