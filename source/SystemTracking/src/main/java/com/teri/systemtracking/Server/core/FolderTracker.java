package com.teri.systemtracking.Server.core;

import com.teri.systemtracking.common.model.Event;
import com.teri.systemtracking.common.model.Folder;
import io.reactivex.rxjava3.core.Observable;

import java.util.List;

public interface FolderTracker {
      List<Folder> getDrives();

      void track(String path);

      void untrack();

      Observable<Event> getEventStream();

}
