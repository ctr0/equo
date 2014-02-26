package org.equo;


public interface Criteria<E extends IEntity> extends ICriteria {
	
	public abstract boolean eval(E record);

}
