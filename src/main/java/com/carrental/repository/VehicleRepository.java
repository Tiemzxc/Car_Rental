package com.carrental.repository;

import com.carrental.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Long save(Vehicle vehicle);
    List<Vehicle> findAll();
    Optional<Vehicle> findById(Long id);
    void update(Vehicle vehicle);
    void deleteById(Long id);
}