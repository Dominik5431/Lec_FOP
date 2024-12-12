package campus.test.matcher;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class WithMessage extends TypeSafeDiagnosingMatcher<Throwable> {
    private String message;

    private WithMessage(String message) {
        this.message = message;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("with message \"%s\"", message));
    }

    @Override
    protected boolean matchesSafely(Throwable item, Description mismatchDescription) {
        if (Objects.equals(item.getMessage(), message)) {
            return true;
        } else {
            mismatchDescription.appendText(String.format("message is \"%s\"", item.getMessage()));
            return false;
        }
    }

    public static WithMessage withMessage(String message) {
        return new WithMessage(message);
    }
}
