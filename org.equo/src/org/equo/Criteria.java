package org.equo;


public interface Criteria<E extends IRecord> extends ICriteria {
	
	public abstract boolean eval(E record);

}
