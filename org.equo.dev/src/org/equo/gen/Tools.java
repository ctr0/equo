package org.equo.gen;

import java.io.File;

import org.equo.Types;
import org.xml.sax.SAXParseException;

class Tools {
	
	static boolean toBoolean(String value) {
		if (value != null && 
		(value.equals("true") || value.equals("1") || value.equals("TRUE"))) {
			return true;
		}
		return false;
	}

	static String[] splitCommaStringAndTrim(String value) {
		String[] values = value.split("\\,");
		for (int i = 0; i < values.length; i++) {
			values[i] = values[i].trim();
		}
		return values;
	}

	static String getErrorMessage(String resource, SAXParseException e) {
		e.printStackTrace();
		return "Error in '" + resource + "' at line " + e.getLineNumber()
				+ " column " + e.getColumnNumber() + ": "
				+ e.getException().getMessage();
	}

	static int toSqlType(String type) {
		return Types.toType(type);
	}

	public static String camelCase(String name) {
		// TODO CTR0
		return name.toLowerCase();
	}
	
	public static File getResourceFile(File moduleSchema, String resource) {
		return new File(moduleSchema.getParentFile() + File.separator + resource);
	}

}
