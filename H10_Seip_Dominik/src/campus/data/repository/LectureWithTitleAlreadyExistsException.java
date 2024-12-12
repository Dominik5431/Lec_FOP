package campus.data.repository;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class LectureWithTitleAlreadyExistsException extends RuntimeException {
    private static final long serialVersionUID = -9215507382333131540L;

    private final String title;

    public LectureWithTitleAlreadyExistsException(String title) {
        super(String.format("Lecture with title %s already exists", title));
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
