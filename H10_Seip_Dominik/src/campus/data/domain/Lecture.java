package campus.data.domain;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class Lecture extends Entity implements Comparable<Lecture> {
    private final static Comparator<Lecture> LECTURE_COMPARATOR =
        Comparator.comparing(Lecture::getTitle)
                  .thenComparing(Comparator.nullsFirst(
                      Comparator.comparing(entity ->
                          entity.getID().orElse(null))));

    private String title;

    public Lecture(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Lecture [ID=%s, title=%s]", getID().orElse(null),
                             title);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(title);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof Lecture)) {
            return false;
        }

        return Objects.equals(title, ((Lecture) obj).title);
    }

    @Override
    public int compareTo(Lecture o) {
        return LECTURE_COMPARATOR.compare(this, o);
    }
}
