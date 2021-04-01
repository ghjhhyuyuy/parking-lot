package com.parking.lot.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddStaffRequest extends Request{
    String name;
    String role;
}
