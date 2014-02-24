package org.equo;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


/**
 * Definition of supported types.
 *
 */
public class Types {
	
    /**
     * Unknown types.
     */
	public static final int UNKNOWN = Integer.MIN_VALUE;
	
    /**
     * Type NULL. The corresponding JDBC type is <code>java.sql.Types.NULL</code>
     */
	public static final int NULL = java.sql.Types.NULL;
    
    /**
     * Type BIT. Values of type BIT are mapped to 
     * <code>java.lang.BooleanDecimal</code> and the corresponding
     * JDBC type is <code>java.sql.Types.BIT</code>
     */
	public static final int BIT = java.sql.Types.BIT;
	
    /**
     * Type DECIMAL. Values of type DECIMAL are mapped to 
     * <code>java.math.BigDecimal</code> and the corresponding
     * JDBC type is <code>java.sql.Types.DECIMAL</code>
     */
	public static final int DECIMAL = java.sql.Types.DECIMAL;
	
    /**
     * Type TINYINT. Values of type TINYINT are mapped to
     * <code>byte</code> and the corresponding JDBC type
     * is <code>java.sql.Types.TINYINT</code>
     */
	public static final int TINYINT = java.sql.Types.TINYINT;
	
    /**
     * Type SMALLINT. Values of type SMALLINT are mapped to
     * <code>short</code> and the corresponding JDBC type
     * is <code>java.sql.Types.SMALLINT</code>
     */
	public static final int SMALLINT = java.sql.Types.SMALLINT;
	
    /**
     * Type BIGINT. Values of type BIGINT are mapped to
     * <code>long</code> and the corresponding JDBC type
     * is <code>java.sql.Types.BIGINT</code>
     */
	public static final int BIGINT = java.sql.Types.BIGINT;
	
    /**
     * Type INTEGER. Values of type INTEGER are mapped to
     * <code>int</code> and the corresponding JDBC type
     * is <code>java.sql.Types.INTEGER</code>
     */
	public static final int INTEGER = java.sql.Types.INTEGER;
	
    /** 
     * Type FLOAT. Values of type FLOAT are mapped to
     * <code>float</code> and the corresponding JDBC type
     * is <code>java.sql.Types.FLOAT</code>
     */
	public static final int FLOAT = java.sql.Types.FLOAT;
	
    /** 
     * Type DOUBLE. Values of type DOUBLE are mapped to
     * <code>double</code> and the corresponding JDBC type
     * is <code>java.sql.Types.DOUBLE</code>
     */
	public static final int DOUBLE = java.sql.Types.DOUBLE;
	
    /**
     * Type DATE. Values of type DATE are mapped to
     * <code>Date</code> and the corresponding JDBC type
     * is <code>java.sql.Types.DATE</code>
     */
	public static final int DATE = java.sql.Types.DATE;
	
    /**
     * Type TIME. Values of type TIME are mapped to
     * <code>Time</code> and the corresponding JDBC type
     * is <code>java.sql.Types.TIME</code>
     */
	public static final int TIME = java.sql.Types.TIME;
	
    /**
     * Type TIMESTAMP. Values of type TIMESTAMP are mapped to
     * <code>Timestamp</code> and the corresponding JDBC type
     * is <code>java.sql.Types.TIMESTAMP</code>
     */
	public static final int TIMESTAMP = java.sql.Types.TIMESTAMP;
	
    /**
     * Type CHAR. Values of type CHAR are mapped to
     * <code>java.lang.String</code> and the corresponding JDBC type
     * is <code>java.sql.Types.CHAR</code>
     */
	public static final int CHAR = java.sql.Types.CHAR;
	
    /**
     * Type VARCHAR. Values of type VARCHAR are mapped to
     * <code>java.lang.String</code> and the corresponding JDBC type
     * is <code>java.sql.Types.VARCHAR</code>
     */
	public static final int VARCHAR = java.sql.Types.VARCHAR;
	
    /**
     * Type LONGVARCHAR. Values of type LONGVARCHAR are mapped to
     * <code>java.lang.String</code> and the corresponding JDBC type
     * is <code>java.sql.Types.LONGVARCHAR</code>
     */
	public static final int LONGVARCHAR = java.sql.Types.LONGVARCHAR;
	
    /**
     * Type BINARY. Values of type BINARY are mapped to
     * <code>byte[]</code> and the corresponding JDBC type
     * is <code>java.sql.Types.BINARY</code>
     */
	public static final int BINARY = java.sql.Types.BINARY;
	
    /**
     * Type VARBINARY. Values of type VARBINARY are mapped to
     * <code>byte[]</code> and the corresponding JDBC type
     * is <code>java.sql.Types.VARBINARY</code>
     */
	public static final int VARBINARY = java.sql.Types.VARBINARY;
	
    /**
     * Type LONGVARBINARY. Values of type LONGVARBINARY are mapped to
     * <code>byte[]</code> and the corresponding JDBC type
     * is <code>java.sql.Types.LONGVARBINARY</code>
     */
	public static final int LONGVARBINARY = java.sql.Types.LONGVARBINARY;

    /**
     * Type OBJECT. Values of type OBJECT are mapped to
     * <code>Object</code> and the corresponding JDBC type
     * is <code>java.sql.Types.JAVA_OBJECT</code>
     */
	public static final int OBJECT = java.sql.Types.JAVA_OBJECT;
	
	private Types() {}
	
	/**
	 * Check if this type is a binary type. Types
	 * <p>
	 * <table>
	 * <tr><td>BINARY</td></tr>
	 * <tr><td>VARBINARY</td></tr>
	 * <tr><td>LONGVARBINARY</td></tr>
	 * </table>
	 * <p>
	 * are considered to be binaries and are mapped to byte arrays.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isBinary(int type) {
		switch (type) {
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				return true;
		}
		return false;		
	}
	
	/**
	 * Check if the argument type is the BIT type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isBit(int type) {
        return (type == Types.BIT);
	}
	
	/**
	 * Check if the argument type is the null type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isNull(int type) {
        return (type == Types.NULL);
	}
	
	/**
	 * Check if the argument type is a number type. Types
	 * <p>
	 * <table>
	 * <tr><td>DECIMAL</td></tr>
	 * <tr><td>TINYINT</td></tr>
	 * <tr><td>SMALLINT</td></tr>
	 * <tr><td>INTEGER</td></tr>
	 * <tr><td>BIGINT</td></tr>
	 * <tr><td>FLOAT</td></tr>
	 * <tr><td>DOUBLE</td></tr>
	 * </table>
	 * <p>
	 * are considered to be numbers.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isNumber(int type) {
		switch (type) {
			case Types.DECIMAL:
			case Types.TINYINT:
			case Types.SMALLINT:
			case Types.INTEGER:
			case Types.BIGINT:
			case Types.FLOAT:
			case Types.DOUBLE:
				return true;
		}
		return false;
	}
	
	/**
	 * Check if this is a floating point type.
	 * <p>
	 * @param type The type to check.
	 * @return A boolean.
	 */
	public static boolean isFloatingPoint(int type) {
		switch (type) {
		case Types.FLOAT:
		case Types.DOUBLE:
			return true;
	}
	return false;
		
	}
	
	/**
	 * Check if the argument type is a decimal type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isDecimal(int type) {
        return (type == Types.DECIMAL);
	}
	
	/**
	 * Check if the argument type is mapped to a <code>java.lang.String</code>.
	 * Values of types
	 * <p>
	 * <table>
	 * <tr><td>CHAR</td></tr>
	 * <tr><td>VARCHAR</td></tr>
	 * <tr><td>LONGVARCHAR</td></tr>
	 * </table>
	 * <p>
	 * are mapped to strings.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isString(int type) {
		switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				return true;
		}
		return false;
	}
	
	/**
	 * Check if the argument type is a Date type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isDate(int type) {
        return (type == Types.DATE);
	}
	
	/**
	 * Check if the argument type is a Time type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isTime(int type) {
        return (type == Types.TIME);
	}
	
	/**
	 * Check if the argument type is a Timestamp type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isTimestamp(int type) {
        return (type == Types.TIMESTAMP);
	}

	/**
	 * Checks if the argument type is an Object type.
	 * <p>
	 * @param type An integer denoting the type
	 * @return A boolean
	 */
	public static boolean isObject(int type) {
        return (type == Types.OBJECT);
	}
	
	/**
	 * Convert a type to its string representation.
	 * @return The type string name.
	 * <p>
	 * @param type The type.
	 */
	public static String toString(int type) {
		switch (type) {
		case NULL:			return "NULL";
		case BIT:			return "BIT";
		case DECIMAL:		return "DECIMAL";
		case TINYINT:		return "TINYINT";
		case SMALLINT:		return "SMALLINT";
		case BIGINT:		return "BIGINT";
		case INTEGER:		return "INTEGER";
		case FLOAT:			return "FLOAT";
		case DOUBLE:		return "DOUBLE";
		case DATE:			return "DATE";
		case TIME:			return "TIME";
		case TIMESTAMP:		return "TIMESTAMP";
		case CHAR:			return "CHAR";
		case LONGVARCHAR:	return "LONGVARCHAR";
		case VARCHAR:		return "VARCHAR";
		case BINARY:		return "BINARY";
		case VARBINARY:		return "VARBINARY";
		case LONGVARBINARY:	return "LONGVARBINARY";
		case OBJECT:		return "OBJECT";
		}
		return "UNKNOWN";
	}
	
	/** 
	 * Converts a type to the java type. 
	 * @param type The type.
	 * @return The java type.
	 */
	public static String toJavaType(int type) {
		switch (type) {
		case BIT:			return "Boolean";
		case DECIMAL:		return "BigDecimal";
		case TINYINT:		return "Byte";
		case SMALLINT:		return "Short";
		case BIGINT:		return "Long";
		case INTEGER:		return "Integer";
		case FLOAT:			return "Float";
		case DOUBLE:		return "Double";
		case DATE:			return "Date";
		case TIME:			return "Time";
		case TIMESTAMP:		return "Timestamp";
		case CHAR:			return "String";
		case LONGVARCHAR:	return "String";
		case VARCHAR:		return "String";
		case BINARY:		return "byte[]"; // FIXME wrapper class
		case VARBINARY:		return "byte[]"; // FIXME wrapper class
		case LONGVARBINARY:	return "byte[]"; // FIXME wrapper class
		case OBJECT:		return "Object";
		}
		throw new IllegalArgumentException("Invalid type "+toString(type)+" to be converted to a Java type.");
	}
	
	/** 
	 * Converts a type to the java type. 
	 * @param type The type.
	 * @return The java type.
	 */
	public static String toJavaType(String type) {
		return toJavaType(toType(type));
	}
	
	public static int getType(Object value) {
		if (value instanceof Boolean) {
			return BIT;
		} else if (value instanceof BigDecimal) {
			return DECIMAL;
		} else {
			return OBJECT;
		}
	}
	
	/**
	 * Convert a string representation to its type.
	 * <p>
	 * @return The type..
	 * @param name The type string name.
	 */
	public static int toType(String name) {
		if (name == null) {
			return UNKNOWN;
		}
		name = name.trim();
		if (name.equals("NULL")) {
			return NULL;
		}
		if (name.equals("BIT")) {
			return BIT;
		}
		if (name.equals("DECIMAL")) {
			return DECIMAL;
		}
		if (name.equals("TINYINT")) {
			return TINYINT;
		}
		if (name.equals("SMALLINT")) {
			return SMALLINT;
		}
		if (name.equals("BIGINT")) {
			return BIGINT;
		}
		if (name.equals("INTEGER")) {
			return INTEGER;
		}
		if (name.equals("FLOAT")) {
			return FLOAT;
		}
		if (name.equals("DOUBLE")) {
			return DOUBLE;
		}
		if (name.equals("DATE")) {
			return DATE;
		}
		if (name.equals("TIME")) {
			return TIME;
		}
		if (name.equals("TIMESTAMP")) {
			return TIMESTAMP;
		}
		if (name.equals("CHAR")) {
			return CHAR;
		}
		if (name.equals("LONGVARCHAR")) {
			return LONGVARCHAR;
		}
		if (name.equals("VARCHAR")) {
			return VARCHAR;
		}
		if (name.equals("BINARY")) {
			return BINARY;
		}
		if (name.equals("VARBINARY")) {
			return VARBINARY;
		}
		if (name.equals("LONGVARBINARY")) {
			return LONGVARBINARY;
		}
		if (name.equals("OBJECT")) {
			return OBJECT;
		}
		return UNKNOWN;
	}
	
	/**
	 * Validates that the argument value is a valid/supported type. Throws an
	 * <code>java.lang.IllegalArgumentException</code> if the type is not supported.
	 * <p>
	 * @param type The type to validate.
	 */
	public static void validate(int type) {
		switch (type) {
		case NULL:
		case BIT:
		case DECIMAL:
		case BIGINT:
		case INTEGER:
		case SMALLINT:
		case FLOAT:
		case DOUBLE:
		case DATE:
		case TIME:
		case TIMESTAMP:
		case CHAR:
		case LONGVARCHAR:
		case VARCHAR:
		case BINARY:
		case VARBINARY:
		case LONGVARBINARY:
		case OBJECT:
			return;
		}
		throw new IllegalArgumentException("Illegal type");
	}
	
	/**
	 * Converts the type, length and decimals to a string representation.
	 * 
	 * @param type The type to set.
	 * @param length The length to set.
	 * @param decimals	The decimals to set.
	 * @return String
	 */
	public static String toString(int type, int length, int decimals) {
		boolean bLength = false;
		boolean bDecimals = false;

		switch (type) {
			case Types.CHAR:
			case Types.LONGVARCHAR:
			case Types.VARCHAR:
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
			case Types.OBJECT:
				bLength = true;
				break;
			case Types.DECIMAL:
				bLength = true;
				bDecimals = true;
				break;
		}
		
		String sType = Types.toString(type);
		if (bLength && bDecimals)
			sType += " (" + length + ", " + decimals + ")";
		else if (bLength)
			sType += " (" + length + ")";
		return sType;
	}

	public static final Object check(Object value, int type) {
		switch (type) {
			case BIT:			return (Boolean) value;
			case DECIMAL:		return (BigDecimal) value;
			case TINYINT:		return (Byte) value;
			case SMALLINT:		return (Short) value;
			case BIGINT:		return (Long) value;
			case INTEGER:		return (Integer) value;
			case FLOAT:			return (Float) value;
			case DOUBLE:		return (Double) value;
			case DATE:			return (Date) value;
			case TIME:			return (Time) value;
			case TIMESTAMP:		return (Timestamp) value;
			case CHAR:			return (String) value;
			case LONGVARCHAR:	return (String) value;
			case VARCHAR:		return (String) value;
//			case BINARY:		return (byte[])  // FIXME wrapper class
//			case VARBINARY:		return (byte[])  // FIXME wrapper class
//			case LONGVARBINARY:	return (byte[])  // FIXME wrapper class
			case OBJECT:		return value; 
		}
		throw new IllegalArgumentException("Unkown type (" + type + ") " + Types.toString(type));
	}
}
