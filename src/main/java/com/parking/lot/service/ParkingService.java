package com.parking.lot.service;

import com.parking.lot.entity.*;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.*;
import com.parking.lot.repository.*;
import com.parking.lot.staff.ParkingStaff;
import com.parking.lot.util.GenerateId;
import com.parking.lot.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.parking.lot.util.StorageUtil.generateStorageList;

@Service
public class ParkingService {

    private final BasementRepository basementRepository;
    private final TicketRepository ticketRepository;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final CarRepository carRepository;
    private final StorageRepository storageRepository;

    @Autowired
    public ParkingService(BasementRepository basementRepository, TicketRepository ticketRepository,
                          StaffRepository staffRepository, RoleRepository roleRepository, CarRepository carRepository,
                          StorageRepository storageRepository) {
        this.basementRepository = basementRepository;
        this.ticketRepository = ticketRepository;
        this.staffRepository = staffRepository;
        this.roleRepository = roleRepository;
        this.storageRepository = storageRepository;
        this.carRepository = carRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Ticket parkingCarBySelf(String basementId, Car car)
            throws OverSizeException, NotFoundResourceException {
        Basement basement = basementRepository.findById(basementId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_BASEMENT));
        return parkingCarInBasement(basement, car);
    }

    private Ticket parkingCarInBasement(Basement basement, Car car) {
        Storage storage = basement.getStorageList().stream()
                .filter(theStorage -> theStorage.getCarId() == null)
                .findFirst().orElseThrow(() -> new OverSizeException(ExceptionMessage.BASEMENT_OVER_SIZE));
        storage.setCarId(car.getId());
        basement.reduceNum();

        Ticket ticket = new Ticket(GenerateId.getUUID(),
                TimeUtil.getTime(0),
                basement.getId(),
                storage.getId());

        saveCarInStorage(basement, car, storage, ticket);
        return ticket;
    }

    private void saveCarInStorage(Basement basement, Car car, Storage storage, Ticket ticket) {
        carRepository.save(car);
        storageRepository.save(storage);
        ticketRepository.save(ticket);
        basementRepository.save(basement);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Car takeCar(String ticketId, String carId)
            throws IllegalTicketException, NotFoundResourceException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_TICKET));
        if (!checkTicket(ticket)) {
            throw new IllegalTicketException(ExceptionMessage.ILLEGAL_TICKET);
        }
        return returnCarIfSameCar(ticket, carId);
    }

    private Car returnCarIfSameCar(Ticket ticket, String carId) {
        Storage storage = storageRepository.findById(ticket.getStorageId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STORAGE));
        Car car = carRepository.findById(storage.getCarId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_CAR));
        if (!car.getId().equals(carId)) {
            throw new NotRightCarException();
        }
        return takeCarFromBasement(ticket, car, storage);
    }

    private boolean checkTicket(Ticket ticket) {
        Storage storage = storageRepository.findById(ticket.getStorageId()).orElseThrow(() ->
                new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STORAGE));
        return storage.getBasementId().equals(ticket.getParkingLotId());
    }

    private ParkingStaff getParkingStaff(String userId) throws NotFoundResourceException {
        Staff staff = staffRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STAFF));
        if (staff.getRemoveDate() != null) {
            throw new NotFoundResourceException(ExceptionMessage.NOT_ACTIVE_STAFF);
        }
        return getParkingStaffByRoleId(staff.getRole());
    }

    //todo RoleType new object
    private ParkingStaff getParkingStaffByRoleId(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoMatchingRoleException(ExceptionMessage.NO_MATCHING_ROLE));
        return RoleType.valueOf(role.getRole()).getParkingStaff();
    }

    private Car takeCarFromBasement(Ticket ticket, Car car, Storage storage) throws NotFoundResourceException {
        storage.removeCarId();
        Basement basement = basementRepository.findById(ticket.getParkingLotId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_BASEMENT));
        basement.increaseNum();
        saveAfterFreeStorage(storage, basement, ticket);
        return car;
    }

    private void saveAfterFreeStorage(Storage storage, Basement basement, Ticket ticket) {
        storageRepository.save(storage);
        basementRepository.save(basement);
        ticketRepository.delete(ticket);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Ticket parkingCarByStaff(String userId, Car car) {
        Basement basement = getBasementByStaff(userId);
        return parkingCarInBasement(basement, car);
    }

    private Basement getBasementByStaff(String userId) {
        ParkingStaff parkingStaff = getParkingStaff(userId);
        List<Basement> basements = basementRepository.findAll();
        return parkingStaff.parking(basements);
    }

    public Basement addBasement(int size) {
        if (size <= 0) {
            throw new IllegalSizeException(ExceptionMessage.ILLEGAL_SIZE);
        }
        String id = GenerateId.getUUID();
        List<Storage> storageList = generateStorageList(size, id);
        Basement basement = new Basement(id, size, storageList);
        basementRepository.save(basement);
        return basement;
    }

    @Transactional
    public void removeBasement(String basementId) {
        deleteRelativeStorage(basementId);
        basementRepository.deleteById(basementId);
    }

    private void deleteRelativeStorage(String basementId) throws StillCarInBasementException {
        List<Storage> storageList = storageRepository.findByBasementId(basementId);
        for (Storage storage : storageList) {
            if (storage.getCarId() != null) {
                throw new StillCarInBasementException();
            }
            storageRepository.delete(storage);
        }
    }
}
