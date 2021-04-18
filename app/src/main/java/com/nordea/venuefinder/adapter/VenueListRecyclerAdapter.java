package com.nordea.venuefinder.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nordea.venuefinder.contract.VenueListContract;
import com.nordea.venuefinder.databinding.VenueListItemBinding;
import com.nordea.venuefinder.model.Venue;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VenueListRecyclerAdapter extends RecyclerView.Adapter<VenueListRecyclerAdapter.VenueListItemViewHolder> {

  private final VenueListContract.Presenter mVenueListPresenter;

  @NonNull
  @Override
  public VenueListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VenueListItemViewHolder(
      VenueListItemBinding.inflate(
        LayoutInflater.from(parent.getContext()),
        parent,
        false
      )
    );
  }

  public void updateVenueList(List<Venue> newList) {
    mVenueListPresenter.buildDiffResult(newList).dispatchUpdatesTo(this);
  }

  @Override
  public void onBindViewHolder(@NonNull VenueListItemViewHolder viewHolder, int position) {
    mVenueListPresenter.onBindVenueItemViewAtPosition(position, viewHolder);
  }

  @Override
  public int getItemCount() {
    return mVenueListPresenter.getVenueListCount();
  }

  public static class VenueListItemViewHolder extends RecyclerView.ViewHolder implements VenueListContract.ItemView {
    private final VenueListItemBinding venueListItemBinding;

    public VenueListItemViewHolder(VenueListItemBinding venueListItemBinding) {
      super(venueListItemBinding.getRoot());
      this.venueListItemBinding = venueListItemBinding;
    }

    @Override
    public void setName(String name) {
      venueListItemBinding.venueName.setText(name);
    }

    @Override
    public void setAddress(String address) {
      venueListItemBinding.venueAddress.setText(address);
    }

    @Override
    public void setDistance(String distance) {
      venueListItemBinding.venueDistance.setText(distance);
    }
  }
}
