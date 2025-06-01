package com.example.deviceapi.controller;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.service.DeviceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeviceController.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeviceService deviceService;

    @Test
    void createDevice_ShouldReturnCreatedDevice() throws Exception {
        DeviceCreateDto createDto = new DeviceCreateDto("iPhone 16", "Apple");
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();

        when(deviceService.createDevice(any(DeviceCreateDto.class))).thenReturn(device);

        mockMvc.perform(post("/api/devices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 16"))
                .andExpect(jsonPath("$.brand").value("Apple"));

        verify(deviceService, times(1)).createDevice(createDto);
    }

    @Test
    void getDevice_ShouldReturnDevice() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();

        when(deviceService.getDeviceById(1L)).thenReturn(device);

        mockMvc.perform(get("/api/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 16"))
                .andExpect(jsonPath("$.brand").value("Apple"));
        verify(deviceService, times(1)).getDeviceById(1L);
    }

    @Test
    void getAllDevices_ShouldReturnDeviceList() throws Exception {
        Device device1 = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();
        Device device2 = Device.builder()
                .id(2L)
                .name("Samsung Galaxy S25")
                .brand("Samsung")
                .build();

        List<Device> devices = Arrays.asList(device1, device2);
        when(deviceService.getAllDevices()).thenReturn(devices);

        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
        verify(deviceService, times(1)).getAllDevices();
    }

    @Test
    void getAllDevicesByBrand_ShouldReturnDeviceList() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();

        when(deviceService.getDevicesByBrand(any())).thenReturn(List.of(device));

        mockMvc.perform(get("/api/devices")
                        .param("brand", "Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(deviceService, times(1)).getDevicesByBrand("Apple");
    }

    @Test
    void getAllDevicesByState_ShouldReturnDeviceList() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        when(deviceService.getDevicesByState(any())).thenReturn(List.of(device));

        mockMvc.perform(get("/api/devices")
                        .param("state", "AVAILABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(deviceService, times(1)).getDevicesByState(DeviceState.AVAILABLE);
    }

    @Test
    void getAllDevicesByStateAndBrand_ShouldReturnDeviceList() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .state(DeviceState.AVAILABLE)
                .build();

        when(deviceService.getDevicesByBrand(any())).thenReturn(List.of(device));

        mockMvc.perform(get("/api/devices")
                        .param("state", "AVAILABLE")
                        .param("brand", "Apple"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(deviceService, times(1)).getDevicesByBrand("Apple");
    }

    @Test
    void updateDevice_ShouldReturnUpdatedDevice() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();
        DeviceUpdateDto updateDto = new DeviceUpdateDto("Iphone 16", "Apple", DeviceState.AVAILABLE);

        when(deviceService.updateDevice(any(), any())).thenReturn(device);
        mockMvc.perform(put("/api/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 16"))
                .andExpect(jsonPath("$.brand").value("Apple"));
        verify(deviceService, times(1)).updateDevice(1L, updateDto);
    }

    @Test
    void partiallyUpdateDevice_ShouldReturnUpdatedDevice() throws Exception {
        Device device = Device.builder()
                .id(1L)
                .name("iPhone 16")
                .brand("Apple")
                .build();
        DeviceUpdateDto updateDto = new DeviceUpdateDto("Iphone 16", "Apple", DeviceState.AVAILABLE);

        when(deviceService.updateDevice(any(), any())).thenReturn(device);
        mockMvc.perform(patch("/api/devices/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("iPhone 16"))
                .andExpect(jsonPath("$.brand").value("Apple"));
        verify(deviceService, times(1)).updateDevice(1L, updateDto);
    }

    @Test
    void deleteDevice_ShouldReturnUpdatedDevice() throws Exception {
        doNothing().when(deviceService).deleteDevice(anyLong());
        mockMvc.perform(delete("/api/devices/1"))
                .andExpect(status().isNoContent());
        verify(deviceService, times(1)).deleteDevice(1L);
    }

}