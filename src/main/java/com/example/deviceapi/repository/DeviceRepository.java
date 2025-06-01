package com.example.deviceapi.repository;

import com.example.deviceapi.entity.Device;
import com.example.deviceapi.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBrandIgnoreCase(String brand);

    List<Device> findByState(DeviceState state);

    @Query("SELECT d FROM Device d WHERE LOWER(d.brand) = LOWER(:brand) AND d.state = :state")
    List<Device> findByBrandAndState(@Param("brand") String brand, @Param("state") DeviceState state);
}
