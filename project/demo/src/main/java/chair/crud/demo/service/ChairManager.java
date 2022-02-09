package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.dto.ChairCreateDto;
import chair.crud.demo.domain.dto.ChairDto;
import chair.crud.demo.domain.extension.SearchOptionT;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ChairManager {

    Iterable<Chair> getAllChairs();

    Optional<Chair> getChair(long id);

    List<ChairDto> getAllChairsDto();

    ChairDto getChairDto(long id);

    Chair addChair(Chair chair);

    ChairDto addChairDto(ChairCreateDto chairCreateDto);

    Iterable<Chair> addAllChairs(Iterable<Chair> chairs);

    Chair updateChair(Chair chair);

    ChairDto updateChair(long id, ChairCreateDto chairCreateDto);

    void deleteChair(long id);

    boolean isChairInDb(long id);

    Stream<Distributor> getAllAvailableDistributorsForChairId(long id);

    void addDistributor(long chairId, Distributor distributor);

    void removeDistributor(long chairId, long distributorId);

    Iterable<Chair> getAllChairsWithManufacturerId(long id);

    boolean isChairModelUnique(long chairId, String model);

    Iterable<Distributor> getAllDistributorsByChairId(long id);

    // Search

    Iterable<Chair> getAllChairsBy(SearchOptionT searchOption, String searchPhrase);

}
