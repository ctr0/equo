package org.equo;

public class DatasourceException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	public DatasourceException(String message) {
        super(message);
    }
	
	public DatasourceException(Throwable cause) {
        super(cause);
    }
	
	public DatasourceException(String message, Throwable cause) {
        super(message, cause);
    }
}