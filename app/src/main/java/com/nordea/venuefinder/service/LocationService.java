package com.nordea.venuefinder.service;

import android.annotation.SuppressLint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LocationService implements LocationListener {

  public static final int MIN_LOCATION_UPDATE_INTERVAL = 5000;
  public static final int MIN_DISTANCE_IN_METERS = 100;

  private final LocationManager mLocationManager;
  private final List<OnLocationChangedListener> locationListeners = new ArrayList<>();

  public LocationService(LocationManager locationManager) {
    this.mLocationManager = locationManager;
  }

  public void addOnLocationChangedListener(OnLocationChangedListener locationListener) {
    if (!locationListeners.contains(locationListener)) {
      locationListeners.add(locationListener);
    }
  }

  public void removeOnLocationChangedListener(OnLocationChangedListener locationListener) {
    locationListeners.remove(locationListener);
  }

  public Location getLastLocation() {
    return getLastKnownLocation(getBestProvider());
  }

  private Location getLastKnownLocation(String bestProvider) {
    try {
      return mLocationManager.getLastKnownLocation(bestProvider);
    } catch (SecurityException e) {
      Log.d(LocationService.class.getName(), String.valueOf(e));
    }
    return null;
  }

  private String getBestProvider() {
    Criteria criteria = new Criteria();
    criteria.setCostAllowed(false);
    criteria.setAccuracy(Criteria.ACCURACY_LOW);

    return mLocationManager.getBestProvider(criteria, false);
  }

  @SuppressLint("MissingPermission")
  public void requestLocationUpdates() {
    mLocationManager.requestLocationUpdates(getBestProvider(), MIN_LOCATION_UPDATE_INTERVAL, MIN_DISTANCE_IN_METERS, this);
  }

  public void removeUpdates() {
    mLocationManager.removeUpdates(this);
  }

  @Override
  public void onLocationChanged(Location location) {
    for (OnLocationChangedListener locationListener : locationListeners)
      locationListener.onLocationChanged(location);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
  }

  @Override
  public void onProviderEnabled(String provider) {
    requestLocationUpdates();
    onLocationChanged(getLastLocation());
  }

  @Override
  public void onProviderDisabled(String provider) {
    mLocationManager.removeUpdates(this);
  }

  public interface OnLocationChangedListener {
    void onLocationChanged(Location location);
  }
}
