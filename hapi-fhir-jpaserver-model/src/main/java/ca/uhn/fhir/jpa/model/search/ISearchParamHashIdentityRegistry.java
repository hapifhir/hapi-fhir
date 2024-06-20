package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.server.util.IndexedSearchParam;

import java.util.Optional;

public interface ISearchParamHashIdentityRegistry {
	Optional<IndexedSearchParam> getIndexedSearchParamByHashIdentity(Long theHashIdentity);
}
