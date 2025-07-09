package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.util.CanonicalIdentifier;
import org.hl7.fhir.instance.model.api.IBaseReference;

import java.util.List;

/**
 * Base class for input parameters used in both $merge and $hapi.fhir.undo-merge operations.
 */
public abstract class BaseMergeOperationsCommonInputParameters {
	private List<CanonicalIdentifier> mySourceResourceIdentifiers;
	private List<CanonicalIdentifier> myTargetResourceIdentifiers;
	private IBaseReference mySourceResource;
	private IBaseReference myTargetResource;

	public abstract String getSourceResourceParameterName();

	public abstract String getTargetResourceParameterName();

	public abstract String getSourceIdentifiersParameterName();

	public abstract String getTargetIdentifiersParameterName();


	public List<CanonicalIdentifier> getSourceIdentifiers() {
		return mySourceResourceIdentifiers;
	}

	public boolean hasAtLeastOneSourceIdentifier() {
		return mySourceResourceIdentifiers != null && !mySourceResourceIdentifiers.isEmpty();
	}

	public void setSourceResourceIdentifiers(List<CanonicalIdentifier> theSourceIdentifiers) {
		this.mySourceResourceIdentifiers = theSourceIdentifiers;
	}

	public List<CanonicalIdentifier> getTargetIdentifiers() {
		return myTargetResourceIdentifiers;
	}

	public boolean hasAtLeastOneTargetIdentifier() {
		return myTargetResourceIdentifiers != null && !myTargetResourceIdentifiers.isEmpty();
	}

	public void setTargetResourceIdentifiers(List<CanonicalIdentifier> theTargetIdentifiers) {
		this.myTargetResourceIdentifiers = theTargetIdentifiers;
	}

	public IBaseReference getSourceResource() {
		return mySourceResource;
	}

	public void setSourceResource(IBaseReference theSourceResource) {
		this.mySourceResource = theSourceResource;
	}

	public IBaseReference getTargetResource() {
		return myTargetResource;
	}

	public void setTargetResource(IBaseReference theTargetResource) {
		this.myTargetResource = theTargetResource;
	}

}
