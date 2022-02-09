package chair.crud.demo.controller.web;

import chair.crud.demo.domain.Chair;
import chair.crud.demo.domain.Distributor;
import chair.crud.demo.domain.form.ChairDistributorForm;
import chair.crud.demo.exceptions.NoSuchChairInDbException;
import chair.crud.demo.service.ChairManager;
import chair.crud.demo.service.ManufacturerManager;
import chair.crud.demo.service.SpecificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ChairController {

    private final ChairManager chairManager;
    private final SpecificationManager specificationManager;
    private final ManufacturerManager manufacturerManager;

    @Autowired
    public ChairController(ChairManager chairManager,
                           SpecificationManager specificationManager,
                           ManufacturerManager manufacturerManager) {
        this.chairManager = chairManager;
        this.specificationManager = specificationManager;
        this.manufacturerManager = manufacturerManager;
    }

    @GetMapping("/chairs/")
    public String viewChairsPage(Model model) {
        model.addAttribute("chairs", chairManager.getAllChairs());
        return "chair/all-chairs";
    }

    @GetMapping("/chairs/add-chair/{id}")
    public String viewChairForm(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        model.addAttribute("manufacturers", manufacturerManager.getAllManufacturers());
        Optional<Chair> chairInDb = chairManager.getChair(objectId);
        if(chairInDb.isPresent()) {
            model.addAttribute("specifications", specificationManager.getAllSpecificationsWhereChairIsNullOrChairId(objectId)
                    .collect(Collectors.toList()));
            model.addAttribute("chairToAdd", chairInDb.get());
            return "chair/add-chair";
        }
        model.addAttribute("specifications", specificationManager.getAllSpecificationsWhereChairIsNullOrChairId(objectId)
                .collect(Collectors.toList()));
        model.addAttribute("chairToAdd", new Chair(0L));
        return "chair/add-chair";
    }

    @PostMapping("/chairs/add-chair")
    public String addChair(@ModelAttribute("chairToAdd") @Valid Chair chairToAdd, BindingResult bindingResult, Model model) {

        boolean modelUnique = chairManager.isChairModelUnique(chairToAdd.getId(), chairToAdd.getModel());
        if(bindingResult.hasErrors() || !modelUnique) {
            if(!modelUnique) {
                model.addAttribute("modelNotUnique", "model " + chairToAdd.getModel() + " already exist.");
            }
            model.addAttribute("specifications", specificationManager.getAllSpecificationsWhereChairIsNullOrChairId(chairToAdd.getId())
                    .collect(Collectors.toList()));
            model.addAttribute("manufacturers", manufacturerManager.getAllManufacturers());
            return "chair/add-chair";
        }
        if(chairManager.isChairInDb(chairToAdd.getId())) {
            chairManager.updateChair(chairToAdd);
            return "redirect:/chairs/";
        }
        chairManager.addChair(chairToAdd);
        return "redirect:/chairs/";
    }

    @GetMapping("/chairs/delete/{id}")
    public String deleteChair(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        chairManager.deleteChair(objectId);

        return "redirect:/chairs/";
    }

    @GetMapping("/chairs/details-chair/{id}")
    public String viewChairDetails(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Optional<Chair> chairInDb = chairManager.getChair(objectId);
        if(chairInDb.isPresent()) {
            model.addAttribute("chair", chairInDb.get());
            model.addAttribute("chairDistributors", chairManager.getAllDistributorsByChairId(objectId));
            return "chair/details-chair";
        }
        throw new NoSuchChairInDbException("Chair is not in db");
    }

    @GetMapping("/chairs/add-distributor/{id}")
    public String viewDistributorChairForm(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Chair chair = chairManager.getChair(objectId).orElseThrow(() -> new NoSuchChairInDbException("Chair is not in db"));
        model.addAttribute("chairDistributorForm", new ChairDistributorForm(chair.getId()));
        model.addAttribute("distributors", chairManager.getAllAvailableDistributorsForChairId(objectId)
                .sorted(Comparator.comparingLong(Distributor::getId)).collect(Collectors.toList()));
        return "chair/add-chair-distributor";
    }

    @PostMapping("/chairs/add-distributor")
    public String addDistributorToChair(@ModelAttribute("chairDistributorForm") ChairDistributorForm chairForm, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("distributors", chairManager.getAllAvailableDistributorsForChairId(chairForm.getChairId())
                    .sorted(Comparator.comparingLong(Distributor::getId)).collect(Collectors.toList()));
            return "chair/add-chair-distributor";
        }
        chairManager.addDistributor(chairForm.getChairId(), chairForm.getDistributor());
        return "redirect:/chairs/details-chair/" + chairForm.getChairId();

    }

    @GetMapping("/chairs/delete-distributor/{chairId}/{distributorId}")
    public String deleteDistributorFromChair(@PathVariable("chairId") String chairId, @PathVariable("distributorId") String distId) {
        int objectId = Integer.parseInt(chairId);
        int distributorId = Integer.parseInt(distId);
        chairManager.removeDistributor(objectId, distributorId);

        return "redirect:/chairs/details-chair/" + objectId;
    }
}
