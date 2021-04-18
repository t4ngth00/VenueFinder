package com.nordea.venuefinder.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Response {

  @Expose
  @SerializedName("venues")
  private List<Venue> venues;
}
