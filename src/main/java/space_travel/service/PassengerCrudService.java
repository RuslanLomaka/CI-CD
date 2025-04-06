package space_travel.service;

import space_travel.entity.Passenger;

import java.util.List;

public interface PassengerCrudService {

    void save(Passenger passenger);

    void update(Passenger passenger);

    void deleteById(Long id);

    Passenger findById(Long id);

    List<Passenger> findAll();
}
