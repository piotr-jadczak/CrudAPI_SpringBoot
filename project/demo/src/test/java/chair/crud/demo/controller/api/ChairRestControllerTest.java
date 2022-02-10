package chair.crud.demo.controller.api;

import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.domain.dto.ChairCreateDto;
import chair.crud.demo.domain.dto.ChairDto;
import chair.crud.demo.domain.dto.DistributorDto;
import chair.crud.demo.domain.dto.SpecificationDto;
import chair.crud.demo.domain.extension.Address;
import chair.crud.demo.domain.extension.DestinationT;
import chair.crud.demo.domain.extension.MaterialT;
import chair.crud.demo.service.ChairManagerImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class ChairRestControllerTest {

    @Mock
    ChairManagerImp chairManager;

    @InjectMocks
    ChairRestController chairRestController;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        //build controller
        mockMvc = MockMvcBuilders.standaloneSetup(chairRestController).build();
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ChairDto getTestChairDto() {
        Address address = new Address("TestStreet", "1234", "TestCity", "1234", "TestCountry");
        Manufacturer manufacturer = new Manufacturer("TestManufacturer", address);
        manufacturer.setId(1L);
        SpecificationDto specificationDto = new SpecificationDto(1L,20, MaterialT.LEATHER);
        DistributorDto distributorDto1 = new DistributorDto(1L, "DistributorTest1", "TestCountry", "12345678", address);
        DistributorDto distributorDto2 = new DistributorDto(2L, "DistributorTest2", "TestCountry", "12345678", address);
        return new ChairDto(1L, "TestModel", DestinationT.BAR, manufacturer, specificationDto, Arrays.asList(distributorDto1, distributorDto2));
    }

    private ChairCreateDto getTestChairCreateDto() {
        return new ChairCreateDto("TestModel", DestinationT.BAR,
                new SpecificationDto(1L, 20, MaterialT.LEATHER), 1L, Arrays.asList(1L, 2L));
    }

    @Test
    public void should_ReturnChairDto_When_PostCorrectChairCreateDto() throws Exception {
        //given
        ChairCreateDto chairCreateDto = getTestChairCreateDto();
        ChairDto chairDto = getTestChairDto();
        when(this.chairManager.addChairDto(any())).thenReturn(chairDto);
        //when
        mockMvc.perform(post("/api/chairs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(chairCreateDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.model").value("TestModel"),
                        jsonPath("$.destination").value("BAR"),
                        jsonPath("$.manufacturer.id").value(1),
                        jsonPath("$.manufacturer.companyName").value("TestManufacturer"),
                        jsonPath("$.specification.id").value(1),
                        jsonPath("$.specification.weight").value(20),
                        jsonPath("$.distributors[0].companyName").value("DistributorTest1"),
                        jsonPath("$.distributors[1].companyName").value("DistributorTest2"));
        //then
        verify(chairManager, times(1)).addChairDto(any());
    }

    @Test
    public void should_ReturnChairDto_When_GetChairCalled() throws Exception {
        //given
        ChairDto chairDto = getTestChairDto();
        when(this.chairManager.getChairDto(anyLong())).thenReturn(chairDto);
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chair/{id}", 1))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.model").value("TestModel"),
                        jsonPath("$.destination").value("BAR"),
                        jsonPath("$.manufacturer.id").value(1),
                        jsonPath("$.manufacturer.companyName").value("TestManufacturer"),
                        jsonPath("$.specification.id").value(1),
                        jsonPath("$.specification.weight").value(20),
                        jsonPath("$.distributors[0].companyName").value("DistributorTest1"),
                        jsonPath("$.distributors[1].companyName").value("DistributorTest2"));
        //then
       verify(chairManager, times(1)).getChairDto(anyLong());
    }

    @Test
    public void should_ReturnChairDtoList_When_GetAllChairsCalled() throws Exception {
        //given
        ChairDto chairDto = getTestChairDto();
        ChairDto chairDto2 = getTestChairDto();
        chairDto2.setId(2L);
        when(this.chairManager.getAllChairsDto()).thenReturn(Arrays.asList(chairDto, chairDto2));
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chairs"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].model").value("TestModel"),
                        jsonPath("$[0].destination").value("BAR"),
                        jsonPath("$[0].manufacturer.id").value(1),
                        jsonPath("$[0].manufacturer.companyName").value("TestManufacturer"),
                        jsonPath("$[0].specification.id").value(1),
                        jsonPath("$[0].specification.weight").value(20),
                        jsonPath("$[0].distributors[0].companyName").value("DistributorTest1"),
                        jsonPath("$[0].distributors[1].companyName").value("DistributorTest2"),
                        jsonPath("$[1].id").value(2));

        //then
        verify(chairManager, times(1)).getAllChairsDto();
    }

    @Test
    public void should_ThrowEmptyDbException_When_ChairListIsEmpty() throws Exception {
        //given
        when(this.chairManager.getAllChairsDto()).thenReturn(Collections.emptyList());
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/chairs"))
                .andExpect(status().isNotFound());
        //then
        verify(chairManager, times(1)).getAllChairsDto();
    }

    @Test
    public void should_ReturnStatusOk_When_DeleteChair() throws Exception {
        //given
        doNothing().when(this.chairManager).deleteChair(anyLong());
        //when
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/chair/{id}", 1))
                .andExpect(status().isOk());
        //then
        verify(chairManager, times(1)).deleteChair(anyLong());
    }

    @Test
    public void should_ReturnChairDto_When_UpdateCorrectChairCreateDto() throws Exception {
        //given
        ChairDto chairDto = getTestChairDto();
        ChairCreateDto chairCreateDto = getTestChairCreateDto();
        when(this.chairManager.updateChair(anyLong(), any())).thenReturn(chairDto);
        //when
        mockMvc.perform(put("/api/chair/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(chairCreateDto)))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.model").value("TestModel"),
                        jsonPath("$.destination").value("BAR"),
                        jsonPath("$.manufacturer.id").value(1),
                        jsonPath("$.manufacturer.companyName").value("TestManufacturer"),
                        jsonPath("$.specification.id").value(1),
                        jsonPath("$.specification.weight").value(20),
                        jsonPath("$.distributors[0].companyName").value("DistributorTest1"),
                        jsonPath("$.distributors[1].companyName").value("DistributorTest2"));
        //then
        verify(chairManager, times(1)).updateChair(anyLong(), any());
    }
}
