package chair.crud.demo.controller.web;

import chair.crud.demo.domain.Specification;
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
import java.util.Optional;

@Controller
public class SpecificationController {

    private final SpecificationManager specificationManager;

    @Autowired
    public SpecificationController(SpecificationManager specificationManager) {
        this.specificationManager = specificationManager;
    }

    @GetMapping("/specifications/")
    public String viewSpecificationsPage(Model model) {
        model.addAttribute("specifications", specificationManager.getAllSpecifications());
        return "specification/all-specifications";
    }

    @GetMapping("/specifications/add-specification/{id}")
    public String viewSpecificationForm(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Optional<Specification> specInDb = specificationManager.getSpecification(objectId);
        if(specInDb.isPresent()) {
            model.addAttribute("specificationToAdd", specInDb.get());
            return "specification/add-specification";
        }
        model.addAttribute("specificationToAdd", new Specification(0L));
        return "specification/add-specification";
    }

    @PostMapping("/specifications/add-specification")
    public String addSpecification(@ModelAttribute("specificationToAdd") @Valid Specification specificationToAdd, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "specification/add-specification";
        }
        if(specificationManager.isSpecificationInDb(specificationToAdd.getId())) {
            specificationManager.updateSpecification(specificationToAdd);
            return "redirect:/specifications/";
        }
        specificationManager.addSpecification(specificationToAdd);
        return "redirect:/specifications/";
    }

    @GetMapping("/specifications/delete/{id}")
    public String deleteSpecification(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        specificationManager.deleteSpecification(objectId);

        return "redirect:/specifications/";
    }

}
