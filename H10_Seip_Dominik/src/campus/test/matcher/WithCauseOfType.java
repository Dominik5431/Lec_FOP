package campus.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class WithCauseOfType extends TypeSafeDiagnosingMatcher<Throwable> {
    private Class<? extends Throwable> type;

    private WithCauseOfType(Class<? extends Throwable> type) {
        this.type = type;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format(
            "with cause of type \"%s\"", type.getName()));
    }

    @Override
    protected boolean matchesSafely(Throwable item, Description mismatchDescription) {
        if (type.isInstance(item.getCause())) {
            return true;
        } else {
            mismatchDescription.appendText(String.format(
                "type of cause is \"%s\"", item.getClass()));
            return false;
        }
    }

    public static WithCauseOfType withCauseOfType(Class<? extends Throwable> type) {
        return new WithCauseOfType(type);
    }
}
