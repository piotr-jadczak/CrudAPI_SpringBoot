package chair.crud.demo.controller.api;

import chair.crud.demo.domain.dto.ChairCreateDto;
import chair.crud.demo.domain.dto.ChairDto;
import chair.crud.demo.exceptions.ChairsDbIsEmptyException;
import chair.crud.demo.service.ChairManager;
import chair.crud.demo.service.DistributorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChairRestController {

    private final ChairManager chairManager;

    @Autowired
    public ChairRestController(ChairManager chairManager) {
        this.chairManager = chairManager;
    }

    @GetMapping("/api/chairs")
    public List<ChairDto> allChairs() {
        List<ChairDto> chairsDto = chairManager.getAllChairsDto();
        if(chairsDto.isEmpty()) {
            throw new ChairsDbIsEmptyException("No chairs in db");
        }
        return chairsDto;
    }

    @GetMapping("/api/chair/{id}")
    public ChairDto getChair(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        return chairManager.getChairDto(objectId);
    }

    @DeleteMapping("/api/chair/{id}")
    public void deleteChair(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        chairManager.deleteChair(objectId);
    }

    @PostMapping("/api/chairs")
    public ChairDto addChair(@RequestBody ChairCreateDto chair) {
        return chairManager.addChairDto(chair);
    }

    @PutMapping("/api/chair/{id}")
    public ChairDto updateChair(@PathVariable("id") String id,@RequestBody ChairCreateDto chair) {
        int objectId = Integer.parseInt(id);
        return chairManager.updateChair(objectId, chair);
    }
}
