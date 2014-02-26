package org.equo;


public class Join {

	boolean inner;

	ITable left;

	ITable right;

	ICriteria criteria;

	public Join(boolean inner, ITable left, ITable right, ICriteria criteria) {
		this.inner = inner;
		this.left = left;
		this.right = right;
		this.criteria = criteria;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Join) {
			Join join = (Join) obj;
			return right.equals(join.right) && left.equals(join.left) 
					&& criteria.equals(join.criteria);
		}
		return false;
	}
}
