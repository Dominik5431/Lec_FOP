package collections;

import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * An item used for linked lists and linked sets.
 *
 * @param <T> the type of the key
 */
public class MyItem<T> {

	public final T key;
	public MyItem<T> next;

	/**
	 * Constructs a {@link MyItem} for the given key.
	 * @param key the key
	 */
	public MyItem(T key) {
		this(key, null);
	}

	/**
	 * Constructs a {@link MyItem} for the given key
	 * and links the given item as the next item.
	 * @param key  the key
	 * @param next the next item
	 */
	public MyItem(T key, MyItem<T> next) {
		this.key = key;
		this.next = next;
	}

	/**
	 * Returns the key.
	 * @return the key
	 */
	public T getKey() {
		return key;
	}

	/**
	 * Links the given item as the next item and returns this item.
	 * @param  next the next item
	 * @return      this item
	 */
	public MyItem<T> setNext(MyItem<T> next) {
		this.next = next;
		return this;
	}

	/**
	 * Returns the item that is linked as the next item.
	 * @return the next item
	 */
	public MyItem<T> getNext() {
		return next;
	}

	/**
	 * <b>You are not allowed to use this method to solve exercises H1 - H4!</b><br>
	 * <br>
	 * Sets the given item as the next item of the last item of this item cascade.
	 * @param  item the item
	 * @return      this item
	 */
	private MyItem<T> concat(MyItem<T> item) {
		if (next != null)
			next.concat(item);
		else
			next = item;
		return this;
	}

	/**
	 * <b>You are not allowed to use this method to solve exercises H1-H4! </b><br>
	 * <br>
	 * Constructs and returns a clone of this item cascade.
	 * @return the clone
	 */
	public MyItem<T> cloneRecursively() {
		return new MyItem<>(key, next != null ? next.cloneRecursively() : null);
	}

	/**
	 * <b>You are not allowed to use this method to solve exercises H1 - H4!</b><br>
	 * <br>
	 * Returns a sequential stream with this item cascade as its source.
	 * @return the stream
	 */
	public Stream<T> stream() {
		return Stream.iterate(this, Objects::nonNull, MyItem::getNext).map(MyItem::getKey);
	}

	public static final class Collectors {

		/**
		 * <b>You are not allowed to use this method to solve exercises H1 - H4!</b><br>
		 * <br>
		 * Returns a collector that accumulates the elements into a new item cascade.
		 * @see java.util.stream.Collector
		 */
		public static <T extends Comparable<? super T>> Collector<T, MyItem<T>, MyItem<T>> toItemCascade() {
			return Collector.of(() -> new MyItem<>(null), (item, element) -> item.concat(new MyItem<>(element)), MyItem::concat, MyItem::getNext);
		}

	}

}
