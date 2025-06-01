package com.example.deviceapi.controller;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import com.example.deviceapi.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device Management", description = "Operations for managing devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    @Operation(summary = "Create a new device")
    @ApiResponse(responseCode = "201", description = "Device created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    public ResponseEntity<Device> createDevice(@Valid @RequestBody DeviceCreateDto createDto) {
        Device device = deviceService.createDevice(createDto);
        return new ResponseEntity<>(device, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a device by ID")
    @ApiResponse(responseCode = "200", description = "Device found")
    @ApiResponse(responseCode = "404", description = "Device not found")
    public ResponseEntity<Device> getDevice(
            @Parameter(description = "Device ID") @PathVariable Long id) {
        Device device = deviceService.getDeviceById(id);
        return ResponseEntity.ok(device);
    }

    @GetMapping
    @Operation(summary = "Get all devices or filter by brand/state")
    @ApiResponse(responseCode = "200", description = "Devices retrieved successfully")
    public ResponseEntity<List<Device>> getDevices(
            @Parameter(description = "Filter by brand") @RequestParam(required = false) String brand,
            @Parameter(description = "Filter by state") @RequestParam(required = false) String state) {

        List<Device> devices;

        if (brand != null && state != null) {
            DeviceState deviceState = DeviceState.fromString(state);
            devices = deviceService.getDevicesByBrand(brand)
                    .stream()
                    .filter(d -> d.getState() == deviceState)
                    .toList();
        } else if (brand != null) {
            devices = deviceService.getDevicesByBrand(brand);
        } else if (state != null) {
            DeviceState deviceState = DeviceState.fromString(state);
            devices = deviceService.getDevicesByState(deviceState);
        } else {
            devices = deviceService.getAllDevices();
        }

        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Fully update a device")
    @ApiResponse(responseCode = "200", description = "Device updated successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Invalid update operation")
    public ResponseEntity<Device> updateDevice(
            @Parameter(description = "Device ID") @PathVariable Long id,
            @RequestBody DeviceUpdateDto updateDto) {
        Device device = deviceService.updateDevice(id, updateDto);
        return ResponseEntity.ok(device);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a device")
    @ApiResponse(responseCode = "200", description = "Device updated successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Invalid update operation")
    public ResponseEntity<Device> partialUpdateDevice(
            @Parameter(description = "Device ID") @PathVariable Long id,
            @RequestBody DeviceUpdateDto updateDto) {
        Device device = deviceService.updateDevice(id, updateDto);
        return ResponseEntity.ok(device);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a device")
    @ApiResponse(responseCode = "204", description = "Device deleted successfully")
    @ApiResponse(responseCode = "404", description = "Device not found")
    @ApiResponse(responseCode = "400", description = "Cannot delete device in use")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "Device ID") @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
