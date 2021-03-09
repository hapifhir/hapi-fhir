package ca.uhn.fhir.jpa.bulk.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Value("#{stepExecutionContext['resourceType']}")
	protected String myResourceType;
	@Value("#{jobExecutionContext['" + BulkExportJobConfig.JOB_UUID_PARAMETER + "']}")
	protected String myJobUUID;
	@Value("#{jobParameters['" + BulkExportJobConfig.READ_CHUNK_PARAMETER + "']}")
	protected Long myReadChunkSize;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected FhirContext myContext;
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private MatchUrlService myMatchUrlService;

	private ISearchBuilder mySearchBuilder;
	private BulkExportJobEntity myJobEntity;
	private RuntimeResourceDefinition myResourceDefinition;

	private Iterator<ResourcePersistentId> myPidIterator;
	private RuntimeSearchParam myPatientSearchParam;

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	protected ISearchBuilder getSearchBuilderForLocalResourceType() {
		if (mySearchBuilder == null) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);
		}
		return mySearchBuilder;
	}

	/**
	 * Generate the list of pids of all resources of the given myResourceType, which reference any group member of the given myGroupId.
	 * Store them in a member iterator.
	 */
	protected void loadResourcePids() {
		//Initialize an array to hold the pids of the target resources to be exported.
		myPidIterator = getResourcePidIterator();
	}

	abstract Iterator<ResourcePersistentId> getResourcePidIterator();

	protected List<SearchParameterMap> createSearchParameterMapsForResourceType() {
		BulkExportJobEntity jobEntity = getJobEntity();
		RuntimeResourceDefinition theDef = getResourceDefinition();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(jobEntity.getRequest());
		String[] typeFilters = requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
		if (typeFilters != null) {
			List<SearchParameterMap> maps = Arrays.stream(typeFilters)
				.filter(typeFilter -> typeFilter.startsWith(myResourceType + "?"))
				.map(filter -> buildSearchParameterMapForTypeFilter(filter, theDef))
				.collect(Collectors.toList());
			return maps;
		}
	}
	public SearchParameterMap buildSearchParameterMapForTypeFilter(String theFilter, RuntimeResourceDefinition theDef) {
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theFilter, theDef);
		if (getJobEntity().getSince() != null) {
			searchParameterMap.setLastUpdated(new DateRangeParam(getJobEntity().getSince(), null));
		}
		searchParameterMap.setLoadSynchronous(true);

	}

	protected RuntimeResourceDefinition getResourceDefinition() {
		if (myResourceDefinition == null) {
			myResourceDefinition = myContext.getResourceDefinition(myResourceType);
		}
		return myResourceDefinition;
	}

	protected BulkExportJobEntity getJobEntity() {
		if (myJobEntity == null) {
			Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
			if (jobOpt.isPresent()) {
				myJobEntity = jobOpt.get();
			} else {
				String errorMessage = String.format("Job with UUID %s does not exist!", myJobUUID);
				throw new IllegalStateException(errorMessage);
			}
		}
		return myJobEntity;
	}

	@Override
	public List<ResourcePersistentId> read() {

		ourLog.info("Bulk export starting generation for batch export job: [{}] with resourceType [{}] and UUID [{}]", getJobEntity(), myResourceType, myJobUUID);

		if (myPidIterator == null) {
			loadResourcePids();
		}

		int count = 0;
		List<ResourcePersistentId> outgoing = new ArrayList<>();
		while (myPidIterator.hasNext() && count < myReadChunkSize) {
			outgoing.add(myPidIterator.next());
			count += 1;
		}

		return outgoing.size() == 0 ? null : outgoing;

	}

	/**
	 * Given the resource type, fetch its patient-based search parameter name
	 * 1. Attempt to find one called 'patient'
	 * 2. If that fails, find one called 'subject'
	 * 3. If that fails, find find by Patient Compartment.
	 *    3.1 If that returns >1 result, throw an error
	 *    3.2 If that returns 1 result, return it
	 */
	protected RuntimeSearchParam getPatientSearchParamForCurrentResourceType() {
		if (myPatientSearchParam == null) {
			RuntimeResourceDefinition runtimeResourceDefinition = myContext.getResourceDefinition(myResourceType);
			myPatientSearchParam = runtimeResourceDefinition.getSearchParam("patient");
			if (myPatientSearchParam == null) {
				myPatientSearchParam = runtimeResourceDefinition.getSearchParam("subject");
				if (myPatientSearchParam == null) {
					myPatientSearchParam = getRuntimeSearchParamByCompartment(runtimeResourceDefinition);
					if (myPatientSearchParam == null) {
						String errorMessage = String.format("[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!", myResourceType);
						throw new IllegalArgumentException(errorMessage);
					}
				}
			}
		}
		return myPatientSearchParam;
	}

	/**
	 * Search the resource definition for a compartment named 'patient' and return its related Search Parameter.
	 */
	protected RuntimeSearchParam getRuntimeSearchParamByCompartment(RuntimeResourceDefinition runtimeResourceDefinition) {
		RuntimeSearchParam patientSearchParam;
		List<RuntimeSearchParam> searchParams = runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		if (searchParams == null || searchParams.size() == 0) {
			String errorMessage = String.format("Resource type [%s] is not eligible for Group Bulk export, as it contains no Patient compartment, and no `patient` or `subject` search parameter", myResourceType);
			throw new IllegalArgumentException(errorMessage);
		} else if (searchParams.size() == 1) {
			patientSearchParam = searchParams.get(0);
		} else {
			String errorMessage = String.format("Resource type [%s] is not eligible for Group Bulk export, as we are unable to disambiguate which patient search parameter we should be searching by.", myResourceType);
			throw new IllegalArgumentException(errorMessage);
		}
		return patientSearchParam;
	}
}
