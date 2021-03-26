package com.parking.lot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Basement {

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
