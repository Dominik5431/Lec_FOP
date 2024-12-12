package memomap;

/**
 * A {@link MemoEntry} is an entry of a {@link MemoMap}.
 * <p>
 * Such consists of<l>
 * <li>a referenced identifiable object,</li>
 * <li>a referenced memo and</li>
 * <li>a finalization state.</li></l>
 * 
 * @version 1.0
 */
public class MemoEntry {

	/**
	 * The object.
	 */
	public Identifiable object;

	/**
	 * The memo.
	 */
	public String memo;

	/**
	 * The finalization state.
	 */
	public boolean finalized;

	/**
	 * Constructs a {@link MemoEntry}.
	 */
	public MemoEntry() {
	}

	/**
	 * Constructs a {@link MemoEntry}.
	 * 
	 * @param object    the object of the entry
	 * @param memo      the memo of the entry
	 * @param finalized the finalization state of the entry
	 */
	public MemoEntry(Identifiable object, String memo, boolean finalized) {
		this.object = object;
		this.memo = memo;
		this.finalized = finalized;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (finalized ? 1231 : 1237);
		result = prime * result + ((memo == null) ? 0 : memo.hashCode());
		result = prime * result + ((object == null) ? 0 : object.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemoEntry other = (MemoEntry) obj;
		if (finalized != other.finalized)
			return false;
		if (memo == null) {
			if (other.memo != null)
				return false;
		} else if (!memo.equals(other.memo))
			return false;
		if (object == null) {
			if (other.object != null)
				return false;
		} else if (!object.equals(other.object))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "MemoEntry [object=" + object + ", memo=" + memo + ", finalized=" + finalized + "]";
	}

}
