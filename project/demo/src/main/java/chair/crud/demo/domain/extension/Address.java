package chair.crud.demo.domain.extension;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

@Embeddable
public class Address {

    @Size(min=2, max=64, message = "street length must be between 2-64")
    @Column(length = 64, nullable = false)
    private String street;

    @Size(min=1, max=16, message = "apt. number length must be between 1-16")
    @Column(length = 16, nullable = false)
    private String apartmentNumber;

    @Size(max=64, message = "apt. number length must be less than 64")
    @Column(length = 64, nullable = true)
    private String city;

    @Size(max=16, message = "zip code length must be less than 16")
    @Column(length = 16, nullable = true)
    private String zipCode;

    @Size(min=2, max=64, message = "country length must be between 2-64")
    @Column(length = 64, nullable = false)
    private String country;

    //Get and Set

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //Constructors

    public Address() {
    }

    public Address(String street, String apartmentNumber, String city, String zipCode, String country) {
        this.street = street;
        this.apartmentNumber = apartmentNumber;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    // Methods

    @Override
    public String toString() {
        return street + " " + apartmentNumber + ", " + city + " " + zipCode + ", " + country;
    }
}
