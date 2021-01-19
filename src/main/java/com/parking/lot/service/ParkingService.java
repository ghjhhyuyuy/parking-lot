package com.parking.lot.service;

import static com.parking.lot.entity.Ticket.getTicket;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.exception.illegalTicketException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import java.text.ParseException;
import java.util.Optional;
import javassist.NotFoundException;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {

  final ParkingRepository parkingRepository;
  final TicketRepository ticketRepository;

  public ParkingService(ParkingRepository parkingRepository, TicketRepository ticketRepository) {
    this.parkingRepository = parkingRepository;
    this.ticketRepository = ticketRepository;
  }

  @Transactional
  public Ticket parkingCar(String parkingId) throws OverSizeException, NotFoundException {
    Parking parking = getCurrentPart(parkingId);
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

  private Parking getCurrentPart(String parkingId) throws NotFoundException {
    Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
    if (optionalParking.isPresent()) {
      return optionalParking.get();
    }
    throw new NotFoundException(ExceptionMessage.NOT_FOUND_PARKING.getMessage());
  }

  public void takeCar(String ticketId)
      throws NotFoundException, illegalTicketException, ParseException {
    Ticket ticket = getCurrentTicket(ticketId);
    if (ticket.checkTicket()) {
      takeCarFromPark(ticket);
    } else {
      throw new illegalTicketException();
    }
  }

  private Ticket getCurrentTicket(String ticketId) throws NotFoundException {
    Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
    if (optionalTicket.isPresent()) {
      return optionalTicket.get();
    } else {
      throw new NotFoundException(ExceptionMessage.NOT_FOUND_TICKET.getMessage());
    }
  }

  private void takeCarFromPark(Ticket ticket) throws NotFoundException {
    Optional<Parking> optionalParking = parkingRepository.findById(ticket.getParkingLotId());
    if (optionalParking.isPresent()) {
      Parking parking = optionalParking.get();
      parking.upSize();
      parkingRepository.save(parking);
    } else {
      throw new NotFoundException(ExceptionMessage.NOT_FOUND_PARKING.getMessage());
    }
  }
}
