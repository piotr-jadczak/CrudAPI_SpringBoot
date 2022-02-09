package chair.crud.demo.domain;

import chair.crud.demo.domain.extension.DestinationT;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "chair")
public class Chair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=4, max=32, message = "model name length must be between 4-32")
    @Column(length = 32, unique = true)
    private String model;

    @Enumerated(EnumType.STRING)
    private DestinationT destination;

    @ManyToOne(optional = true)
    private Manufacturer manufacturer;

    @ManyToMany
    private Set<Distributor> distributors;

    @NotNull
    @OneToOne(cascade = CascadeType.REMOVE)
    private Specification specification;

    // Get and Set

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DestinationT getDestination() {
        return destination;
    }

    public void setDestination(DestinationT destination) {
        this.destination = destination;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Set<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(Set<Distributor> distributors) {
        this.distributors = distributors;
    }

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    // Constructors

    public Chair() {
    }

    public Chair(String model, DestinationT destination) {
        this.model = model;
        this.destination = destination;
    }

    public Chair(long id) {
        this.id = id;
    }

    // Methods

    public void addDistributor(Distributor distributor) {
        distributors.add(distributor);
    }

    public void removeDistributor(Distributor distributor) {
        distributors.remove(distributor);
    }

}


