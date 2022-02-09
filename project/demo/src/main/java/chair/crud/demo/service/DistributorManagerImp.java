package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.exceptions.NoSuchDistributorInDbException;
import chair.crud.demo.repository.ChairRepository;
import chair.crud.demo.repository.DistributorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class DistributorManagerImp implements DistributorManager {

    private final DistributorRepository distributorRepository;
    private final ChairRepository chairRepository;

    @Autowired
    public DistributorManagerImp(DistributorRepository distributorRepository,
                                 ChairRepository chairRepository) {
        this.distributorRepository = distributorRepository;
        this.chairRepository = chairRepository;
    }

    @Override
    public Iterable<Distributor> getAllDistributors() {
        return distributorRepository.findAll();
    }

    @Override
    public Optional<Distributor> getDistributor(long id) {
        return distributorRepository.findById(id);
    }

    @Override
    public void addDistributor(Distributor distributor) {
        distributorRepository.save(distributor);
    }

    @Override
    public void updateDistributor(Distributor distributor) {
        Distributor distributorToUpdate = distributorRepository.findById(distributor.getId())
                .orElseThrow(() -> new NoSuchDistributorInDbException("distributor doesn't exist in db"));
        distributorToUpdate.setCompanyName(distributor.getCompanyName());
        distributorToUpdate.setCountryOfOperation(distributor.getCountryOfOperation());
        distributorToUpdate.setContactPhone(distributor.getContactPhone());
        distributorToUpdate.setHeadquarters(distributor.getHeadquarters());
    }

    @Override
    public void deleteDistributor(long id) {
        Distributor distributorToDelete = distributorRepository.findById(id)
                .orElseThrow(() -> new NoSuchDistributorInDbException("distributor doesn't exist in db"));
        Iterable<Chair> chairsWithDeleteObject = chairRepository.findAllByDistributorsContains(distributorToDelete);
        for(Chair chair : chairsWithDeleteObject) {
            chair.getDistributors().remove(distributorToDelete);
        }
        chairRepository.saveAll(chairsWithDeleteObject);
        distributorRepository.delete(distributorToDelete);
    }

    @Override
    public boolean isDistributorInDb(long id) {
        return distributorRepository.findById(id).isPresent();
    }

    @Override
    public Iterable<Chair> getAllChairsByDistributorId(long id) {
        return distributorRepository.searchAllChairsByDistributorId(id);
    }
}
