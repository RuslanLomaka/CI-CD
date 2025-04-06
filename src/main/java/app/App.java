package app;
import org.flywaydb.core.Flyway;
import space_travel.entity.Passenger;
import space_travel.entity.Planet;
import space_travel.entity.Ticket;
import space_travel.impl.PassengerCrudServiceImpl;
import space_travel.impl.PlanetCrudServiceImpl;
import space_travel.impl.TicketCrudServiceImpl;

import java.time.Instant;
import java.util.List;

public class App {
    public static void main(String[] args) {

        // Initialize services
        PassengerCrudServiceImpl passengerService = new PassengerCrudServiceImpl();
        PlanetCrudServiceImpl planetService = new PlanetCrudServiceImpl();
        TicketCrudServiceImpl ticketService = new TicketCrudServiceImpl();

        // === PLANET ===
        Planet mars = new Planet();
        mars.setId("MARS");
        mars.setName("Mars");
        try {
            planetService.save(mars);
            System.out.println("✅ Планету успішно збережено.");
        } catch (Exception e) {
            System.out.println("⚠️ Помилка при збереженні: " + e.getMessage());
        }

        try {
            planetService.save(mars);
            System.out.println("✅ Планету успішно збережено.");
        } catch (Exception e) {
            System.out.println("⚠️ Помилка при збереженні: " + e.getMessage());
        }

        mars.setName("Red Planet");
        planetService.update(mars);

        Planet foundPlanet = planetService.findById("MARS");
        System.out.println("Found Planet: " + foundPlanet);

        List<Planet> allPlanets = planetService.findAll();
        System.out.println("All Planets: " + allPlanets);

        // === PASSENGER ===
        Passenger p = new Passenger();
        p.setName("Test Passenger");
        p.setPassport("TP001");

        try {
            passengerService.save(p);
        } catch (IllegalArgumentException e) {
            System.out.println("ℹ️ Passenger already exists, maybe update?");
        }

        try {
            passengerService.save(p);
        } catch (IllegalArgumentException e) {
            System.out.println("ℹ️ Passenger already exists, maybe update?");
        }

        // Find the saved passenger to get the generated ID
        Passenger savedPassenger = passengerService.findAll().stream()
                .filter(passenger -> "TP001".equals(passenger.getPassport()))
                .findFirst().orElseThrow();

        savedPassenger.setName("Updated Passenger");
        passengerService.update(savedPassenger);

        Passenger foundPassenger = passengerService.findById(savedPassenger.getPassengerId());
        System.out.println("Found Passenger: " + foundPassenger);

        List<Passenger> allPassengers = passengerService.findAll();
        System.out.println("All Passengers: " + allPassengers);

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
        System.out.println("Found Ticket: " + foundTicket);

        List<Ticket> allTickets = ticketService.findAll();
        System.out.println("All Tickets: " + allTickets);

        // === DELETE ===
        passengerService.deleteById(savedPassenger.getPassengerId());
        planetService.deleteById("MARS");
        ticketService.deleteById(savedTicket.getTicketId());

        System.out.println("Everything executed.");
    }

    public static void migrate(){
        Flyway.configure()
                .dataSource("jdbc:h2:./spacetrain", "", "")
                .load()
                .migrate();
    }
}