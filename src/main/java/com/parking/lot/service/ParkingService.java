package com.parking.lot.service;

import static com.parking.lot.entity.Ticket.getTicket;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
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
    Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
    if (optionalParking.isPresent()) {
      Parking parking = optionalParking.get();
      parking.checkSize();
      Ticket ticket = getTicket(parkingId);
      ticketRepository.save(ticket);
      parking.reduceSize();
      parkingRepository.save(parking);
      return ticket;
    }
    throw new NotFoundException("not found parking");
  }

  public void takeCar(String ticketId)
      throws NotFoundException, illegalTicketException, ParseException {
    Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
    if (optionalTicket.isPresent()) {
      Ticket ticket = optionalTicket.get();
      if(ticket.checkTicket()){
        Optional<Parking> optionalParking = parkingRepository.findById(ticket.getParkingLotId());
        optionalParking.ifPresent(Parking::upSize);
      }else {
        throw new illegalTicketException();
      }
    }else {
      throw new NotFoundException("not found ticket");
    }
  }
}
