package campus.data.domain;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public enum Grade {
    GRADE10(1.0), GRADE13(1.3), GRADE17(1.7),
    GRADE20(2.0), GRADE23(2.3), GRADE27(2.7),
    GRADE30(3.0), GRADE33(3.3), GRADE37(3.7),
    GRADE40(4.0), GRADE50(5.0);

    private final double value;

    Grade(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%.1f", value);
    }
}
