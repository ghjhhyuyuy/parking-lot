package com.parking.lot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
  String id;
  String timeoutDate;
  String parkingLotId;
}
