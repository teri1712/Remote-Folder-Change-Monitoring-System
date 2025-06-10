package com.teri.systemtracking.Server.navigate;

import com.teri.systemtracking.Server.model.Client;

public interface Navigator {

      void navigateTo(Client client);

      void navigateToHome();
}
