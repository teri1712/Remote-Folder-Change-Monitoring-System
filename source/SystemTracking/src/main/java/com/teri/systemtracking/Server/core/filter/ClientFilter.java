package com.teri.systemtracking.Server.core.filter;

import com.teri.systemtracking.Server.model.Client;

public interface ClientFilter {
      Client filter(Client client);
}
