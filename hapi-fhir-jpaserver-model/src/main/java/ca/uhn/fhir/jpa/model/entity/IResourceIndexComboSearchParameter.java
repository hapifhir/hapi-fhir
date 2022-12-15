package ca.uhn.fhir.jpa.model.entity;

import org.hl7.fhir.instance.model.api.IIdType;


/**
 * Provides a common interface used to extract Combo Unique ({@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique})
 * and Combo Non-Unique ({@link ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique}) SearchParameters
 */
public interface IResourceIndexComboSearchParameter {

	IIdType getSearchParameterId();

	void setSearchParameterId(IIdType theSearchParameterId);

	String getIndexString();

	ResourceTable getResource();

	void setResource(ResourceTable theResourceTable);
}
