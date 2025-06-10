package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.FilePanel;
import com.teri.systemtracking.Server.navigate.FolderNavigator;
import com.teri.systemtracking.common.model.File;

import javax.swing.*;

public class FilePresenter implements Presenter {
      private FileSelectionPresenter.TrackPreparation trackPreparation;
      private final FolderNavigator navigator;
      private final FilePanel filePanel;
      private final File file;
      private boolean selected;

      public FilePresenter(
            FilePanel filePanel,
            File file,
            FolderNavigator navigator) {
            this.navigator = navigator;
            this.filePanel = filePanel;
            this.file = file;
      }

      public void setTrackPreparation(FileSelectionPresenter.TrackPreparation trackPreparation) {
            this.trackPreparation = trackPreparation;
      }

      void setSelected(boolean selected) {
            this.selected = selected;
            filePanel.setSelected(selected);
      }

      public File getFile() {
            return file;
      }

      public void onClicked() {
            if (!selected)
                  if (trackPreparation != null)
                        trackPreparation.prepare();
      }

      public void onDoubleClicked() {
            if (!file.isFile())
                  navigator.navigateToFolder(file.getPath());
      }

      @Override
      public void start() {
            filePanel.initView(file.getName());
            filePanel.setPresenter(this);
      }

      @Override
      public void stop() {
            filePanel.setPresenter(null);
      }

      @Override
      public JComponent getView() {
            return filePanel;
      }
}
