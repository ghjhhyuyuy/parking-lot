package com.parking.lot.service;

import static com.parking.lot.entity.Ticket.getTicket;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.entity.helper.Manager;
import com.parking.lot.entity.helper.NormalHelper;
import com.parking.lot.entity.helper.ParkingHelper;
import com.parking.lot.entity.helper.SmartHelper;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.InstanseType;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.NoMatchingRoleException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotParkingHelperException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import com.parking.lot.repository.UserRepository;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingService {

  private final ParkingRepository parkingRepository;
  private final TicketRepository ticketRepository;
  private final UserRepository userRepository;

  @Autowired
  public ParkingService(ParkingRepository parkingRepository, TicketRepository ticketRepository,
      UserRepository userRepository) {
    this.parkingRepository = parkingRepository;
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Retryable(backoff = @Backoff(multiplier = 1.5))
  public Ticket parkingCar(String parkingId)
      throws OverSizeException, NotFoundResourceException {
    Parking parking = getCurrentPark(parkingId);
    parking.checkSize();
    return parkingCarInPark(parking);
  }

  private Ticket parkingCarInPark(Parking parking) {
    Ticket ticket = getTicket(parking.getId());
    ticketRepository.save(ticket);
    parking.reduceSize();
    parkingRepository.save(parking);
    return ticket;
  }

  private Parking getCurrentPark(String parkingId) throws NotFoundResourceException {
    Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
    if (optionalParking.isPresent()) {
      return optionalParking.get();
    }
    throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void takeCar(String ticketId)
      throws IllegalTicketException, ParseException, NotFoundResourceException {
    Ticket ticket = getCurrentTicket(ticketId);
    if (ticket.checkTicket()) {
      takeCarFromPark(ticket);
    } else {
      throw new IllegalTicketException(ExceptionMessage.ILLEGAL_TICKET);
    }
  }

  public List<Parking> getAllParking(String userId)
      throws NotParkingHelperException, NotFoundResourceException {
    if (getParkingHelper(userId).isAllow()) {
      return parkingRepository.findAll();
    }
    throw new NotParkingHelperException(ExceptionMessage.NOT_PARKING_HELPER);
  }

  private ParkingHelper getParkingHelper(String userId) throws NotFoundResourceException {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      return getInstanceById(user.getRole());
    }
    throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER);
  }

  private ParkingHelper getInstanceById(String roleId) {
    if (roleId.equals(InstanseType.MANGER.getId())) {
      return new Manager();
    } else if (roleId.equals(InstanseType.SMART_HELPER.getId())) {
      return new SmartHelper();
    } else if(roleId.equals(InstanseType.NORMAL_HELPER.getId())){
      return new NormalHelper();
    }
    throw new NoMatchingRoleException(ExceptionMessage.NO_MATCHING_ROLE);
  }

  private Ticket getCurrentTicket(String ticketId) throws NotFoundResourceException {
    Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
    if (optionalTicket.isPresent()) {
      return optionalTicket.get();
    } else {
      throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_TICKET);
    }
  }

  private void takeCarFromPark(Ticket ticket) throws NotFoundResourceException {
    Optional<Parking> optionalParking = parkingRepository.findById(ticket.getParkingLotId());
    if (optionalParking.isPresent()) {
      Parking parking = optionalParking.get();
      parking.upSize();
      parkingRepository.save(parking);
    } else {
      throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING);
    }
  }

  public void helperSave(String userId) {
    ParkingHelper parkingHelper = getParkingHelper(userId);
    throw new NotParkingHelperException(ExceptionMessage.NOT_PARKING_HELPER);
  }
}
