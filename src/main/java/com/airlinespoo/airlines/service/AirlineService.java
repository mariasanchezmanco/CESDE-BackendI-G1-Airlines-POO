package com.airlinespoo.airlines.service;

import com.airlinespoo.airlines.model.Airline;
import com.airlinespoo.airlines.repository.AirlineRepository;
import com.airlinespoo.core.exception.BusinessRuleException;
import com.airlinespoo.core.exception.NotFoundException;
import com.airlinespoo.core.util.IdGenerator;

import java.util.List;

public class AirlineService {

    private final AirlineRepository airlineRepository;
    private final IdGenerator idGenerator;

    public AirlineService(AirlineRepository airlineRepository, IdGenerator idGenerator) {
        this.airlineRepository = airlineRepository;
        this.idGenerator = idGenerator;
    }

    /**
     * Registra una nueva aerolínea validando que el nombre no exista previamente.
     */
    public Airline createAirline(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la aerolínea no puede estar vacío");
        }

        String normalizedName = name.trim();

        // Validar unicidad por nombre usando BusinessRuleException o la regla correspondiente
        if (airlineRepository.findByName(normalizedName).isPresent()) {
            throw new BusinessRuleException("Ya existe una aerolínea con el nombre: " + normalizedName);
        }

        // Obtener el siguiente id del generador (el método del kit base es next())
        Integer id = idGenerator.next();

        Airline airline = new Airline(id, normalizedName);
        return airlineRepository.create(airline);
    }

    /**
     * Obtiene una aerolínea por su id, lanzando la NotFoundException del kit base.
     */
    public Airline getAirline(Integer id) {
        return airlineRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("aerolínea", id));
    }

    /**
     * Lista todas las aerolíneas ordenadas por nombre alfabéticamente.
     */
    public List<Airline> listAirlines() {
        return airlineRepository.findAll().stream()
            .sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()))
            .toList();
    }

    /**
     * Renombra una aerolínea existente aplicando validación de unicidad.
     */
    public Airline renameAirline(Integer id, String newName) {
        Airline existing = getAirline(id);

        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nuevo nombre no puede estar vacío");
        }

        String normalizedName = newName.trim();

        // Verificar si el nuevo nombre ya lo tiene otra aerolínea distinta
        airlineRepository.findByName(normalizedName).ifPresent(other -> {
            if (!other.getId().equals(id)) {
                throw new BusinessRuleException("Ya existe otra aerolínea con el nombre: " + normalizedName);
            }
        });

        existing.setName(normalizedName);
        airlineRepository.update(existing);
        return existing;
    }

    /**
     * Elimina una aerolínea por su id.
     */
    public void deleteAirline(Integer id) {
        if (!airlineRepository.existsById(id)) {
            throw new NotFoundException("aerolínea", id);
        }
        airlineRepository.deleteById(id);
    }

    /**
     * Busca aerolíneas por coincidencia parcial en el nombre.
     */
    public List<Airline> searchAirlinesByName(String partialName) {
        return airlineRepository.searchByName(partialName);
    }

    /**
     * Retorna la cantidad total de aerolíneas registradas.
     */
    public long countAirlines() {
        return airlineRepository.count();
    }
}
