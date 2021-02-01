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
  public Ticket parkingCarBySelf(String parkingId)
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
    return parkingRepository.findById(parkingId)
        .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Retryable(backoff = @Backoff(multiplier = 1.5))
  public void takeCar(String ticketId)
      throws IllegalTicketException, NotFoundResourceException {
    Ticket ticket = getCurrentTicket(ticketId);
    if (ticket.checkTicket()) {
      takeCarFromPark(ticket);
    } else {
      throw new IllegalTicketException(ExceptionMessage.ILLEGAL_TICKET);
    }
  }

  public List<Parking> getAllParking(String userId)
      throws NotParkingHelperException, NotFoundResourceException {
    return Optional.of(getParkingHelper(userId))
        .map(ParkingHelper::isAllow)
        .filter(isAllow -> isAllow == true)
        .map(p -> parkingRepository.findAll())
        .orElseThrow(() -> new NotParkingHelperException(ExceptionMessage.NOT_PARKING_HELPER));
  }

  private ParkingHelper getParkingHelper(String userId) throws NotFoundResourceException {
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

  private Ticket getCurrentTicket(String ticketId) throws NotFoundResourceException {
    return ticketRepository.findById(ticketId)
        .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_TICKET));
  }

  private void takeCarFromPark(Ticket ticket) throws NotFoundResourceException {
    Parking parking = parkingRepository.findById(ticket.getParkingLotId())
        .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_PARKING));
    parking.upSize();
    parkingRepository.save(parking);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  @Retryable(backoff = @Backoff(multiplier = 1.5))
  public Ticket parkingCarByHelper(String userId, boolean byOrderForManager) {
    ParkingHelper parkingHelper = getParkingHelper(userId);
    List<Parking> parkings = getAllParking(userId);
    Parking parking = parkingHelper.parking(parkings, byOrderForManager);
    return parkingCarInPark(parking);
  }

  public User addUser(String name, String role) {
    User user = User.createUser(name, role);
    return userRepository.save(user);
  }

  public User removeUser(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_USER));
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
