package org.equo;


public class CreateTable implements ICommand {
	
	private ITable peer;

	public CreateTable(ITable peer) {
		this.peer = peer;
	}

	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitCreateTable(peer);
	}

}
 