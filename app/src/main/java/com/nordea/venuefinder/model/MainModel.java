package com.nordea.venuefinder.model;

import com.nordea.venuefinder.api.FoursquareService;
import com.nordea.venuefinder.contract.MainContract;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class MainModel implements MainContract.Model {

  private final FoursquareService mFoursquareService;
  private String mFormattedLocation;

  @Inject
  public MainModel(FoursquareService foursquareService) {
    this.mFoursquareService = foursquareService;
  }

  @Override
  public Single<List<Venue>> searchVenues(String query, String coordinator) {
    return mFoursquareService.searchVenues(query, coordinator)
      .map(res -> res.getResponse().getVenues())
      .flatMapIterable(venue -> venue)
      .map(Venue::fromApiVenue)
      .toList();
  }

  @Override
  public String getFormattedLocation() {
    return mFormattedLocation;
  }

  @Override
  public void setFormattedLocation(String location) {
    mFormattedLocation = location;
  }
}