package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.DriveEntityPanel;
import com.teri.systemtracking.Server.GUI.FilePanel;
import com.teri.systemtracking.Server.GUI.FileSelectionPanel;
import com.teri.systemtracking.Server.core.FolderTracker;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.Server.navigate.FolderNavigator;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.model.File;
import com.teri.systemtracking.common.model.Folder;
import io.reactivex.rxjava3.functions.Consumer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FileSelectionPresenter extends AbstractSendDisposablePresenter implements FolderNavigator {

      private final List<DrivePresenter> drivePresenters = new ArrayList<>();
      private final FileSelectionPanel fileSelectionPanel;
      private final SearchPresenter searchPresenter;
      private final Stack<Folder> folderStack = new Stack<>();
      private final FolderTracker folderTracker;

      private OpenedFolder openedFolder;
      private TrackPreparation currentPreparation;

      public FileSelectionPresenter(FileSelectionPanel fileSelectionPanel, MessageSender messageSender, FolderTracker folderTracker) {
            super(messageSender);
            this.folderTracker = folderTracker;
            this.fileSelectionPanel = fileSelectionPanel;
            this.searchPresenter = new SearchPresenter(fileSelectionPanel.getSearchBar(), messageSender, this);

            for (Folder drive : folderTracker.getDrives()) {
                  DriveEntityPanel driveEntityPanel = new DriveEntityPanel();
                  driveEntityPanel.setAlignmentX(0.5f);
                  DrivePresenter drivePresenter = new DrivePresenter(driveEntityPanel, this, drive.getPath());
                  drivePresenters.add(drivePresenter);
            }
      }

      public void trackSelectedFile() {
            if (currentPreparation == null)
                  return;

            String path = currentPreparation.filePresenter.getFile().getPath();
            folderTracker.track(path);
      }

      @Override
      public void navigateToDrive(String path) {
            for (DrivePresenter drivePresenter : drivePresenters) {
                  drivePresenter.setActive(drivePresenter.getDrive().equals(path));
            }
            navigateToFolder(path);
      }

      public void navigateToFolder(String path) {
            Message message = new Message("list", path);
            send(message, new Consumer<Object>() {
                  @Override
                  public void accept(Object o) throws Throwable {
                        Folder folder = (Folder) o;
                        onFolderChanged(folder);
                  }
            });
      }

      public void navigateBack() {
            if (folderStack.size() <= 1)
                  return;
            folderStack.pop();
            Folder previous = folderStack.pop();
            onFolderChanged(previous);
      }


      private void onFolderChanged(Folder folder) {
            folderStack.push(folder);
            final List<FilePanel> list = new ArrayList<>();
            final List<FilePresenter> filePresenterList = new ArrayList<>();
            for (File file : folder.getFiles()) {

                  FilePanel filePanel = new FilePanel();
                  FilePresenter filePresenter = new FilePresenter(filePanel, file, this);
                  TrackPreparation trackPreparation = new TrackPreparation(filePresenter);
                  filePresenter.setTrackPreparation(trackPreparation);
                  filePresenterList.add(filePresenter);
                  list.add(filePanel);
            }

            fileSelectionPanel.setEnteredFolder(folder.getPath());
            fileSelectionPanel.getListFilePanel().removeAll();
            for (FilePanel panel : list)
                  fileSelectionPanel.getListFilePanel().add(panel);

            fileSelectionPanel.getListFilePanel().revalidate();
            openedFolder = new OpenedFolder(folder, filePresenterList);
            for (FilePresenter filePresenter : filePresenterList)
                  filePresenter.start();

      }


      @Override
      public void doStart() {
            this.fileSelectionPanel.setPresenter(this);
            this.searchPresenter.start();
            this.fileSelectionPanel.getDrivePanel().removeAll();
            for (DrivePresenter drivePresenter : drivePresenters)
                  this.fileSelectionPanel.getDrivePanel().add(drivePresenter.getView());
            this.drivePresenters.forEach(DrivePresenter::start);
      }

      @Override
      public void doStop() {
            if (openedFolder != null) {
                  for (FilePresenter filePresenter : openedFolder.filePresenters)
                        filePresenter.stop();
                  openedFolder = null;
            }
            this.drivePresenters.forEach(DrivePresenter::stop);
            this.fileSelectionPanel.setPresenter(null);
            this.searchPresenter.stop();
      }

      @Override
      public JComponent getView() {
            return fileSelectionPanel;
      }


      public class TrackPreparation {
            private final FilePresenter filePresenter;

            public TrackPreparation(FilePresenter filePresenter) {
                  this.filePresenter = filePresenter;
            }

            public void prepare() {
                  if (currentPreparation != null)
                        currentPreparation.filePresenter.setSelected(false);
                  currentPreparation = this;
                  filePresenter.setSelected(true);
            }
      }

      static class OpenedFolder {
            private final Folder folder;
            private final List<FilePresenter> filePresenters;

            OpenedFolder(Folder folder, List<FilePresenter> filePresenters) {
                  this.folder = folder;
                  this.filePresenters = filePresenters;
            }
      }
}
