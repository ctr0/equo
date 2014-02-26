package org.equo;

import java.lang.reflect.Constructor;

import org.equo.util.Iterator;


public abstract class Peer<E extends IRecord> extends ITable {
	
	public abstract E createEntity();
	
	protected abstract Constructor<E> getEntityFactory();
	
	public abstract Datasource getDatasource();
	
	protected Constructor<E> getEntityConstructor(Class<E> c) {
		try {
			if (c == Record.class) return c.getConstructor(Peer.class);
			else return c.getConstructor(getClass());
		} catch (Exception e) {
			throw new IllegalStateException("Cannot retrieve entity constructor", e);
		}
	}
	
	boolean refresh(IRecord entity) throws DatasourceException {
		try (Iterator<IRecord> iterator = getDatasource().iterator(Select.wrap(entity))) {
			if (iterator.hasNext()) {
				// Only if peer columns are inmutable
				entity.values = iterator.next().getObjects();
				entity.dirty.clear();
				return true;
			} else {
				return false;
			}
		}
	}

	boolean exists(IRecord entity) throws DatasourceException {
		try (Iterator<?> iterator = getDatasource().iterator(Select.wrap(entity))) {
			return iterator.hasNext();
		}
	}

	boolean delete(IRecord entity) throws DatasourceException {
		return false;
	}

	void insert(IRecord entity) throws DatasourceException {
		getDatasource().execute(Insert.wrap(entity));
	}

	boolean update(IRecord entity) throws DatasourceException {
		return getDatasource().execute(Insert.wrap(entity)) != 0;
	}

// TODO Transaction, Version, Select for update
//	boolean save(IRecord record) throws DatasourceException {
//		//XXX select for update with $VERSION 
//		//XXX Create command before lock ?
//		ITransaction t = getDatasource().getTransaction();
//		try {
//			t.start();
//			// FIXME select for update
//			if (t.refreshForUpdate(record)) {
//				return t.execute(Update.wrap(record)) != 0;
//			} else {
//				return t.execute(Insert.wrap(record)) != 0;
//			}
//		} catch (Throwable e) {
//			t.rollback();
//			throw e;
//		} finally {
//			t.commit();
//		}
//	}

}
