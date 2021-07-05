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
    private int orderRegis;
    private String wardId;

    public RegisOrder(long regisId, int orderRegis, String wardId) {
        this.id = regisId;
        this.orderRegis = orderRegis;
        this.wardId = wardId;
    }
}
