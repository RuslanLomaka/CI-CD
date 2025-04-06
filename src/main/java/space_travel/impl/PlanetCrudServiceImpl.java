package space_travel.impl;

import space_travel.entity.Planet;
import space_travel.service.PlanetCrudService;

import java.util.List;

public class PlanetCrudServiceImpl implements PlanetCrudService {

    @Override
    public void save(Planet planet) throws IllegalArgumentException {
        if (planet.getId() == null) {
            throw new IllegalArgumentException("Planet ID must not be null for save().");
        }

        if (HibernateHelper.getEntityIfExists(Planet.class, planet.getId()) != null) {
            throw new IllegalArgumentException("Planet with ID '" + planet.getId() + "' already exists. Use update() instead.");
        }

        HibernateHelper.saveEntity(planet);
    }

    @Override
    public void update(Planet planet) {
        if (planet.getId() == null) {
            throw new IllegalArgumentException("Planet ID must not be null for update.");
        }
        HibernateHelper.updateEntity(planet); // делегуємо утиліті
    }

    @Override
    public void deleteById(String id) {
        HibernateHelper.deleteEntityById(Planet.class, id); // делегуємо утиліті
    }

    @Override
    public Planet findById(String id) {
        return HibernateHelper.findEntityById(Planet.class, id); // делегуємо утиліті
    }

    @Override
    public List<Planet> findAll() {
        return HibernateHelper.findAllEntities(Planet.class); // делегуємо утиліті
    }

}