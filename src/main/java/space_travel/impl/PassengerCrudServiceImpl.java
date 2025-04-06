package space_travel.impl;

import space_travel.entity.Passenger;
import space_travel.service.PassengerCrudService;

import java.util.List;

public class PassengerCrudServiceImpl implements PassengerCrudService {

    @Override
    public void save(Passenger passenger) throws IllegalArgumentException {
        if (passenger.getPassengerId() != null && findById(passenger.getPassengerId()) != null) {
            throw new IllegalArgumentException("Passenger with ID '" + passenger.getPassengerId() + "' already exists. Use update() instead.");
        }
        HibernateHelper.saveEntity(passenger); // делегуємо збереження утиліті
    }

    @Override
    public void update(Passenger passenger) {
        if (passenger.getPassengerId() == null) {
            throw new IllegalArgumentException("Passenger ID must not be null for update.");
        }
        HibernateHelper.updateEntity(passenger); // делегуємо утиліті
    }

    @Override
    public void deleteById(Long id) {
        HibernateHelper.deleteEntityById(Passenger.class, id); // делегуємо утиліті
    }

    @Override
    public Passenger findById(Long id) {
        return HibernateHelper.findEntityById(Passenger.class, id); // делегуємо утиліті
    }

    @Override
    public List<Passenger> findAll() {
        return HibernateHelper.findAllEntities(Passenger.class); // делегуємо утиліті
    }
}
