package restAPI.models.location;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    @Column(columnDefinition="VARCHAR(45)")
    private String type;

    @OneToMany(mappedBy = "province")
    @JsonIgnore
    private List<District> districts;
}
