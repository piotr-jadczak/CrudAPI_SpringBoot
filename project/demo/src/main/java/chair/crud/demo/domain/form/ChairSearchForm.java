package chair.crud.demo.domain.form;

import chair.crud.demo.domain.extension.SearchOptionT;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChairSearchForm {

    @NotNull
    private SearchOptionT searchOption;

    @NotEmpty
    private String phrase;

    // Get and Set

    public SearchOptionT getSearchOption() {
        return searchOption;
    }

    public void setSearchOption(SearchOptionT searchOption) {
        this.searchOption = searchOption;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    // Constructors

    public ChairSearchForm() {
    }
}
