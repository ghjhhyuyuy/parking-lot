package com.parking.lot.service;

import static com.parking.lot.entity.Ticket.getTicket;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Role;
import com.parking.lot.entity.Ticket;
import com.parking.lot.entity.User;
import com.parking.lot.entity.helper.ParkingHelper;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.enums.RoleType;
import com.parking.lot.exception.IllegalTicketException;
import com.parking.lot.exception.NoMatchingRoleException;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.exception.NotParkingHelperException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.RoleRepository;
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
  private final RoleRepository roleRepository;

  @Autowired
  public ParkingService(ParkingRepository parkingRepository, TicketRepository ticketRepository,
      UserRepository userRepository, RoleRepository roleRepository) {
    this.parkingRepository = parkingRepository;
    this.ticketRepository = ticketRepository;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
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
  @Retryable(backoff = @Backoff(multiplier = 1.5))
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
    if (optionalUser.isPresent() && optionalUser.get().getRemoveDate() == null) {
      User user = optionalUser.get();
      return getParkingHelperByRoleId(user.getRole());
    }
    throw new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER);
  }

  private ParkingHelper getParkingHelperByRoleId(String roleId) {
    Optional<Role> roleOptional = roleRepository.findById(roleId);
    if (roleOptional.isPresent()) {
      Role role = roleOptional.get();
      return RoleType.valueOf(role.getRole()).getParkingHelper();
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

  //  @Transactional(isolation = Isolation.SERIALIZABLE)
//  @Retryable(backoff = @Backoff(multiplier = 1.5))
  public Ticket helperSave(String userId, boolean byOrderForManager) {
    ParkingHelper parkingHelper = getParkingHelper(userId);
    List<Parking> parkings = getAllParking(userId);
    Parking parking = parkingHelper.parking(parkings, byOrderForManager);
    return parkingCarInPark(parking);
  }

  public User addUser(User user) {
    user = User.createUser(user);
    return userRepository.save(user);
  }

  public User removeUser(User user) {
    user = User.removeUser(user);
    return userRepository.save(user);
  }

  public Parking addParking(int size) {
    Parking parking = Parking.createParking(size);
    return parkingRepository.save(parking);
  }

  public void removeParking(String parkingId) {
    parkingRepository.deleteById(parkingId);
  }
}
