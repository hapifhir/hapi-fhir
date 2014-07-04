package ca.uhn.fhir.rest.gclient;

import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;

public interface ITransaction {

	ITransactionTyped<List<IResource>> withResources(List<IResource> theResources);
	
	ITransactionTyped<Bundle> withBundle(Bundle theResources);

}
