package chair.crud.demo.service;

import chair.crud.demo.domain.Specification;

import java.util.Optional;
import java.util.stream.Stream;

public interface SpecificationManager {

    Iterable<Specification> getAllSpecifications();

    Stream<Specification> getAllSpecificationsWhereChairIsNullOrChairId(long id);

    Optional<Specification> getSpecification(long id);

    void addSpecification(Specification specification);

    void updateSpecification(Specification specification);

    void deleteSpecification(long id);

    boolean isSpecificationInDb(long id);

}
