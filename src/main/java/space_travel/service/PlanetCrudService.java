package space_travel.service;

import space_travel.entity.Planet;

import java.util.List;

public interface PlanetCrudService {
    void save(Planet planet);

    void update(Planet planet);

    void deleteById(String id);

    Planet findById(String id);  // Change from Long to String

    List<Planet> findAll();
}