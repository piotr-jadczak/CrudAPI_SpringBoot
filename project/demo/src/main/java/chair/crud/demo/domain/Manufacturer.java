package chair.crud.demo.domain;

import chair.crud.demo.domain.extension.Address;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@Entity
@Table(name="manufacturer")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=2, max=64, message = "name length must be between 2-32")
    @Column(length = 64)
    private String companyName;

    @Embedded
    @Valid
    private Address headquarters;

    // Get and Set

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(Address headquarters) {
        this.headquarters = headquarters;
    }

    // Constructors

    public Manufacturer() {
    }

    public Manufacturer(String companyName, Address headquarters) {
        this.companyName = companyName;
        this.headquarters = headquarters;
    }

    public Manufacturer(long id) {
        this.id = id;
    }
}
