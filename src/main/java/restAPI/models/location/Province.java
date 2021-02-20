package restAPI.models.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="province")
@Getter @Setter @NoArgsConstructor
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(min = 6, max = 6)
    private String id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    @Column(columnDefinition="VARCHAR(45)")
    private String type;

    public Province(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
}
