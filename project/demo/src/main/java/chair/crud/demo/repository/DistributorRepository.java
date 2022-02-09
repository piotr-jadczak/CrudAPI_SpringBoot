package chair.crud.demo.repository;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorRepository extends CrudRepository<Distributor, Long> {

    @Query("SELECT c FROM Distributor d JOIN d.chairs c WHERE d.id=?1")
    Iterable<Chair> searchAllChairsByDistributorId(long id);
}
