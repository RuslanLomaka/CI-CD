package space_travel.service;

import space_travel.entity.Ticket;

import java.util.List;

public interface TicketCrudService {

    void save(Ticket passenger);

    void update(Ticket passenger);

    void deleteById(Long id);

    Ticket findById(Long id);

    List<Ticket> findAll();
}
