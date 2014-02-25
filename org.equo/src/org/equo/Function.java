package org.equo;

public class Function<E extends IRecord, T> extends Column<E, T> {
	
	public static final int FUNC_MAX = 0;
	public static final int FUNC_MIN = 1;
	
	private int op; 

	public Function(IColumn column, int op) {
		super(column);
		this.op = op;
	}

	public int getFunction() {
		return op;
	}
	
}
