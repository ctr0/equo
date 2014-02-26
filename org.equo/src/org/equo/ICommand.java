package org.equo;

import org.equo.Update.Set;


public interface ICommand {

	abstract void accept(CommandVisitor visitor) throws DatasourceException;
	
	public interface CommandVisitor {
		
		public void visitSelect(IColumn[] columns) throws DatasourceException;
		
		public void visitUpdate(ITable peer) throws DatasourceException;
		
		public void visitDelete() throws DatasourceException;
		
		public void visitInsert(ITable peer, IColumn[] columns) throws DatasourceException;
		
		public void visitFrom(ITable peer) throws DatasourceException;
		
		public void visitJoin(boolean inner, ITable left, ITable right, ICriteria criteria) throws DatasourceException;
		
		public void visitSets(Set[] sets) throws DatasourceException;
		
		public void visitValues(Object[][] values) throws DatasourceException;

		public void visitWhere(ICriteria criteria) throws DatasourceException;

		public void visitOrderBy(Order order) throws DatasourceException;

		public void visitGroupBy(IColumn[] columns) throws DatasourceException;
		
		public void visitCreateTable(ITable peer) throws DatasourceException;
	
	}
	
}
