package com.parking.lot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import java.util.Optional;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class ParkingServiceTest {
  ParkingService parkingService;
  @Mock
  TicketRepository ticketRepository;
  @Mock
  ParkingRepository parkingRepository;
  @BeforeEach
  void setUp() {
    openMocks(this);
    parkingService = new ParkingService(parkingRepository,ticketRepository);
  }
  @Test
  void should_get_ticket_and_reduce_size_when_park_car() throws NotFoundException {
    Parking parking = getParking();
    int parkingEmpty = parking.getSize() - 1;
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    Ticket returnTicket = parkingService.parkingCar(parking.getId());
    assertNotNull(returnTicket);
    assertEquals(parkingEmpty,parking.getSize());
  }

  @Test
  void should_throw_NotFoundException_when_not_found_park() {
    when(parkingRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundException.class,
        () -> {
          parkingService.parkingCar("123");
        },"not found parking");
  }

  @Test
  void should_throw_OverSizeException_when_over_park_size() {
    Parking parking = getFullParking();
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    assertThrows(
        OverSizeException.class,
        () -> {
          parkingService.parkingCar(parking.getId());
        });
  }

  private Parking getFullParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096c").size(0).build();
  }

  private Parking getParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").size(20).build();
  }
}