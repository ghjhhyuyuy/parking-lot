package com.parking.lot.service;

import static com.parking.lot.util.StorageUtil.generateStorageList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.parking.lot.entity.Car;
import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Role;
import com.parking.lot.entity.Storage;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.NoMatchingRoleException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotParkingHelperException;
import com.parking.lot.exception.NotRightCarException;
import com.parking.lot.exception.OutOfSetException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.CarRepository;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.RoleRepository;
import com.parking.lot.repository.StorageRepository;
import com.parking.lot.repository.TicketRepository;
import com.parking.lot.repository.UserRepository;
import com.parking.lot.util.GenerateId;
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
  @Mock
  RoleRepository roleRepository;
  @Mock
  CarRepository carRepository;
  @Mock
  StorageRepository storageRepository;

  public static Parking getParking() {
    String id = GenerateId.getUUID();
    List<Storage> storageList = generateStorageList(5, id);
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").size(20).storageList(storageList).build();
  }

  @BeforeEach
  void setUp() {
    openMocks(this);
    parkingService = new ParkingService(parkingRepository, ticketRepository, userRepository,
        roleRepository, carRepository, storageRepository);
  }

  @Test
  void should_get_ticket_and_reduce_size_when_park_car()
      throws NotFoundResourceException {
    Car car = getCar();
    Parking parking = getParking();
    int parkingEmptyNum = parking.getStorageList().size();
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    Ticket returnTicket = parkingService.parkingCarBySelf(parking.getId(), car);
    assertNotNull(returnTicket);
    assertEquals(parkingEmptyNum - 1, parking.getStorageList().size());
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_park() {
    Car car = getCar();
    when(parkingRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.parkingCarBySelf("123", car),
        ExceptionMessage.NOT_FOUND_PARKING.getMessage());
  }

  @Test
  void should_throw_overSizeException_when_over_park_size() {
    Parking parking = getFullParking();
    Car car = getCar();
    when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
    assertThrows(
        OverSizeException.class,
        () -> parkingService.parkingCarBySelf(parking.getId(), car));
  }

  @Test
  void should_get_car_and_add_park_size_when_give_right_ticket()
      throws IllegalTicketException, NotFoundResourceException {
    Ticket ticket = getRightTicket();
    Parking parking = getParking();
    Car car = getCar();
    Storage storage = getStorage();
    int parkingEmptyNum = parking.getStorageList().size();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
    when(carRepository.findById("test")).thenReturn(Optional.of(car));
    parkingService.takeCar("123", "test");
    assertEquals(parkingEmptyNum + 1, parking.getStorageList().size());
  }

  private Storage getStorage() {
    return Storage.builder().carId("test").address("3").zone("A").id("1").build();
  }

  private Car getCar() {
    return Car.builder().id("test").brand("奔驰").color("黑").build();
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_ticket() {
    when(ticketRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.takeCar("123", "test"),
        ExceptionMessage.NOT_FOUND_TICKET.getMessage());
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_storage() {
    Ticket ticket = getRightTicket();
    Parking parking = getParking();
    Storage storage = getStorage();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
    when(carRepository.findById("test")).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.takeCar("123", "test"),
        ExceptionMessage.NOT_FOUND_TICKET.getMessage());
  }

  @Test
  void should_throw_notFoundResourceException_when_not_found_car() {
    Ticket ticket = getRightTicket();
    Parking parking = getParking();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    when(storageRepository.findById("1")).thenReturn(Optional.empty());
    when(ticketRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.takeCar("123", "test"),
        ExceptionMessage.NOT_FOUND_TICKET.getMessage());
  }

  @Test
  void should_throw_illegalTicketException_when_not_right_ticket() {
    Ticket ticket = getWrongTicket();
    Parking parking = getParking();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    assertThrows(
        IllegalTicketException.class,
        () -> parkingService.takeCar("123", "test"));
  }

  @Test
  void should_return_false_when_not_take_right_car() {
    Ticket ticket = getRightTicket();
    Parking parking = getParking();
    Car car = getCar();
    Storage storage = getStorage();
    when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
    when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
    when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
    when(carRepository.findById("test")).thenReturn(Optional.of(car));
    assertThrows(
        NotRightCarException.class,
        () -> parkingService.takeCar("123", "test1"));
  }

  @Test
  void should_get_all_parking_when_request()
      throws NotParkingHelperException, NotFoundResourceException {
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    User user = getNormalUser();
    Role role = getNormalRole();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    List<Parking> parkings = parkingService.getAllParking(anyString());
    assertEquals(parkings, parkingList);
  }

  @Test
  void should_throw_noMatchingRoleException_when_not_parking_helper() {
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
  void should_throw_notFoundResourceException_when_not_found_user() {
    when(userRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(
        NotFoundResourceException.class,
        () -> parkingService.getAllParking(anyString()));
  }

  @Test
  void should_reduce_first_parking_when_given_normal_helper_and_first_not_empty() {
    User normalHelper = getNormalUser();
    Car car = getCar();
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    int initNumber = parkingList.get(0).getStorageList().size();
    Role role = getNormalRole();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    Ticket returnTicket = parkingService.parkingCarByHelper(anyString(), car);
    assertNotNull(returnTicket);
    assertEquals(initNumber - 1, parkingList.get(0).getStorageList().size());
  }

  @Test
  void should_reduce_second_parking_when_given_normal_helper_and_first_is_empty() {
    User normalHelper = getNormalUser();
    Car car = getCar();
    Role role = getNormalRole();
    List<Parking> emptyFistParkingList = getParkingListWithFirstEmpty();
    int initNumber = emptyFistParkingList.get(1).getStorageList().size();
    when(parkingRepository.findAll()).thenReturn(emptyFistParkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    Ticket returnTicket = parkingService.parkingCarByHelper(anyString(), car);
    assertNotNull(returnTicket);
    assertEquals(initNumber - 1, emptyFistParkingList.get(1).getStorageList().size());
  }

  private Role getNormalRole() {
    return Role.builder().role("NORMAL_HELPER").build();
  }

  private List<Parking> getParkingListWithFirstEmpty() {
    Parking parking = getFullParking();
    List<Parking> parkings = new ArrayList<>(Collections.singletonList(parking));
    parkings.add(getParking());
    return parkings;
  }

  @Test
  void should_reduce_max_empty_parking_when_given_smart_helper() {
    User smartHelper = getSmartUser();
    Car car = getCar();
    Role role = getSmartRole();
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    int initNumber = parkingList.get(parkingList.size() - 1).getStorageList().size();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    Ticket returnTicket = parkingService.parkingCarByHelper(anyString(), car);
    assertNotNull(returnTicket);
    assertEquals(initNumber - 1, parkingList.get(parkingList.size() - 1).getStorageList().size());
  }

  @Test
  void should_parking_success_by_manager() {
    User manager = getManager();
    Car car = getCar();
    Role role = getManagerRole();
    List<Parking> parkingList = getParkingListWithLargeParkingInLast();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(manager));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    Ticket returnTicket = parkingService.parkingCarByHelper(anyString(), car);
    assertNotNull(returnTicket);
  }

  private Role getManagerRole() {
    return Role.builder().role("MANGER").build();
  }

  private User getManager() {
    return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
        .createDate("2020-10-12 15:33:33").removeDate(null).role("3").build();
  }

  @Test
  void should_throw_outOfSetException_when_given_full_parkings_with_smart_helper() {
    User smartHelper = getSmartUser();
    Car car = getCar();
    Role role = getSmartRole();
    List<Parking> parkingList = getFullParkingList();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    assertThrows(OutOfSetException.class,
        () -> parkingService.parkingCarByHelper(anyString(), car));
  }

  private Role getSmartRole() {
    return Role.builder().role("SMART_HELPER").build();
  }

  @Test
  void should_throw_outOfSetException_when_given_full_parkings_with_normal_helper() {
    User normalHelper = getNormalUser();
    Car car = getCar();
    Role role = getNormalRole();
    List<Parking> parkingList = getFullParkingList();
    when(parkingRepository.findAll()).thenReturn(parkingList);
    when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
    when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
    assertThrows(OutOfSetException.class,
        () -> parkingService.parkingCarByHelper(anyString(), car));
  }

  private List<Parking> getFullParkingList() {
    Parking parking = getFullParking();
    return new ArrayList<>(Collections.nCopies(3, parking));
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
        .storageId("1")
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
        .storageId("1")
        .build();
  }

  private List<Parking> getParkingListWithLargeParkingInLast() {
    Parking parking = getParking();
    List<Parking> parkings = new ArrayList<>(Collections.nCopies(3, parking));
    parkings.add(getLargeSizeParking());
    return parkings;
  }

  private Parking getLargeSizeParking() {
    String id = GenerateId.getUUID();
    List<Storage> storageList = generateStorageList(20, id);
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096c").size(20000).storageList(storageList).build();
  }

  private Parking getFullParking() {
    String id = GenerateId.getUUID();
    List<Storage> storageList = generateStorageList(0, id);
    return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096c").size(20).storageList(storageList).build();
  }
}