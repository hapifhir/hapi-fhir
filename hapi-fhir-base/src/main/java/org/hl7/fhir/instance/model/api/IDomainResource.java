package org.hl7.fhir.instance.model.api;

import java.util.List;

public interface IDomainResource {

	List<? extends IAnyResource> getContained();

	INarrative getText();

}
