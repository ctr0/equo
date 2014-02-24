package org.equo.util;



public interface Iterator<E> extends java.util.Iterator<E>, AutoCloseable {

	public boolean hasNext() throws IllegalStateException;

	public E next() throws IllegalStateException;
	
	public void close() throws IllegalStateException;
	
	public void remove() throws IllegalStateException, UnsupportedOperationException;
	
}
