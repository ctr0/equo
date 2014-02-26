package org.equo;



public class View<E extends IRecord> extends EntityPeer<E> {
	
	View(Datasource ds) {
		super(ds, null, null);
	}
	
	View(Datasource ds, String name, Class<E> entity) {
		super(ds, name, entity);
	}
	
	View(Datasource ds, String name, Class<E> entity, IColumn[] columns) {
		super(ds, name, entity);
		setColumns(columns);
	}
	
	// TODO select without peers
//	View(Datasource ds, String name, Class<E> entity, String[] columns) {
//		super(ds, name, entity);
//		setColumns(Parser.parseColumnsAndAlias(this, columns));
//	}
	
	View(Peer<E> peer) {
		super(peer);
		setColumns(peer.getColumns());
	}

	@SafeVarargs
	public View(Peer<E> peer, IColumn... columns) {
		super(peer);
		setColumns(columns);
	}

	// XXX modifies array !!!
	protected void setColumns(IColumn... columns) {
		int i =0;
		for (IColumn column : columns) {
			Field field = column.getField();
			columns[i] = Column.createColumn(column.getTable(), field, i++);
		}
		setColumns(columns);
	}

	
	public Join[] getJoin(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
