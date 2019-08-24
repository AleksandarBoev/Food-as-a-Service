package tu.faas.domain.entities;

import javax.persistence.*;

@MappedSuperclass
public class BaseEntity {
    private Long id;

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
