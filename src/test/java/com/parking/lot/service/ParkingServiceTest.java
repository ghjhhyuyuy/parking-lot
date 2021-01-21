package com.parking.lot.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.NoMatchingRoleException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotParkingHelperException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import com.parking.lot.repository.UserRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
  @Mock
  UserRepository userRepository;

  public static Parking getParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").size(20).build();
  }

  @BeforeEach
  void setUp() {
    openMocks(this);
    parkingService = new ParkingService(parkingRepository, ticketRepository, userRepository);
  }

  @Test
  void should_get_ticket_and_reduce_size_when_park_car()
      throws NotFoundResourceException {
    Parking parking = getParking();
    int parkingEmptyNum = parking.getSize();
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    Ticket returnTicket = parkingService.parkingCar(parking.getId());
    assertNotNull(returnTicket);
    assertEquals(parkingEmptyNum - 1, parking.getSize());
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_park() {
    when(parkingRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.parkingCar("123"),
        ExceptionMessage.NOT_FOUND_PARKING.getMessage());
  }

  @Test
  void should_throw_overSizeException_when_over_park_size() {
    Parking parking = getFullParking();
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    assertThrows(
        OverSizeException.class,
        () -> parkingService.parkingCar(parking.getId()));
  }

  @Test
  void should_add_park_size_when_give_right_ticket()
      throws ParseException, IllegalTicketException, NotFoundResourceException {
    Ticket ticket = getRightTicket();
    Parking parking = getParking();
    int parkingEmptyNum = parking.getSize();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    parkingService.takeCar("123");
    assertEquals(parkingEmptyNum + 1, parking.getSize());
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_ticket() {
    when(ticketRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.takeCar("123"), ExceptionMessage.NOT_FOUND_TICKET.getMessage());
  }

  @Test
  void should_throw_illegalTicketException_when_not_right_ticket() {
    Ticket ticket = getWrongTicket();
    Parking parking = getParking();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    assertThrows(
        IllegalTicketException.class,
        () -> parkingService.takeCar("123"));
  }

  @Test
  void should_get_all_parking_when_request()
      throws NotParkingHelperException, NotFoundResourceException {
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    User user = getNormalUser();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    List<Parking> parkings = parkingService.getAllParking(anyString());
    assertEquals(parkings, parkingList);
  }

  @Test
  void should_throw_noMatchingRoleException_when_not_parking_helper(){
    User user = getNotParkingHelperUser();
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    assertThrows(
        NoMatchingRoleException.class,
        () -> parkingService.getAllParking(anyString()));
  }

  private User getNotParkingHelperUser() {
    return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
        .createDate("2020-10-12 15:33:33").removeDate(null).role("4").build();
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_user(){
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.getAllParking(anyString()));
  }

  @Test
  void should_get_parking_when_given_normal_helper(){
    User normalHelper = getNormalUser();
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    int initNumber = parkingList.get(0).getSize();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
    parkingService.helperSave(anyString());
    assertEquals(initNumber - 1,parkingList.get(0).getSize());
  }

  @Test
  void should_get_parking_when_given_smart_helper(){
    User smartHelper = getSmartUser();
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    int initNumber = parkingList.get(parkingList.size() - 1).getSize();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
    parkingService.helperSave(anyString());
    assertEquals(initNumber - 1,parkingList.get(parkingList.size() - 1).getSize());
  }

  private User getSmartUser() {
    return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
        .createDate("2020-10-12 15:33:33").removeDate(null).role("2").build();
  }

  private User getNormalUser() {
    return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
        .createDate("2020-10-12 15:33:33").removeDate(null).role("1").build();
  }

  private Ticket getWrongTicket() {
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId("123")
        .timeoutDate("2021-01-18 20:20:20")
        .build();
  }

  private Ticket getRightTicket() {
    Date date = new Date(System.currentTimeMillis() + 300000);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String outTime = simpleDateFormat.format(date);
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId("123")
        .timeoutDate(outTime)
        .build();
  }

  private List<Parking> getParkingListWithLargeParkingInLast() {
    Parking parking = getParking();
    List<Parking> parkings = new ArrayList<>(Collections.nCopies(3, parking));
    parkings.add(getLargeSizeParking());
    return parkings;
  }

  private Parking getLargeSizeParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096c").size(20000).build();
  }

  private Parking getFullParking() {
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096c").size(0).build();
  }
}