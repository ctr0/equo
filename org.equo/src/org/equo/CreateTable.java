package org.equo;


public class CreateTable implements ICommand {
	
	private IPeer peer;

	public CreateTable(IPeer peer) {
		this.peer = peer;
	}

	@Override
	public void accept(CommandVisitor visitor) throws DatasourceException {
		visitor.visitCreateTable(peer);
	}

}
 