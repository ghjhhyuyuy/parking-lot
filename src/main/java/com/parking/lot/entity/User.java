package com.parking.lot.entity;

import com.parking.lot.util.GenerateID;
import com.parking.lot.util.TimeUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
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

  @Id
  private String id;
  private String name;
  private String role;
  private String createDate;
  private String removeDate;

  public static User createUser(String name,String role) {
    return new User(GenerateID.getUUID(),name,role,TimeUtil.getTime(0),null);
  }

  public static User removeUser(User user) {
    return User.builder().id(user.getId()).name(user.getName()).createDate(user.getCreateDate()).role(
        user.getRole()).removeDate(TimeUtil.getTime(0)).build();
  }
}
