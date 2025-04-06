package spaceTravel.planet;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Table(name = "planet")
@Data
@Entity
public class Planet {
    @Id
    @Pattern(regexp = "^[A-Z0-9]+$", message = "ID must contain only uppercase letters and digits")
    private String id;

    @Column(nullable = false, length = 500)
    @Size(min = 1, max = 500)
    private String name;


    @Override
    public String toString() {
        return "Planet [id=" + id + ", name=" + name + "]";

    }
}