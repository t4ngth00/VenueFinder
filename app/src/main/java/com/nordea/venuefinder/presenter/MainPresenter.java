package com.nordea.venuefinder.presenter;

import android.location.Location;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxbinding4.InitialValueObservable;
import com.nordea.venuefinder.contract.MainContract;
import com.nordea.venuefinder.model.Venue;
import com.nordea.venuefinder.service.LocationService;
import com.nordea.venuefinder.view.MainViewState;
import com.nordea.venuefinder.view.state.ActiveView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainPresenter implements MainContract.Presenter, LocationService.OnLocationChangedListener {

  private final MainContract.View mView;
  private final MainContract.Model mModel;
  private final LocationService mLocationService;
  private ActiveView mActiveView;

  @Inject
  public MainPresenter(MainContract.View view, MainContract.Model model, LocationService locationService) {
    mView = view;
    mModel = model;
    mLocationService = locationService;
  }

  @Override
  public void onViewCreated() {
    mView.initLocationPermissionExplanation();
    mLocationService.addOnLocationChangedListener(this);
    requestLocationPermissionStatus();

    mView.initSearchInputListener();
    mView.initVenueListRecyclerView();
  }

  @Override
  public void subscribe(@Nullable MainContract.ViewState state) {
    mActiveView = state != null ? state.getActiveView() : ActiveView.INITIAL;

    switch (mActiveView) {
      case INITIAL:
        mView.renderInitialView();
        break;
      case LOADING:
        mView.renderLoadingView();
        break;
      case NO_RESULT:
        mView.renderNoResultView();
        break;
      case ERROR:
        mView.renderErrorView();
        break;
      case CONTENT:
      default:
        mView.renderContentView();
    }
  }

  @Override
  public void unsubscribe() {
    mLocationService.removeOnLocationChangedListener(this);
    mLocationService.removeUpdates();
  }

  @NonNull
  @Override
  public MainContract.ViewState getState() {
    return new MainViewState(mActiveView);
  }

  @Override
  public void observeSearchInputChanges(InitialValueObservable<CharSequence> observable) {
    observable
      .debounce(350, TimeUnit.MILLISECONDS)
      .skip(1)
      .map(CharSequence::toString)
      .distinctUntilChanged()
      .observeOn(AndroidSchedulers.mainThread()) // Switch to main thread to show loader
      .doOnNext(string -> {
        mActiveView = ActiveView.LOADING;
        mView.renderLoadingView();
      })
      .observeOn(Schedulers.io()) // Switch to io thread for calling API
      .switchMapSingle(query -> {
        if (!mView.isLocationPermissionGranted()) {
          mView.showLocationExplanation();
        }

        if (query.isEmpty()) {
          List<Venue> list = new ArrayList<>();
          return Single.just(Pair.create(query, list));
        }

        return mModel.searchVenues(
          query,
          mModel.getFormattedLocation()
        ).map(venueList -> Pair.create(query, venueList));
      })
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError(throwable -> {
        mActiveView = ActiveView.ERROR;
        mView.renderErrorView();
      })
      .retry()
      .subscribe(pair -> {
        String query = pair.first;
        List<Venue> venueList = pair.second;

        if (query.isEmpty()) {
          mActiveView = ActiveView.INITIAL;
          mView.renderInitialView();
        } else if (venueList.isEmpty()) {
          mActiveView = ActiveView.NO_RESULT;
          mView.renderNoResultView();
        } else {
          mActiveView = ActiveView.CONTENT;
          mView.renderContentView();
        }

        mView.updateVenueList(venueList);
      });
  }

  @Override
  public void onLocationChanged(Location location) {
    this.updateCurrentLocation();
  }

  @Override
  public void updateCurrentLocation() {
    if (mView.isLocationPermissionGranted()) {
      Location lastLocation = mLocationService.getLastLocation();
      String formattedLocation = String.valueOf(lastLocation.getLatitude()) + ',' + lastLocation.getLongitude();

      mModel.setFormattedLocation(formattedLocation);
    }
  }

  private void requestLocationPermissionStatus() {
    if (mView.isLocationPermissionGranted()) {
      updateCurrentLocation();
    } else {
      mView.requestLocationPermission();
    }
  }
}
