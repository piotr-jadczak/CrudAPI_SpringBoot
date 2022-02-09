package chair.crud.demo.controller.web;

import chair.crud.demo.domain.form.ChairSearchForm;
import chair.crud.demo.service.ChairManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SearchController {

    private final ChairManager chairManager;

    @Autowired
    public SearchController(ChairManager chairManager) {
        this.chairManager = chairManager;
    }

    @GetMapping("/search/")
    public String viewSearchForm(Model model) {

        model.addAttribute("searchForm", new ChairSearchForm());
        return "chair/search-chair";
    }

    @PostMapping("/search")
    public String searchChair(@ModelAttribute("searchForm") @Valid ChairSearchForm searchForm, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            return "chair/search-chair";
        }
        model.addAttribute("chairs", chairManager.getAllChairsBy(searchForm.getSearchOption(), searchForm.getPhrase()));
        return "chair/search-result";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException exc, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("exceptionMessage",
                "entered phrase is incorrect");
        return "redirect:/search/";
    }
}
