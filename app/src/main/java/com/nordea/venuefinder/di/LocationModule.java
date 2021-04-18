package com.nordea.venuefinder.di;

import android.content.Context;
import android.location.LocationManager;

import com.nordea.venuefinder.service.LocationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class LocationModule {

  @Singleton
  @Provides
  public static LocationService provideLocationService(
    LocationManager locationManager
  ) {
    return new LocationService(locationManager);
  }

  @Singleton
  @Provides
  public static LocationManager provideLocationManager(
    @ApplicationContext Context appContext
  ) {
    return (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
  }
}
