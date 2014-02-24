package org.equo;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class PeerBuilder<E extends IRecord> {

	public static PeerBuilder<Record> createPeer(Datasource ds, String name) {
		return new PeerBuilder<Record>(ds, name, Record.class);
	}
	
	public static <E extends IRecord> PeerBuilder<E> createPeer(Datasource ds, String name, Class<E> c) {
		return new PeerBuilder<E>(ds, name, c);
	}
	
	private Datasource ds;
	private String peer;
	private Class<E> entity;
	
	private final List<IColumn> columns = new ArrayList<>();

	PeerBuilder(Datasource ds, String name, Class<E> entity) {
		this.ds = ds;
		this.peer = name;
		this.entity = entity;
	}
	
	public <T> Column<E, T> addColumn(String name, Domain domain) {
		Column<IRecord, Object> c = new Column<>(peer, new Field(peer, name, domain), columns.size());
		this.columns.add(c);
		return (Column<E, T>) c;
	}
	
	public <T> Column<E, T> addColumn(String name, int type) {
		Domain domain = new Domain(name, type);
		Column<IRecord, Object> c = new Column<>(peer, new Field(peer, name, domain), columns.size());
		this.columns.add(c);
		return (Column<E, T>) c;
	}
	
	public <T> Column<E, T> addColumn(String name, int type, int length) {
		Domain domain = new Domain(name, type, length);
		Column<IRecord, Object> c = new Column<>(peer, new Field(peer, name, domain), columns.size());
		this.columns.add(c);
		return (Column<E, T>) c;
	}
	
	public <T> Column<E, T> addColumn(String name, int type, int length, int scale) {
		Domain domain = new Domain(name, type, length, scale);
		Column<IRecord, Object> c = new Column<>(peer, new Field(peer, name, domain), columns.size());
		this.columns.add(c);
		return (Column<E, T>) c;
	}
	
	public <T> Column<E, T> addColumn(String name) {
		Field field = new Field(peer, name, Domain.VARCHAR20);
		Column<E, Object> c = new Column<E, Object>(peer, field, columns.size());
		this.columns.add(c);
		return (Column<E, T>) c;
	}

	public Peer<E> getPeer() {
		EntityPeer<E> p = new EntityPeer<E>(ds, peer, entity);
		p.setColumns(columns.toArray(new IColumn[columns.size()]));
		return p;
	}
	
}
