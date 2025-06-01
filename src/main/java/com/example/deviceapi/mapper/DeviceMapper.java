package com.example.deviceapi.mapper;

import com.example.deviceapi.dto.DeviceCreateDto;
import com.example.deviceapi.dto.DeviceUpdateDto;
import com.example.deviceapi.entity.Device;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
@Component
public interface DeviceMapper {

    @Mapping(target = "state", constant = "AVAILABLE")
    Device createDtoToDevice(DeviceCreateDto createDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceFromDTO(DeviceUpdateDto updateDto, @MappingTarget Device device);
}
