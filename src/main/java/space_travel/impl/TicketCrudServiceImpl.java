package space_travel.impl;

import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.service.ClientCrudService;
import space_travel.service.PlanetCrudService;
import space_travel.service.TicketCrudService;

import java.util.List;

public class TicketCrudServiceImpl implements TicketCrudService {


    @SuppressWarnings("java:S1068") // Поле ще не використовується, але буде потрібно для перевірки клієнта в методі save().
    private final ClientCrudService clientCrudService;

    @SuppressWarnings("java:S1068") // Аналогічно, це поле буде використано для перевірки планети у збереженні квитка.
    private final PlanetCrudService planetCrudService;

    public TicketCrudServiceImpl(ClientCrudService clientService, PlanetCrudService planetService) {
        this.clientCrudService = clientService;
        this.planetCrudService = planetService;
    }

    @Override
    public void save(Ticket ticket) throws IllegalArgumentException {
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket must not be null.");
        }

        // Check if client is null or not found
        if (ticket.getClient() == null) {
            throw new IllegalArgumentException("Client must not be null.");
        }
        Client client = clientCrudService.findById(ticket.getClient().getId());
        if (client == null) {
            throw new IllegalArgumentException("Client not found in the database.");
        }

        // Check if source planet is null or not found
        if (ticket.getFromPlanet() == null) {
            throw new IllegalArgumentException("Source planet must not be null.");
        }
        Planet from = planetCrudService.findById(ticket.getFromPlanet().getId());
        if (from == null) {
            throw new IllegalArgumentException("Source planet not found in the database.");
        }

        if (ticket.getToPlanet() == null) {
            throw new IllegalArgumentException("Destination planet must not be null.");
        }
        Planet to = planetCrudService.findById(ticket.getToPlanet().getId());
        if (to == null) {
            throw new IllegalArgumentException("Destination planet not found in the database.");
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
