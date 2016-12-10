package ca.uhn.fhir.tinder.model;

public class ResourceBlockCopy extends Child {

	private ResourceBlock referencedBlock = null;
	
	@Override
	public boolean isBlockRef() {
		return referencedBlock != null;
	}

	public ResourceBlock getReferencedBlock () {
		return this.referencedBlock;
	}
	public void setReferencedBlock (ResourceBlock referencedBlock) {
		this.referencedBlock = referencedBlock;
	}
}
