package com.parking.lot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ticket {

  @Id
  private String id;
  private String entryTime;
  private String parkingLotId;
  private String storageId;

}
