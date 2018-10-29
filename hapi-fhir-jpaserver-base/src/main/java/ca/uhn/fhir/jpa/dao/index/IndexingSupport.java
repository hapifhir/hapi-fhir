package ca.uhn.fhir.jpa.dao.index;

import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.ISearchParamExtractor;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.data.IForcedIdDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedCompositeStringUniqueDao;

public interface IndexingSupport {
	public DaoConfig getConfig();
	public ISearchParamExtractor getSearchParamExtractor();
	public ISearchParamRegistry getSearchParamRegistry();
	public FhirContext getContext();
	public EntityManager getEntityManager();
	public <R extends IBaseResource> IFhirResourceDao<R> getDao(Class<R> theType);
	public Map<Class<? extends IBaseResource>, IFhirResourceDao<?>> getResourceTypeToDao();
	public boolean isLogicalReference(IIdType nextId);
	public IForcedIdDao getForcedIdDao();
	public <R extends IBaseResource> Set<Long> processMatchUrl(String theMatchUrl, Class<R> theResourceType);
	public Long translateForcedIdToPid(String theResourceName, String theResourceId);
	public String toResourceName(Class<? extends IBaseResource> theResourceType);
	public IResourceIndexedCompositeStringUniqueDao getResourceIndexedCompositeStringUniqueDao();
}
