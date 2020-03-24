package ca.uhn.fhir.jpaserver.test.dao;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DeleteMethodOutcome;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.delete.DeleteConflictList;
import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.jpa.searchparam.provider.SearchableHashMapResourceProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

// FIXME KHS delete these two classes from cdr and depend over here instead
// Please implement methods as you need them!
public class HashMapBackedDao<T extends IBaseResource> implements IFhirResourceDao<T> {

	private final SearchableHashMapResourceProvider<T> myResourceProvider;

	public HashMapBackedDao(FhirContext theFhirContext, Class<T> theResourceType, SearchParamMatcher theSearchParamMatcher) {
		myResourceProvider = new SearchableHashMapResourceProvider<>(theFhirContext, theResourceType, theSearchParamMatcher);
	}

	@Override
	public void addTag(IIdType theId, TagTypeEnum theTagType, String theScheme, String theTerm, String theLabel, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome create(T theResource) {
		MethodOutcome outcome = myResourceProvider.create(theResource);

		DaoMethodOutcome retval = new DaoMethodOutcome();
		retval.setCreated(outcome.getCreated());
		retval.setResource(theResource);
		retval.setId(outcome.getId());
		return retval;
	}

	@Override
	public DaoMethodOutcome create(T theResource, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, boolean thePerformIndexing, Date theUpdateTimestamp, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome create(T theResource, String theIfNoneExist, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome delete(IIdType theResourceId) {
		myResourceProvider.delete(theResourceId);
		return new DaoMethodOutcome();
	}

	@Override
	public DaoMethodOutcome delete(IIdType theResource, DeleteConflictList theDeleteConflictsListToPopulate, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome delete(IIdType theResource, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteMethodOutcome deleteByUrl(String theUrl, DeleteConflictList theDeleteConflictsListToPopulate, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DeleteMethodOutcome deleteByUrl(String theString, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExpungeOutcome expunge(IIdType theIdType, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ExpungeOutcome forceExpungeInExistingTransaction(IIdType theIIdType, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<T> getResourceType() {
		return myResourceProvider.getResourceType();
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBundleProvider history(IIdType theId, Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <MT extends IBaseMetaType> MT metaAddOperation(IIdType theId1, MT theMetaAdd, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <MT extends IBaseMetaType> MT metaDeleteOperation(IIdType theId1, MT theMetaDel, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, IIdType theId, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <MT extends IBaseMetaType> MT metaGetOperation(Class<MT> theType, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome patch(IIdType theId, String theConditionalUrl, PatchTypeEnum thePatchType, String thePatchBody, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T read(IIdType theId) {
		return myResourceProvider.read(theId, null);
	}

	@Override
	public T read(IIdType theId, RequestDetails theRequestDetails) {
		return myResourceProvider.read(theId, theRequestDetails);
	}

	@Override
	public IBaseResource readByPid(ResourcePersistentId thePid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public T read(IIdType theId, RequestDetails theRequestDetails, boolean theDeletedOk) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseHasResource readEntity(IIdType theId, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public BaseHasResource readEntity(IIdType theId, boolean theCheckForForcedId, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void reindex(T theResource, ResourceTable theEntity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeTag(IIdType theId, TagTypeEnum theTagType, String theSystem, String theCode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return new SimpleBundleProvider(myResourceProvider.searchByParams(theParams, null));
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails) {
		return new SimpleBundleProvider(myResourceProvider.searchByParams(theParams, theRequestDetails));
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams, RequestDetails theRequestDetails, HttpServletResponse theServletResponse) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<ResourcePersistentId> searchForIds(SearchParameterMap theParams, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void translateRawParameters(Map<String, List<String>> theSource, SearchParameterMap theTarget) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome update(T theResource) {
		MethodOutcome outcome = myResourceProvider.update(theResource, "");
		DaoMethodOutcome retVal = new DaoMethodOutcome();
		retVal.setCreated(outcome.getCreated());
		retVal.setResource(theResource);
		return retVal;
	}

	@Override
	public DaoMethodOutcome update(T theResource, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DaoMethodOutcome update(T theResource, String theMatchUrl, boolean thePerformIndexing, boolean theForceUpdateVersion, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MethodOutcome validate(T theResource, IIdType theId, String theRawResource, EncodingEnum theEncoding, ValidationModeEnum theMode, String theProfile, RequestDetails theRequestDetails) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RuntimeResourceDefinition validateCriteriaAndReturnResourceDefinition(String criteria) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FhirContext getContext() {
		return myResourceProvider.getFhirContext();
	}

	@Override
	public void injectDependenciesIntoBundleProvider(PersistedJpaBundleProvider theProvider) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IBaseResource toResource(BaseHasResource theEntity, boolean theForHistoryOperation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <R extends IBaseResource> R toResource(Class<R> theResourceType, IBaseResourceEntity theEntity, Collection<ResourceTag> theTagList, boolean theForHistoryOperation) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ISearchParamRegistry getSearchParamRegistry() {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		myResourceProvider.clear();
	}
}
