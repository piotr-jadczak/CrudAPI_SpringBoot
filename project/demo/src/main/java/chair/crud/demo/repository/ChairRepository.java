package chair.crud.demo.repository;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.extension.DestinationT;
import chair.crud.demo.domain.extension.MaterialT;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChairRepository extends CrudRepository<Chair, Long> {

    Iterable<Chair> findAllByManufacturer_Id(long id);
    Iterable<Chair> findAllByDistributorsContains(Distributor distributor);
    Optional<Chair> findByModel(String model);
    Iterable<Chair> findAllByDestination(DestinationT destinationT);

    @Query("SELECT d FROM Chair c JOIN c.distributors d WHERE c.id=?1")
    Iterable<Distributor> searchAllDistributorsByChairId(long id);

    @Query("SELECT c FROM Chair c JOIN c.specification s WHERE s.material=?1")
    Iterable<Chair> searchAllBYSpecificationMaterial(MaterialT material);

    @Query("SELECT c FROM Chair c JOIN c.specification s WHERE s.weight<?1")
    Iterable<Chair> searchAllBySpecificationWeigthLessThan(double weigth);

    @Query("SELECT c FROM Chair c JOIN c.manufacturer m WHERE m.headquarters.country=?1")
    Iterable<Chair> searchAllByManufacturerHqCountry(String country);

    @Query("SELECT DISTINCT c FROM Chair c JOIN c.distributors d WHERE d.countryOfOperation=?1")
    Iterable<Chair> searchAllByDistributorCountryOfOperation(String country);

}
