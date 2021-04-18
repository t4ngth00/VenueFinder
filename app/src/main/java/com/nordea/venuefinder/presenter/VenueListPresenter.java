package com.nordea.venuefinder.presenter;

import androidx.recyclerview.widget.DiffUtil;

import com.nordea.venuefinder.adapter.util.VenueListDiffCallback;
import com.nordea.venuefinder.contract.VenueListContract;
import com.nordea.venuefinder.model.Venue;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VenueListPresenter implements VenueListContract.Presenter {

  private final List<Venue> mVenueList;

  public DiffUtil.DiffResult buildDiffResult(List<Venue> venueList) {
    VenueListDiffCallback diffUtilCallback = new VenueListDiffCallback(mVenueList, venueList);
    DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);

    mVenueList.clear();
    mVenueList.addAll(venueList);

    return diffResult;
  }

  public void onBindVenueItemViewAtPosition(int position, VenueListContract.ItemView listItemView) {
    Venue venue = mVenueList.get(position);

    listItemView.setName(venue.getName());
    listItemView.setAddress(venue.getAddress());
    listItemView.setDistance(venue.getDistance());
  }

  public int getVenueListCount() {
    return mVenueList.size();
  }
}
