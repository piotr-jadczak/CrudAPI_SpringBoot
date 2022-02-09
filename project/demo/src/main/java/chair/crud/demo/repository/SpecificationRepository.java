package chair.crud.demo.repository;

import chair.crud.demo.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationRepository extends CrudRepository<Specification, Long> {

    Iterable<Specification> findAllByChairIsNullOrChair_Id(long id);
}
