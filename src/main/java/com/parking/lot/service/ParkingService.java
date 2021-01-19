package com.parking.lot.service;

import com.parking.lot.entity.Parking;
import com.parking.lot.entity.Ticket;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.repository.ParkingRepository;
import com.parking.lot.repository.TicketRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {
  final ParkingRepository parkingRepository;
  final TicketRepository ticketRepository;
  final int ticket_time = 3600000;

  public ParkingService(ParkingRepository parkingRepository,TicketRepository ticketRepository){
    this.parkingRepository = parkingRepository;
    this.ticketRepository = ticketRepository;
  }

  public Ticket parkingCar(String parkingId) throws OverSizeException, NotFoundException {
    Optional<Parking> optionalParking = parkingRepository.findById(parkingId);
    if(optionalParking.isPresent()){
      Parking parking = optionalParking.get();
      if(parking.getSize() > 0) {
        Ticket ticket = getTicket();
        ticketRepository.save(ticket);
        parking.reduceSize();
        parkingRepository.save(parking);
        return ticket;
      }else {
        throw new OverSizeException();
      }
    }
    throw new NotFoundException("not found parking");
  }

  private Ticket getTicket() {
    Date date = new Date(System.currentTimeMillis() + ticket_time);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return Ticket.builder()
        .id(UUID.randomUUID().toString())
        .parkingLotId(UUID.randomUUID().toString())
        .timeoutDate(simpleDateFormat.format(date))
        .build();
  }
}
