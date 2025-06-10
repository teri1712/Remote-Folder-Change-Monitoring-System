package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.DriveEntityPanel;
import com.teri.systemtracking.Server.navigate.FolderNavigator;

import javax.swing.*;

public class DrivePresenter implements Presenter {
      private final String drive;
      private final DriveEntityPanel driveEntityPanel;
      private final FolderNavigator navigator;

      public DrivePresenter(DriveEntityPanel driveEntityPanel, FolderNavigator navigator, String drive) {
            this.navigator = navigator;
            this.driveEntityPanel = driveEntityPanel;
            this.drive = drive;
      }

      void setActive(boolean active) {
            driveEntityPanel.setIsActive(active);
      }

      public String getDrive() {
            return drive;
      }

      public void navigateToDrive() {
            navigator.navigateToDrive(drive);
      }

      @Override
      public void start() {
            driveEntityPanel.setPresenter(this);
            driveEntityPanel.initView(drive);
      }

      @Override
      public void stop() {
            driveEntityPanel.setPresenter(null);
      }

      @Override
      public JComponent getView() {
            return driveEntityPanel;
      }
}
