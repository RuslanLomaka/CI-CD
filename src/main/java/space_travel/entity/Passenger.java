package space_travel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "passenger")
@Data
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    @Column
    private String name;

    @Column
    private String passport;

    @Override
    public String toString(){

        return "Passenger [id=" + passengerId + ", name=" + name + ", passport=" + passport + "]";
    }

}
