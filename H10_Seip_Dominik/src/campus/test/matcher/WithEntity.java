package campus.test.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import campus.data.domain.Entity;
import campus.data.repository.NonPersistentContextException;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public final class WithEntity extends TypeSafeDiagnosingMatcher<NonPersistentContextException> {
    private Entity entity;

    private WithEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(String.format("with entity \"%s\"", entity));
    }

    @Override
    protected boolean matchesSafely(NonPersistentContextException item, Description mismatchDescription) {
        if (item.getEntity().equals(entity)) {
            return true;
        } else {
            mismatchDescription.appendText(String.format(
                "entity is \"%s\"", item.getEntity()));
            return false;
        }
    }

    public static WithEntity withEntity(Entity entity) {
        return new WithEntity(entity);
    }
}
