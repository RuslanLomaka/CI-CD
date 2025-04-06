package space_travel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;


@Table(name = "ticket")
@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;

    @Column
    private Instant createdAt = Instant.now();

    @OneToOne
    @JoinColumn(name = "from_planet_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Planet fromPlanet;

    @OneToOne
    @JoinColumn(name = "to_planet_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Planet toPlanet;

    @Override
    public String toString() {
        return "Ticket [ticketId=" + ticketId + ", createdAt=" + createdAt + "]";
    }
}