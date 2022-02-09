package chair.crud.demo.service;

import chair.crud.demo.domain.Manufacturer;

import java.util.Optional;

public interface ManufacturerManager {

    Iterable<Manufacturer> getAllManufacturers();

    Optional<Manufacturer> getManufacturer(long id);

    void addManufacturer(Manufacturer manufacturer);

    void updateManufacturer(Manufacturer manufacturer);

    void deleteManufacturer(long id);

    boolean isManufacturerInDb(long id);
}
