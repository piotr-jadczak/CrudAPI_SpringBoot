package chair.crud.demo.domain.dto;

import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.domain.extension.DestinationT;

import java.util.List;


public class ChairDto {

    private long id;

    private String model;

    private DestinationT destination;

    private Manufacturer manufacturer;

    private SpecificationDto specification;

    private List<DistributorDto> distributors;

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

    public SpecificationDto getSpecification() {
        return specification;
    }

    public void setSpecification(SpecificationDto specification) {
        this.specification = specification;
    }

    public List<DistributorDto> getDistributors() {
        return distributors;
    }

    public void setDistributors(List<DistributorDto> distributors) {
        this.distributors = distributors;
    }

    // Constructors

    public ChairDto() {
    }

    public ChairDto(long id, String model, DestinationT destination, Manufacturer manufacturer, SpecificationDto specification, List<DistributorDto> distributors) {
        this.id = id;
        this.model = model;
        this.destination = destination;
        this.manufacturer = manufacturer;
        this.specification = specification;
        this.distributors = distributors;
    }
}
