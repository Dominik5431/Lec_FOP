package campus.test.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import org.junit.jupiter.api.function.Executable;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class ThrowsOfType<T> extends TypeSafeDiagnosingMatcher<Executable> {
    private Class<T> exceptionClass;
    private Matcher<? super T> matcher;

    private ThrowsOfType(Class<T> exceptionClass) {
        this(exceptionClass, null);
    }

    private ThrowsOfType(Class<T> exceptionClass, Matcher<? super T> matcher) {
        this.exceptionClass = exceptionClass;
        this.matcher = matcher;
    }

    @Override
    public void describeTo(Description description) {
        description
            .appendText("Throwable of type ")
            .appendText(exceptionClass.getName())
            .appendText(" to be thrown");
        if (matcher != null) {
            description
                .appendText(" ")
                .appendDescriptionOf(matcher);
        }
    }

    @Override
    protected boolean matchesSafely(Executable item, Description mismatchDescription) {
        try {
            item.execute();
            mismatchDescription.appendText("no Throwable was thrown");
            return false;
        } catch (Throwable ex) {
            if (exceptionClass.isInstance(ex)) {
                if (matcher != null && !matcher.matches(ex)) {
                    matcher.describeMismatch(ex, mismatchDescription);
                    return false;
                } else {
                    return true;
                }
            } else {
                mismatchDescription
                    .appendText("type of Throwable was ")
                    .appendText(ex.getClass().getName());
                return false;
            }
        }
    }

    public static <T extends Throwable> Matcher<Executable> throwsOfType(Class<T> exceptionClass) {
        return new ThrowsOfType<>(exceptionClass);
    }

    public static <T extends Throwable> Matcher<Executable> throwsOfType(Class<T> exceptionClass, Matcher<? super T> matcher) {
        return new ThrowsOfType<>(exceptionClass, matcher);
    }


}
