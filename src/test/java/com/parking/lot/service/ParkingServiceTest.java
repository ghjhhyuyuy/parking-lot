package com.parking.lot.service;

import com.parking.lot.entity.*;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.*;
import com.parking.lot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.parking.lot.util.StorageUtil.generateStorageList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

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

    @BeforeEach
    void setUp() {
        openMocks(this);
        parkingService = new ParkingService(parkingRepository, ticketRepository, userRepository,
                roleRepository, carRepository, storageRepository);
    }

    @Nested
    @DisplayName("Self Parking Car")
    class SelfParkingCarCases {
        @Test
        void should_get_ticket_and_reduce_size_when_park_car()
                throws NotFoundResourceException {
            Car car = getCar();
            Parking parking = getParking();
            List<Parking> parkings = Collections.singletonList(parking);
            int parkingEmptyNum = getStorageListSizeInFirstParking(parkings);
            when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
            Ticket returnTicket = parkingService.parkingCarBySelf(parking.getId(), car);
            assertNotNull(returnTicket);
            assertEquals(parkingEmptyNum - 1, getStorageListSizeInFirstParking(parkings));
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
    }

    @Nested
    @DisplayName("Take Car")
    class TakeCarCases {
        @Test
        void should_get_car_and_add_park_size_when_give_right_ticket()
                throws IllegalTicketException, NotFoundResourceException {
            Ticket ticket = getRightTicket();
            Parking parking = getParking();
            Car car = getCar();
            Storage storage = getStorage();
            List<Parking> parkings = Collections.singletonList(parking);
            int parkingEmptyNum = getStorageListSizeInFirstParking(parkings);
            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
            when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
            when(carRepository.findById("test")).thenReturn(Optional.of(car));
            parkingService.takeCar("123", "test");
            assertEquals(parkingEmptyNum + 1, getStorageListSizeInFirstParking(parkings));
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
            Storage storage = getWrongStorage();
            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(parkingRepository.findById("123")).thenReturn(Optional.of(parking));
            when(storageRepository.findById("2")).thenReturn(Optional.of(storage));
            assertThrows(
                    IllegalTicketException.class,
                    () -> parkingService.takeCar("123", "test"));
        }

        private Storage getWrongStorage() {
            return Storage.builder().carId("test").address("3").id("B").build();
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
    }

    @Nested
    @DisplayName("Get All Parking")
    class GetAllParkingCases {
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
        void should_throw_notFoundResourceException_when_not_found_user() {
            when(userRepository.findById(anyString())).thenReturn(Optional.empty());
            assertThrows(
                    NotFoundResourceException.class,
                    () -> parkingService.getAllParking(anyString()));
        }
    }

    @Nested
    @DisplayName("Staff Parking Car")
    class StaffParkingCarCases {
        @Test
        void should_reduce_first_parking_when_given_normal_helper_and_first_not_empty() {
            User normalHelper = getNormalUser();
            Car car = getCar();
            List<Parking> parkingList = getParkingListWithLargeParkingInLast();
            int initNumber = getStorageListSizeInFirstParking(parkingList);
            Role role = getNormalRole();
            when(parkingRepository.findAll()).thenReturn(parkingList);
            when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);
            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInFirstParking(parkingList));
        }

        @Test
        void should_reduce_second_parking_when_given_normal_helper_and_first_is_empty() {
            User normalHelper = getNormalUser();
            Car car = getCar();
            Role role = getNormalRole();
            List<Parking> emptyFistParkingList = getParkingListWithFirstFull();
            int initNumber = getStorageListSizeInSecondParking(emptyFistParkingList);
            when(parkingRepository.findAll()).thenReturn(emptyFistParkingList);
            when(userRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);
            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInSecondParking(emptyFistParkingList));
        }

        @Test
        void should_reduce_max_empty_parking_when_given_smart_helper() {
            User smartHelper = getSmartUser();
            Car car = getCar();
            Role role = getSmartRole();
            List<Parking> parkingList = getParkingListWithLargeParkingInLast();
            int initNumber = getStorageListSizeInLastParking(parkingList);
            when(parkingRepository.findAll()).thenReturn(parkingList);
            when(userRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);
            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInLastParking(parkingList));
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
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);
            assertNotNull(returnTicket);
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
                    () -> parkingService.parkingCarByStaff(anyString(), car));
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
                    () -> parkingService.parkingCarByStaff(anyString(), car));
        }
    }

    private int getStorageListSizeInFirstParking(List<Parking> parkingList) {
        return parkingList.get(0).getEmptyNumber();
    }

    private int getStorageListSizeInSecondParking(List<Parking> parkingList) {
        return parkingList.get(1).getEmptyNumber();
    }

    private int getStorageListSizeInLastParking(List<Parking> parkingList) {
        return parkingList.get(parkingList.size() - 1).getEmptyNumber();
    }

    private Role getNormalRole() {
        return Role.builder().id("1").role("NORMAL_HELPER").build();
    }

    private Role getSmartRole() {
        return Role.builder().id("2").role("SMART_HELPER").build();
    }

    private Role getManagerRole() {
        return Role.builder().role("3").role("MANGER").build();
    }

    private Storage getStorage() {
        return Storage.builder().carId("test").address("3").id("1").build();
    }

    private Storage getEmptyStorage() {
        return Storage.builder().address("4").id("2").build();
    }

    private Car getCar() {
        return Car.builder().id("test").build();
    }

    private User getManager() {
        return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("3").build();
    }

    private User getSmartUser() {
        return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("2").build();
    }

    private User getNormalUser() {
        return User.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("1").build();
    }

    private String getNowTime(){
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    private Ticket getWrongTicket() {
        return Ticket.builder()
                .id(UUID.randomUUID().toString())
                .parkingLotId("123")
                .entryTime(getNowTime())
                .storageId("2")
                .build();
    }

    private Ticket getRightTicket() {
        return Ticket.builder()
                .id(UUID.randomUUID().toString())
                .parkingLotId("123")
                .entryTime(getNowTime())
                .storageId("1")
                .build();
    }

    private List<Parking> getFullParkingList() {
        Parking parking = getFullParking();
        return new ArrayList<>(Collections.singletonList(parking));
    }

    private List<Parking> getParkingListWithLargeParkingInLast() {
        Parking parking = getParking();
        List<Parking> parkings = new ArrayList<>(Collections.singletonList(parking));
        parkings.add(getLargeSizeParking());
        return parkings;
    }

    private List<Parking> getParkingListWithFirstFull() {
        Parking parking = getFullParking();
        List<Parking> parkings = new ArrayList<>(Collections.singletonList(parking));
        parkings.add(getParking());
        return parkings;
    }

    private Parking getLargeSizeParking() {
        String id = "42f408b2-3ee6-48fd-8159-b49789f7096d";
        List<Storage> storageList = generateStorageList(20, id);
        return Parking.builder().id(id).emptyNumber(20)
                .storageList(storageList).build();
    }

    private Parking getFullParking() {
        String id = "42f408b2-3ee6-48fd-8159-b49789f7096c";
        List<Storage> storageList = generateStorageList(0, id);
        return Parking.builder().id(id).emptyNumber(0)
                .storageList(storageList).build();
    }

    private Parking getParking() {
        Storage storage = getStorage();
        Storage emptyStorage = getEmptyStorage();
        List<Storage> storageList = new ArrayList<>();
        storageList.add(storage);
        storageList.add(emptyStorage);
        return Parking.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").emptyNumber(1)
                .storageList(storageList).build();
    }
}