package com.nordea.venuefinder.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding4.InitialValueObservable;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.nordea.venuefinder.R;
import com.nordea.venuefinder.adapter.VenueListRecyclerAdapter;
import com.nordea.venuefinder.contract.MainContract;
import com.nordea.venuefinder.databinding.ActivityMainBinding;
import com.nordea.venuefinder.model.Venue;
import com.nordea.venuefinder.view.state.ActiveView;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements MainContract.View {

  private static final int ACCESS_FINE_LOCATION_REQUEST_CODE = 777;
  private static final String ACTIVE_VIEW_KEY = "ACTIVE_VIEW_KEY";

  @Inject
  MainContract.Presenter mMainPresenter;

  @Inject
  VenueListRecyclerAdapter mVenueListRecyclerAdapter;

  private Snackbar mLocationExplanationSnackbar;
  private ActivityMainBinding mBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Set up view binding
    mBinding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(mBinding.getRoot());

    // Initialize UI components and event listeners
    mMainPresenter.onViewCreated();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mMainPresenter.updateCurrentLocation();
  }

  @Override
  public void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mMainPresenter.subscribe(savedInstanceState != null ? readStateFromBundle(savedInstanceState) : null);
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    writeStateToBundle(outState, mMainPresenter.getState());
  }

  @Override
  public void onStop() {
    super.onStop();
    mMainPresenter.unsubscribe();
  }

  @Override
  public void initSearchInputListener() {
    InitialValueObservable<CharSequence> searchInputObservable = RxTextView.textChanges(mBinding.searchEditText);
    mMainPresenter.observeSearchInputChanges(searchInputObservable);
  }

  @Override
  public void initVenueListRecyclerView() {
    RecyclerView recyclerView = mBinding.venueList.venueRecyclerView;
    LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
    dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.list_item_divider_wrapper)));

    recyclerView.setAdapter(mVenueListRecyclerAdapter);
    recyclerView.addItemDecoration(dividerItemDecoration);
  }

  @Override
  public void initLocationPermissionExplanation() {
    mLocationExplanationSnackbar = Snackbar.make(mBinding.getRoot(), R.string.location_request_explanation, Snackbar.LENGTH_INDEFINITE)
      .setBehavior(new BaseTransientBottomBar.Behavior() {
        @Override
        public boolean canSwipeDismissView(View child) {
          return false;
        }
      })
      .setAction(R.string.settings, __ -> {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        startActivity(intent);
      });
  }

  @Override
  public void updateVenueList(List<Venue> venueList) {
    mVenueListRecyclerAdapter.updateVenueList(venueList);
  }

  @Override
  public boolean isLocationPermissionGranted() {
    return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }

  @Override
  public void requestLocationPermission() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
  }

  @Override
  public void showLocationExplanation() {
    if (mLocationExplanationSnackbar != null && !mLocationExplanationSnackbar.isShown()) {
      mLocationExplanationSnackbar.show();
    }
  }

  @Override
  public void dismissLocationExplanation() {
    if (mLocationExplanationSnackbar != null && mLocationExplanationSnackbar.isShown()) {
      mLocationExplanationSnackbar.dismiss();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if (requestCode == ACCESS_FINE_LOCATION_REQUEST_CODE) {
      // Location permission is granted.
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Dismiss the location permission explanation snackbar
        dismissLocationExplanation();
        mMainPresenter.updateCurrentLocation();
      } else {
        // Show explanation to the user that location permission is needed in order to search venues
        showLocationExplanation();
      }
    }
  }

  @Override
  public void renderInitialView() {
    mBinding.loadingView.loadingIndicator.setVisibility(View.GONE);
    mBinding.noResultView.getRoot().setVisibility(View.GONE);
    mBinding.errorView.getRoot().setVisibility(View.GONE);
    mBinding.venueList.venueRecyclerView.setVisibility(View.GONE);

    mBinding.initialView.getRoot().setVisibility(View.VISIBLE);
  }

  @Override
  public void renderLoadingView() {
    mBinding.loadingView.loadingIndicator.setVisibility(View.VISIBLE);
  }

  @Override
  public void renderNoResultView() {
    mBinding.loadingView.loadingIndicator.setVisibility(View.GONE);
    mBinding.initialView.getRoot().setVisibility(View.GONE);
    mBinding.errorView.getRoot().setVisibility(View.GONE);
    mBinding.venueList.venueRecyclerView.setVisibility(View.GONE);

    mBinding.noResultView.getRoot().setVisibility(View.VISIBLE);
  }

  @Override
  public void renderErrorView(@StringRes int resId) {
    mBinding.loadingView.loadingIndicator.setVisibility(View.GONE);
    mBinding.noResultView.getRoot().setVisibility(View.GONE);
    mBinding.initialView.getRoot().setVisibility(View.GONE);
    mBinding.venueList.venueRecyclerView.setVisibility(View.GONE);

    mBinding.errorView.getRoot().setVisibility(View.VISIBLE);
    mBinding.errorView.errorMessage.setText(getString(resId));
  }

  @Override
  public void renderContentView() {
    mBinding.loadingView.loadingIndicator.setVisibility(View.GONE);
    mBinding.noResultView.getRoot().setVisibility(View.GONE);
    mBinding.errorView.getRoot().setVisibility(View.GONE);
    mBinding.initialView.getRoot().setVisibility(View.GONE);

    mBinding.venueList.venueRecyclerView.setVisibility(View.VISIBLE);
  }

  private MainContract.ViewState readStateFromBundle(Bundle savedInstanceState) {
    ActiveView storedActiveView = (ActiveView) savedInstanceState.get(ACTIVE_VIEW_KEY);
    return new MainViewState(storedActiveView);
  }

  private void writeStateToBundle(Bundle outState, MainContract.ViewState state) {
    outState.putSerializable(ACTIVE_VIEW_KEY, state.getActiveView());
  }
}