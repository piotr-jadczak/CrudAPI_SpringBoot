package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;

import java.util.Optional;

public interface DistributorManager {

    Iterable<Distributor> getAllDistributors();

    Optional<Distributor> getDistributor(long id);

    void addDistributor(Distributor distributor);

    void updateDistributor(Distributor distributor);

    void deleteDistributor(long id);

    boolean isDistributorInDb(long id);

    Iterable<Chair> getAllChairsByDistributorId(long id);
}
