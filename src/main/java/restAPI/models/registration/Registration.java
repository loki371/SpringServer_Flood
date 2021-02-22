package restAPI.models.registration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import restAPI.models.UserInfo;
import restAPI.models.location.Ward;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "registrations")
@Setter @Getter
@NoArgsConstructor
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="VARCHAR(45)")
    private String name;

    private Float longitude;

    private Float latitude;

    @ManyToOne
    private Ward ward;

    private String phone;

    private int numPerson;

    @ManyToOne
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo savedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo createBy;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> commentList;
}
