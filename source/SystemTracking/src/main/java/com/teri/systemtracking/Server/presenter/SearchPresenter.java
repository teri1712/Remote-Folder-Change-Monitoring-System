package com.teri.systemtracking.Server.presenter;

import com.teri.systemtracking.Server.GUI.FileSearchBar;
import com.teri.systemtracking.Server.core.MessageSender;
import com.teri.systemtracking.Server.navigate.FolderNavigator;
import com.teri.systemtracking.common.Message;
import com.teri.systemtracking.common.model.File;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;

import javax.swing.*;
import java.util.List;

public class SearchPresenter extends AbstractSendDisposablePresenter {

      private final FileSearchBar searchBar;
      private final FolderNavigator navigator;
      private SearchQuery currentSearch;

      static class SearchQuery {
            private final String path;
            private final Disposable disposable;

            SearchQuery(String path, Disposable disposable) {
                  this.path = path;
                  this.disposable = disposable;
            }
      }

      public SearchPresenter(FileSearchBar searchBar,
                             MessageSender sender,
                             FolderNavigator navigator) {
            super(sender);
            this.searchBar = searchBar;
            this.navigator = navigator;
      }

      public void searchByPath(final String path) throws InterruptedException {
            if (currentSearch != null && path.equals(currentSearch.path))
                  return;
            Message message = new Message("search", path);
            Disposable disposable = send(message, new Consumer<Object>() {
                  @Override
                  public void accept(Object o) throws Throwable {
                        List<File> result = (List<File>) o;
                        onSearchResult(path, result);
                  }
            });
            currentSearch = new SearchQuery(path, disposable);
      }

      public FolderNavigator getNavigator() {
            return navigator;
      }

      private void onSearchResult(String query, List<File> files) {
            if (!query.equals(currentSearch.path)) {
                  return;
            }
            searchBar.setRecommendationList(files);
      }

      @Override
      public void doStart() {
            this.searchBar.setPresenter(this);
      }

      @Override
      public void doStop() {
            this.searchBar.setPresenter(null);
      }

      @Override
      public JComponent getView() {
            return searchBar;
      }
}
