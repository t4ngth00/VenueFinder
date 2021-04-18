package com.nordea.venuefinder.presenter;

import android.location.Location;

import com.nordea.venuefinder.R;
import com.nordea.venuefinder.contract.MainContract;
import com.nordea.venuefinder.service.LocationService;
import com.nordea.venuefinder.view.MainViewState;
import com.nordea.venuefinder.view.state.ActiveView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class MainPresenterTest {

  @Mock
  private MainContract.View mView;

  @Mock
  private MainContract.Model mModel;

  @Mock
  private LocationService mLocationService;

  private MainPresenter mPresenter;

  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
    mPresenter = new MainPresenter(
      mView,
      mModel,
      mLocationService
    );
  }

  @Test
  @Parameters(method = "addLocationPermissionStatusParams")
  public void givenLocationPermissionGrantedStatus_andCurrentLocationIsAvailable_whenOnViewCreatedMethodPresenterIsCalled_thenPresenterRegisterViewComponentsAndLocationEventListenersSuccessfully(boolean isLocationPermissionGranted) {
    // Mock location permission request status
    when(mView.isLocationPermissionGranted()).thenReturn(isLocationPermissionGranted);

    // Mock returned location from LocationService
    double MOCK_LATITUDE = 1.0;
    double MOCK_LONGITUDE = 2.0;
    String FORMATTED_LAT_LONG = String.valueOf(MOCK_LATITUDE) + ',' + MOCK_LONGITUDE;
    Location mockedLocation = mock(Location.class);
    when(mockedLocation.getLatitude()).thenReturn(MOCK_LATITUDE);
    when(mockedLocation.getLongitude()).thenReturn(MOCK_LONGITUDE);
    when(mLocationService.getLastLocation()).thenReturn(mockedLocation);

    mPresenter.onViewCreated();

    // Verify venue list recycler and search input watcher methods are called
    verify(mView, times(1)).initSearchInputListener();
    verify(mView, times(1)).initVenueListRecyclerView();

    // Verify whether location permissions, location data and views are properly handled
    if (isLocationPermissionGranted) {
      verify(mLocationService, times(1)).addOnLocationChangedListener(mPresenter);
      verify(mLocationService, times(1)).getLastLocation();
      verify(mModel, times(1)).setFormattedLocation(FORMATTED_LAT_LONG);
    } else {
      verify(mView, times(1)).initLocationPermissionExplanation();
      verify(mView, times(1)).requestLocationPermission();
    }
  }

  @Test
  @Parameters(method = "addLocationPermissionStatusParams")
  public void givenLocationPermissionGrantedStatus_andCurrentLocationIsUnavailable_whenOnViewCreatedMethodPresenterIsCalled_thenPresenterRegisterViewComponentsAndShowLocationErrorView(boolean isLocationPermissionGranted) {
    // Mock location permission request status
    when(mView.isLocationPermissionGranted()).thenReturn(isLocationPermissionGranted);

    // Mock returned location from LocationService
    when(mLocationService.getLastLocation()).thenReturn(null);

    mPresenter.onViewCreated();

    // Verify venue list recycler and search input watcher methods are called
    verify(mView, times(1)).initSearchInputListener();
    verify(mView, times(1)).initVenueListRecyclerView();

    // Verify whether location permissions, location data and views are properly handled
    if (isLocationPermissionGranted) {
      verify(mLocationService, times(1)).addOnLocationChangedListener(mPresenter);
      verify(mLocationService, times(1)).getLastLocation();
      verify(mModel, times(1)).setFormattedLocation(null);
    } else {
      verify(mView, times(1)).initLocationPermissionExplanation();
      verify(mView, times(1)).requestLocationPermission();
    }
  }

  @Test
  @Parameters(method = "addViewStateParams")
  public void givenActiveViewState_whenViewIsSubscribedToPresenter_thenCorrectViewIsDisplayed(ActiveView activeView) {
    mPresenter.subscribe(new MainViewState(activeView));

    // Assert that view method will be called correctly according to view state
    switch (activeView) {
      case LOADING:
        verify(mView, times(1)).renderLoadingView();
        break;
      case NO_RESULT:
        verify(mView, times(1)).renderNoResultView();
        break;
      case ERROR:
        verify(mView, times(1)).renderErrorView(R.string.something_went_wrong);
        break;
      case CONTENT:
        verify(mView, times(1)).renderContentView();
        break;
      case INITIAL:
      default:
        verify(mView, times(1)).renderInitialView();
    }

    // Assert the active view state retriever from presenter return correct value
    Assert.assertEquals(mPresenter.getState().getActiveView(), activeView);
  }

  @Test
  public void givenSubscribedView_whenViewIsUnsubscribedFromPresenter_thenPresenterStopListeningToCurrentLocationChanges() {
    mPresenter.unsubscribe();

    verify(mLocationService, times(1)).removeOnLocationChangedListener(mPresenter);
    verify(mLocationService, times(1)).removeUpdates();
  }

  private Object[] addLocationPermissionStatusParams() {
    return new Object[]{
      new Object[]{true},
      new Object[]{false},
    };
  }

  private Object[] addViewStateParams() {
    return new Object[]{
      new Object[]{ActiveView.INITIAL},
      new Object[]{ActiveView.LOADING},
      new Object[]{ActiveView.NO_RESULT},
      new Object[]{ActiveView.ERROR},
      new Object[]{ActiveView.CONTENT},
    };
  }
}