package com.teri.systemtracking.Server.navigate;

public interface FolderNavigator {

      void navigateToDrive(String path);

      void navigateToFolder(String path);

      void navigateBack();
}
