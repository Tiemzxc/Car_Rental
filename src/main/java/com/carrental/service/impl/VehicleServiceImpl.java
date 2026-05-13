package com.carrental.service.impl;

import com.carrental.model.Vehicle;
import com.carrental.repository.VehicleRepository;
import com.carrental.service.VehicleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> listAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> getById(Long id) {
        return vehicleRepository.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        Long id = vehicleRepository.save(vehicle);
        vehicle.setId(id);
        return vehicle;
    }

    @Override
    public void update(Vehicle vehicle) {
        vehicleRepository.update(vehicle);
    }

    @Override
    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }
}