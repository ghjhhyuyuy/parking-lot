package com.parking.lot.service;

import com.parking.lot.entity.Staff;
import com.parking.lot.enums.ExceptionMessage;
import com.parking.lot.exception.NotFoundResourceException;
import com.parking.lot.repository.StaffRepository;
import com.parking.lot.util.GenerateId;
import com.parking.lot.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class StaffService {
    private final StaffRepository staffRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public Staff addStaff(String name, String role) {
        Staff staff = new Staff(GenerateId.getUUID(), name, role, dateTimeFormatter.format(TimeUtil.getTime(0)),
                null);
        return staffRepository.save(staff);
    }

    public Staff removeStaff(String userId) {
        Staff staff = staffRepository.findById(userId)
                .orElseThrow(() -> new NotFoundResourceException(ExceptionMessage.NOT_FOUND_STAFF));
        staff = remove(staff);
        return staffRepository.save(staff);
    }

    private Staff remove(Staff staff) {
        return Staff.builder().id(staff.getId()).name(staff.getName()).createDate(staff.getCreateDate())
                .role(
                        staff.getRole()).removeDate(dateTimeFormatter.format(TimeUtil.getTime(0))).build();
    }
}
