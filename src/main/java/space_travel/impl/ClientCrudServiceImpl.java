package space_travel.impl;

import space_travel.entity.Client;
import space_travel.service.ClientCrudService;

import java.util.List;

public class ClientCrudServiceImpl implements ClientCrudService {

    @Override
    public void save(Client client) throws IllegalArgumentException {
        if (client.getId() != null && findById(client.getId()) != null) {
            throw new IllegalArgumentException("Passenger with ID '" + client.getId() + "' already exists. Use update() instead.");
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
