package org.equo.util;

public abstract class BitArray {
	
	public static BitArray create(int size) {
		if (size > 64) {
			return new BitArrayList(size);
		} else if (size > 32){
			return new BitArray64();
		} else {
			return new BitArray32();
		}
	}
	
	public abstract void set(int index);
	
	public abstract void unset(int index);
	
	public abstract boolean get(int index);
	
	public abstract boolean any();
	
	public abstract void clear();
	
	private static class BitArray32 extends BitArray {
		
		private int data;

		@Override
		public void set(int index) {
			checkRange32(index);
			data |= 1 << index;
		}

		@Override
		public void unset(int index) {
			checkRange32(index);
			data &= ~(1 << index);
		}

		@Override
		public boolean get(int index) {
			checkRange32(index);
			return (data & (1 << index)) != 0;
		}

		@Override
		public boolean any() {
			return data != 0;
		}

		@Override
		public void clear() {
			data = 0;
		}
	}
	
	private static class BitArray64 extends BitArray {
		
		private long data;

		@Override
		public void set(int index) {
			checkRange64(index);
			data |= 1L << index;
		}

		@Override
		public void unset(int index) {
			checkRange32(index);
			data &= ~(1L << index);
		}

		@Override
		public boolean get(int index) {
			checkRange32(index);
			return (data & (1L << index)) != 0;
		}

		@Override
		public boolean any() {
			return data != 0;
		}
		
		@Override
		public void clear() {
			data = 0;
		}
	}
	
	private static class BitArrayList extends BitArray {
		
		private long[] data;
		private int size;
		
		BitArrayList(int size) {
			data = new long[this.size = size];
		}

		@Override
		public void set(int index) {
			checkRange(index, size);
			data[index / 64] |= 1 << (index % 64);
		}
		
		@Override
		public void unset(int index) {
			checkRange(index, size);
			data[index / 64] &= ~(1 << (index % 64));
		}

		@Override
		public boolean get(int index) {
			checkRange(index, size);
			return (data[index / 64] & (1 << (index % 64))) != 0;
		}
		
		@Override
		public boolean any() {
			long[] data = this.data;
			for (int i = 0; i < data.length; i++) {
				if (data[i] != 0) return true;
			}
			return false;
		}
		
		@Override
		public void clear() {
			long[] data = this.data;
			for (int i = 0; i < data.length; i++) {
				data[i] = 0;
			}
		}
	}
	
	private static void checkRange32(int index) {
		if (index < 0 || index >= 32) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}
	
	private static void checkRange64(int index) {
		if (index < 0 || index >= 64) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}
	
	private static void checkRange(int index, int size) {
		if (index < 0 || index >= size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}
}
