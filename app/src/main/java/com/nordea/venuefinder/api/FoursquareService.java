package com.nordea.venuefinder.api;

import com.nordea.venuefinder.BuildConfig;
import com.nordea.venuefinder.api.dto.FoursquareResponse;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FoursquareService {

  String REQUIRED_PARAMS = "&client_id=" + BuildConfig.FOURSQUARE_CLIENT_ID +
    "&client_secret=" + BuildConfig.FOURSQUARE_CLIENT_SECRET +
    "&v=" + BuildConfig.FOURSQUARE_VERSION;


  @GET("venues/search?" + REQUIRED_PARAMS)
  Observable<FoursquareResponse> searchVenues(@Query("query") String query, @Query("ll") String coordinate);
}
