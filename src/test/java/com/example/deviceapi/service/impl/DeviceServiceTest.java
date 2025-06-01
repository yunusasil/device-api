package com.example.deviceapi.service.impl;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.exception.DeviceNotFoundException;
import com.example.deviceapi.exception.DeviceValidationException;
import com.example.deviceapi.mapper.DeviceMapper;
import com.example.deviceapi.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @InjectMocks
    private DeviceServiceImpl deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceMapper deviceMapper;

    private Device testDevice;

    @BeforeEach
    void setUp() {
        testDevice = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();
    }

    @Test
    void createDevice_ShouldCreateAndReturnDevice() {
        DeviceCreateDto createDto = new DeviceCreateDto("iPhone 16", "Apple");
        when(deviceRepository.save(any(Device.class))).thenReturn(testDevice);
        when(deviceMapper.createDtoToDevice(any(DeviceCreateDto.class))).thenReturn(testDevice);

        Device result = deviceService.createDevice(createDto);

        assertNotNull(result);
        assertEquals("iPhone 16", result.getName());
        assertEquals("Apple", result.getBrand());
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    void getDeviceById_WhenDeviceExists_ShouldReturnDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        Device result = deviceService.getDeviceById(1L);

        assertEquals(testDevice, result);
    }

    @Test
    void getDeviceById_WhenDeviceNotExists_ShouldThrowException() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> deviceService.getDeviceById(1L));
    }

    @Test
    void updateDevice_ShouldUpdate() {
        DeviceUpdateDto updateDto = new DeviceUpdateDto();
        updateDto.setName("New Name");
        testDevice.setName(updateDto.getName());

        when(deviceRepository.findById(any())).thenReturn(Optional.of(testDevice));
        doNothing().when(deviceMapper).updateDeviceFromDTO(any(), any());
        when(deviceRepository.save(any())).thenReturn(testDevice);

        Device result = deviceService.updateDevice(1L, updateDto);

        assertEquals("New Name", result.getName());
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceMapper, times(1)).updateDeviceFromDTO(updateDto, testDevice);
        verify(deviceRepository, times(1)).save(testDevice);
    }

    @Test
    void updateDevice_WhenInUse_ShouldNotAllowNameBrandUpdate() {
        testDevice.setState(DeviceState.IN_USE);
        DeviceUpdateDto updateDto = new DeviceUpdateDto();
        updateDto.setName("New Name");

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        assertThrows(DeviceValidationException.class,
                () -> deviceService.updateDevice(1L, updateDto));
    }

    @Test
    void deleteDevice_WhenInUse_ShouldThrowException() {
        testDevice.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        assertThrows(DeviceValidationException.class,
                () -> deviceService.deleteDevice(1L));
    }

    @Test
    void deleteDevice_ShouldDelete() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(testDevice));

        deviceService.deleteDevice(1L);
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).delete(testDevice);
    }

    @Test
    void getAllDevices_ShouldReturnDevices() {
        when(deviceRepository.findAll()).thenReturn(List.of(testDevice));

        List<Device> result = deviceService.getAllDevices();

        assertEquals(testDevice, result.getFirst());
        assertEquals(1, result.size());
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void getDevicesByBrand_ShouldReturnDevices() {
        when(deviceRepository.findByBrandIgnoreCase(any())).thenReturn(List.of(testDevice));

        List<Device> result = deviceService.getDevicesByBrand("Apple");

        assertEquals(testDevice, result.getFirst());
        assertEquals(1, result.size());
        verify(deviceRepository, times(1)).findByBrandIgnoreCase("Apple");
    }

    @Test
    void getDevicesByState_ShouldReturnDevices() {
        when(deviceRepository.findByState(any())).thenReturn(List.of(testDevice));

        List<Device> result = deviceService.getDevicesByState(DeviceState.AVAILABLE);

        assertEquals(testDevice, result.getFirst());
        assertEquals(1, result.size());
        verify(deviceRepository, times(1)).findByState(DeviceState.AVAILABLE);
    }


}