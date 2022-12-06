package ca.uhn.fhir.jpa.model.entity;

import org.hl7.fhir.instance.model.api.IIdType;

public interface IResourceIndexComboSearchParameter {
	IIdType getSearchParameterId();
	String getIndexString();
}
