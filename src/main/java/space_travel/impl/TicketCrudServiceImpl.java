package space_travel.impl;

import space_travel.entity.Ticket;
import space_travel.service.TicketCrudService;

import java.util.List;

public class TicketCrudServiceImpl implements TicketCrudService {

    @Override
    public void save(Ticket ticket) throws IllegalArgumentException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket must not be null.");
        }

        if (ticket.getFromPlanet() == null || ticket.getToPlanet() == null) {
            throw new IllegalArgumentException("Both fromPlanet and toPlanet must be specified.");
        }

        HibernateHelper.saveEntity(ticket);
    }

    @Override
    public void update(Ticket ticket) {
        if (ticket.getTicketId() == 0) {
            throw new IllegalArgumentException("Ticket ID must not be zero for update.");
        }
        HibernateHelper.updateEntity(ticket);
    }

    @Override
    public void deleteById(Long id) {
        HibernateHelper.deleteEntityById(Ticket.class, id);
    }

    @Override
    public Ticket findById(Long id) {
        return HibernateHelper.findEntityById(Ticket.class, id);
    }

    @Override
    public List<Ticket> findAll() {
        return HibernateHelper.findAllEntities(Ticket.class);
    }
}
