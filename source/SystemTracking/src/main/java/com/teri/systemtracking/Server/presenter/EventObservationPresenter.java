package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.EventObservationPanel;
import com.teri.systemtracking.Server.GUI.EventPanel;
import com.teri.systemtracking.Server.core.FolderTracker;
import com.teri.systemtracking.common.model.Event;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import javax.swing.*;

public class EventObservationPresenter implements Presenter {

      private final EventObservationPanel eventObservationPanel;
      private final FolderTracker folderTracker;
      private Disposable streamDisposable;

      public EventObservationPresenter(EventObservationPanel eventObservationPanel, FolderTracker folderTracker) {
            this.eventObservationPanel = eventObservationPanel;
            this.folderTracker = folderTracker;
      }

      @Override
      public void start() {
            streamDisposable = folderTracker.getEventStream().subscribe(new Consumer<Event>() {
                  @Override
                  public void accept(Event event) throws Throwable {
                        if (!event.getAction().equals("track") && !event.getAction().equals("untrack")) {
                              final EventPanel eventPanel = new EventPanel();
                              eventPanel.initView(event);
                              eventObservationPanel.addEvent(eventPanel);
                        }
                  }
            });
            eventObservationPanel.setPresenter(this);
      }

      public FolderTracker getFolderTracker() {
            return folderTracker;
      }

      @Override
      public void stop() {
            if (streamDisposable != null) {
                  streamDisposable.dispose();
                  streamDisposable = null;
            }
            eventObservationPanel.clearEvents();
            eventObservationPanel.setPresenter(null);
      }

      @Override
      public JComponent getView() {
            return eventObservationPanel;
      }
}
