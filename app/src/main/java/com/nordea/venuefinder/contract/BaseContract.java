package com.nordea.venuefinder.contract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface BaseContract {

  interface Model {
  }

  interface View {
  }

  interface ViewState {
  }

  interface Presenter<S extends ViewState> {

    void subscribe(@Nullable S state);

    void unsubscribe();

    @NonNull
    S getState();
  }
}
