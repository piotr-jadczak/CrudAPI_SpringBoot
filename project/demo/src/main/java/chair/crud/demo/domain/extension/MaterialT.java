package chair.crud.demo.domain.extension;

public enum MaterialT {
    SYNTHETIC("synthetic"),
    WOOD("wood"),
    LEATHER("leather"),
    PLASTIC("plastic");

    private final String value;

    MaterialT(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
