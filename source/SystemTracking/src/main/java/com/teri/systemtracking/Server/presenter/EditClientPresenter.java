package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.EditClientPanel;
import com.teri.systemtracking.Server.core.FolderTracker;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.Server.model.Client;
import com.teri.systemtracking.common.model.Event;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import javax.swing.*;

public class EditClientPresenter extends AbstractSendDisposablePresenter {

      private final EditClientPanel editClientPanel;
      private final FileSelectionPresenter fileSelectionPresenter;
      private final EventObservationPresenter eventObservationPresenter;

      private final Client client;
      private final FolderTracker folderTracker;
      private Disposable trackDisposable;

      public EditClientPresenter(Client client, FolderTracker folderTracker, MessageSender sender) {
            super(sender);
            this.client = client;
            this.folderTracker = folderTracker;
            this.editClientPanel = new EditClientPanel();
            this.fileSelectionPresenter = new FileSelectionPresenter(editClientPanel.getFileSelectonPanel(), sender, folderTracker);
            this.eventObservationPresenter = new EventObservationPresenter(editClientPanel.getObservedListPanel(), folderTracker);
      }

      @Override
      public void doStart() {
            editClientPanel.initView(client.getId());
            fileSelectionPresenter.start();
            eventObservationPresenter.start();
            trackDisposable = folderTracker.getEventStream().subscribe(new Consumer<Event>() {
                  @Override
                  public void accept(Event event) throws Throwable {
                        if (event.getAction().equals("track")) {
                              eventObservationPresenter.restart();
                              editClientPanel.showObservedListPanel();
                        } else if (event.getAction().equals("untrack")) {
                              editClientPanel.showFileSelectionPanel();
                        }
                  }
            });
      }

      @Override
      public void doStop() {
            trackDisposable.dispose();
            trackDisposable = null;
            fileSelectionPresenter.stop();
            eventObservationPresenter.stop();
      }

      public Client getClient() {
            return client;
      }

      @Override
      public JComponent getView() {
            return editClientPanel;
      }

}
