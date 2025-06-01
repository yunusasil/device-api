package com.example.deviceapi.service;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DeviceService {
    Device createDevice(DeviceCreateDto createDto);

    Device updateDevice(Long id, DeviceUpdateDto updateDto);

    @Transactional(readOnly = true)
    Device getDeviceById(Long id);

    @Transactional(readOnly = true)
    List<Device> getAllDevices();

    @Transactional(readOnly = true)
    List<Device> getDevicesByBrand(String brand);

    @Transactional(readOnly = true)
    List<Device> getDevicesByState(DeviceState state);

    void deleteDevice(Long id);
}
