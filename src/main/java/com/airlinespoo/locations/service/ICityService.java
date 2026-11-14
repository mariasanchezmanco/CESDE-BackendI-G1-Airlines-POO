package com.airlinespoo.locations.service;

import com.airlinespoo.locations.model.City;

/**
 * Operaciones de negocio para gestionar ciudades.
 */
public interface ICityService {

    /**
     * Registra una ciudad con un identificador consecutivo generado por el programa.
     * El nombre no puede repetirse, sin distinguir mayúsculas de minúsculas.
     *
     * @param name nombre de la ciudad
     * @return la ciudad registrada
     */
    City register(String name);

    /**
     * Cambia el nombre de una ciudad existente, respetando la misma regla de unicidad.
     *
     * @param id identificador de la ciudad
     * @param name nuevo nombre
     * @return la ciudad actualizada
     * @throws com.airlinespoo.core.exception.NotFoundException si la ciudad no existe
     */
    City rename(int id, String name);

    /**
     * Elimina una ciudad existente si no tiene aeropuertos asociados.
     *
     * @param id identificador de la ciudad
     * @throws com.airlinespoo.core.exception.NotFoundException si la ciudad no existe
     * @throws com.airlinespoo.core.exception.BusinessRuleException si tiene aeropuertos
     */
    void delete(int id);
}
