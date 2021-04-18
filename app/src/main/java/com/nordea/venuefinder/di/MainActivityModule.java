package com.nordea.venuefinder.di;

import android.app.Activity;

import com.nordea.venuefinder.view.MainActivity;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;

@Module
@InstallIn({ActivityComponent.class, ActivityRetainedComponent.class})
public class MainActivityModule {

  @Provides
  public MainActivity bindActivity(Activity activity) {
    return (MainActivity) activity;
  }
}

