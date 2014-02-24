package org.equo;

public interface CriteriaVisitor {
	
	public void visitSegment(Segment<?> segment);
	
	public void visitCondition(Condition<?> segment);

}
