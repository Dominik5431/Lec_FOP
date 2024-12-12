package campus.data.domain;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class Student extends Entity implements Comparable<Student> {
    private final static Comparator<Student> STUDENT_COMPARATOR =
        Comparator.comparing(Student::getLastName)
                  .thenComparing(Student::getFirstName)
                  .thenComparing(Comparator.nullsFirst(
                      Comparator.comparing(entity ->
                          entity.getID().orElse(null))));

    private String firstName;
    private String lastName;

    public Student(
        String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("Student [ID=%s, firstName=%s, lastName=%s]",
                             getID().orElse(null), firstName, lastName);
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + Objects.hash(firstName, lastName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!super.equals(obj)) {
            return false;
        }

        if (!(obj instanceof Student)) {
            return false;
        }

        Student other = (Student) obj;
        return Objects.equals(firstName, other.firstName)
            && Objects.equals(lastName, other.lastName);
    }

    @Override
    public int compareTo(Student o) {
        return STUDENT_COMPARATOR.compare(this, o);
    }
}
