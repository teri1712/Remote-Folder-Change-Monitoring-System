package com.teri.systemtracking.Server.core.filter;

import com.teri.systemtracking.Server.model.Client;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DateFilter implements ClientFilter {
      private final Date date;

      public DateFilter(Date date) {
            this.date = date;
      }

      @Override
      public Client filter(Client client) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(date);
            c2.setTime(client.getCreatedDate());
            return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                  && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) ? client : null;
      }

      @Override
      public boolean equals(Object o) {
            if (!(o instanceof DateFilter)) return false;
            DateFilter that = (DateFilter) o;
            return Objects.equals(date, that.date);
      }

      @Override
      public int hashCode() {
            return Objects.hashCode(date);
      }
}
