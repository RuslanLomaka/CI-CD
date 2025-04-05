package passenger;

import jakarta.persistence.*;
import lombok.Data;

@Table(name = "passenger")
@Data
@Entity
public class Passenger {
    @Id
    private long passengerId;

    @Column
    private String name;

    @Column
    private String passport;

    @OneToOne
    public String toString(){
        return passengerId + "\t" + name + "\t" + passport;
    }

}
