package com.nordea.venuefinder.model;

import com.nordea.venuefinder.api.FoursquareService;
import com.nordea.venuefinder.api.dto.FoursquareResponse;
import com.nordea.venuefinder.api.dto.Location;
import com.nordea.venuefinder.api.dto.Response;
import com.nordea.venuefinder.api.dto.Venue;
import com.nordea.venuefinder.contract.MainContract;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.observers.TestObserver;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainModelTest {

  @Mock
  private FoursquareService mFoursquareService;

  private MainContract.Model mModel;

  @Before
  public void setup() {
    mModel = new MainModel(mFoursquareService);
  }

  @Test
  public void givenSuccessfulFoursquareResponse_whenSearchVenuesMethodIsCalledFromModel_thenCorrectResponseTransformationIsEmittedDownStream() {
    // Mock Foursquare API response
    List<Venue> venueList = new ArrayList<>();
    venueList.add(new Venue("1", "name1", new Location("address1", 100)));
    venueList.add(new Venue("2", "name2", new Location("address2", 1234.56)));
    Response response = new Response(venueList);
    FoursquareResponse foursquareResponse = new FoursquareResponse(response);

    when(mFoursquareService.searchVenues("any", "any")).thenReturn(Observable.just(foursquareResponse));

    // Trigger test method
    TestObserver<List<com.nordea.venuefinder.model.Venue>> testObserver = mModel.searchVenues("any", "any").test();

    // Build expected result
    com.nordea.venuefinder.model.Venue expectedFirstVenue = com.nordea.venuefinder.model.Venue.builder()
      .id("1")
      .name("name1")
      .address("address1")
      .distance("100 m")
      .build();
    com.nordea.venuefinder.model.Venue expectedSecondVenue = com.nordea.venuefinder.model.Venue.builder()
      .id("2")
      .name("name2")
      .address("address2")
      .distance("1.2 km")
      .build();
    List<com.nordea.venuefinder.model.Venue> expectedVenueList = new ArrayList<>();
    expectedVenueList.add(expectedFirstVenue);
    expectedVenueList.add(expectedSecondVenue);

    // Verify
    testObserver.assertValue(expectedVenueList);
    testObserver.dispose();
  }
}