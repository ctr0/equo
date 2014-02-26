package org.equo;



public interface IColumn {
	
	public static final int NO_FUNC = -1;
	public static final int FUNC_MAX = 0;
	public static final int FUNC_MIN = 1;
	
	public String getName();
	
	public String getAlias();
	
	public ITable getTable();
	
	public Field getField();
	
	public int getIndex();

	public boolean isFunction();
	
	public int getFunction();
	
	public IColumn as(String alias);
	
}
