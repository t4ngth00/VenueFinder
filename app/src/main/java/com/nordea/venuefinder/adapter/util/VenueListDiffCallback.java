package com.nordea.venuefinder.adapter.util;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.nordea.venuefinder.model.Venue;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VenueListDiffCallback extends DiffUtil.Callback {

  private final List<Venue> mOldVenueList;
  private final List<Venue> mNewVenueList;

  @Override
  public int getOldListSize() {
    return mOldVenueList.size();
  }

  @Override
  public int getNewListSize() {
    return mNewVenueList.size();
  }

  @Override
  public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
    return mOldVenueList.get(oldItemPosition).getId().equals(mNewVenueList.get(newItemPosition).getId());
  }

  @Override
  public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
    return mOldVenueList.get(oldItemPosition).getName().equals(mNewVenueList.get(newItemPosition).getName());
  }

  @Nullable
  @Override
  public Object getChangePayload(int oldItemPosition, int newItemPosition) {
    return super.getChangePayload(oldItemPosition, newItemPosition);
  }
}
