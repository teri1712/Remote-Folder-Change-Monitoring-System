package com.teri.systemtracking.Server.core.filter;


import com.teri.systemtracking.Server.model.Client;

import java.util.ArrayList;
import java.util.List;

public class CompositeClientFilter implements ClientFilter {

      private final List<ClientFilter> filters = new ArrayList<>();


      public void addFilter(ClientFilter filter) {
            filters.add(filter);
      }

      public void removeFilter(ClientFilter filter) {
            filters.remove(filter);
      }

      public void clearFilters() {
            filters.clear();
      }

      @Override
      public Client filter(Client client) {
            for (ClientFilter filter : filters) {
                  client = filter.filter(client);
                  if (client == null)
                        break;
            }
            return client;
      }
}
