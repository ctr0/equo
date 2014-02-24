package org.equo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.equo.AbstractEntity;
import org.equo.DatasourceException;
import org.equo.IColumn;

/**
 * <p>Taking a source {@link DatasourceIterator} this class performs a <b>group by</b> operation 
 * based on the parameterized key. 
 * <p>The iterator elements are arrays of entities than can be processed as a whole groups.
 * <p>The source iterator <b>must be ordered according to the given key</b> to ensure correct functionality.
 * 
 * @author Jordi Carretero
 * @version 1.0
 */
public class KeyIterator<E extends AbstractEntity<E>> implements Iterator<List<E>> {
	
	private Iterator<E> iterator;
	private IColumn[] keys;
	private Object[] prevKey;
	private Object[] currKey;
	private E last;
	private Boolean hasNext;

	/**
	 * @param iterator The data source iterator
	 * @param keys The indexes 
	 */
	public KeyIterator(Iterator<E> iterator, IColumn... keys) {
		this.iterator = iterator;
		this.keys = keys;
	}

	@Override
	public boolean hasNext() {
		try {
			return hasNext0() || last != null;
		} catch (DatasourceException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public final List<E> next() {
		try {
			List<E> nextGroup = new ArrayList<E>();
			if (last != null) {
				nextGroup.add(last);
			}
			while (hasNext0()) {
				last = next0();
				currKey = last.getObjects(keys);
				if (prevKey == null) {
					prevKey = currKey;
				}
				if (Arrays.equals(prevKey, currKey)) {
					nextGroup.add(last);
				} else {
					prevKey = currKey;
					return nextGroup;
				}
			}
			last = null;
			return nextGroup;
		} catch (DatasourceException e) {
			throw new IllegalStateException(e);
		}
	}
	
//	public final <F extends Entity<F>> List<F> next(Foreign<E, F> foreign) {
//		try {
//			List<F> nextGroup = new ArrayList<F>();
//			if (last != null) {
//				nextGroup.add(last.get(foreign));
//			}
//			while (hasNext0()) {
//				last = next0();
//				currKey = last.getValues(keys);
//				if (prevKey == null) {
//					prevKey = currKey;
//				}
//				if (Arrays.equals(prevKey, currKey)) {
//					nextGroup.add(last.get(foreign));
//				} else {
//					prevKey = currKey;
//					return nextGroup;
//				}
//			}
//			last = null;
//			return nextGroup;
//		} catch (DatasourceException e) {
//			throw new IllegalStateException(e);
//		}
//	}
	
	public E last() {
		return last;
	}
	
	private boolean hasNext0() throws DatasourceException {
		if (hasNext != null) {
			return hasNext;
		}
		return hasNext = iterator.hasNext();
	}

	private E next0() throws DatasourceException {
		hasNext = null;
		return iterator.next();
	}

	@Override
	public void close() {
		iterator.close();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
