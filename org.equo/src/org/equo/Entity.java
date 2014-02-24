package org.equo;

import java.util.Arrays;

@SuppressWarnings("unchecked")
public class Entity<E extends Entity<E>> extends org.equo.AbstractEntity<E> {
	
	private Entity<?>[] foreigns;

	protected Entity(PeerTable<E> peer) {
		super(peer);
	}
	
	public PeerTable<E> getPeer() {
		return (PeerTable<E>) super.getPeer();
	}

	public <T> T getObject(TableColumn<E, T> column) {
		if (column.isForeign()) {
			return (T) getForeign0(column.getTable()).getObject(column.getIndex());
		} else {
			return (T) getObject(column.getIndex());
		}
	}
	
	public <F extends Entity<F>> F getForeign(Table<E, F> table) {
		return (F) getForeign0(table);
	}
	
	private Entity<?> getForeign0(IForeign table) {
		IForeign t = table;
		int i = 0, size = 8;
		int[] path = new int[size];
		do {
			path[i++] = t.getIndex();
			if (i == size) {
				path = Arrays.copyOf(path, size*= 2);
			}
		} while ((t = t.getParent()) != null);
		
		Entity<?> f  = this;
		for (int j = i - 1; j > 0; j--) {
			f = f.foreigns[path[j]];
			if (f == null) {
				throw new IllegalArgumentException("Unbound foreign " + "TODO");
			}
		}
		return f;
	}
	
}
 