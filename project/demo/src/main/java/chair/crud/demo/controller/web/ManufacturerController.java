package chair.crud.demo.controller.web;

import chair.crud.demo.domain.Manufacturer;
import chair.crud.demo.service.ManufacturerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ManufacturerController {

    private final ManufacturerManager manufacturerManager;

    @Autowired
    public ManufacturerController(ManufacturerManager manufacturerManager) {
        this.manufacturerManager = manufacturerManager;
    }

    @GetMapping("/manufacturers/")
    public String viewManufacturersPage(Model model) {
        model.addAttribute("manufacturers", manufacturerManager.getAllManufacturers());
        return "manufacturer/all-manufacturers";
    }

    @GetMapping("/manufacturers/add-manufacturer/{id}")
    public String viewManufacturerForm(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Optional<Manufacturer> manufacturerInDb = manufacturerManager.getManufacturer(objectId);
        if(manufacturerInDb.isPresent()) {
            model.addAttribute("manufacturerToAdd", manufacturerInDb.get());
            return "manufacturer/add-manufacturer";
        }
        model.addAttribute("manufacturerToAdd", new Manufacturer(0L));
        return "manufacturer/add-manufacturer";
    }

    @PostMapping("/manufacturers/add-manufacturer")
    public String addManufacturer(@ModelAttribute("manufacturerToAdd") @Valid Manufacturer manufacturerToAdd, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "manufacturer/add-manufacturer";
        }
        if(manufacturerManager.isManufacturerInDb(manufacturerToAdd.getId())) {
            manufacturerManager.updateManufacturer(manufacturerToAdd);
            return "redirect:/manufacturers/";
        }
        manufacturerManager.addManufacturer(manufacturerToAdd);
        return "redirect:/manufacturers/";
    }

    @GetMapping("/manufacturers/delete/{id}")
    public String deleteManufacturer(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        manufacturerManager.deleteManufacturer(objectId);

        return "redirect:/manufacturers/";
    }
}
