package org.equo.db;

import java.util.List;

import org.equo.ICommand.CommandVisitor;

public interface CommandBuilder extends CommandVisitor {
	
	public abstract CommandBuilder newInstance();
	
	public abstract List<Object> getValues();

	public abstract String toSql();
}
