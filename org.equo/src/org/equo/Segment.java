package org.equo;

import java.util.ArrayList;
import java.util.List;


public class Segment<E extends IRecord> implements Criteria<E> {

	private List<ICriteria> conditions;
	private boolean and;
	
	public Segment() {
		this(true);
	}
	
	public Segment(boolean and) {
		this.and = and;
		this.conditions = new ArrayList<ICriteria>();
	}

	@SafeVarargs
	protected Segment(boolean and, Criteria<E>... criterias) {
		this.and = and;
		this.conditions = new ArrayList<ICriteria>();
		add(criterias);
	}
	
	@SafeVarargs
	public final void add(ICriteria... criterias) {
		for (ICriteria criteria : criterias) {
			this.conditions.add(criteria);
		}
	}
	
	public boolean isAnd() {
		return and;
	}
	
	public List<ICriteria> getConditions() {
		return conditions;
	}
	
	@Override
	public void accept(CriteriaVisitor visitor) {
		visitor.visitSegment(this);
	}
	
	public boolean isEmpty() {
		return conditions.size() == 0;
	}

	@Override
	public boolean eval(E entity) {
//		boolean eval = and;
//		if (eval) {
//			for (Criteria<E> condition : conditions) {
//				if(!condition.eval(entity))
//					return false;
//			}
//		} else if (conditions.size() != 0) {
//			for (Criteria<E> condition : conditions) {
//				if (condition.eval(entity))
//					return true;
//			}
//		}
//		return eval;
		return false;
	}

}
