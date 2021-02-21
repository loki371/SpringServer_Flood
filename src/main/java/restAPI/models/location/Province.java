package restAPI.models.location;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="provinces")
@Getter @Setter @NoArgsConstructor
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    @Column(columnDefinition="VARCHAR(45)")
    private String type;

    @OneToMany(mappedBy = "province")
    private List<District> districts;
}
