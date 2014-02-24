package org.equo;

public interface IPeer {
	
	public String getName();
	
	public String getAlias();
	
	public IColumn[] getColumns();
	
	public int getColumnsCount();

	public IColumn[] getKeys();
	
	public IColumn getColumn(String alias);

}
