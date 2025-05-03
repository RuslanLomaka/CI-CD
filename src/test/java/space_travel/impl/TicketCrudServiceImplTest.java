package space_travel.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.exception.DataFetchException;
import space_travel.exception.DataRetrievalException;
import space_travel.service.ClientCrudService;
import space_travel.service.PlanetCrudService;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TicketCrudServiceImplTest {

    private TicketCrudServiceImpl ticketService;
    private ClientCrudService mockClientService;
    private PlanetCrudService mockPlanetService;

    @BeforeEach
    void setUp() {
        mockClientService = Mockito.mock(ClientCrudService.class);
        mockPlanetService = Mockito.mock(PlanetCrudService.class);
        ticketService = new TicketCrudServiceImpl(mockClientService, mockPlanetService);
    }

    @Test
    void save_shouldThrow_whenClientIsNull() {
        Ticket ticket = new Ticket();
        ticket.setClient(null);

        assertThrows(DataFetchException.class, () -> ticketService.save(ticket));
    }

    @Test
    void save_shouldThrow_whenClientNotFoundInDb() {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(1L);
        ticket.setClient(client);

        Planet planet = new Planet();
        planet.setId("PL1");
        ticket.setFromPlanet(planet);
        ticket.setToPlanet(planet);

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(null);

        assertThrows(DataRetrievalException.class, () -> ticketService.save(ticket));
    }

    @Test
    void save_shouldThrow_whenSourcePlanetIsNull() {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(1L);
        ticket.setClient(client);
        ticket.setFromPlanet(null);
        ticket.setToPlanet(new Planet());

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);

        assertThrows(DataFetchException.class, () -> ticketService.save(ticket));
    }

    @Test
    void save_shouldThrow_whenSourcePlanetNotFoundInDb() {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(1L);
        ticket.setClient(client);

        Planet from = new Planet();
        from.setId("PL1");
        ticket.setFromPlanet(from);
        ticket.setToPlanet(from);

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);
        Mockito.when(mockPlanetService.findById(from.getId())).thenReturn(null);

        assertThrows(DataRetrievalException.class, () -> ticketService.save(ticket));
    }

    @Test
    void save_shouldThrow_whenDestinationPlanetIsNull() {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(1L);
        ticket.setClient(client);

        Planet from = new Planet();
        from.setId("PL1");
        ticket.setFromPlanet(from);
        ticket.setToPlanet(null);

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);
        Mockito.when(mockPlanetService.findById(from.getId())).thenReturn(from);

        assertThrows(DataFetchException.class, () -> ticketService.save(ticket));
    }

    @Test
    void save_shouldThrow_whenDestinationPlanetNotFoundInDb() {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(1L);
        ticket.setClient(client);

        Planet from = new Planet();
        from.setId("PL1");
        Planet to = new Planet();
        to.setId("PL2");
        ticket.setFromPlanet(from);
        ticket.setToPlanet(to);

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);
        Mockito.when(mockPlanetService.findById(from.getId())).thenReturn(from);
        Mockito.when(mockPlanetService.findById(to.getId())).thenReturn(null);

        assertThrows(DataRetrievalException.class, () -> ticketService.save(ticket));
    }

}
/*
 🧪 Нотатка про Mockito (для себе на майбутнє)

 Цей клас тестує логіку збереження квитків (TicketCrudServiceImpl.save()).

 ✅ Ми використовуємо Mockito, тому що:
 - У save() є залежності: ClientCrudService, PlanetCrudService, HibernateHelper
 - Ми не хочемо створювати реальні сервіси чи підключатися до бази даних
 - Mockito дозволяє створити "фейкові" сервіси й задати їх поведінку
 - Це робить тести легкими, швидкими й незалежними від БД

 ❗ Без Mockito довелося би:
 - Створювати реальні реалізації сервісів (ClientCrudServiceImpl тощо)
 - Налаштовувати Hibernate + БД навіть для простих перевірок null
 - Це вже був би інтеграційний тест, а не юніт-тест

 📌 Якщо тест не перевіряє взаємодію з іншими класами — Mockito не потрібен.
 Але якщо є залежності — Mockito рятує.

 🔁 Щоб згадати:
 - Mockito.mock(...) створює мок
 - when(...).thenReturn(...) задає поведінку
 - assertThrows(...) перевіряє, чи метод кидає виняток
*/
