package space_travel.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.service.ClientCrudService;
import space_travel.service.PlanetCrudService;

import static org.junit.jupiter.api.Assertions.*;

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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenClientIsNull - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("client"));
    }

    @Test
    void save_shouldThrow_whenClientNotFoundInDb() {
        Ticket ticket = new Ticket();

        Client client = new Client();
        client.setId(1L);
        client.setName("Test Passenger");
        client.setPassport("TP001");
        ticket.setClient(client);

        Planet planet = new Planet();
        planet.setId("PL1");
        planet.setName("Test Planet");
        ticket.setFromPlanet(planet);
        ticket.setToPlanet(planet); // Ensure we reach client check

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenClientNotFoundInDb - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("client"));
    }

    @Test
    void save_shouldThrow_whenSourcePlanetIsNull() {
        Ticket ticket = new Ticket();

        Client client = new Client();
        client.setId(1L);
        client.setName("Test Passenger");
        client.setPassport("TP001");
        ticket.setClient(client);
        ticket.setFromPlanet(null);
        ticket.setToPlanet(new Planet()); // dummy planet for destination

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenSourcePlanetIsNull - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("source"));
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
        ticket.setToPlanet(from); // dummy destination to ensure we get to source check

        Mockito.when(mockClientService.findById(client.getId())).thenReturn(client);
        Mockito.when(mockPlanetService.findById(from.getId())).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenSourcePlanetNotFoundInDb - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("source"));
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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenDestinationPlanetIsNull - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("destination"));
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

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ticketService.save(ticket));
        System.out.println("✅ save_shouldThrow_whenDestinationPlanetNotFoundInDb - " + ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("destination"));
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

 ❗ Без Mockito довелось би:
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
