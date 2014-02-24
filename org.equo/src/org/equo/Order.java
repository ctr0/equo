package org.equo;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private List<Segment> segments = new ArrayList<>(8);
	
	public static class Segment {
		
		private IColumn column;
		
		private boolean asc;
		
		private Segment(IColumn column, boolean asc) {
			this.column = column;
			this.asc = asc;
		}

		public IColumn getColumn() {
			return column;
		}

		public boolean isAsc() {
			return asc;
		}
	}
	
	public Order() {}
	
	public Order(IColumn... columns) {
		this(true, columns);
	}

	public Order(boolean asc, IColumn... columns) {
		addSegments(asc, columns);
	}

	public void addSegments(boolean asc, IColumn... columns) {
		if (columns == null) throw new IllegalArgumentException("null");
		for (IColumn column : columns) {
			segments.add(new Segment(column, asc));
		}
	}
	
	public Iterable<Segment> getSegments() {
		return segments;
	}
	
	public boolean isEmpty() {
		return segments.size() == 0;
	}
}
