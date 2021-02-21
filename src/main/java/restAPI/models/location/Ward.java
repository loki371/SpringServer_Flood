package restAPI.models.location;

import javax.persistence.*;

@Entity
@Table(name = "ward")
public class Ward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    @Column(columnDefinition="VARCHAR(45)")
    private String type;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;
}
