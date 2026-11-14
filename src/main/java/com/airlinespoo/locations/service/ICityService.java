package com.airlinespoo.locations.service;

import com.airlinespoo.locations.model.City;

/**
 * Operaciones de negocio para gestionar ciudades.
 */
public interface ICityService {

    City register(String name);

    City rename(int id, String name);

    void delete(int id);
}
