package com.parking.lot.entity;

import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Role {

  @Id
  String id;
  String role;
}
