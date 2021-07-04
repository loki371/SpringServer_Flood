package restAPI.models.registration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regisoder")
@Setter
@Getter
@NoArgsConstructor
public class RegisOrder {
    @Id
    private Long id;
    private int order;
    private String wardId;

    public RegisOrder(long regisId, int order, String wardId) {
        this.id = regisId;
        this.order = order;
        this.wardId = wardId;
    }
}
