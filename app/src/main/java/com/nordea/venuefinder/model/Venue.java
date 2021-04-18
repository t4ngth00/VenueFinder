package com.nordea.venuefinder.model;

import com.nordea.venuefinder.api.dto.Location;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Venue {

  private final String id;
  private final String name;
  private final String address;
  private final String distance;

  public static Venue fromApiVenue(com.nordea.venuefinder.api.dto.Venue venue) {
    Location venueLocation = venue.getLocation();

    return Venue.builder()
      .id(venue.getId())
      .name(venue.getName())
      .address(formatAddress(venueLocation.getAddress()))
      .distance(formatDistance(venueLocation.getDistance()))
      .build();
  }

  private static String formatAddress(String address) {
    return address != null ? address : "Unknown address";
  }

  private static String formatDistance(double distanceInMeters) {
    if (distanceInMeters < 1000) {
      return new BigDecimal(distanceInMeters).setScale(0, BigDecimal.ROUND_HALF_UP).toString() + " m";
    }

    double distanceInKilometer = distanceInMeters / 1000;
    return new BigDecimal(distanceInKilometer).setScale(1, BigDecimal.ROUND_HALF_UP).toString() + " km";
  }
}
