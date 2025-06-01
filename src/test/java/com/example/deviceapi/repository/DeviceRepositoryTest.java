package com.example.deviceapi.repository;

import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DeviceRepositoryTest {

    @Autowired
    private DeviceRepository deviceRepository;

    private Device device1;
    private Device device2;
    private Device device3;

    @BeforeEach
    void setUp() {
        device1 = Device.builder()
                .name("iPhone 16")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        device2 = Device.builder()
                .name("Samsung Galaxy S25")
                .brand("Samsun")
                .state(DeviceState.IN_USE)
                .build();

        device3 = Device.builder()
                .name("Macbook Pro ")
                .brand("Apple")
                .state(DeviceState.INACTIVE)
                .build();

        deviceRepository.save(device1);
        deviceRepository.save(device2);
        deviceRepository.save(device3);
    }

    @Test
    void findByBrandIgnoreCase_ShouldReturnDevicesWithMatchingBrand() {
        List<Device> appleDevices = deviceRepository.findByBrandIgnoreCase("apple");

        assertEquals(2, appleDevices.size());
        assertTrue(appleDevices.stream().allMatch(d -> d.getBrand().equalsIgnoreCase("Apple")));
    }

    @Test
    void findByState_ShouldReturnDevicesWithMatchingState() {
        List<Device> availableDevices = deviceRepository.findByState(DeviceState.AVAILABLE);

        assertEquals(1, availableDevices.size());
        assertEquals(device1.getName(), availableDevices.getFirst().getName());
    }

    @Test
    void findByBrandAndState_ShouldReturnDevicesMatchingBothCriteria() {
        List<Device> appleInactiveDevices = deviceRepository.findByBrandAndState("Apple", DeviceState.INACTIVE);

        assertEquals(1, appleInactiveDevices.size());
        assertEquals(device3.getName(), appleInactiveDevices.getFirst().getName());
    }

}