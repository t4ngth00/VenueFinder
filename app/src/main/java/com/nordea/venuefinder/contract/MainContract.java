package com.nordea.venuefinder.contract;

import androidx.annotation.StringRes;

import com.jakewharton.rxbinding4.InitialValueObservable;
import com.nordea.venuefinder.model.Venue;
import com.nordea.venuefinder.view.state.ActiveView;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public interface MainContract {

  interface Model extends BaseContract.Model {

    Single<List<Venue>> searchVenues(String query, String coordinator);

    String getFormattedLocation();

    void setFormattedLocation(String location);
  }

  interface View extends BaseContract.View {

    void initSearchInputListener();

    void initVenueListRecyclerView();

    void initLocationPermissionExplanation();

    void updateVenueList(List<Venue> venueList);

    boolean isLocationPermissionGranted();

    void requestLocationPermission();

    void showLocationExplanation();

    void dismissLocationExplanation();

    void renderInitialView();

    void renderLoadingView();

    void renderNoResultView();

    void renderErrorView(@StringRes int resId);

    void renderContentView();
  }

  interface ViewState extends BaseContract.ViewState {

    ActiveView getActiveView();
  }

  interface Presenter extends BaseContract.Presenter<ViewState> {

    void onViewCreated();

    void observeSearchInputChanges(InitialValueObservable<CharSequence> observable);

    void updateCurrentLocation();
  }
}
