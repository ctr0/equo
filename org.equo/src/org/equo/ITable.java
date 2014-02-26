package org.equo;

public abstract class ITable {
	
	public abstract String getName();
	
	public abstract String getAlias();
	
	public abstract IColumn[] getColumns();
	
	public abstract int getColumnsCount();

	public abstract IColumn[] getKeys();
	
	public abstract IColumn getColumn(String alias);
	
	abstract int getIndex();

	abstract ITable getParent();
	
	abstract String[] getForeignPath();
	
	abstract String getDatasourceName();

}
