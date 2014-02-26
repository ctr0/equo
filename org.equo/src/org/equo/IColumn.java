package org.equo;



public interface IColumn {
	
	public String getName();
	
	public String getAlias();
	
	public ITable getTable();
	
	public Field getField();
	
	public int getIndex();
	
	public IColumn as(String alias);
	
}
