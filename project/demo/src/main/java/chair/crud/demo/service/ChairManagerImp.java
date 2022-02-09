package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.domain.Specification;
import chair.crud.demo.domain.dto.ChairCreateDto;
import chair.crud.demo.domain.dto.ChairDto;
import chair.crud.demo.domain.dto.DistributorDto;
import chair.crud.demo.domain.dto.SpecificationDto;
import chair.crud.demo.domain.extension.DestinationT;
import chair.crud.demo.domain.extension.MaterialT;
import chair.crud.demo.domain.extension.SearchOptionT;
import chair.crud.demo.exceptions.*;
import chair.crud.demo.repository.ChairRepository;
import chair.crud.demo.repository.DistributorRepository;
import chair.crud.demo.repository.ManufacturerRepository;
import chair.crud.demo.repository.SpecificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ChairManagerImp implements ChairManager {
    private final ChairRepository chairRepository;
    private final DistributorRepository distributorRepository;
    private final SpecificationRepository specificationRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Autowired
    public ChairManagerImp(ChairRepository chairRepository,
                           DistributorRepository distributorRepository,
                           SpecificationRepository specificationRepository,
                           ManufacturerRepository manufacturerRepository) {
        this.chairRepository = chairRepository;
        this.distributorRepository = distributorRepository;
        this.specificationRepository = specificationRepository;
        this.manufacturerRepository = manufacturerRepository;
    }

    @Override
    public Iterable<Chair> getAllChairs() {
        return chairRepository.findAll();
    }

    @Override
    public Optional<Chair> getChair(long id) {
        return chairRepository.findById(id);
    }

    @Override
    public List<ChairDto> getAllChairsDto() {
        Iterable<Chair> chairs = chairRepository.findAll();
        List<ChairDto> chairsDto = new ArrayList<>();
        for(Chair chair : chairs) {
            chairsDto.add(getChairDto(chair.getId()));
        }
        return chairsDto;
    }

    @Override
    public ChairDto getChairDto(long id) {
        Chair chair = chairRepository.findById(id).orElseThrow(() -> new NoSuchChairInDbException("chair doesn't exist in db"));
        Iterable<Distributor> chairDistributors = chairRepository.searchAllDistributorsByChairId(id);
        List<DistributorDto> chairDtoDistributors = new ArrayList<>();
        for(Distributor distributor : chairDistributors) {
            chairDtoDistributors.add(new DistributorDto(distributor.getId(), distributor.getCompanyName(),
                    distributor.getCountryOfOperation(), distributor.getContactPhone(), distributor.getHeadquarters()));
        }
        SpecificationDto specificationDto = new SpecificationDto(chair.getSpecification().getId(),
                chair.getSpecification().getWeight(), chair.getSpecification().getMaterial());
        ChairDto chairDto = new ChairDto(chair.getId(), chair.getModel(), chair.getDestination(), chair.getManufacturer(), specificationDto, chairDtoDistributors);
        return chairDto;
    }

    @Override
    public Chair addChair(Chair chair) {
        validateAddChairWebForm(chair);
        return chairRepository.save(chair);
    }

    @Override
    public ChairDto addChairDto(ChairCreateDto chairCreateDto) {
        StringBuilder errorMessage = new StringBuilder("");
        boolean isChairWithModelInDb = chairRepository.findByModel(chairCreateDto.getModel()).isPresent();
        if(!chairCreateDto.isValid(errorMessage) || isChairWithModelInDb) {
            if(isChairWithModelInDb) {
                errorMessage.append("model ").append(chairCreateDto.getModel()).append(" already exist;");
            }
            throw new NotValidChairException(errorMessage.toString());
        }
        Chair savedChair = createChair(chairCreateDto);
        return getChairDto(savedChair.getId());
    }

    @Override
    public Iterable<Chair> addAllChairs(Iterable<Chair> chairs) {
        return chairRepository.saveAll(chairs);
    }

    @Override
    public Chair updateChair(Chair chair) {
        validateAddChairWebForm(chair);
        Chair chairToUpdate = chairRepository.findById(chair.getId()).orElseThrow(() -> new NoSuchChairInDbException("chair doesn't exist in db"));
        chairToUpdate.setDestination(chair.getDestination());
        chairToUpdate.setManufacturer(chair.getManufacturer());
        chairToUpdate.setModel(chair.getModel());
        chairToUpdate.setSpecification(chair.getSpecification());
        return chairToUpdate;
    }

    @Override
    public ChairDto updateChair(long id, ChairCreateDto chairCreateDto) {
        Chair chairToUpdate = chairRepository.findById(id).orElseThrow(() -> new NoSuchChairInDbException("chair doesn't exist in db"));
        StringBuilder errorMessage = new StringBuilder("");
        boolean modelUnique = isChairModelUnique(id, chairCreateDto.getModel());
        if(!chairCreateDto.isValid(errorMessage) || !modelUnique) {
            if(!modelUnique) {
                errorMessage.append("model ").append(chairCreateDto.getModel()).append(" already exist;");
            }
            throw new NotValidChairException(errorMessage.toString());
        }
        Chair updatedChair = updateChair(chairToUpdate, chairCreateDto);
        return getChairDto(updatedChair.getId());
    }

    @Override
    public void deleteChair(long id) {
        Chair chairToDelete = chairRepository.findById(id).orElseThrow(() -> new NoSuchChairInDbException("chair doesn't exist in db"));
        chairRepository.delete(chairToDelete);
    }

    @Override
    public boolean isChairInDb(long id) {
        return chairRepository.findById(id).isPresent();
    }

    @Override
    public Stream<Distributor> getAllAvailableDistributorsForChairId(long id) {
        Chair chair = this.getChair(id).orElseThrow(() -> new NoSuchChairInDbException("Chair is not in db"));
        Set<Distributor> chairDistributors = chair.getDistributors();
        Set<Distributor> allDistributors = StreamSupport.stream(distributorRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());
        allDistributors.removeAll(chairDistributors);
        return allDistributors.stream();
    }

    @Override
    public void addDistributor(long chairId, Distributor distributor) {
        Chair chair = chairRepository.findById(chairId).orElseThrow(() -> new NoSuchChairInDbException("Chair is not in db"));
        Stream<Distributor> chairDistributors = StreamSupport.stream(chairRepository.searchAllDistributorsByChairId(chair.getId()).spliterator(), false);

        if(chairDistributors.anyMatch(d -> d.getId() == distributor.getId())) {
            throw new NoSuchDistributorInDbException("Distributor is already in chair collection");
        }
        chair.addDistributor(distributor);
    }

    @Override
    public void removeDistributor(long chairId, long distributorId) {
        Chair chair = getChair(chairId).orElseThrow(() -> new NoSuchChairInDbException("Chair is not in db"));
        Stream<Distributor> chairDistributors = StreamSupport.stream(chairRepository.searchAllDistributorsByChairId(chair.getId()).spliterator(), false);
        Distributor distributor = distributorRepository.findById(distributorId)
                .orElseThrow(() -> new NoSuchDistributorInDbException("Distributor is not in db"));
        if(chairDistributors.noneMatch(d -> d.getId() == distributor.getId())) {
            throw new NoSuchDistributorInDbException("Distributor is not in chair collection");
        }
        chair.removeDistributor(distributor);
    }

    @Override
    public Iterable<Chair> getAllChairsWithManufacturerId(long id) {
        return chairRepository.findAllByManufacturer_Id(id);
    }

    @Override
    public boolean isChairModelUnique(long chairId, String model) {
        Optional<Chair> chairInDb = chairRepository.findByModel(model);
        return chairInDb.map(value -> value.getId() == chairId).orElse(true);
    }

    @Override
    public Iterable<Distributor> getAllDistributorsByChairId(long id) {
        return chairRepository.searchAllDistributorsByChairId(id);
    }

    @Override
    public Iterable<Chair> getAllChairsBy(SearchOptionT searchOption, String searchPhrase) {

        switch (searchOption) {
            case DESTINATION:
                DestinationT searchedDestination = DestinationT.valueOf(searchPhrase.toUpperCase());
                return chairRepository.findAllByDestination(searchedDestination);
            case MATERIAL:
                MaterialT searchedMaterial = MaterialT.valueOf(searchPhrase.toUpperCase());
                return chairRepository.searchAllBYSpecificationMaterial(searchedMaterial);
            case WEIGTH_LESS:
                double searchedWeight = Double.parseDouble(searchPhrase);
                return chairRepository.searchAllBySpecificationWeigthLessThan(searchedWeight);
            case MANUFACTURER_HQ_COUNTRY:
                return chairRepository.searchAllByManufacturerHqCountry(searchPhrase);
            case DISTRIBUTOR_COUNTRY_OF_OPERATION:
                return chairRepository.searchAllByDistributorCountryOfOperation(searchPhrase);
            default:
                throw new IllegalArgumentException();
        }
    }

    private Chair createChair(ChairCreateDto chairCreateDto) {
        Chair chair = new Chair();
        Specification specification = specificationRepository
                .save(new Specification(chairCreateDto.getSpecificationDto().getWeight(), chairCreateDto.getSpecificationDto().getMaterial()));
        chair.setSpecification(specification);
        chair.setModel(chairCreateDto.getModel());
        chair.setDestination(chairCreateDto.getDestinationT());
        Manufacturer manufacturer = manufacturerRepository.findById(chairCreateDto.getManufacturerId())
                .orElseThrow(() -> new NoSuchManufacturerInDbException("manufacturer doesn't exist in db"));
        chair.setManufacturer(manufacturer);
        chair.setDistributors(new HashSet<>());
        for(Long distributorId : chairCreateDto.getDistributorsId()) {
            Distributor distributorToAdd = distributorRepository.findById(distributorId)
                    .orElseThrow(() -> new NoSuchDistributorInDbException("distributor doesn't exist in db"));
            System.out.println(distributorToAdd.getCompanyName());
            chair.addDistributor(distributorToAdd);
        }
        return chairRepository.save(chair);
    }

    private Chair updateChair(Chair chairToUpdate,ChairCreateDto chairCreateDto) {
        Specification chairSpecification = specificationRepository.findById(chairToUpdate.getSpecification().getId())
                        .orElseThrow(() -> new NoSuchSpecificationInDbException("specification doesn't exsist in db"));
        chairSpecification.setMaterial(chairCreateDto.getSpecificationDto().getMaterial());
        chairSpecification.setWeight(chairCreateDto.getSpecificationDto().getWeight());
        specificationRepository.save(chairSpecification);
        chairToUpdate.setModel(chairCreateDto.getModel());
        chairToUpdate.setDestination(chairCreateDto.getDestinationT());
        Manufacturer manufacturer = manufacturerRepository.findById(chairCreateDto.getManufacturerId())
                .orElseThrow(() -> new NoSuchManufacturerInDbException("manufacturer doesn't exist in db"));
        chairToUpdate.setManufacturer(manufacturer);
        chairToUpdate.setDistributors(new HashSet<>());
        for(Long distributorId : chairCreateDto.getDistributorsId()) {
            Distributor distributorToAdd = distributorRepository.findById(distributorId)
                    .orElseThrow(() -> new NoSuchDistributorInDbException("distributor doesn't exist in db"));
            System.out.println(distributorToAdd.getCompanyName());
            chairToUpdate.addDistributor(distributorToAdd);
        }
        return chairRepository.save(chairToUpdate);
    }

    private void validateAddChairWebForm(Chair chair) {
        if(chair.getSpecification().getChair() != null && chair.getSpecification().getChair().getId() != chair.getId())
            throw new NoSuchSpecificationInDbException("Specification with that id is already taken");
    }
}
