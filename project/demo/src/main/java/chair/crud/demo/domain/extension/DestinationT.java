package chair.crud.demo.domain.extension;

public enum DestinationT {
    OFFICE("office"),
    KITCHEN("kitchen"),
    BAR("bar");

    private final String value;

    DestinationT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
