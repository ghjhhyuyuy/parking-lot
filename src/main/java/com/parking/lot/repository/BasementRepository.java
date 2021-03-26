package com.parking.lot.repository;

import com.parking.lot.entity.Basement;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasementRepository extends CrudRepository<Basement, String> {

  @Override
  List<Basement> findAll();
}
