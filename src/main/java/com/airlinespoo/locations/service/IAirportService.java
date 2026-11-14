package com.airlinespoo.locations.service;

import com.airlinespoo.locations.model.Airport;

import java.util.List;
import java.util.UUID;

/**
 * Operaciones de negocio para gestionar aeropuertos.
 */
public interface IAirportService {

    Airport register(String name, int cityId);

    Airport findById(UUID id);

    List<Airport> findByCity(int cityId);

    Airport rename(UUID id, String name);

    void delete(UUID id);
}
