package com.carrental.service;

import com.carrental.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleService {
    List<Vehicle> listAll();
    Optional<Vehicle> getById(Long id);
    Vehicle save(Vehicle vehicle);
    void update(Vehicle vehicle);
    void delete(Long id);
}