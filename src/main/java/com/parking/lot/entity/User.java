package com.parking.lot.entity;

import com.parking.lot.util.GenerateID;
import com.parking.lot.util.TimeUtil;
import java.time.format.DateTimeFormatter;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

  private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  @Id
  private String id;
  private String name;
  private String role;
  private String createDate;
  private String removeDate;

  public static User createUser(String name, String role) {
    return new User(GenerateID.getUUID(), name, role, dateTimeFormatter.format(TimeUtil.getTime(0)),
        null);
  }

  public static User removeUser(User user) {
    return User.builder().id(user.getId()).name(user.getName()).createDate(user.getCreateDate())
        .role(
            user.getRole()).removeDate(dateTimeFormatter.format(TimeUtil.getTime(0))).build();
  }
}
