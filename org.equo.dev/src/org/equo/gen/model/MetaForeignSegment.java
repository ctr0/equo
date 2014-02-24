package org.equo.gen.model;

public class MetaForeignSegment {
	
	private String localField;
	private String foreignField;
	private String model;
	private boolean resolved;
	
	public MetaForeignSegment(String local, String foreign, String model) {
		this.localField = local;
		this.foreignField = foreign;
		this.model = model;
	}

	public String getLocalField() {
		return localField;
	}

	public String getForeignField() {
		return foreignField;
	}

	public String getModel() {
		return model;
	}

	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return localField + " - " + foreignField;
	}
}
