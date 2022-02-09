package chair.crud.demo.service;

import chair.crud.demo.domain.Specification;
import chair.crud.demo.exceptions.NoSuchSpecificationInDbException;
import chair.crud.demo.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class SpecificationManagerImp implements SpecificationManager {

    private final SpecificationRepository specificationRepository;

    @Autowired
    public SpecificationManagerImp(SpecificationRepository specificationRepository) {
        this.specificationRepository = specificationRepository;
    }

    @Override
    public Iterable<Specification> getAllSpecifications() {
        return specificationRepository.findAll();
    }

    @Override
    public Stream<Specification> getAllSpecificationsWhereChairIsNullOrChairId(long id) {
        return StreamSupport.stream(specificationRepository.findAllByChairIsNullOrChair_Id(id).spliterator(), false);
    }

    @Override
    public Optional<Specification> getSpecification(long id) {
        return specificationRepository.findById(id);
    }

    @Override
    public void addSpecification(Specification specification) {
        specificationRepository.save(specification);
    }

    @Override
    public void updateSpecification(Specification specification) {
        Specification specToUpdate = specificationRepository.findById(specification.getId())
                .orElseThrow(() -> new NoSuchSpecificationInDbException("specification doesn't exist in db"));
        specToUpdate.setMaterial(specification.getMaterial());
        specToUpdate.setWeight(specification.getWeight());
    }


    @Override
    @Transactional
    public void deleteSpecification(long id) {
        Specification specificationToDelete = specificationRepository.findById(id)
                .orElseThrow(() -> new NoSuchSpecificationInDbException("specification doesn't exist in db"));
        specificationRepository.delete(specificationToDelete);
    }

    @Override
    public boolean isSpecificationInDb(long id) {
        return specificationRepository.findById(id).isPresent();
    }
}
