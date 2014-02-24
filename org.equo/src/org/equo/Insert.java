package org.equo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Insert<E extends IRecord> implements ICommand {
	
	public static final <E extends IRecord> Insert<E> insert(Peer<E> master) {
		return new Insert<E>(master);
	}
	
	private Peer<E> peer;
	Collection<Object[]> values;
	
	public Insert(Peer<E> peer) {
		this.peer = peer;
		values = new ArrayList<>(8);
		// TODO session properties to customize initial array capacity
	}
	
	public Insert(Peer<E> peer, Object[] values) {
		this.peer = peer;
		this.values = Collections.singleton(values);
	}
	
	@SafeVarargs
	public final Insert<E> entities(E... entities) {
		Collection<Object[]> list = this.values;
		for (E e : entities) {
			list.add(e.values);
		}	
		return this;
	}
	
	public final Insert<E> entities(Collection<E> entities) {
		Collection<Object[]> list = this.values;
		for (E e : entities) {
			list.add(e.values);
		}	
		return this;
	}
	
	public int execute() throws DatasourceException {
		return peer.getDatasource().execute(this);
	}

	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitInsert(peer, peer.getColumns());
		visitor.visitValues(values.toArray(new Object[values.size()][]));
	}
	
	@SuppressWarnings("unchecked")
	public static final <E extends IRecord> Insert<E> wrap(E e) {
		return (Insert<E>) new Insert<>(e.getPeer(), e.values);
	}

}
