package com.nordea.venuefinder.di;

import com.nordea.venuefinder.adapter.VenueListRecyclerAdapter;
import com.nordea.venuefinder.contract.VenueListContract;
import com.nordea.venuefinder.presenter.VenueListPresenter;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class VenueListModule {

  @Singleton
  @Provides
  public static VenueListRecyclerAdapter provideVenueRecyclerAdapter(
    VenueListContract.Presenter presenter
  ) {
    return new VenueListRecyclerAdapter(presenter);
  }

  @Singleton
  @Provides
  public static VenueListContract.Presenter provideVenueListPresenter() {
    return new VenueListPresenter(new ArrayList<>());
  }
}