package collections;

/**
 * A generic construct used for linked lists.
 *
 * @param <T> the type of keys
 */
public class MyList<T> {

	public MyItem<T> head;

	/**
	 * Constructs an empty {@link MyList}.
	 */
	public MyList() {
		this(null);
	}

	/**
	 * Constructs a {@link MyList} using the given item as the head item.
	 * @param head the head item
	 */
	public MyList(MyItem<T> head) {
		this.head = head;
	}

	/**
	 * Sets the head item of this list.
	 * @param head
	 */
	public void setHead(MyItem<T> head) {
		this.head = head;
	}

	/**
	 * Returns the head item of this list.
	 * @return
	 */
	public MyItem<T> getHead() {
		return head;
	}

	// Feel free to add further methods here!

}
