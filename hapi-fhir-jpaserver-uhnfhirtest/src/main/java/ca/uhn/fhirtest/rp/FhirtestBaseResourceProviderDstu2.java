package ca.uhn.fhirtest.rp;

import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.model.api.IResource;

public class FhirtestBaseResourceProviderDstu2<T extends IResource> extends BaseJpaResourceProvider<T> {}
