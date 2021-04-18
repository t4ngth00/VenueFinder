package com.nordea.venuefinder.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class FoursquareResponse {

  @Expose
  @SerializedName("response")
  private Response response;
}
