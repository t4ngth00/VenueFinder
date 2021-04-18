package com.nordea.venuefinder.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Venue {

  @Expose
  @SerializedName("id")
  private String id;

  @Expose
  @SerializedName("name")
  private String name;

  @Expose
  @SerializedName("location")
  private Location location;
}
