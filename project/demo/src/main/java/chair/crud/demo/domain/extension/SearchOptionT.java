package chair.crud.demo.domain.extension;

public enum SearchOptionT {

    DESTINATION("destination"),
    MATERIAL("material"),
    WEIGTH_LESS("weigth less"),
    MANUFACTURER_HQ_COUNTRY("manufacturer headquarters country"),
    DISTRIBUTOR_COUNTRY_OF_OPERATION("distributor country of operation");

    private final String value;

    SearchOptionT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
