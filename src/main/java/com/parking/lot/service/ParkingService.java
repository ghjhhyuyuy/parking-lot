package com.parking.lot.service;

import com.parking.lot.entity.*;
import com.parking.lot.entity.helper.ParkingHelper;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.*;
import com.parking.lot.repository.*;
import com.parking.lot.util.GenerateId;
import com.parking.lot.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.parking.lot.util.StorageUtil.generateStorageList;

@Service
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CarRepository carRepository;
    private final StorageRepository storageRepository;

    @Autowired
    public ParkingService(ParkingRepository parkingRepository, TicketRepository ticketRepository,
                          UserRepository userRepository, RoleRepository roleRepository, CarRepository carRepository,
                          StorageRepository storageRepository) {
        this.parkingRepository = parkingRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.storageRepository = storageRepository;
        this.carRepository = carRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Ticket parkingCarBySelf(String parkingId, Car car)
            throws OverSizeException, NotFoundResourceException {
        Parking parking = getParkingById(parkingId);
        ifSizeMoreThanZero(parking);
        return parkingCarInPark(parking, car);
    }

    private void ifSizeMoreThanZero(Parking parking) {
        if (parking.getEmptyNumber() > 0) {
            return;
        }
        throw new OverSizeException(ExceptionMessage.PARKING_OVER_SIZE);
    }

    private Ticket parkingCarInPark(Parking parking, Car car) {
        Storage storage = getStorageAndSaveCarInThisStorage(parking, car);
        carRepository.save(car);
        storageRepository.save(storage);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");
        Ticket ticket = new Ticket(GenerateId.getUUID(), parking.getId(), dateTimeFormatter.format(TimeUtil.getTime(0)), storage.getId());
        ticketRepository.save(ticket);
        parkingRepository.save(parking);
        return ticket;
    }

    private Storage getStorageAndSaveCarInThisStorage(Parking parking, Car car) {
        Storage storage = parking.getStorageList().stream().filter(theStorage -> theStorage.getCarId() == null).collect(Collectors.toList()).get(0);
        storage.parkingCar(car.getId());
        parking.reduceNum();
        return storage;
    }

    private Parking getParkingById(String parkingId) throws NotFoundResourceException {
        return parkingRepository.findById(parkingId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Car takeCar(String ticketId, String carId)
            throws IllegalTicketException, NotFoundResourceException {
        Ticket ticket = getTicketById(ticketId);
        if (checkTicket(ticket)) {
            Car car = getCarByTicket(ticket);
            if (car.getId().equals(carId)) {
                return takeCarFromPark(ticket, car);
            }
            throw new NotRightCarException();
        } else {
            throw new IllegalTicketException(ExceptionMessage.ILLEGAL_TICKET);
        }
    }

    private boolean checkTicket(Ticket ticket) {
        Parking parking = parkingRepository.findById(ticket.getParkingLotId()).orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
        Storage storage = storageRepository.findById(ticket.getStorageId()).orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STORAGE));
        return parking.getStorageList().stream().filter(theStorage -> theStorage.getId().equals(storage.getId())).count() == 1;
    }

    public List<Parking> getAllParking(String userId)
            throws NotParkingHelperException, NotFoundResourceException {
        return Optional.of(getParkingStaff(userId))
                .map(ParkingHelper::isAllow)
                .filter(isAllow -> isAllow == true)
                .map(p -> parkingRepository.findAll())
                .orElseThrow(() -> new NotParkingHelperException(ExceptionMessage.NOT_PARKING_HELPER));
    }

    private ParkingHelper getParkingStaff(String userId) throws NotFoundResourceException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER));
        if (user.getRemoveDate() == null) {
            return getParkingHelperByRoleId(user.getRole());
        }
        throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER);
    }

    private ParkingHelper getParkingHelperByRoleId(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoMatchingRoleException(ExceptionMessage.NO_MATCHING_ROLE));
        return RoleType.valueOf(role.getRole()).getParkingHelper();
    }

    private Ticket getTicketById(String ticketId) throws NotFoundResourceException {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_TICKET));
    }

    private Car takeCarFromPark(Ticket ticket, Car car) throws NotFoundResourceException {
        Parking parking = reuseStorageInThisParking(ticket);
        parkingRepository.save(parking);
        ticketRepository.delete(ticket);
        return car;
    }

    private Parking reuseStorageInThisParking(Ticket ticket) {
        Storage storage = getStorageByTicket(ticket);
        storage.removeCarId();
        Parking parking = getParkingByTicket(ticket);
        parking.increaseNum();
        return parking;
    }

    private Storage getStorageByTicket(Ticket ticket) {
        return storageRepository.findById(ticket.getStorageId())
                .orElseThrow(() -> new NotFoundResourceException(
                        ExceptionMessage.NOT_FOUND_STORAGE));
    }

    private Parking getParkingByTicket(Ticket ticket) {
        return parkingRepository.findById(ticket.getParkingLotId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
    }

    private Car getCarByTicket(Ticket ticket) {
        Storage storage = storageRepository.findById(ticket.getStorageId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STORAGE));
        return carRepository.findById(storage.getCarId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_CAR));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Ticket parkingCarByStaff(String userId, Car car) {
        Parking parking = getParkingByStaff(userId);
        return parkingCarInPark(parking, car);
    }

    private Parking getParkingByStaff(String userId) {
        ParkingHelper parkingHelper = getParkingStaff(userId);
        List<Parking> parkings = getAllParking(userId);
        return parkingHelper.parking(parkings);
    }

    public Parking addParking(int size) {
        Parking parking = createParking(size);
        return parkingRepository.save(parking);
    }

    private Parking createParking(int size) {
        if (size > 0) {
            String id = GenerateId.getUUID();
            List<Storage> storageList = generateStorageList(size, id);
            return new Parking(GenerateId.getUUID(), size, storageList);
        }
        throw new IllegalSizeException(ExceptionMessage.ILLEGAL_SIZE);
    }

    @Transactional
    public void removeParking(String parkingId) {
        deleteRelativeStorage(parkingId);
        parkingRepository.deleteById(parkingId);
    }

    private void deleteRelativeStorage(String parkingId) throws StillCarInParkingException {
        List<Storage> storageList = storageRepository.findByParkingId(parkingId);
        for (Storage storage : storageList) {
            if (storage.getCarId() == null) {
                storageRepository.delete(storage);
            } else {
                throw new StillCarInParkingException();
            }
        }
    }
}
