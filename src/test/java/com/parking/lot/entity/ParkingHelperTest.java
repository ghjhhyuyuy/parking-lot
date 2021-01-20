package com.parking.lot.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.parking.lot.CommonMethod;
import com.parking.lot.repository.ParkingRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ParkingHelperTest {

  ParkingHelper parkingHelper;
  @Mock
  ParkingRepository parkingRepository;

  @BeforeEach
  void setUp() {
    openMocks(this);
    parkingHelper = new ParkingHelper();
  }

  @Test
  void should_get_all_parking_when_request() {
    List<Parking> parkingList = getParkingList();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    List<Parking> parkings = parkingHelper.getAllParking();
    assertEquals(parkings, parkingList);
  }

  private List<Parking> getParkingList() {
    Parking parking = CommonMethod.getParking();
    return new ArrayList<>(Collections.nCopies(3, parking));
  }
}