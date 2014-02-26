package org.equo;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class Session {
	
	private static final Map<String, Datasource> datasources = new HashMap<String, Datasource>(4);
	private static final Map<String, ITable> peers = new HashMap<String, ITable>(32);
	private static final Map<String, Table<?, ?>> tables = new HashMap<String, Table<?, ?>>(32);
	
	private static final Object[] NULL_TABLE_CTOR_ARGS = new Object[] {null, null};
	
	public Session() {}
	
	public static final Datasource getDatasource(String name) {
		synchronized (datasources) {
			return datasources.get(name);
		}
	}
	
	public final Datasource createDatasource(DatasourceInfo info) throws DatasourceException {
		synchronized (datasources) {
			if (datasources.containsKey(info.getName())) {
				throw new DatasourceException("Datasource already exists");
			}
			Datasource ds = createDatasource0(info);
			datasources.put(info.getName(), ds);
			return ds;
		}
	}
	
	protected abstract Datasource createDatasource0(DatasourceInfo info) 
			throws DatasourceException;

	public URL getResource(String name) {
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}
	
	public InputStream getResourceStream(String name) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
	}
	
	public void close() {
		for (Datasource ds : datasources.values()) {
			try {
				ds.close();
			} catch (DatasourceException e) {
				// TODO errors
				e.printStackTrace();
			}
		}
	}
	
	public ITable getPeer(String name) {
		ITable peer = peers.get(name);
		if (peer == null) throw new IllegalArgumentException("Cannot find peer " + name);
		return peer;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Entity<E>> PeerTable<E> getPeer(Class<E> c) {
		Table<E, E> table = null;
		try {
			table = (Table<E, E>) getPeerTableInvoker(c).invoke(null, (Object[]) null);
		} catch (Exception e) {
			throw new RuntimeException("Cannot instantiate peer table from entity class " + c.getCanonicalName(), e);
		}
		if (table.getParent() != null) {
			throw new IllegalStateException("Illegal table instance " + table);
		}
		ITable peer;
		synchronized (peers) {
			peer = peers.get(table.getName());
			if (peer == null) {
				Datasource ds = getDatasource(table.getDatasourceName());
				if (ds == null) throw new IllegalArgumentException("Unkown datasource " + table.getDatasourceName());
				peer = new PeerTable<E>(this, ds, table, c);
				peers.put(peer.getName(), peer);
			}
		}
		return (PeerTable<E>) peer;
	}
	
	private static Method getPeerTableInvoker(Class<?> c) {
		Method method = null;
		for (Method m : c.getDeclaredMethods()) {
			if (m.getAnnotation(APeerTable.class) != null) {
				if (method != null) {
					throw new IllegalStateException("Multiple @PeerTable annotations found");
				}
				method = m;
			}
		}
		if (method == null) throw new IllegalStateException("No @PeerTable annotation found");
		return method;
	}
	
//	@SuppressWarnings("unchecked")
//	public <E extends Entity<E>> PeerTable<E> getPeer(Table<?, E> table) {
//		IPeer peer;
//		synchronized (peers) {
//			peer = peers.get(table.getName());
//			if (peer == null) {
//				Datasource ds = getDatasource(table.getDatasourceName());
//				if (ds == null) throw new IllegalArgumentException("Unkown datasource " + table.getDatasourceName());
//				peer = new PeerTable<E>(this, ds, table, getEntityParameterType((Class<? extends Table<?, E>>) table.getClass()));
//				peers.put(peer.getName(), peer);
//			}
//		}
//		return (PeerView<E>) peer;
//	}
	
//	@SuppressWarnings("unchecked")
//	private <E extends Entity<E>> Class<E> getEntityParameterType(Class<? extends Table<?, E>> c) {
//		ParameterizedType pt = (ParameterizedType) c.getGenericSuperclass();
//		if (pt != null) {
//			Type[] types = pt.getActualTypeArguments();
//			if (types.length > 1) {
//				return (Class<E>) types[1];
//			}
//		}
//		throw new RuntimeException("Cannot retrieve entity class from table class " + c);
//	}

	@SuppressWarnings("unchecked")
	public static final <T extends Table<?, ?>> Table<?, ?> getTable(Class<T> c) {
		T table;
		synchronized (tables) {
			table = (T) tables.get(c.getSimpleName());
			if (table == null) {
				try {
					table = c.getConstructor(Table.class, String.class).newInstance(NULL_TABLE_CTOR_ARGS);
				} catch (Exception e) {
					throw new RuntimeException("Cannot instantiate peer table class " + c.getCanonicalName(), e);
				}
				tables.put(table.getName(), table);
			}
		}
		return table;
	}
	
	@SuppressWarnings("unchecked")
	public static final <E extends Entity<E>, F extends Entity<F>> Table<E, F> getTable(String name) {
		synchronized (tables) {
			return (Table<E, F>) tables.get(name);
		}
	}
	 
}
