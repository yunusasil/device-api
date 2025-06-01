package com.example.deviceapi.service.impl;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.exception.DeviceNotFoundException;
import com.example.deviceapi.exception.DeviceValidationException;
import com.example.deviceapi.mapper.DeviceMapper;
import com.example.deviceapi.repository.DeviceRepository;
import com.example.deviceapi.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;

    @Override
    public Device createDevice(DeviceCreateDto createDto) {
        Device device = deviceMapper.createDtoToDevice(createDto);
        return deviceRepository.save(device);
    }

    @Override
    public Device updateDevice(Long id, DeviceUpdateDto updateDto) {
        Device device = getDeviceById(id);

        if (device.getState() == DeviceState.IN_USE) {
            throw new DeviceValidationException(
                    "Cannot update name or brand of a device that is in use");
        }
        deviceMapper.updateDeviceFromDTO(updateDto, device);
        return deviceRepository.save(device);
    }

    @Transactional(readOnly = true)
    @Override
    public Device getDeviceById(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Device> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrandIgnoreCase(brand);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Device> getDevicesByState(DeviceState state) {
        return deviceRepository.findByState(state);
    }

    @Override
    public void deleteDevice(Long id) {
        Device device = getDeviceById(id);

        if (device.getState() == DeviceState.IN_USE) {
            throw new DeviceValidationException("Cannot delete a device that is in use");
        }

        deviceRepository.delete(device);
    }
}
