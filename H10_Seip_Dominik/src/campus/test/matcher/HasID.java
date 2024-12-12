package campus.test.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import campus.data.domain.Entity;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class HasID<E extends Entity> extends TypeSafeDiagnosingMatcher<E> {
    private long id;

    private HasID(long id) {
        this.id = id;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("entity to have ID ").appendValue(id);
    }

    @Override
    protected boolean matchesSafely(E item, Description mismatchDescription) {
        if (item.getID().isPresent()) {
            if (item.getID().get() == id) {
                return true;
            } else {
                mismatchDescription.appendText("ID was ")
                    .appendValue(item.getID().get());
                return false;
            }
        } else {
            mismatchDescription.appendText("entity does not have an ID");
            return false;
        }
    }

    public static <E extends Entity> Matcher<E> hasID(long id) {
        return new HasID<E>(id);
    }
}
