package com.nordea.venuefinder.view;

import com.nordea.venuefinder.contract.MainContract;
import com.nordea.venuefinder.view.state.ActiveView;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MainViewState implements MainContract.ViewState {

  private final ActiveView activeView;
}
