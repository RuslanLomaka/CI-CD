package app;
import org.flywaydb.core.Flyway;
import space_travel.entity.Client;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.impl.ClientCrudServiceImpl;
import space_travel.impl.PlanetCrudServiceImpl;
import space_travel.impl.TicketCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import space_travel.service.ClientCrudService;
import space_travel.service.PlanetCrudService;
import space_travel.service.TicketCrudService;

import java.time.Instant;
import java.util.List;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        // Migrate
        migrate(); // uncomment if needed

        // Initialize services
        ClientCrudService passengerService = new ClientCrudServiceImpl();
        PlanetCrudService planetService = new PlanetCrudServiceImpl();
        TicketCrudService ticketService = new TicketCrudServiceImpl();

        // === PLANET ===
        Planet mars = new Planet();
        mars.setId("MARS");
        mars.setName("Mars");
        try {
            planetService.save(mars);
            if (logger.isInfoEnabled()) logger.info("✅ Планету успішно збережено.");
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("⚠️ Помилка при збереженні: {}", e.getMessage());
            }
        }

        try {
            planetService.save(mars);
            logger.info("✅ Планету успішно збережено.");
        } catch (Exception e) {
            logger.warn("⚠️ Помилка при збереженні: {}", e.getMessage());
        }

        mars.setName("Red Planet");
        planetService.update(mars);

        Planet foundPlanet = planetService.findById("MARS");
        logger.info("Found Planet: {}", foundPlanet);

        List<Planet> allPlanets = planetService.findAll();
        logger.info("All Planets: {}", allPlanets);

        // === PASSENGER ===
        Client p = new Client();
        p.setName("Test Passenger");
        p.setPassport("TP001");

        try {
            passengerService.save(p);
        } catch (IllegalArgumentException e) {
            logger.info("ℹ️ Passenger already exists, maybe update?");
        }

        try {
            passengerService.save(p);
        } catch (IllegalArgumentException e) {
            logger.info("ℹ️ Passenger already exists, maybe update?");
        }

        // Find the saved passenger to get the generated ID
        Client savedClient = passengerService.findAll().stream()
                .filter(client -> "TP001".equals(client.getPassport()))
                .findFirst().orElseThrow();

        savedClient.setName("Updated Passenger");
        passengerService.update(savedClient);

        Client foundClient = passengerService.findById(savedClient.getId());
        logger.info("Found Passenger: {}", foundClient);

        List<Client> allClients = passengerService.findAll();
        logger.info("All Passengers: {}", allClients);

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
        logger.info("Found Ticket: {}", foundTicket);

        List<Ticket> allTickets = ticketService.findAll();
        logger.info("All Tickets: {}", allTickets);

        // === DELETE ===
        passengerService.deleteById(savedClient.getId());
        planetService.deleteById("MARS");
        ticketService.deleteById(savedTicket.getTicketId());

        logger.info("Everything executed.");
    }

    public static void migrate() {
        Flyway.configure()
                .dataSource("jdbc:h2:./spacetrain", "", "")
                .load()
                .migrate();
    }
}