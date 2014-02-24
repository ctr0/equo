package org.equo;

public interface ICriteria {
	
	public void accept(CriteriaVisitor visitor);

	public boolean isEmpty();

}
