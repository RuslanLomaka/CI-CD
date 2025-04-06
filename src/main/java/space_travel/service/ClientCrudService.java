package space_travel.service;

import space_travel.entity.Client;

import java.util.List;

public interface ClientCrudService {

    void save(Client client);

    void update(Client client);

    void deleteById(Long id);

    Client findById(Long id);

    List<Client> findAll();
}
