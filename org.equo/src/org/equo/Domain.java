package org.equo;


public class Domain {
	
	public static final Domain VARCHAR20 = new Domain("VARCHAR20", Types.VARCHAR, 20);
	
	// TODO Functions
//	public static final Domain FUNC_COUNT = new Domain("COUNT()", Types.BIGINT);
	
	final int type;
	final private String name;
	final private int length;
	final private int scale;
	
	public Domain(String name, int type, int length, int scale) {
		Types.validate(type);
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("Illegal domain name");
		}
//		if (length <= 0) {
//			throw new IllegalArgumentException("Illegal domain length");
//		}
//		if (scale < 0) {
//			throw new IllegalArgumentException("Illegal domain scale");
//		}
		this.type = type;
		this.name = name;
		this.length = length;
		this.scale = scale;
	}
	
	public Domain(String name, int type, int length) {
		this(name, type, length, -1);
	}

	public Domain(String name, int type) {
		this(name, type, -1, -1);
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getScale() {
		return scale;
	}

	public int getLength() {
		return length;
	}
}
