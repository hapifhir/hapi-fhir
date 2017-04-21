package ca.uhn.fhir.jpa.dao;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.param.DateRangeParam;

public interface ISearchBuilder {

	Iterator<Long> createQuery(SearchParameterMap theParams);

	void setType(Class<? extends IBaseResource> theResourceType, String theResourceName);

	void loadResourcesByPid(Collection<Long> theIncludePids, List<IBaseResource> theResourceListToPopulate, Set<Long> theRevIncludedPids, boolean theForHistoryOperation, EntityManager theEntityManager,
			FhirContext theContext, IDao theDao);

	Set<Long> loadReverseIncludes(IDao theCallingDao, FhirContext theContext, EntityManager theEntityManager, Collection<Long> theMatches, Set<Include> theRevIncludes, boolean theReverseMode,
			DateRangeParam theLastUpdated);

}
