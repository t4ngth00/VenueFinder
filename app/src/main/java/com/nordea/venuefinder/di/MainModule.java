package com.nordea.venuefinder.di;

import com.nordea.venuefinder.view.MainActivity;
import com.nordea.venuefinder.contract.MainContract;
import com.nordea.venuefinder.model.MainModel;
import com.nordea.venuefinder.presenter.MainPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;

@Module
@InstallIn({ActivityComponent.class, ActivityRetainedComponent.class})
public abstract class MainModule {

  @Binds
  public abstract MainContract.View bindActivity(MainActivity activity);

  @Binds
  public abstract MainContract.Presenter bindPresenter(MainPresenter presenter);

  @Binds
  public abstract MainContract.Model bindModel(MainModel model);
}
