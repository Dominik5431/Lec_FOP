package campus.data.domain;

import java.util.Date;
import java.util.Objects;
import java.util.Comparator;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class Exam extends Entity implements Comparable<Exam> {
    private final static Comparator<Exam> EXAM_COMPARATOR =
        Comparator.comparing(Exam::getDate)
                  .thenComparing(
                      Comparator.nullsFirst(
                          Comparator.comparing(entity ->
                              entity.getID().orElse(null))));

    private Date date;

    public Exam(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("Exam [ID=%s, date=%s]", getID().orElse(null),
                             date);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof Exam)) {
            return false;
        }

        var other = (Exam) obj;
        return Objects.equals(date, other.date);
    }

    @Override
    public int compareTo(Exam o) {
        return EXAM_COMPARATOR.compare(this, o);
    }
}
