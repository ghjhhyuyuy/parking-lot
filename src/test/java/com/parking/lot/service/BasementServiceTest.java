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

import java.time.LocalDateTime;
import java.util.*;

import static com.parking.lot.util.StorageUtil.generateStorageList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class BasementServiceTest {

    ParkingService parkingService;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    BasementRepository basementRepository;
    @Mock
    StaffRepository staffRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    CarRepository carRepository;
    @Mock
    StorageRepository storageRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        parkingService = new ParkingService(basementRepository, ticketRepository, staffRepository,
                roleRepository, carRepository, storageRepository);
    }

    @Nested
    @DisplayName("Self Parking Car")
    class SelfBasementCarCases {
        @Test
        void should_get_ticket_and_reduce_size_when_park_car()
                throws NotFoundResourceException {
            //given
            Car car = getCar();
            Basement basement = getParking();
            int parkingEmptyNum = basement.getEmptyNumber();

            //when
            when(basementRepository.findById(basement.getId())).thenReturn(Optional.of(basement));
            Ticket returnTicket = parkingService.parkingCarBySelf(basement.getId(), car);

            //then
            assertNotNull(returnTicket);
            assertEquals(parkingEmptyNum - 1, basement.getEmptyNumber());
        }

        @Test
        void should_throw_notFoundResourceException_when_not_found_park() {
            Car car = getCar();
            when(basementRepository.findById(anyString())).thenReturn(Optional.empty());
            assertThrows(
                    NotFoundResourceException.class,
                    () -> parkingService.parkingCarBySelf("123", car),
                    ExceptionMessage.NOT_FOUND_BASEMENT.getMessage());
        }

        @Test
        void should_throw_overSizeException_when_over_park_size() {
            Basement basement = getFullParking();
            Car car = getCar();
            when(basementRepository.findById(basement.getId())).thenReturn(Optional.of(basement));
            assertThrows(
                    OverSizeException.class,
                    () -> parkingService.parkingCarBySelf(basement.getId(), car));
        }
    }

    @Nested
    @DisplayName("Take Car")
    class TakeCarCases {
        @Test
        void should_get_car_and_add_park_size_when_give_right_ticket()
                throws IllegalTicketException, NotFoundResourceException {
            Ticket ticket = getRightTicket();
            Basement basement = getParking();
            Car car = getCar();
            Storage storage = getStorage();
            int parkingEmptyNum = basement.getEmptyNumber();

            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(basementRepository.findById("123")).thenReturn(Optional.of(basement));
            when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
            when(carRepository.findById("test")).thenReturn(Optional.of(car));
            parkingService.takeCar("123", "test");

            assertEquals(parkingEmptyNum + 1, basement.getEmptyNumber());
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
            Basement basement = getParking();
            Storage storage = getStorage();

            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(basementRepository.findById("123")).thenReturn(Optional.of(basement));
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
            Basement basement = getParking();
            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(basementRepository.findById("123")).thenReturn(Optional.of(basement));
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
            Basement basement = getParking();
            Storage storage = getWrongStorage();
            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(basementRepository.findById("123")).thenReturn(Optional.of(basement));
            when(storageRepository.findById("300")).thenReturn(Optional.of(storage));
            assertThrows(
                    IllegalTicketException.class,
                    () -> parkingService.takeCar("123", "test"));
        }

        @Test
        void should_return_NotRightCarException_when_not_take_right_car() {
            Ticket ticket = getRightTicket();
            Basement basement = getParking();
            Car car = getCar();
            Storage storage = getStorage();
            when(ticketRepository.findById("123")).thenReturn(Optional.of(ticket));
            when(basementRepository.findById("123")).thenReturn(Optional.of(basement));
            when(storageRepository.findById("1")).thenReturn(Optional.of(storage));
            when(carRepository.findById("test")).thenReturn(Optional.of(car));
            assertThrows(
                    NotRightCarException.class,
                    () -> parkingService.takeCar("123", "test1"));
        }
    }

    @Nested
    @DisplayName("Staff Parking Car")
    class StaffBasementCarCases {
        @Test
        void should_reduce_first_basement_when_given_normal_helper_and_first_not_empty() {
            Staff normalHelper = getNormalUser();
            Car car = getCar();
            List<Basement> basementList = getParkingListWithLargeParkingInLast();
            int initNumber = getStorageListSizeInFirstParking(basementList);
            Role role = getNormalRole();

            when(basementRepository.findAll()).thenReturn(basementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);

            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInFirstParking(basementList));
        }

        @Test
        void should_reduce_second_basement_when_given_normal_helper_and_first_is_empty() {
            Staff normalHelper = getNormalUser();
            Car car = getCar();
            Role role = getNormalRole();
            List<Basement> emptyFistBasementList = getParkingListWithFirstFull();
            int initNumber = getStorageListSizeInSecondParking(emptyFistBasementList);

            when(basementRepository.findAll()).thenReturn(emptyFistBasementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);

            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInSecondParking(emptyFistBasementList));
        }

        @Test
        void should_reduce_max_empty_basement_when_given_smart_helper() {
            Staff smartHelper = getSmartUser();
            Car car = getCar();
            Role role = getSmartRole();
            List<Basement> basementList = getParkingListWithLargeParkingInLast();
            int initNumber = getStorageListSizeInLastParking(basementList);

            when(basementRepository.findAll()).thenReturn(basementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);

            assertNotNull(returnTicket);
            assertEquals(initNumber - 1, getStorageListSizeInLastParking(basementList));
        }

        @Test
        void should_parking_success_by_manager() {
            Staff manager = getManager();
            Car car = getCar();
            Role role = getManagerRole();
            List<Basement> basementList = getParkingListWithLargeParkingInLast();

            when(basementRepository.findAll()).thenReturn(basementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(manager));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            Ticket returnTicket = parkingService.parkingCarByStaff(anyString(), car);

            assertNotNull(returnTicket);
        }

        @Test
        void should_throw_outOfSetException_when_given_full_basements_with_smart_helper() {
            Staff smartHelper = getSmartUser();
            Car car = getCar();
            Role role = getSmartRole();
            List<Basement> basementList = getFullParkingList();
            when(basementRepository.findAll()).thenReturn(basementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(smartHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            assertThrows(OutOfSetException.class,
                    () -> parkingService.parkingCarByStaff(anyString(), car));
        }

        @Test
        void should_throw_outOfSetException_when_given_full_basements_with_normal_helper() {
            Staff normalHelper = getNormalUser();
            Car car = getCar();
            Role role = getNormalRole();
            List<Basement> basementList = getFullParkingList();
            when(basementRepository.findAll()).thenReturn(basementList);
            when(staffRepository.findById(anyString())).thenReturn(Optional.of(normalHelper));
            when(roleRepository.findById(anyString())).thenReturn(Optional.of(role));
            assertThrows(OutOfSetException.class,
                    () -> parkingService.parkingCarByStaff(anyString(), car));
        }
    }

    private int getStorageListSizeInFirstParking(List<Basement> basementList) {
        return basementList.get(0).getEmptyNumber();
    }

    private int getStorageListSizeInSecondParking(List<Basement> basementList) {
        return basementList.get(1).getEmptyNumber();
    }

    private int getStorageListSizeInLastParking(List<Basement> basementList) {
        return basementList.get(basementList.size() - 1).getEmptyNumber();
    }

    private Role getNormalRole() {
        return Role.builder().id("1").role("NORMAL_STAFF").build();
    }

    private Role getSmartRole() {
        return Role.builder().id("2").role("SMART_STAFF").build();
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

    private Storage getWrongStorage() {
        return Storage.builder().carId("test").address("3").id("B").build();
    }

    private Car getCar() {
        return Car.builder().id("test").build();
    }

    private Staff getManager() {
        return Staff.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("3").build();
    }

    private Staff getSmartUser() {
        return Staff.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("2").build();
    }

    private Staff getNormalUser() {
        return Staff.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").name("Tom")
                .createDate("2020-10-12 15:33:33").removeDate(null).role("1").build();
    }

    private LocalDateTime getNowTime() {
        return LocalDateTime.now();
    }

    private Ticket getWrongTicket() {
        return Ticket.builder()
                .id(UUID.randomUUID().toString())
                .parkingLotId("123")
                .entryTime(getNowTime())
                .storageId("300")
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

    private List<Basement> getFullParkingList() {
        Basement basement = getFullParking();
        return new ArrayList<>(Collections.singletonList(basement));
    }

    private List<Basement> getParkingListWithLargeParkingInLast() {
        Basement basement = getParking();
        List<Basement> basements = new ArrayList<>(Collections.singletonList(basement));
        basements.add(getLargeSizeParking());
        return basements;
    }

    private List<Basement> getParkingListWithFirstFull() {
        Basement basement = getFullParking();
        List<Basement> basements = new ArrayList<>(Collections.singletonList(basement));
        basements.add(getParking());
        return basements;
    }

    private Basement getLargeSizeParking() {
        String id = "42f408b2-3ee6-48fd-8159-b49789f7096d";
        List<Storage> storageList = generateStorageList(20, id);
        return Basement.builder().id(id).emptyNumber(20)
                .storageList(storageList).build();
    }

    private Basement getFullParking() {
        String id = "42f408b2-3ee6-48fd-8159-b49789f7096c";
        List<Storage> storageList = generateStorageList(0, id);
        return Basement.builder().id(id).emptyNumber(0)
                .storageList(storageList).build();
    }

    private Basement getParking() {
        Storage storage = getStorage();
        Storage emptyStorage = getEmptyStorage();
        List<Storage> storageList = new ArrayList<>();
        storageList.add(storage);
        storageList.add(emptyStorage);
        return Basement.builder().id("42f408b2-3ee6-48fd-8159-b49789f7096b").emptyNumber(1)
                .storageList(storageList).build();
    }
}