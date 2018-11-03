package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.event.exceptions.DuplicateEventException;
import seedu.address.model.event.exceptions.EventNotFoundException;

/**
 * A list of events that enforces uniqueness between its elements and does not allow nulls.
 * An event is considered unique by comparing using {@code PEvent#isSameEvent(Event)}. As such, adding of
 * events uses PEvent#isSameEvent(Event) for equality so as to ensure that the event being added is
 * unique in terms of identity in the UniqueEventList. However, the removal of an event uses Event#equals(Object) so
 * as to ensure that the event with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Event#isSameEvent(Event)
 */
public class UniqueEventList implements Iterable<Event> {

    private final ObservableList<Event> internalList = FXCollections.observableArrayList();

    /**
     * Returns true if the list contains an equivalent student as the given argument.
     */
    public boolean contains(Event toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEvent);
    }

    /**
     * Adds an event to the list.
     * The event must not already exist in the list.
     */
    public void add(Event toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(toAdd);
        internalList.sort(Event.COMPARATOR);
    }

    /**
     * Removes the equivalent student from the list.
     * The student must exist in the list.
     */
    public void remove(Event toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new EventNotFoundException();
        }
    }

    public void setEvents(UniqueEventList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code events}.
     * (@code events) must not contain duplicate events.
     */
    public void setEvents(List<Event> events) {
        requireAllNonNull(events);
        if (!eventsAreUnique(events)) {
            throw new DuplicateEventException();
        }

        internalList.setAll(events);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Event> asUnmodifiableObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEventList // instanceof handles nulls
                && internalList.equals(((UniqueEventList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    /**
     * Returns true if {@code events} contains only unique students.
     */
    private boolean eventsAreUnique(List<Event> events) {
        for (int i = 0; i < events.size() - 1; i++) {
            for (int j = i + 1; j < events.size(); j++) {
                if (events.get(i).isSameEvent(events.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
