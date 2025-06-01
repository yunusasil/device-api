package com.example.deviceapi.dto;

import com.example.deviceapi.entity.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUpdateDto {

    private String name;
    private String brand;
    private DeviceState state;
}
