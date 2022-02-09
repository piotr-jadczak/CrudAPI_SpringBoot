package chair.crud.demo.domain;

import chair.crud.demo.domain.extension.MaterialT;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="specification")
public class Specification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMax("100.0") @DecimalMin("0.1") //in kg
    private double weight;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MaterialT material;

    @OneToOne(cascade=CascadeType.REMOVE, mappedBy = "specification")
    private Chair chair;

    // Get and Set

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public MaterialT getMaterial() {
        return material;
    }

    public void setMaterial(MaterialT materialT) {
        this.material = materialT;
    }

    public Chair getChair() {
        return chair;
    }

    public void setChair(Chair chair) {
        this.chair = chair;
    }

    // Constructors

    public Specification() {
    }

    public Specification(double weight, MaterialT materialT) {
        this.weight = weight;
        this.material = materialT;
    }

    public Specification(Long id, double weight, MaterialT material) {
        this.id = id;
        this.weight = weight;
        this.material = material;
    }

    public Specification(Long id) {
        this.id = id;
    }

    // Methods

    @Override
    public String toString() {
        return "id: " + id + ", weight: " + String.format("%.2f", weight) + ", material: " + material.name();
    }
}
