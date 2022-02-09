package chair.crud.demo.domain.dto;

import chair.crud.demo.domain.extension.DestinationT;

import java.util.List;

public class ChairCreateDto {

    private String model;
    private DestinationT destinationT;
    private SpecificationDto specificationDto;
    private long manufacturerId;
    private List<Long> distributorsId;

    // Get and Set

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DestinationT getDestinationT() {
        return destinationT;
    }

    public void setDestinationT(DestinationT destinationT) {
        this.destinationT = destinationT;
    }

    public SpecificationDto getSpecificationDto() {
        return specificationDto;
    }

    public void setSpecificationDto(SpecificationDto specificationDto) {
        this.specificationDto = specificationDto;
    }

    public long getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(long manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public List<Long> getDistributorsId() {
        return distributorsId;
    }

    public void setDistributorsId(List<Long> distributorsId) {
        this.distributorsId = distributorsId;
    }

    // Constructors

    public ChairCreateDto() {
    }

    public ChairCreateDto(String model, DestinationT destinationT, SpecificationDto specificationDto, long manufacturerId, List<Long> distributorsId) {
        this.model = model;
        this.destinationT = destinationT;
        this.specificationDto = specificationDto;
        this.manufacturerId = manufacturerId;
        this.distributorsId = distributorsId;
    }

    // Methods

    public boolean isValid(StringBuilder errorMessage) {

        if(!isModelValid(errorMessage)) {
            return false;
        }
        if(!specificationDto.isValid(errorMessage)) {
            return false;
        }
        return true;
    }

    private boolean isModelValid(StringBuilder errorMessage) {
        if(model != null && model.length() >= 4 && model.length() <= 32) {
            return  true;
        }
        errorMessage.append("model name length must be between 4-32;");
        return false;
    }
}
