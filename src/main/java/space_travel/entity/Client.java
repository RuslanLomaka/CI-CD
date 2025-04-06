package space_travel.entity;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "client")
@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String passport;

    @Override
    public String toString(){

        return "Passenger [id=" + id + ", name=" + name + ", passport=" + passport + "]";
    }

}
