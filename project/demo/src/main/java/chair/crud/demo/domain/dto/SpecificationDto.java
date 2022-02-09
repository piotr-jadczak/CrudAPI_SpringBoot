package chair.crud.demo.domain.dto;

import chair.crud.demo.domain.extension.MaterialT;


public class SpecificationDto {

    private long id;

    private double weight;

    private MaterialT material;

    // Get and Set

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public void setMaterial(MaterialT material) {
        this.material = material;
    }

    // Constructors

    public SpecificationDto() {
    }

    public SpecificationDto(long id, double weight, MaterialT material) {
        this.id = id;
        this.weight = weight;
        this.material = material;
    }

    // Methods

    public boolean isValid(StringBuilder errorMessage) {

        if(weight >= 0.1 && weight <= 100.0) {
            return true;
        }
        errorMessage.append("weight must be between 0.1-100;");
        return false;
    }
}
