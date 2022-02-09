package chair.crud.demo.service;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.domain.Specification;
import chair.crud.demo.domain.dto.ChairDto;
import chair.crud.demo.domain.extension.Address;
import chair.crud.demo.domain.extension.DestinationT;
import chair.crud.demo.domain.extension.MaterialT;
import chair.crud.demo.repository.ChairRepository;
import chair.crud.demo.repository.DistributorRepository;
import chair.crud.demo.repository.ManufacturerRepository;
import chair.crud.demo.repository.SpecificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChairManagerTest {

    @Mock
    private ChairRepository chairRepository;
    @Mock
    private DistributorRepository distributorRepository;
    @Mock
    private SpecificationRepository specificationRepository;
    @Mock
    private ManufacturerRepository manufacturerRepository;

    @InjectMocks
    private ChairManagerImp chairManager;

    @Test
    void should_ReturnAllChairs_When_GetAllChairsCalled() {
        //given
        Chair chair1 = new Chair("Model 1", DestinationT.OFFICE);
        Chair chair2 = new Chair("Model 2", DestinationT.BAR);
        when(this.chairRepository.findAll()).thenReturn(Arrays.asList(chair1, chair2));
        int expectedSize = 2;
        //when
        Iterable<Chair> actual = chairManager.getAllChairs();
        int actualSize = (int) StreamSupport.stream(actual.spliterator(), false).count();
        List<Chair> actualList = StreamSupport.stream(actual.spliterator(), false).collect(Collectors.toList());

        //then
        assertAll(() -> assertEquals(expectedSize, actualSize),
                () -> assertEquals(chair1.getModel(), actualList.get(0).getModel()),
                () -> assertEquals(chair1.getDestination(), actualList.get(0).getDestination()),
                () -> assertEquals(chair2.getModel(), actualList.get(1).getModel()),
                () -> assertEquals(chair2.getDestination(), actualList.get(1).getDestination()));
    }

    @Test
    void should_ReturnOptionalChair_When_GetChairCalled() {
        //given
        Optional<Chair> chair1 = Optional.of(new Chair("Model 1", DestinationT.OFFICE));
        when(this.chairRepository.findById(anyLong())).thenReturn(chair1);
        String expectedModel = chair1.get().getModel();
        DestinationT expectedDestination = chair1.get().getDestination();
        //when
        Optional<Chair> actual = chairManager.getChair(anyLong());
        //then
        assertAll(() -> assertTrue(actual.isPresent()),
                () -> assertEquals(expectedModel, actual.get().getModel()),
                () -> assertEquals(expectedDestination, actual.get().getDestination()));
    }

    @Test
    void should_ReturnChairDto_When_GetChairDtoCalled() {
        //given
        Address address = new Address("Street", "99", "TestCity", "9999", "TestCountry");
        Manufacturer manufacturer = new Manufacturer("TestManufacturer", address);
        manufacturer.setId(1L);
        Specification specification = new Specification(20, MaterialT.LEATHER);
        specification.setId(1L);
        Optional<Chair> chair = Optional.of(new Chair("Model 1", DestinationT.BAR));
        chair.get().setId(1L);
        chair.get().setSpecification(specification);
        chair.get().setManufacturer(manufacturer);

        Distributor distributor1 = new Distributor("TestDistributor1", "TestCOO", "12345678", address);
        distributor1.setId(1L);
        Distributor distributor2 = new Distributor("TestDistributor2", "TestCOO", "12345678", address);
        distributor2.setId(2L);

        when(this.chairRepository.findById(anyLong())).thenReturn(chair);
        when(this.chairRepository.searchAllDistributorsByChairId(anyLong())).thenReturn(Arrays.asList(distributor1, distributor2));
        //when
        ChairDto chairDto = chairManager.getChairDto(anyLong());
        //then
        assertAll(() -> assertEquals(chairDto.getId(), 1),
                () -> assertEquals(chairDto.getManufacturer().getCompanyName(), "TestManufacturer"),
                () -> assertEquals(chairDto.getSpecification().getWeight(), 20.0),
                () -> assertEquals(chairDto.getDistributors().size(), 2));
    }
}
