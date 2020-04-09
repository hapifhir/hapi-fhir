package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.search.reindex.ResourceReindexingSvcImpl;
import ca.uhn.fhir.jpa.searchparam.JpaRuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.util.SpringObjectCaster;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.INDEX_STATUS_INDEXED;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirResourceDaoR4UniqueSearchParamTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4UniqueSearchParamTest.class);
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	private List<String> myMessages = new ArrayList<>();

	@After
	public void after() throws Exception {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(new DaoConfig().isUniqueIndexesCheckedBeforeSave());
		myDaoConfig.setSchedulingDisabled(new DaoConfig().isSchedulingDisabled());
		myDaoConfig.setUniqueIndexesEnabled(new DaoConfig().isUniqueIndexesEnabled());
		myDaoConfig.setReindexThreadCount(new DaoConfig().getReindexThreadCount());

		ResourceReindexingSvcImpl svc = SpringObjectCaster.getTargetObject(myResourceReindexingSvc, ResourceReindexingSvcImpl.class);
		svc.initExecutor();
	}

	@Before
	public void before() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);
		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setUniqueIndexesEnabled(true);

		myInterceptorBroadcaster = mock(IInterceptorBroadcaster.class);
		when(mySrd.getInterceptorBroadcaster()).thenReturn(myInterceptorBroadcaster);
		when(mySrd.getServer().getPagingProvider()).thenReturn(new DatabaseBackedPagingProvider());

		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_WARNING))).thenReturn(true);
		when(myInterceptorBroadcaster.hasHooks(eq(Pointcut.JPA_PERFTRACE_INFO))).thenReturn(true);
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_INFO), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("INFO " + params.get(StorageProcessingMessage.class).getMessage());
			return null;
		});
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_WARNING), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("WARN " + params.get(StorageProcessingMessage.class).getMessage());
			return null;
		});
		when(myInterceptorBroadcaster.callHooks(eq(Pointcut.JPA_PERFTRACE_SEARCH_REUSING_CACHED), ArgumentMatchers.any(HookParams.class))).thenAnswer(t -> {
			HookParams params = t.getArgument(1, HookParams.class);
			myMessages.add("REUSING CACHED SEARCH");
			return null;
		});
	}


	private void createUniqueBirthdateAndGenderSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-birthdate");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createUniqueIndexCoverageBeneficiary() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary");
		sp.setCode("beneficiary");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-identifier");
		sp.setCode("identifier");
		sp.setExpression("Coverage.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary-identifier");
		sp.setCode("coverage-beneficiary-identifier");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition("/SearchParameter/coverage-beneficiary");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition("/SearchParameter/coverage-identifier");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexObservationSubject() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject");
		sp.setCode("observation-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-uniq-subject");
		sp.setCode("observation-uniq-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("/SearchParameter/observation-subject");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexPatientIdentifier() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexPatientIdentifierCount1() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("first-identifier");
		sp.setExpression("Patient.identifier.first()");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueNameAndManagingOrganizationSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-organization");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("organization");
		sp.setExpression("Patient.managingOrganization");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name-organization");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-name");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-organization");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueObservationSubjectDateCode() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/obs-subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("subject");
		sp.setExpression("Observation.subject");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addTarget("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-effective");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("date");
		sp.setExpression("Observation.effective");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-code");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("code");
		sp.setExpression("Observation.code");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject-date-code");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.setExpression("Observation.code");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-subject");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-effective");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-code");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();
	}

	@Test
	public void testDoubleMatchingOnAnd_Search() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		String id1 = myPatientDao.create(pt).getId().toUnqualifiedVersionless().getValue();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("333");
		String id2 = myPatientDao.create(pt).getId().toUnqualifiedVersionless().getValue();

		pt = new Patient();
		pt.setActive(false);
		pt.addIdentifier().setSystem("urn").setValue("444");
		myPatientDao.create(pt);

		String unformattedSql;

		// Two AND values
		myCaptureQueriesListener.clear();
		SearchParameterMap sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("urn", "111"))
				.addAnd(new TokenParam("urn", "222"))
		);
		IBundleProvider outcome = myPatientDao.search(sp);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql, stringContainsInOrder(
			"IDX_STRING='Patient?identifier=urn%7C111'",
			"HASH_SYS_AND_VALUE in ('-3122824860083758210')"
		));
		assertThat(unformattedSql, not(containsString(("RES_DELETED_AT"))));
		assertThat(unformattedSql, not(containsString(("RES_TYPE"))));
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(id1));

		// Two OR values on the same resource - Currently composite SPs don't work for this
		myCaptureQueriesListener.clear();
		sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("urn", "111"), new TokenParam("urn", "222"))
		);
		outcome = myPatientDao.search(sp);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(id1));
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql, containsString("HASH_SYS_AND_VALUE in ('4101160957635429999' , '-3122824860083758210')"));
		assertThat(unformattedSql, not(containsString(("IDX_STRING"))));
		assertThat(unformattedSql, not(containsString(("RES_DELETED_AT"))));
		assertThat(unformattedSql, not(containsString(("RES_TYPE"))));

		// Not matching the composite SP at all
		myCaptureQueriesListener.clear();
		sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("active",
			new TokenAndListParam()
				.addAnd(new TokenParam(null, "true"))
		);
		outcome = myPatientDao.search(sp);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(id1, id2));
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql, not(containsString(("IDX_STRING"))));
		assertThat(unformattedSql, not(containsString(("RES_DELETED_AT"))));
		assertThat(unformattedSql, not(containsString(("RES_TYPE"))));

	}

	@Test
	public void testDoubleMatchingOnAnd_Search2() {
		SearchParameter sp;

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("patient");
		sp.setName("patient");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase(ServiceRequest.class.getName());
		sp.setExpression("ServiceRequest.subject.where(resolve() is Patient)");
		String patientParamId = mySearchParameterDao.create(sp).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("performer");
		sp.setName("performer");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase(ServiceRequest.class.getName());
		sp.setExpression("ServiceRequest.performer");
		String performerParamId = mySearchParameterDao.create(sp).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("identifier");
		sp.setName("identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.addBase(ServiceRequest.class.getName());
		sp.setExpression("ServiceRequest.identifier");
		String identifierParamId = mySearchParameterDao.create(sp).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("procreq-patient-performer-identifier");
		sp.setExpression("ServiceRequest.patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("ServiceRequest");
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(patientParamId); // SearchParameter?base=ServiceRequest&name=patient
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(performerParamId); // SearchParameter?base=ServiceRequest&name=performer
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(identifierParamId); // SearchParameter?base=ServiceRequest&name=identifier
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));

		// Now create matching/non-matching resources
		Patient pt = new Patient();
		pt.setActive(true);
		IIdType ptId = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		Practitioner pract = new Practitioner();
		pract.setActive(true);
		IIdType practId = myPractitionerDao.create(pract).getId().toUnqualifiedVersionless();

		ServiceRequest sr = new ServiceRequest();
		sr.addIdentifier().setSystem("sys").setValue("111");
		sr.addIdentifier().setSystem("sys").setValue("222");
		sr.setSubject(new Reference(ptId));
		sr.addPerformer(new Reference(practId));
		String srId = myServiceRequestDao.create(sr).getId().toUnqualifiedVersionless().getValue();

		sr = new ServiceRequest();
		sr.addIdentifier().setSystem("sys").setValue("888");
		sr.addIdentifier().setSystem("sys").setValue("999");
		sr.setSubject(new Reference(ptId));
		sr.addPerformer(new Reference(practId));
		myServiceRequestDao.create(sr).getId().toUnqualifiedVersionless().getValue();

		String unformattedSql;

		// Use qualified references
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("sys", "111"))
				.addAnd(new TokenParam("sys", "222"))
		);
		map.add("patient", new ReferenceParam(ptId.getValue()));
		map.add("performer", new ReferenceParam(practId.getValue()));
		IBundleProvider outcome = myServiceRequestDao.search(map);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(srId));
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql, stringContainsInOrder(
			"IDX_STRING='ServiceRequest?identifier=sys%7C111&patient=Patient%2F" + ptId.getIdPart() + "&performer=Practitioner%2F" + practId.getIdPart() + "'",
			"HASH_SYS_AND_VALUE in ('6795110643554413877')"
		));
		assertThat(unformattedSql, not(containsString(("RES_DELETED_AT"))));
		assertThat(unformattedSql, not(containsString(("RES_TYPE"))));

		// Don't use qualified references
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("sys", "111"))
				.addAnd(new TokenParam("sys", "222"))
		);
		map.add("patient", new ReferenceParam(ptId.getIdPart()));
		map.add("performer", new ReferenceParam(practId.getIdPart()));
		outcome = myServiceRequestDao.search(map);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome), containsInAnyOrder(srId));
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql, stringContainsInOrder(
			"SRC_PATH='ServiceRequest.subject.where(resolve() is Patient)'",
			"SRC_PATH='ServiceRequest.performer'"
		));
		assertThat(unformattedSql, not(containsString(("RES_DELETED_AT"))));
		assertThat(unformattedSql, not(containsString(("RES_TYPE"))));

	}

	@Test
	public void testDoubleMatchingOnOr_ConditionalCreate() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		// Make sure entries are saved
		runInTransaction(() -> {
			List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
			assertEquals(2, all.size());
		});

	}

	@Test
	public void testDoubleMatchingPut() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	@Test
	public void testDuplicateUniqueValuesAreReIndexed() throws Exception {
		myDaoConfig.setSchedulingDisabled(true);
		myDaoConfig.setReindexThreadCount(1);

		ResourceReindexingSvcImpl svc = SpringObjectCaster.getTargetObject(myResourceReindexingSvc, ResourceReindexingSvcImpl.class);
		svc.initExecutor();

		List<JpaRuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams("Observation");
		assertEquals(0, uniqueSearchParams.size());

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		/*
		 * Both of the following resources will match the unique index we'll
		 * create afterward. So we can create them both, but then when we create
		 * the unique index that matches them both that's a problem...
		 */

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id3 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  - ID2: {}  - ID3: {}", id1, id2, id3);

		createUniqueObservationSubjectDateCode();

		uniqueSearchParams = mySearchParamRegistry.getActiveUniqueSearchParams("Observation");
		assertEquals(1, uniqueSearchParams.size());
		assertEquals(3, uniqueSearchParams.get(0).getComponents().size());

		myResourceReindexingSvc.markAllResourcesForReindexing();
		assertEquals(6, myResourceReindexingSvc.forceReindexingPass());
		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		assertEquals(0, myResourceReindexingSvc.forceReindexingPass());

		runInTransaction(()->{
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
			assertEquals(uniques.toString(), 1, uniques.size());
			assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue(), either(equalTo("Observation/" + id2.getIdPart())).or(equalTo("Observation/" + id3.getIdPart())));
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

			myResourceIndexedCompositeStringUniqueDao.deleteAll();
		});

		assertEquals(1, mySearchParamRegistry.getActiveUniqueSearchParams("Observation").size());

		myResourceReindexingSvc.markAllResourcesForReindexing("Observation");
		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		assertEquals(0, myResourceReindexingSvc.forceReindexingPass());

		runInTransaction(()->{
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
			assertEquals(uniques.toString(), 1, uniques.size());
			assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue(), either(equalTo("Observation/" + id2.getIdPart())).or(equalTo("Observation/" + id3.getIdPart())));
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
		});

	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingDisabled() {
		myDaoConfig.setUniqueIndexesCheckedBeforeSave(false);

		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals("The operation has failed with a unique index constraint failure. This probably means that the operation was trying to create/update a resource that would have resulted in a duplicate value for a unique index.", e.getMessage());
		}
	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingEnabled() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("Can not create resource of type Patient as it would create a duplicate index matching query: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale (existing index belongs to Patient/" + id1.getIdPart() + ")", e.getMessage());
		}
	}

	@Test
	public void testIndexFirstMatchOnly() {
		createUniqueIndexPatientIdentifierCount1();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt);

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		try {
			myPatientDao.create(pt);
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("333");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	@Test
	public void testIndexTransactionWithMatchUrl() {
		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		Coverage cov = new Coverage();
		cov.getBeneficiary().setReference(id2.getValue());
		cov.addIdentifier().setSystem("urn:foo:bar").setValue("123");
		IIdType id3 = myCoverageDao.create(cov).getId().toUnqualifiedVersionless();

		createUniqueIndexCoverageBeneficiary();

		myResourceReindexingSvc.markAllResourcesForReindexing("Coverage");
		// The first pass as a low of EPOCH
		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		// The second pass has a low of Coverage.lastUpdated
		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		// The third pass has a low of (Coverage.lastUpdated + 1ms)
		assertEquals(0, myResourceReindexingSvc.forceReindexingPass());

		runInTransaction(() -> {
			List<ResourceTable> tables = myResourceTableDao.findAll();
			String resourceIds = tables.stream().map(t -> t.getIdDt().getValue()).collect(Collectors.joining(", "));
			// 1 patient, 1 coverage, 3 search parameters
			assertEquals(resourceIds, 5, tables.size());
			for (int i = 0; i < tables.size(); i++) {
				assertEquals(INDEX_STATUS_INDEXED, tables.get(i).getIndexStatus().intValue());
			}
		});

		runInTransaction(() -> {
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
			ourLog.info("** Uniques: {}", uniques);
			assertEquals(uniques.toString(), 1, uniques.size());
			assertEquals("Coverage/" + id3.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Coverage?beneficiary=Patient%2F" + id2.getIdPart() + "&identifier=urn%3Afoo%3Abar%7C123", uniques.get(0).getIndexString());
		});


	}


	@Test
	public void testIndexTransactionWithMatchUrl2() {
		createUniqueIndexCoverageBeneficiary();

		String input = "{\n" +
			"  \"resourceType\": \"Bundle\",\n" +
			"  \"type\": \"transaction\",\n" +
			"  \"entry\": [\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Patient\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"use\": \"official\",\n" +
			"            \"type\": {\n" +
			"              \"coding\": [\n" +
			"                {\n" +
			"                  \"system\": \"http://hl7.org/fhir/v2/0203\",\n" +
			"                  \"code\": \"MR\"\n" +
			"                }\n" +
			"              ]\n" +
			"            },\n" +
			"            \"system\": \"FOOORG:FOOSITE:patientid:MR:R\",\n" +
			"            \"value\": \"007811959\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Patient?identifier=FOOORG%3AFOOSITE%3Apatientid%3AMR%3AR%7C007811959%2CFOOORG%3AFOOSITE%3Apatientid%3AMR%3AB%7C000929990%2CFOOORG%3AFOOSITE%3Apatientid%3API%3APH%7C00589363%2Chttp%3A%2F%2Fhl7.org%2Ffhir%2Fsid%2Fus-ssn%7C657-01-8133\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:b58ff639-11d1-4dac-942f-abf4f9a625d7\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Coverage\",\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"FOOORG:FOOSITE:coverage:planId\",\n" +
			"            \"value\": \"0403-010101\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"beneficiary\": {\n" +
			"          \"reference\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\"\n" +
			"        }\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0403-010101\"\n" +
			"      }\n" +
			"    },\n" +
			"    {\n" +
			"      \"fullUrl\": \"urn:uuid:13f5da1a-6601-4c1a-82c9-41527be23fa0\",\n" +
			"      \"resource\": {\n" +
			"        \"resourceType\": \"Coverage\",\n" +
			"        \"contained\": [\n" +
			"          {\n" +
			"            \"resourceType\": \"RelatedPerson\",\n" +
			"            \"id\": \"1\",\n" +
			"            \"name\": [\n" +
			"              {\n" +
			"                \"family\": \"SMITH\",\n" +
			"                \"given\": [\n" +
			"                  \"FAKER\"\n" +
			"                ]\n" +
			"              }\n" +
			"            ]\n" +
			"          },\n" +
			"          {\n" +
			"            \"resourceType\": \"Organization\",\n" +
			"            \"id\": \"2\",\n" +
			"            \"name\": \"MEDICAID\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"identifier\": [\n" +
			"          {\n" +
			"            \"system\": \"FOOORG:FOOSITE:coverage:planId\",\n" +
			"            \"value\": \"0404-010101\"\n" +
			"          }\n" +
			"        ],\n" +
			"        \"policyHolder\": {\n" +
			"          \"reference\": \"#1\"\n" +
			"        },\n" +
			"        \"beneficiary\": {\n" +
			"          \"reference\": \"urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3\"\n" +
			"        },\n" +
			"        \"payor\": [\n" +
			"          {\n" +
			"            \"reference\": \"#2\"\n" +
			"          }\n" +
			"        ]\n" +
			"      },\n" +
			"      \"request\": {\n" +
			"        \"method\": \"PUT\",\n" +
			"        \"url\": \"/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0404-010101\"\n" +
			"      }\n" +
			"    }\n" +
			"  ]\n" +
			"}";

		Bundle inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(inputBundle));
		mySystemDao.transaction(mySrd, inputBundle);

		inputBundle = myFhirCtx.newJsonParser().parseResource(Bundle.class, input);
		mySystemDao.transaction(mySrd, inputBundle);

	}

	@Test
	public void testNonTransaction() {
		createUniqueBirthdateAndGenderSps();

		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.MALE);
		p.setBirthDateElement(new DateType("2001-01-01"));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(p)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&birthdate=2001-01-01");

		Bundle output0 = mySystemDao.transaction(mySrd, input);
		Bundle output1 = mySystemDao.transaction(mySrd, input);
		assertEquals(output1.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());
		Bundle output2 = mySystemDao.transaction(mySrd, input);
		assertEquals(output2.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());

	}


	@Test
	public void testObservationSubject() {
		createUniqueIndexObservationSubject();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		IIdType ptid = myPatientDao.create(pt).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(ptid));
		IIdType encid = myEncounterDao.create(enc).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.setSubject(new Reference(ptid));
		obs.setEncounter(new Reference(encid));
		myObservationDao.create(obs);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedCompositeStringUnique> all = myResourceIndexedCompositeStringUniqueDao.findAll();
				assertEquals(all.toString(), 1, all.size());
			}
		});

	}

	@Test
	public void testSearchSynchronousUsingUniqueComposite() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1.getValue()));

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale"));
		myMessages.clear();

	}


	@Test
	public void testSearchUsingUniqueComposite() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		String id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale"));
		myMessages.clear();

		// Other order
		myMessages.clear();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-01"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params, mySrd);
		assertEquals(searchId, results.getUuid());
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id1));
		// Because we just reuse the last search
		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("REUSING"));
		assertThat(myMessages.toString(), not(containsString("Using unique index")));
		myMessages.clear();

		myMessages.clear();
		params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?birthdate=2011-01-03&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale"));
		myMessages.clear();

		myMessages.clear();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(results), empty());
		// STANDARD QUERY
		logCapturedMessages();
		assertThat(myMessages.toString(), not(containsString("unique index")));
		myMessages.clear();

	}


	@Test
	public void testUniqueValuesAreIndexed_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_RefAndDateAndToken() {
		createUniqueObservationSubjectDateCode();

		List<ResourceIndexedCompositeStringUnique> uniques;
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		IIdType id3 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  -  ID2: {}   - ID3:  {}", id1, id2, id3);

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_Reference_UsingModifierSyntax() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG", mySrd).getId().toUnqualifiedVersionless();

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?name=FAMILY1&organization=Organization%2FORG"));
		myMessages.clear();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again with a change
		pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG", mySrd).getId().toUnqualifiedVersionless();

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?name=FAMILY1&organization=Organization%2FORG"));
		myMessages.clear();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

	}

	private void logCapturedMessages() {
		ourLog.info("Messages:\n  {}", String.join("\n  ", myMessages));
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		Collections.sort(uniques);

		assertEquals(3, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(1).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN1&organization=Organization%2FORG", uniques.get(1).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(2).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN2&organization=Organization%2FORG", uniques.get(2).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditional() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG").getId().toUnqualifiedVersionless();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG").getId().toUnqualifiedVersionless();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditionalInTransaction() {
		createUniqueNameAndManagingOrganizationSps();
		List<ResourceIndexedCompositeStringUnique> uniques;

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		String orgId = "urn:uuid:" + UUID.randomUUID().toString();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId.replace(":", "%3A"));

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		IIdType id1 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		// Again

		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		orgId = IdType.newRandomUuid().getValue();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId);

		resp = mySystemDao.transaction(mySrd, bundle);

		id1 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(1, uniques.size());
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt;
		List<ResourceIndexedCompositeStringUnique> uniques;

		pt = new Patient();
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.setBirthDateElement(new DateType());
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		List<ResourceIndexedCompositeStringUnique> uniques;
		Patient pt;

		pt = new Patient();
		pt.setManagingOrganization(new Reference("Organization/ORG"));
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());

		pt = new Patient();
		pt.setActive(true);
		myPatientDao.create(pt).getId().toUnqualifiedVersionless();
		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 0, uniques.size());
	}

	@Test
	public void testUniqueValuesAreReIndexed() {
		createUniqueObservationSubjectDateCode();

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();

		List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

		myResourceIndexedCompositeStringUniqueDao.deleteAll();

		myResourceReindexingSvc.markAllResourcesForReindexing();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();

		uniques = myResourceIndexedCompositeStringUniqueDao.findAll();
		assertEquals(uniques.toString(), 1, uniques.size());
		assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

	}


	@Test
	public void testDetectUniqueSearchParams() {
		createUniqueBirthdateAndGenderSps();
		List<JpaRuntimeSearchParam> params = mySearchParamRegistry.getActiveUniqueSearchParams("Patient");

		assertEquals(1, params.size());
		assertEquals(params.get(0).isUnique(), true);
		assertEquals(2, params.get(0).getCompositeOf().size());
		// Should be alphabetical order
		assertEquals("birthdate", params.get(0).getCompositeOf().get(0).getName());
		assertEquals("gender", params.get(0).getCompositeOf().get(1).getName());
	}

	@Test
	public void testDuplicateUniqueValuesAreRejected() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1).getId().toUnqualifiedVersionless();
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		pt2 = new Patient();
		pt2.setId(id2);
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		try {
			myPatientDao.update(pt2);
			fail();
		} catch (PreconditionFailedException e) {
			// good
		}

	}

	@Test
	public void testReplaceOneWithAnother() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1).getId().toUnqualified();
		assertNotNull(id1);

		ourLog.info("** Replacing");

		pt1 = new Patient();
		pt1.setId(id1);
		pt1.setGender(Enumerations.AdministrativeGender.FEMALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		id1 = myPatientDao.update(pt1).getId().toUnqualified();
		assertNotNull(id1);
		assertEquals("2", id1.getVersionIdPart());

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id2 = myPatientDao.create(pt2).getId().toUnqualifiedVersionless();

		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results), containsInAnyOrder(id2.getValue()));

		logCapturedMessages();
		assertThat(myMessages.toString(), containsString("Using unique index for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale"));
		myMessages.clear();

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
