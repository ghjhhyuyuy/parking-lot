package com.parking.lot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Builder
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {
    @Id
    private String id;
}
