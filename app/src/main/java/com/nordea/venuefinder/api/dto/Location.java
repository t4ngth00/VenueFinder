package com.nordea.venuefinder.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Location {

  @Expose
  @SerializedName("address")
  private String address;

  @Expose
  @SerializedName("distance")
  private float distance;
}
