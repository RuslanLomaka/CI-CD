package space_travel.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space_travel.entity.Client;
import space_travel.exception.DataUpdateException;
import space_travel.service.ClientCrudService;

import java.util.List;

public class ClientCrudServiceImpl implements ClientCrudService {
    private static final Logger logger = LoggerFactory.getLogger(ClientCrudServiceImpl.class);

    @Override
    public void save(Client client) throws IllegalArgumentException {
        if (client.getId() != null && findById(client.getId()) != null) {
            logger.warn("Client already exists with ID: {}", client.getId());//для дебагінгу
            throw new DataUpdateException("Unable to save client."); // generic для API юзерів (з міркувань безпеки)
        }
        HibernateHelper.saveEntity(client); // делегуємо збереження утиліті
    }

    @Override
    public void update(Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Passenger ID must not be null for update.");
        }
        HibernateHelper.updateEntity(client); // делегуємо утиліті
    }

    @Override
    public void deleteById(Long id) {
        HibernateHelper.deleteEntityById(Client.class, id); // делегуємо утиліті
    }

    @Override
    public Client findById(Long id) {
        return HibernateHelper.findEntityById(Client.class, id); // делегуємо утиліті
    }

    @Override
    public List<Client> findAll() {
        return HibernateHelper.findAllEntities(Client.class); // делегуємо утиліті
    }
}

