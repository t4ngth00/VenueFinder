package com.nordea.venuefinder.contract;

import androidx.recyclerview.widget.DiffUtil;

import com.nordea.venuefinder.model.Venue;

import java.util.List;

public interface VenueListContract {

  interface Presenter {

    DiffUtil.DiffResult buildDiffResult(List<Venue> venueList);

    void onBindVenueItemViewAtPosition(int position, ItemView viewHolder);

    int getVenueListCount();
  }

  interface ItemView {

    void setName(String name);

    void setAddress(String address);

    void setDistance(String distance);
  }
}
