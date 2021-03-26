package com.parking.lot.service;

import com.parking.lot.entity.*;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.*;
import com.parking.lot.helper.ParkingStaff;
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
import java.util.stream.Collectors;

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
    public Ticket parkingCarBySelf(String parkingId, Car car)
            throws OverSizeException, NotFoundResourceException {
        Basement basement = basementRepository.findById(parkingId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
        ifSizeMoreThanZero(basement);
        return parkingCarInBasement(basement, car);
    }

    private void ifSizeMoreThanZero(Basement basement) {
        if (basement.getEmptyNumber() <= 0) {
            throw new OverSizeException(ExceptionMessage.PARKING_OVER_SIZE);
        }
    }

    private Ticket parkingCarInBasement(Basement basement, Car car) {
        Storage storage = basement.getStorageList().stream()
                .filter(theStorage -> theStorage.getCarId() == null).collect(Collectors.toList()).get(0);
        storage.setCarId(car.getId());
        basement.reduceNum();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");
        Ticket ticket = new Ticket(GenerateId.getUUID(),
                dateTimeFormatter.format(TimeUtil.getTime(0)),
                basement.getId(),
                storage.getId());

        saveCarInStorage(basement,car,storage,ticket);
        return ticket;
    }

    private void saveCarInStorage(Basement basement, Car car, Storage storage, Ticket ticket) {
        carRepository.save(car);
        storageRepository.save(storage);
        //todo change type time
        ticketRepository.save(ticket);
        basementRepository.save(basement);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Retryable(backoff = @Backoff(multiplier = 1.5))
    public Car takeCar(String ticketId, String carId)
            throws IllegalTicketException, NotFoundResourceException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_TICKET));
        if (checkTicket(ticket)) {
            return returnCarIfSameCar(ticket,carId);
        } else {
            throw new IllegalTicketException(ExceptionMessage.ILLEGAL_TICKET);
        }
    }

    private Car returnCarIfSameCar(Ticket ticket, String carId) {
        Car car = getCarByTicket(ticket);
        if (car.getId().equals(carId)) {
            return takeCarFromBasement(ticket, car);
        }
        throw new NotRightCarException();
    }

    private boolean checkTicket(Ticket ticket) {
        Basement basement = basementRepository.findById(ticket.getParkingLotId()).orElseThrow(() ->
                new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
        return basement.getStorageList().stream().filter(theStorage ->
                theStorage.getId().equals(ticket.getStorageId())).count() == 1;
    }

    private ParkingStaff getParkingStaff(String userId) throws NotFoundResourceException {
        Staff staff = staffRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER));
        if (staff.getRemoveDate() == null) {
            return getParkingStaffByRoleId(staff.getRole());
        }
        throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER);
    }

    private ParkingStaff getParkingStaffByRoleId(String roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new NoMatchingRoleException(ExceptionMessage.NO_MATCHING_ROLE));
        return RoleType.valueOf(role.getRole()).getParkingHelper();
    }

    private Car takeCarFromBasement(Ticket ticket, Car car) throws NotFoundResourceException {
        Storage storage = storageRepository.findById(ticket.getStorageId())
                .orElseThrow(() -> new NotFoundResourceException(
                        ExceptionMessage.NOT_FOUND_STORAGE));
        storage.removeCarId();
        Basement basement = basementRepository.findById(ticket.getParkingLotId())
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
        basement.increaseNum();
        saveAfterFreeStorage(storage,basement,ticket);
        return car;
    }

    private void saveAfterFreeStorage(Storage storage, Basement basement, Ticket ticket) {
        storageRepository.save(storage);
        basementRepository.save(basement);
        ticketRepository.delete(ticket);
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
        Basement basement = getBasementByStaff(userId);
        return parkingCarInBasement(basement, car);
    }

    private Basement getBasementByStaff(String userId) {
        ParkingStaff parkingStaff = getParkingStaff(userId);
        List<Basement> basements = basementRepository.findAll();
        return parkingStaff.parking(basements);
    }

    public Basement addBasement(int size) {
        Basement basement = createBasement(size);
        return basementRepository.save(basement);
    }

    private Basement createBasement(int size) {
        if (size > 0) {
            String id = GenerateId.getUUID();
            List<Storage> storageList = generateStorageList(size, id);
            return new Basement(GenerateId.getUUID(), size, storageList);
        }
        throw new IllegalSizeException(ExceptionMessage.ILLEGAL_SIZE);
    }

    @Transactional
    public void removeBasement(String basementId) {
        deleteRelativeStorage(basementId);
        basementRepository.deleteById(basementId);
    }

    private void deleteRelativeStorage(String basementId) throws StillCarInParkingException {
        List<Storage> storageList = storageRepository.findByParkingId(basementId);
        for (Storage storage : storageList) {
            if (storage.getCarId() == null) {
                storageRepository.delete(storage);
            } else {
                throw new StillCarInParkingException();
            }
        }
    }
}
