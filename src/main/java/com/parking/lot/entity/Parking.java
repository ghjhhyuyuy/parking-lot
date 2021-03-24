package com.parking.lot.entity;

import static com.parking.lot.util.StorageUtil.generateStorageList;

import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.IllegalSizeException;
import com.parking.lot.exception.OverSizeException;
import com.parking.lot.util.GenerateId;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Parking {

  @Id
  private String id;
  private int emptyNumber;
  @OneToMany(mappedBy = "parkingId", cascade = {CascadeType.MERGE})
  @NotFound(action = NotFoundAction.IGNORE)
  private List<Storage> storageList;

  public void increaseNum() {
    emptyNumber++;
  }

  public void reduceNum() {
    emptyNumber--;
  }
}
