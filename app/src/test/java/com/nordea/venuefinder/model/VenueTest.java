package com.nordea.venuefinder.model;

import com.nordea.venuefinder.api.dto.Location;
import com.nordea.venuefinder.api.dto.Venue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class VenueTest {

  @Test
  @Parameters(method = "addVenueParams")
  public void fromApiVenue(Venue apiVenue, com.nordea.venuefinder.model.Venue expectedModelVenue) {
    com.nordea.venuefinder.model.Venue modelVenue = com.nordea.venuefinder.model.Venue.fromApiVenue(apiVenue);

    Assert.assertEquals(modelVenue, expectedModelVenue);
  }

  private Object[] addVenueParams() {
    return new Object[]{
      new Object[]{
        new Venue("1", "name1", new Location("address1", 789)),
        com.nordea.venuefinder.model.Venue.builder()
          .id("1")
          .name("name1")
          .address("address1")
          .distance("789 m")
          .build()
      },
      new Object[]{
        new Venue("2", "name2", new Location("address2", 1000)),
        com.nordea.venuefinder.model.Venue.builder()
          .id("2")
          .name("name2")
          .address("address2")
          .distance("1.0 km")
          .build()
      },
      new Object[]{
        new Venue("3", "name3", new Location("address3", 1234.56)),
        com.nordea.venuefinder.model.Venue.builder()
          .id("3")
          .name("name3")
          .address("address3")
          .distance("1.2 km")
          .build()
      },
      new Object[]{
        new Venue("4", "name4", new Location("address4", 1567.89)),
        com.nordea.venuefinder.model.Venue.builder()
          .id("4")
          .name("name4")
          .address("address4")
          .distance("1.6 km")
          .build()
      }
    };
  }
}
