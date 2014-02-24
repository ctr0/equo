package org.equo;



public interface IForeign extends IPeer {

	//public ITable getForeign(String alias);

	public int getIndex();

	public IForeign getParent();
	
	public String getName();
	
	public String getAlias();
	
	public String getDatasourceName();
}
