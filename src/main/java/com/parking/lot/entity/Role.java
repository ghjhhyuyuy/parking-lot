package com.parking.lot.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
public class Role {

  @Id
  private final String id;
  private final String role;
}
