package com.nordea.venuefinder.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {

  @Expose
  @SerializedName("address")
  private String address;

  @Expose
  @SerializedName("distance")
  private double distance;
}
