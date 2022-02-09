package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.exceptions.NoSuchManufacturerInDbException;
import chair.crud.demo.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ManufacturerManagerImp implements ManufacturerManager {

    private final ManufacturerRepository manufacturerRepository;
    private final ChairManager chairManager;

    @Autowired
    public ManufacturerManagerImp(ManufacturerRepository manufacturerRepository,
                                  ChairManager chairManager) {
        this.manufacturerRepository = manufacturerRepository;
        this.chairManager = chairManager;
    }

    @Override
    public Iterable<Manufacturer> getAllManufacturers() {
        return manufacturerRepository.findAll();
    }

    @Override
    public Optional<Manufacturer> getManufacturer(long id) {
        return manufacturerRepository.findById(id);
    }

    @Override
    public void addManufacturer(Manufacturer manufacturer) {
        manufacturerRepository.save(manufacturer);
    }

    @Override
    public void updateManufacturer(Manufacturer manufacturer) {
        Manufacturer manufacturerToUpdate = manufacturerRepository.findById(manufacturer.getId())
                .orElseThrow(() -> new NoSuchManufacturerInDbException("manufacturer doesn't exist in db"));
        manufacturerToUpdate.setCompanyName(manufacturer.getCompanyName());
        manufacturerToUpdate.setHeadquarters(manufacturer.getHeadquarters());
    }

    @Override
    public void deleteManufacturer(long id) {
        Manufacturer manufacturerToDelete = manufacturerRepository.findById(id)
                .orElseThrow(() -> new NoSuchManufacturerInDbException("manufacturer doesn't exist in db"));
        Iterable<Chair> chairsToExcludeManufacturer = chairManager.getAllChairsWithManufacturerId(id);
        chairsToExcludeManufacturer.forEach(c -> c.setManufacturer(null));
        chairManager.addAllChairs(chairsToExcludeManufacturer);
        manufacturerRepository.delete(manufacturerToDelete);
    }

    @Override
    public boolean isManufacturerInDb(long id) {
        return manufacturerRepository.findById(id).isPresent();
    }
}
