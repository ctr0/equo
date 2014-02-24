package org.equo;




public class Condition<E extends IRecord> implements Criteria<E> {
	
	private int op;
	private IColumn field;
	private IColumn value;
	private Object[] values;
	
	Condition() {}
	
	public Condition(int op, IColumn field, Object... values) {
		this.op = op;
		this.field = field;
		this.values = new Object[values.length];
		for (int i = 0; i < values.length; i++) {
			this.values[i] = values[i];
		}
	}
	
	public Condition(int op, IColumn field, IColumn value) {
		this.op = op;
		this.field = field;
		this.value = value;
	}
	
	@SafeVarargs
	public final Segment<E> or(Criteria<E>... conditions) {
		return new Segment<E>(false, conditions);
	}
	
	@SafeVarargs
	public final Segment<E> AND(Criteria<E>... conditions) {
		return new Segment<E>(true, conditions);
	}
	
	public int getOp() {
		return op;
	}
	
	public IColumn getField() {
		return field;
	}
	
	public IColumn getValueField() {
		return value;
	}
	
	public Object[] getValues() {
		return values;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.ctro.jsql.ICondition#accept(org.ctro.jsql.CriteriaVisitor)
	 */
	@Override
	public void accept(CriteriaVisitor visitor) {
		visitor.visitCondition(this);
	}

	public boolean eval(E record) {
//		switch (op) {
//			case Operator.EQ: return record.getObject(field).equals(values[0]);
//			case Operator.NE: return !record.getObject(field).equals(values[0]);
//			case Operator.GT: break;
//			case Operator.LT: break;
//			case Operator.IL: Object v1 = record.getObject(field);
//					 for (Object o : values) {
//						if (v1.equals(o)) {
//							return true;
//						}
//					 }
//					 return false;
//			case Operator.NL: Object v2 = record.getObject(field);
//					 for (Object o : values) {
//						if (v2.equals(o)) {
//							return true;
//						}
//					 }
//					 return false;
//			case Operator.NU: return values[0] == null;
//			case Operator.NN: return values[0] != null;
//			default: break;
//		}
		return false;
	}
	
	public static interface Operator {
		
		public static final int EQ = 0;
		public static final int NE = 1;
		public static final int GT = 2;
		public static final int GE = 3;
		public static final int LT = 4;
		public static final int LE = 5;
		public static final int IL = 6;
		public static final int NL = 7;
		public static final int NU = 8;
		public static final int NN = 9;
	}
	
}
