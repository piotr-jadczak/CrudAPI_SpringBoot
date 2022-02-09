package chair.crud.demo.controller.web;

import chair.crud.demo.domain.Distributor;
import chair.crud.demo.exceptions.NoSuchDistributorInDbException;
import chair.crud.demo.service.DistributorManager;
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
public class DistributorController {

    private final DistributorManager distributorManager;

    @Autowired
    public DistributorController(DistributorManager distributorManager) {
        this.distributorManager = distributorManager;
    }

    @GetMapping("/distributors/")
    public String viewDistributorsPage(Model model) {
        model.addAttribute("distributors", distributorManager.getAllDistributors());
        return "distributor/all-distributors";
    }

    @GetMapping("/distributors/add-distributor/{id}")
    public String viewDistributorPage(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Optional<Distributor> distributorInDb = distributorManager.getDistributor(objectId);
        if(distributorInDb.isPresent()) {
            model.addAttribute("distributorToAdd", distributorInDb.get());
            return "distributor/add-distributor";
        }
        model.addAttribute("distributorToAdd", new Distributor(0L));
        return "distributor/add-distributor";
    }

    @PostMapping("/distributors/add-distributor")
    public String addDistributor(@ModelAttribute("distributorToAdd") @Valid Distributor distributorToAdd, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "distributor/add-distributor";
        }
        if(distributorManager.isDistributorInDb(distributorToAdd.getId())) {
            distributorManager.updateDistributor(distributorToAdd);
            return "redirect:/distributors/";
        }
        distributorManager.addDistributor(distributorToAdd);
        return "redirect:/distributors/";
    }

    @GetMapping("/distributors/delete/{id}")
    public String deleteDistributor(@PathVariable("id") String id) {
        int objectId = Integer.parseInt(id);
        distributorManager.deleteDistributor(objectId);

        return "redirect:/distributors/";
    }

    @GetMapping("/distributors/details-distributor/{id}")
    public String viewDistributorDetails(@PathVariable("id") String id, Model model) {
        int objectId = Integer.parseInt(id);
        Optional<Distributor> distributorInDb = distributorManager.getDistributor(objectId);
        if(distributorInDb.isPresent()) {
            model.addAttribute("distributor", distributorInDb.get());
            model.addAttribute("distributorChairs", distributorManager.getAllChairsByDistributorId(objectId));
            return "distributor/details-distributor";
        }
        throw new NoSuchDistributorInDbException("Distributor is not in db");
    }
}
