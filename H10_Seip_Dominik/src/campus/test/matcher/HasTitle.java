package campus.test.matcher;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import campus.data.domain.Lecture;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class HasTitle extends TypeSafeDiagnosingMatcher<Lecture> {
    private String title;

    private HasTitle(String title) {
        this.title = title;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("lecture to have title ").appendValue(title);
    }

    @Override
    protected boolean matchesSafely(Lecture item, Description mismatchDescription) {
        if (item.getTitle().equals(title)) {
            return true;
        } else {
            mismatchDescription.appendText("title was ")
                .appendValue(item.getTitle());
            return false;
        }
    }

    public static Matcher<Lecture> hasTitle(String title) {
        return new HasTitle(title);
    }
}
