package com.parking.lot.entity;

import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Basement {

    @Id
    private String id;
    private int emptyNumber;
    @OneToMany(mappedBy = "basementId", cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Storage> storageList;

    public void increaseNum() {
        emptyNumber++;
    }

    public void reduceNum() {
        emptyNumber--;
    }
}
