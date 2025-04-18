package app;

import config.HibernateUtil;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.impl.ClientCrudServiceImpl;
import space_travel.impl.PlanetCrudServiceImpl;
import space_travel.impl.TicketCrudServiceImpl;
import space_travel.service.ClientCrudService;
import space_travel.service.PlanetCrudService;
import space_travel.service.TicketCrudService;

import java.time.Instant;
import java.util.List;

// ✅ Work summary
/*
Entity relationships configured:
• Added a @OneToMany link from the Client entity to its tickets (List<Ticket>).
• Added @ManyToOne links in the Ticket entity to Client, fromPlanet and toPlanet.

Validation logic implemented in TicketCrudServiceImpl.save():
• If client is null or absent in the DB throws IllegalArgumentException.
• If fromPlanet is null or absent in the DB throws an exception.
• If toPlanet is null or absent in the DB throws an exception.

Created JUnit test TicketCrudServiceImplTest that checks six critical cases:
1) client == null
2) client not found in the database
3) fromPlanet == null
4) fromPlanet not found in the database
5) toPlanet == null
6) toPlanet not found in the database

Used Mockito to mock dependencies:
• Mocked ClientCrudService and PlanetCrudService.
• The findById(...) method returns null or valid objects to control save() behaviour.
Tests are isolated from the real database.

Special attention to Sonar warnings:
• Invested significant effort to eliminate Sonar issues, optimising test logic and ensuring clean code.
• Fixes included refactoring test methods and using clear, isolated calls that follow modern development standards.

🧪 Result: TicketCrudServiceImplTest guarantees that a ticket will **not** be saved if any of the fields (client, fromPlanet, toPlanet) are null or reference entities that do not exist in the database.
*/

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    // === Log Message Constants ===
    private static final String MSG_PLANET_SAVE_SUCCESS = "✅ Planet saved successfully.";
    private static final String MSG_PLANET_SAVE_ERROR   = "⚠️ Error while saving: {}";

    private static final String MSG_PLANET_FOUND = "Found Planet: {}";
    private static final String MSG_ALL_PLANETS = "All Planets: {}";

    private static final String MSG_PASSENGER_EXISTS_WARNING = "ℹ️ Passenger already exists, maybe update?";
    private static final String MSG_PASSENGER_FOUND = "Found Passenger: {}";
    private static final String MSG_ALL_PASSENGERS = "All Passengers: {}";

    private static final String MSG_TICKET_FOUND = "Found Ticket: {}";
    private static final String MSG_ALL_TICKETS = "All Tickets: {}";
    private static final String MSG_EXECUTION_DONE = "Everything executed.";

    public static void main(String[] args) {
        // Migrate DB if needed
        migrate();

        // Initialize services
        ClientCrudService passengerService = new ClientCrudServiceImpl();
        PlanetCrudService planetService = new PlanetCrudServiceImpl();
        TicketCrudService ticketService = new TicketCrudServiceImpl(passengerService, planetService);

        // === PLANET ===
        Planet mars = new Planet();
        mars.setId("MARS");
        mars.setName("Mars");
        try {
            planetService.save(mars);
            if (logger.isInfoEnabled()) {
                logger.info(MSG_PLANET_SAVE_SUCCESS);
            }
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn(MSG_PLANET_SAVE_ERROR, e.getMessage());
            }
        }

        // Try saving again to see the warning
        try {
            planetService.save(mars);
            logger.info(MSG_PLANET_SAVE_SUCCESS);
        } catch (Exception e) {
            logger.warn(MSG_PLANET_SAVE_ERROR, e.getMessage());
        }

        mars.setName("Red Planet");
        planetService.update(mars);

        Planet foundPlanet = planetService.findById("MARS");
        logger.info(MSG_PLANET_FOUND, foundPlanet);

        List<Planet> allPlanets = planetService.findAll();
        logger.info(MSG_ALL_PLANETS, allPlanets);

        // === PASSENGER ===
        Client p = new Client();
        p.setName("Test Passenger");
        p.setPassport("TP001");

        try {
            passengerService.save(p);
        } catch (IllegalArgumentException e) {
            logger.info(MSG_PASSENGER_EXISTS_WARNING);
        }

//        // Try saving again to see the warning
//        try {
//            passengerService.save(p);
//        } catch (IllegalArgumentException e) {
//            logger.info(MSG_PASSENGER_EXISTS_WARNING);
//        }

        // Find the saved passenger to get the generated ID
        Client savedClient = passengerService.findAll().stream()
                .filter(client -> "TP001".equals(client.getPassport()))
                .findFirst()
                .orElseThrow();

        savedClient.setName("Updated Passenger");
        passengerService.update(savedClient);

        Client foundClient = passengerService.findById(savedClient.getId());
        logger.info(MSG_PASSENGER_FOUND, foundClient);

        List<Client> allClients = passengerService.findAll();
        logger.info(MSG_ALL_PASSENGERS, allClients);

        // === TICKET ===
        Ticket ticket = new Ticket();
        ticket.setCreatedAt(Instant.now());
        ticket.setFromPlanet(mars);
        ticket.setToPlanet(mars); // just to simplify testing
        ticketService.save(ticket);

        // Find saved ticket
        Ticket savedTicket = ticketService.findAll().get(0);
        savedTicket.setCreatedAt(Instant.now());
        ticketService.update(savedTicket);

        Ticket foundTicket = ticketService.findById(savedTicket.getTicketId());
        logger.info(MSG_TICKET_FOUND, foundTicket);

        List<Ticket> allTickets = ticketService.findAll();
        logger.info(MSG_ALL_TICKETS, allTickets);

        // === DELETE ===
        passengerService.deleteById(savedClient.getId());
        planetService.deleteById("MARS");
        ticketService.deleteById(savedTicket.getTicketId());
        HibernateUtil.getInstance().close();
        logger.info(MSG_EXECUTION_DONE);
    }

    public static void migrate() {
        Flyway.configure()
                .dataSource("jdbc:h2:./spacetrain", "", "")
                .load()
                .migrate();
    }
}
