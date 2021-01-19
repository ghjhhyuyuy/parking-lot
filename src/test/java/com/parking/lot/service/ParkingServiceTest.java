package com.parking.lot.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.parking.lot.entity.Ticket;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
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
    parkingService = new ParkingService();
  }
  @Test
  void should_get_ticket_when_park_car(){
    Ticket ticket = getTicket();
    when(ticketRepository.save(ticket)).thenReturn(ticket);
    Ticket returnTicket = parkingService.parkingCar();
    assertEquals(ticket,returnTicket);
    verify(parkingRepository).save(any());
  }

  private Ticket getTicket() {
    Date date = new Date(System.currentTimeMillis() + 3600);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId(UUID.randomUUID().toString())
        .timeoutDate(simpleDateFormat.format(date))
        .build();
  }
}