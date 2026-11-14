package com.airlinespoo.locations.service;

import com.airlinespoo.locations.model.Airport;

import java.util.List;
import java.util.UUID;

/**
 * Operaciones de negocio para gestionar aeropuertos.
 */
public interface IAirportService {

    /**
     * Registra un aeropuerto en una ciudad existente. El nombre no puede repetirse
     * dentro de esa ciudad.
     *
     * @param name nombre del aeropuerto
     * @param cityId identificador de la ciudad
     * @return el aeropuerto registrado
     * @throws com.airlinespoo.core.exception.NotFoundException si la ciudad no existe
     */
    Airport register(String name, int cityId);

    /**
     * Busca un aeropuerto por su identificador.
     *
     * @param id identificador del aeropuerto
     * @return el aeropuerto encontrado
     * @throws com.airlinespoo.core.exception.NotFoundException si no existe
     */
    Airport findById(UUID id);

    /**
     * Lista los aeropuertos de una ciudad existente, ordenados por nombre.
     *
     * @param cityId identificador de la ciudad
     * @return los aeropuertos, o una lista vacía si la ciudad no tiene ninguno
     * @throws com.airlinespoo.core.exception.NotFoundException si la ciudad no existe
     */
    List<Airport> findByCity(int cityId);

    /**
     * Cambia el nombre de un aeropuerto, respetando la unicidad en su ciudad.
     *
     * @param id identificador del aeropuerto
     * @param name nuevo nombre
     * @return el aeropuerto actualizado
     * @throws com.airlinespoo.core.exception.NotFoundException si el aeropuerto no existe
     */
    Airport rename(UUID id, String name);

    /**
     * Elimina un aeropuerto existente. La restricción por vuelos se integrará después.
     *
     * @param id identificador del aeropuerto
     * @throws com.airlinespoo.core.exception.NotFoundException si el aeropuerto no existe
     */
    void delete(UUID id);
}
