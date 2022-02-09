package chair.crud.demo.domain.dto;

import chair.crud.demo.domain.extension.Address;

public class DistributorDto {

    private long id;

    private String companyName;

    private String countryOfOperation;

    private String contactPhone;

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

    // Constructors

    public DistributorDto() {
    }

    public DistributorDto(long id, String companyName, String countryOfOperation, String contactPhone, Address headquarters) {
        this.id = id;
        this.companyName = companyName;
        this.countryOfOperation = countryOfOperation;
        this.contactPhone = contactPhone;
        this.headquarters = headquarters;
    }
}
