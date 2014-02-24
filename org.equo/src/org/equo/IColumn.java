package org.equo;



public interface IColumn {
	
	public String getName();
	
	public String getAlias();
	
	public String getPeer();
	
	public Field getField();
	
	public int getIndex();
	
	public IColumn as(String alias);
	
}

class MetaColumn implements IColumn {
	
	IPeer table;
	Field field;
	int index;

	@Override
	public String getName() {
		return field.getName();
	}

	@Override
	public String getAlias() {
		return null;
	}

	@Override
	public String getPeer() {
		return null;
	}

	@Override
	public Field getField() {
		return null;
	}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public IColumn as(String alias) {
		return null;
	}
	
}

class DelegateColumn implements IColumn {
	
	IPeer peer;
	IColumn column;

	public String getName() {
		return column.getName();
	}

	public String getAlias() {
		return column.getAlias();
	}

	public String getPeer() {
		return peer.getName();
	}
	
	@Override
	public Field getField() {
		return null;
	}

	public int getIndex() {
		return column.getIndex();
	}

	public IColumn as(String alias) {
		return column.as(alias);
	}

}
