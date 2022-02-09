package chair.crud.demo.domain;

import chair.crud.demo.domain.extension.Address;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name="distributor")
public class Distributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=2, max=64, message = "name length must be between 2-32")
    @Column(length = 64)
    private String companyName;

    @Size(min=2, max=32, message = "name length must be between 2-32")
    @Column(length = 32)
    private String countryOfOperation;

    @Size(min=9, max=32, message = "phone length must be between 9-32")
    @Column(length = 32)
    private String contactPhone;

    @Embedded
    @Valid
    private Address headquarters;

    @ManyToMany(mappedBy = "distributors")
    private Set<Chair> chairs;

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

    public String getCountryOfOperation() {
        return countryOfOperation;
    }

    public void setCountryOfOperation(String countryOfOperation) {
        this.countryOfOperation = countryOfOperation;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Address getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(Address headquarters) {
        this.headquarters = headquarters;
    }

    public Set<Chair> getChairs() {
        return chairs;
    }

    public void setChairs(Set<Chair> chairs) {
        this.chairs = chairs;
    }

    // Constructors

    public Distributor() {
    }

    public Distributor(String companyName, String countryOfOperation, String contactPhone, Address headquarters) {
        this.companyName = companyName;
        this.countryOfOperation = countryOfOperation;
        this.contactPhone = contactPhone;
        this.headquarters = headquarters;
    }

    public Distributor(long id) {
        this.id = id;
    }

    // Methods

    public void addChair(Chair chair) {
        chairs.add(chair);
    }

    public void removeChair(Chair chair) {
        chairs.remove(chair);
    }
}
