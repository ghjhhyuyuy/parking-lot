package com.parking.lot.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {
  @Id
  private String id;
  private String color;
  private String brand;
}
