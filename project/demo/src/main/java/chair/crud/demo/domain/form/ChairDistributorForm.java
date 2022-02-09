package chair.crud.demo.domain.form;

import chair.crud.demo.domain.Distributor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ChairDistributorForm {

    @NotEmpty
    private long chairId;

    @NotNull
    private Distributor distributor;

    // Get and Set

    public long getChairId() {
        return chairId;
    }

    public void setChairId(long chairId) {
        this.chairId = chairId;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    // Constructors

    public ChairDistributorForm() {
    }

    public ChairDistributorForm(long chairId) {
        this.chairId = chairId;
    }
}
