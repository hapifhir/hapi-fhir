package ca.uhn.fhir.jpa.dao;

import java.util.Set;

import ca.uhn.fhir.jpa.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.model.api.IResource;

interface ISearchParamExtractor {

	public abstract Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(ResourceTable theEntity, IResource theResource);

	public abstract Set<ResourceIndexedSearchParamDate> extractSearchParamDates(ResourceTable theEntity, IResource theResource);

	public abstract Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(ResourceTable theEntity, IResource theResource);

	public abstract Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(ResourceTable theEntity, IResource theResource);

	public abstract Set<ResourceIndexedSearchParamString> extractSearchParamStrings(ResourceTable theEntity, IResource theResource);

	public abstract Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(ResourceTable theEntity, IResource theResource);

	public abstract Set<ResourceIndexedSearchParamUri> extractSearchParamUri(ResourceTable theEntity, IResource theResource);

}
