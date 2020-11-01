package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTag;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.util.TestUtil.sleepAtLeast;
import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitioningSqlR4Test extends BaseJpaR4SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PartitioningSqlR4Test.class);

	private MyReadWriteInterceptor myPartitionInterceptor;
	private LocalDate myPartitionDate;
	private LocalDate myPartitionDate2;
	private int myPartitionId;
	private int myPartitionId2;
	private boolean myHaveDroppedForcedIdUniqueConstraint;
	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;

	@AfterEach
	public void after() {
		myPartitionInterceptor.assertNoRemainingIds();

		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyReadWriteInterceptor);
		myInterceptor = null;

		if (myHaveDroppedForcedIdUniqueConstraint) {
			runInTransaction(() -> {
				myEntityManager.createNativeQuery("delete from HFJ_FORCED_ID").executeUpdate();
				myEntityManager.createNativeQuery("alter table HFJ_FORCED_ID add constraint IDX_FORCEDID_TYPE_FID unique (RESOURCE_TYPE, FORCED_ID)");
			});
		}

		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
	}

	@Override
	@BeforeEach
	public void before() throws ServletException {
		super.before();

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());

		myDaoConfig.setUniqueIndexesEnabled(true);

		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionDate2 = LocalDate.of(2020, Month.JANUARY, 15);
		myPartitionId = 1;
		myPartitionId2 = 2;

		myPartitionInterceptor = new MyReadWriteInterceptor();
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(3).setName("PART-3"));

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}

	@Test
	public void testCreateSearchParameter_DefaultPartition() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		Long id = mySearchParameterDao.create(sp).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(RequestPartitionId.defaultPartition(), resourceTable.getPartitionId());
		});
	}

	@Test
	public void testCreate_CrossPartitionReference_ByPid_Allowed() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);

		// Create patient in partition 1
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition 2
		addCreatePartition(myPartitionId2, myPartitionDate2);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());

		myCaptureQueriesListener.clear();
		IIdType obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		assertEquals(2, selectQueries.size());
		// Look up the partition
		assertThat(selectQueries.get(0).getSql(true, false).toLowerCase(), containsString(" from hfj_partition "));
		// Look up the referenced subject/patient
		assertThat(selectQueries.get(1).getSql(true, false).toLowerCase(), containsString(" from hfj_resource "));
		assertEquals(0, StringUtils.countMatches(selectQueries.get(1).getSql(true, false).toLowerCase(), "partition"));

		runInTransaction(() -> {
			List<ResourceLink> resLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links:\n{}", resLinks.toString());
			assertEquals(2, resLinks.size());
			assertEquals(obsId.getIdPartAsLong(), resLinks.get(0).getSourceResourcePid());
			assertEquals(patientId.getIdPartAsLong(), resLinks.get(0).getTargetResourcePid());
		});
	}

	@Test
	public void testCreate_CrossPartitionReference_ByPid_NotAllowed() {

		// Create patient in partition 1
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition 2
		addCreatePartition(myPartitionId2, myPartitionDate2);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());

		try {
			myObservationDao.create(obs).getId().toUnqualifiedVersionless();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("Resource Patient/" + patientId.getIdPart() + " not found, specified in path: Observation.subject"));
		}

	}

	@Test
	public void testCreate_CrossPartitionReference_ByForcedId_Allowed() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		// Create patient in partition 1
		addReadPartition(myPartitionId);
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient patient = new Patient();
		patient.setId("ONE");
		patient.setActive(true);
		IIdType patientId = myPatientDao.update(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition 2
		addCreatePartition(myPartitionId2, myPartitionDate2);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());
		IIdType obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceLink> resLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links:\n{}", resLinks.toString());
			assertEquals(2, resLinks.size());
			assertEquals(obsId.getIdPartAsLong(), resLinks.get(0).getSourceResourcePid());
			assertEquals(patientId.getIdPart(), resLinks.get(0).getTargetResourceId());
		});
	}

	@Test
	public void testCreate_CrossPartitionReference_ByForcedId_NotAllowed() {

		// Create patient in partition 1
		addReadPartition(myPartitionId);
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient patient = new Patient();
		patient.setId("ONE");
		patient.setActive(true);
		IIdType patientId = myPatientDao.update(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition 2
		addCreatePartition(myPartitionId2, myPartitionDate2);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());

		try {
			myObservationDao.create(obs).getId().toUnqualifiedVersionless();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("Resource Patient/ONE not found, specified in path: Observation.subject"));
		}

	}

	@Test
	public void testCreate_SamePartitionReference_DefaultPartition_ByPid() {
		// Create patient in partition NULL
		addCreateDefaultPartition(myPartitionDate);
		Patient patient = new Patient();
		patient.setActive(true);
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition NULL
		addCreateDefaultPartition(myPartitionDate);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());
		IIdType obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceLink> resLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links:\n{}", resLinks.toString());
			assertEquals(2, resLinks.size());
			assertEquals(obsId.getIdPartAsLong(), resLinks.get(0).getSourceResourcePid());
			assertEquals(patientId.getIdPartAsLong(), resLinks.get(0).getTargetResourcePid());
		});
	}

	@Test
	public void testCreate_SamePartitionReference_DefaultPartition_ByForcedId() {
		// Create patient in partition NULL
		addReadDefaultPartition();
		addCreateDefaultPartition(myPartitionDate);
		Patient patient = new Patient();
		patient.setId("ONE");
		patient.setActive(true);
		IIdType patientId = myPatientDao.update(patient).getId().toUnqualifiedVersionless();

		// Create observation in partition NULL
		addCreateDefaultPartition(myPartitionDate);
		Observation obs = new Observation();
		obs.getSubject().setReference(patientId.getValue());
		IIdType obsId = myObservationDao.create(obs).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceLink> resLinks = myResourceLinkDao.findAll();
			ourLog.info("Resource links:\n{}", resLinks.toString());
			assertEquals(2, resLinks.size());
			assertEquals(obsId.getIdPartAsLong(), resLinks.get(0).getSourceResourcePid());
			assertEquals(patientId.getIdPart(), resLinks.get(0).getTargetResourceId());
		});
	}

	@Test
	public void testCreateSearchParameter_DefaultPartitionWithDate() {
		addCreateDefaultPartition(myPartitionDate);

		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		Long id = mySearchParameterDao.create(sp, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertNull(resourceTable.getPartitionId());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());
		});
	}


	@Test
	public void testCreateSearchParameter_NonDefaultPartition() {
		addCreatePartition(myPartitionId, myPartitionDate);

		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		try {
			mySearchParameterDao.create(sp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Resource type SearchParameter can not be partitioned", e.getMessage());
		}
	}

	@Test
	public void testCreate_UnknownPartition() {
		addCreatePartition(99, null);

		Patient p = new Patient();
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		try {
			myPatientDao.create(p);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("No partition exists with ID 99", e.getMessage());
		}

	}

	@Test
	public void testCreate_ServerId_NoPartition() {
		addCreateDefaultPartition();

		Patient p = new Patient();
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(RequestPartitionId.defaultPartition(), resourceTable.getPartitionId());
		});
	}


	@Test
	public void testCreate_ServerId_WithPartition() {
		createUniqueCompositeSp();
		createRequestId();

		addCreatePartition(myPartitionId, myPartitionDate);
		Organization org = new Organization();
		org.setName("org");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		addCreatePartition(myPartitionId, myPartitionDate);
		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		p.getManagingOrganization().setReferenceElement(orgId);
		Long patientId = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());

			// HFJ_RES_TAG
			List<ResourceTag> tags = myResourceTagDao.findAll();
			assertEquals(1, tags.size());
			assertEquals(myPartitionId, tags.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, tags.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_VER
			ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, 1L);
			assertEquals(myPartitionId, version.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, version.getPartitionId().getPartitionDate());

			// HFJ_HISTORY_TAG
			List<ResourceHistoryTag> historyTags = myResourceHistoryTagDao.findAll();
			assertEquals(1, historyTags.size());
			assertEquals(myPartitionId, historyTags.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, historyTags.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_VER_PROV
			assertNotNull(version.getProvenance());
			assertEquals(myPartitionId, version.getProvenance().getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, version.getProvenance().getPartitionId().getPartitionDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(myPartitionId, strings.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, strings.get(0).getPartitionId().getPartitionDate());

			// HFJ_SPIDX_DATE
			List<ResourceIndexedSearchParamDate> dates = myResourceIndexedSearchParamDateDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", dates.stream().map(ResourceIndexedSearchParamDate::toString).collect(Collectors.joining("\n * ")));
			assertEquals(2, dates.size());
			assertEquals(myPartitionId, dates.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, dates.get(0).getPartitionId().getPartitionDate());
			assertEquals(myPartitionId, dates.get(1).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, dates.get(1).getPartitionId().getPartitionDate());

			// HFJ_RES_LINK
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForSourceResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(myPartitionId, resourceLinks.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceLinks.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(3, presents.size());
			assertEquals(myPartitionId, presents.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, presents.get(0).getPartitionId().getPartitionDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceIdForUnitTest(patientId);
			assertEquals(1, uniques.size());
			assertEquals(myPartitionId, uniques.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, uniques.get(0).getPartitionId().getPartitionDate());
		});

	}

	@Test
	public void testCreate_ServerId_DefaultPartition() {
		createUniqueCompositeSp();
		createRequestId();

		addCreateDefaultPartition(myPartitionDate);
		Organization org = new Organization();
		org.setName("org");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		addCreateDefaultPartition(myPartitionDate);
		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		p.getManagingOrganization().setReferenceElement(orgId);
		Long patientId = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(null, resourceTable.getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());

			// HFJ_RES_TAG
			List<ResourceTag> tags = myResourceTagDao.findAll();
			assertEquals(1, tags.size());
			assertEquals(null, tags.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, tags.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_VER
			ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, 1L);
			assertEquals(null, version.getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, version.getPartitionId().getPartitionDate());

			// HFJ_HISTORY_TAG
			List<ResourceHistoryTag> historyTags = myResourceHistoryTagDao.findAll();
			assertEquals(1, historyTags.size());
			assertEquals(null, historyTags.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, historyTags.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_VER_PROV
			assertNotNull(version.getProvenance());
			assertEquals(null, version.getProvenance().getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, version.getProvenance().getPartitionId().getPartitionDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(null, strings.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, strings.get(0).getPartitionId().getPartitionDate());

			// HFJ_SPIDX_DATE
			List<ResourceIndexedSearchParamDate> dates = myResourceIndexedSearchParamDateDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", dates.stream().map(ResourceIndexedSearchParamDate::toString).collect(Collectors.joining("\n * ")));
			assertEquals(2, dates.size());
			assertEquals(null, dates.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, dates.get(0).getPartitionId().getPartitionDate());
			assertEquals(null, dates.get(1).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, dates.get(1).getPartitionId().getPartitionDate());

			// HFJ_RES_LINK
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForSourceResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(null, resourceLinks.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, resourceLinks.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(3, presents.size());
			assertEquals(null, presents.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, presents.get(0).getPartitionId().getPartitionDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceIdForUnitTest(patientId);
			assertEquals(1, uniques.size());
			assertEquals(null, uniques.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, uniques.get(0).getPartitionId().getPartitionDate());
		});

	}


	@Test
	public void testCreate_ForcedId_WithPartition() {
		addReadPartition(myPartitionId);
		addCreatePartition(myPartitionId, myPartitionDate);
		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

		addReadPartition(myPartitionId);
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient p = new Patient();
		p.setId("pat");
		p.getManagingOrganization().setReferenceElement(orgId);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_FORCED_ID
			List<ForcedId> forcedIds = myForcedIdDao.findAll();
			assertEquals(2, forcedIds.size());
			assertEquals(myPartitionId, forcedIds.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, forcedIds.get(0).getPartitionId().getPartitionDate());
			assertEquals(myPartitionId, forcedIds.get(1).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, forcedIds.get(1).getPartitionId().getPartitionDate());
		});

	}

	@Test
	public void testCreate_ForcedId_NoPartition() {
		addReadDefaultPartition();
		addCreateDefaultPartition();
		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

		addReadDefaultPartition();
		addCreateDefaultPartition();
		Patient p = new Patient();
		p.setId("pat");
		p.getManagingOrganization().setReferenceElement(orgId);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_FORCED_ID
			List<ForcedId> forcedIds = myForcedIdDao.findAll();
			assertEquals(2, forcedIds.size());
			assertEquals(null, forcedIds.get(0).getPartitionId().getPartitionId());
			assertEquals(null, forcedIds.get(1).getPartitionId().getPartitionId());
		});

	}

	@Test
	public void testCreate_ForcedId_DefaultPartition() {
		addReadDefaultPartition();
		addCreateDefaultPartition(myPartitionDate);
		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

		addReadDefaultPartition();
		addCreateDefaultPartition(myPartitionDate);
		Patient p = new Patient();
		p.setId("pat");
		p.getManagingOrganization().setReferenceElement(orgId);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_FORCED_ID
			List<ForcedId> forcedIds = myForcedIdDao.findAll();
			assertEquals(2, forcedIds.size());
			assertEquals(null, forcedIds.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, forcedIds.get(0).getPartitionId().getPartitionDate());
			assertEquals(null, forcedIds.get(1).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, forcedIds.get(1).getPartitionId().getPartitionDate());
		});

	}


	@Test
	public void testCreateInTransaction_ServerId_WithPartition() {
		createUniqueCompositeSp();
		createRequestId();

		addCreatePartition(myPartitionId, myPartitionDate);
		addCreatePartition(myPartitionId, myPartitionDate);

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);

		Organization org = new Organization();
		org.setId(IdType.newRandomUuid());
		org.setName("org");
		input.addEntry()
			.setFullUrl(org.getId())
			.setResource(org)
			.getRequest().setUrl("Organization").setMethod(Bundle.HTTPVerb.POST);

		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		p.getManagingOrganization().setReference(org.getId());
		input.addEntry()
			.setFullUrl(p.getId())
			.setResource(p)
			.getRequest().setUrl("Patient").setMethod(Bundle.HTTPVerb.POST);
		Bundle output = mySystemDao.transaction(mySrd, input);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Long patientId = new IdType(output.getEntry().get(1).getResponse().getLocation()).getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());
		});

	}

	@Test
	public void testUpdateResourceWithPartition() {
		createRequestId();

		// Create a resource
		addCreatePartition(myPartitionId, myPartitionDate);
		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.setActive(true);
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();
		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());
		});

		// Update that resource
		addReadPartition(myPartitionId);
		p = new Patient();
		p.setId("Patient/" + patientId);
		p.setActive(false);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());

			// HFJ_RES_VER
			int version = 2;
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, version);
			assertEquals(myPartitionId, resVer.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resVer.getPartitionId().getPartitionDate());

			// HFJ_HISTORY_TAG
			List<ResourceHistoryTag> historyTags = myResourceHistoryTagDao.findAll();
			assertEquals(2, historyTags.size());
			assertEquals(myPartitionId, historyTags.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, historyTags.get(0).getPartitionId().getPartitionDate());
			assertEquals(myPartitionId, historyTags.get(1).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, historyTags.get(1).getPartitionId().getPartitionDate());

			// HFJ_RES_VER_PROV
			assertNotNull(resVer.getProvenance());
			assertNotNull(resVer.getPartitionId());
			assertEquals(myPartitionId, resVer.getProvenance().getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resVer.getProvenance().getPartitionId().getPartitionDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(myPartitionId, strings.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, strings.get(0).getPartitionId().getPartitionDate());

		});

	}

	@Test
	public void testUpdateConditionalInPartition() {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.DISABLED);
		createRequestId();

		// Create a resource
		addCreatePartition(myPartitionId, myPartitionDate);
		addReadPartition(myPartitionId);
		Patient p = new Patient();
		p.setActive(false);
		p.addIdentifier().setValue("12345");
		Long patientId = myPatientDao.update(p, "Patient?identifier=12345", mySrd).getId().getIdPartAsLong();
		runInTransaction(() -> {
			// HFJ_RESOURCE
			assertEquals(1, myResourceTableDao.count());
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());

			// HFJ_SPIDX_TOKEN
			ourLog.info("Tokens:\n * {}", myResourceIndexedSearchParamTokenDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			assertEquals(3, myResourceIndexedSearchParamTokenDao.countForResourceId(patientId));
		});

		// Update that resource
		addReadPartition(myPartitionId);
		p = new Patient();
		p.setActive(true);
		p.addIdentifier().setValue("12345");
		Long patientId2 = myPatientDao.update(p, "Patient?identifier=12345", mySrd).getId().getIdPartAsLong();

		assertEquals(patientId, patientId2);

		runInTransaction(() -> {
			// HFJ_RESOURCE
			assertEquals(1, myResourceTableDao.count());
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myPartitionId, resourceTable.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceTable.getPartitionId().getPartitionDate());

			// HFJ_SPIDX_TOKEN
			ourLog.info("Tokens:\n * {}", myResourceIndexedSearchParamTokenDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			assertEquals(3, myResourceIndexedSearchParamTokenDao.countForResourceId(patientId));

			// HFJ_RES_VER
			int version = 2;
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, version);
			assertEquals(myPartitionId, resVer.getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resVer.getPartitionId().getPartitionDate());

		});

	}

	@Test
	public void testRead_PidId_AllPartitions() {
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue());

		{
			addReadAllPartitions();
			myCaptureQueriesListener.clear();
			IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId1, gotId1);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);

			// Only the read columns should be used, no criteria use partition
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID as "));
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		}
		{
			addReadAllPartitions();
			IdType gotId2 = myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId2, gotId2);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);

			// Only the read columns should be used, no criteria use partition
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID as "));
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		}
	}

	@Test
	public void testRead_PidId_SpecificPartition() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue());

		// Read in correct Partition
		{
			myCaptureQueriesListener.clear();
			addReadPartition(1);
			IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId1, gotId1);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);

			// Only the read columns should be used, no criteria use partition
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID as "));
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		}

		// Read in null Partition
		{
			addReadPartition(1);
			try {
				myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
				fail();
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage(), matchesPattern("Resource Patient/[0-9]+ is not known"));
			}
		}

		// Read in wrong Partition
		{
			addReadPartition(1);
			try {
				myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
				fail();
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage(), matchesPattern("Resource Patient/[0-9]+ is not known"));
			}
		}
	}

	@Test
	public void testRead_PidId_MultiplePartitions() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue());

		// Read in correct Partition
		{
			myCaptureQueriesListener.clear();
			myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionId(1, null));
			IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId1, gotId1);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);

			// Only the read columns should be used, no criteria use partition
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID as "));
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		}

		// Read in null Partition
		{
			addReadPartition(1);
			try {
				myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
				fail();
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage(), matchesPattern("Resource Patient/[0-9]+ is not known"));
			}
		}

		// Read in wrong Partition
		{
			addReadPartition(1);
			try {
				myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
				fail();
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage(), matchesPattern("Resource Patient/[0-9]+ is not known"));
			}
		}
	}

	@Test
	public void testRead_PidId_DefaultPartition() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		createPatient(withPartition(2), withActiveTrue());

		// Read in correct Partition
		{
			myCaptureQueriesListener.clear();
			addReadDefaultPartition();
			IdType gotId1 = myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientIdNull, gotId1);

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);

			// Only the read columns should be used, no criteria use partition
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID as "));
			assertEquals(2, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		}

		// Read in wrong Partition
		{
			addReadDefaultPartition();
			try {
				myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
				fail();
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage(), matchesPattern("Resource Patient/[0-9]+ is not known"));
			}
		}
	}

	@Test
	public void testRead_ForcedId_SpecificPartition() {
		IIdType patientIdNull = createPatient(withPutPartition(null), withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(withPutPartition(1), withActiveTrue(), withId("ONE"));
		IIdType patientId2 = createPatient(withPutPartition(2), withActiveTrue(), withId("TWO"));

		// Read in correct Partition
		addReadPartition(1);
		IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
		assertEquals(patientId1, gotId1);

		// Read in null Partition
		addReadPartition(1);
		try {
			myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/NULL is not known"));
		}

		// Read in wrong Partition
		addReadPartition(1);
		try {
			myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/TWO is not known"));
		}
	}

	@Test
	public void testRead_ForcedId_DefaultPartition() {
		IIdType patientIdNull = createPatient(withPutPartition(null), withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(withPutPartition(1), withActiveTrue(), withId("ONE"));
		IIdType patientId2 = createPatient(withPutPartition(2), withActiveTrue(), withId("TWO"));

		// Read in correct Partition
		addReadDefaultPartition();
		IdType gotId1 = myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
		assertEquals(patientIdNull, gotId1);

		// Read in null Partition
		addReadDefaultPartition();
		try {
			myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/ONE is not known"));
		}

		// Read in wrong Partition
		addReadDefaultPartition();
		try {
			myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/TWO is not known"));
		}
	}

	@Test
	public void testRead_ForcedId_AllPartition() {
		IIdType patientIdNull = createPatient(withPutPartition(null), withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(withPutPartition(1), withActiveTrue(), withId("ONE"));
		createPatient(withPutPartition(2), withActiveTrue(), withId("TWO"));
		{
			addReadAllPartitions();
			IdType gotId1 = myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientIdNull, gotId1);
		}
		{
			addReadAllPartitions();
			IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId1, gotId1);
		}
	}

	@Test
	public void testRead_ForcedId_AllPartition_WithDuplicate() {
		dropForcedIdUniqueConstraint();
		IIdType patientIdNull = createPatient(withPutPartition(null), withActiveTrue(), withId("FOO"));
		IIdType patientId1 = createPatient(withPutPartition(1), withActiveTrue(), withId("FOO"));
		IIdType patientId2 = createPatient(withPutPartition(2), withActiveTrue(), withId("FOO"));
		assertEquals(patientIdNull, patientId1);
		assertEquals(patientIdNull, patientId2);

		{
			addReadAllPartitions();
			try {
				myPatientDao.read(patientIdNull, mySrd);
				fail();
			} catch (PreconditionFailedException e) {
				assertEquals("Non-unique ID specified, can not process request", e.getMessage());
			}
		}
	}

	@Test
	public void testSearch_MissingParamString_SearchAllPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		IIdType patientId2 = createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadAllPartitions();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_ACTIVE, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'true'"));
		}

		// :missing=false
		{
			addReadAllPartitions();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_FAMILY, new StringParam().setMissing(false));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'false'"));
		}
	}


	@Test
	public void testSearch_MissingParamString_SearchOnePartition() {
		createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadPartition(1);
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_ACTIVE, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientId1));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID = '1'"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'true'"), searchSql);
		}

		// :missing=false
		{
			addReadPartition(1);
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_FAMILY, new StringParam().setMissing(false));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientId1));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID = '1'"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'false'"));
		}
	}

	@Test
	public void testSearch_MissingParamString_SearchDefaultPartition() {
		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadDefaultPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_ACTIVE, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID IS NULL"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'true'"));
		}

		// :missing=false
		{
			addReadDefaultPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_FAMILY, new StringParam().setMissing(false));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID IS NULL"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING = 'false'"));
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchAllPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		IIdType patientId2 = createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadAllPartitions();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_GENERAL_PRACTITIONER, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE = '1919227773735728687'"));
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchOnePartition_IncludePartitionInHashes() {
		myPartitionSettings.setIncludePartitionInSearchHashes(true);

		createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadPartition(1);
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_GENERAL_PRACTITIONER, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientId1));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID = '1'"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE = '-3438137196820602023'"), searchSql);
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchOnePartition_DontIncludePartitionInHashes() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadPartition(1);
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_GENERAL_PRACTITIONER, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientId1));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID = '1'"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE = '1919227773735728687'"), searchSql);
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchDefaultPartition() {
		IIdType patientIdDefault = createPatient(withPartition(null), withFamily("FAMILY"));
		createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		// :missing=true
		{
			addReadDefaultPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_GENERAL_PRACTITIONER, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdDefault));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID IS NULL"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"), searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE = '1919227773735728687'"), searchSql);
		}
	}


	@Test
	public void testSearch_NoParams_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue());

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
	}

	@Test
	public void testSearch_NoParams_SearchOnePartition() {
		createPatient(withPartition(null), withActiveTrue());
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue());
		createPatient(withPartition(2), withActiveTrue());

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
	}

	@Test
	public void testSearch_DateParam_SearchAllPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withBirthdate("2020-04-20"));
		IIdType patientId1 = createPatient(withPartition(1), withBirthdate("2020-04-20"));
		IIdType patientId2 = createPatient(withPartition(2), withBirthdate("2020-04-20"));
		createPatient(withPartition(null), withBirthdate("2021-04-20"));
		createPatient(withPartition(1), withBirthdate("2021-04-20"));
		createPatient(withPartition(2), withBirthdate("2021-04-20"));

		// Date param

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-04-20"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// Date OR param

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateOrListParam().addOr(new DateParam("2020-04-20")).addOr(new DateParam("2020-04-22")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// Date AND param

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateAndListParam().addAnd(new DateOrListParam().addOr(new DateParam("2020"))).addAnd(new DateOrListParam().addOr(new DateParam("2020-04-20"))));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// DateRangeParam

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateRangeParam(new DateParam("2020-01-01"), new DateParam("2020-04-25")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

	}


	@Test
	public void testSearch_DateParam_SearchSpecificPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withBirthdate("2020-04-20"));
		IIdType patientId1 = createPatient(withPartition(1), withBirthdate("2020-04-20"));
		IIdType patientId2 = createPatient(withPartition(2), withBirthdate("2020-04-20"));
		createPatient(withPartition(null), withBirthdate("2021-04-20"));
		createPatient(withPartition(1), withBirthdate("2021-04-20"));
		createPatient(withPartition(2), withBirthdate("2021-04-20"));

		// Date param

		ourLog.info("Date indexes:\n * {}", myResourceIndexedSearchParamDateDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		addReadPartition(1);
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-04-20"));
		map.setLoadSynchronous(true);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(map);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"), searchSql);

		// Date OR param

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateOrListParam().addOr(new DateParam("2020-04-20")).addOr(new DateParam("2020-04-22")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// Date AND param

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateAndListParam().addAnd(new DateOrListParam().addOr(new DateParam("2020"))).addAnd(new DateOrListParam().addOr(new DateParam("2020-04-20"))));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// DateRangeParam

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateRangeParam(new DateParam("2020-01-01"), new DateParam("2020-04-25")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

	}


	@Test
	public void testSearch_DateParam_SearchDefaultPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withBirthdate("2020-04-20"));
		IIdType patientId1 = createPatient(withPartition(1), withBirthdate("2020-04-20"));
		IIdType patientId2 = createPatient(withPartition(2), withBirthdate("2020-04-20"));
		createPatient(withPartition(null), withBirthdate("2021-04-20"));
		createPatient(withPartition(1), withBirthdate("2021-04-20"));
		createPatient(withPartition(2), withBirthdate("2021-04-20"));

		// Date param

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-04-20"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// Date OR param

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateOrListParam().addOr(new DateParam("2020-04-20")).addOr(new DateParam("2020-04-22")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// Date AND param

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateAndListParam().addAnd(new DateOrListParam().addOr(new DateParam("2020"))).addAnd(new DateOrListParam().addOr(new DateParam("2020-04-20"))));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

		// DateRangeParam

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateRangeParam(new DateParam("2020-01-01"), new DateParam("2020-04-25")));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(2, StringUtils.countMatches(searchSql, "SP_VALUE_LOW"));

	}

	@Test
	public void testSearch_HasParam_SearchOnePartition() {
		addReadPartition(1);
		addCreatePartition(1, null);
		Organization org = new Organization();
		org.setId("ORG");
		org.setName("ORG");
		myOrganizationDao.update(org);

		addReadPartition(1);
		addCreatePartition(1, null);
		Practitioner practitioner = new Practitioner();
		practitioner.setId("PRACT");
		practitioner.addName().setFamily("PRACT");
		myPractitionerDao.update(practitioner);

		addReadPartition(1);
		addCreatePartition(1, null);
		PractitionerRole role = new PractitionerRole();
		role.setId("ROLE");
		role.getPractitioner().setReference("Practitioner/PRACT");
		role.getOrganization().setReference("Organization/ORG");
		myPractitionerRoleDao.update(role);

		addReadPartition(1);
		SearchParameterMap params = SearchParameterMap.newSynchronous();
		HasAndListParam value = new HasAndListParam();
		value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "_id", "ROLE")));
		params.add("_has", value);
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPractitionerDao.search(params);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(1);
		assertEquals(1, outcome.getResources(0, 1).size());

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
	}


	@Test
	public void testSearch_StringParam_SearchAllPartitions() {
		myPartitionSettings.setIncludePartitionInSearchHashes(false);

		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		IIdType patientId2 = createPatient(withPartition(2), withFamily("FAMILY"));

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_NORMALIZED"));
	}

	@Test
	public void testSearch_StringParam_SearchDefaultPartition() {
		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		addReadDefaultPartition();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		searchSql = searchSql.toUpperCase();
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID IS NULL"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_NORMALIZED"));
	}

	@Test
	public void testSearch_StringParam_SearchOnePartition() {
		createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_NORMALIZED"));
	}

	@Test
	public void testSearch_StringParam_SearchAllPartitions_IncludePartitionInHashes() {
		myPartitionSettings.setIncludePartitionInSearchHashes(true);

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		try {
			IBundleProvider value = myPatientDao.search(map);
			value.size();
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals("This server is not configured to support search against all partitions", e.getMessage());
		}
	}

	@Test
	public void testSearch_StringParam_SearchDefaultPartition_IncludePartitionInHashes() {
		myPartitionSettings.setIncludePartitionInSearchHashes(true);

		IIdType patientIdNull = createPatient(withPartition(null), withFamily("FAMILY"));
		createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		addReadDefaultPartition();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		searchSql = searchSql.toUpperCase();
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID IS NULL"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_NORMALIZED"));
	}

	@Test
	public void testSearch_StringParam_SearchOnePartition_IncludePartitionInHashes() {
		myPartitionSettings.setIncludePartitionInSearchHashes(true);

		createPatient(withPartition(null), withFamily("FAMILY"));
		IIdType patientId1 = createPatient(withPartition(1), withFamily("FAMILY"));
		createPatient(withPartition(2), withFamily("FAMILY"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_FAMILY, new StringParam("FAMILY"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "SP_VALUE_NORMALIZED"));
	}

	@Test
	public void testSearch_TagNotParam_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"), withIdentifier("http://foo", "bar"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"), withIdentifier("http://foo", "bar"));
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code2"));
		createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code2"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code2"));

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));

		// And with another param

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.add(Patient.SP_IDENTIFIER, new TokenParam("http://foo", "bar"));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1));

		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "t1.HASH_SYS_AND_VALUE ="), searchSql);


	}

	@Test
	public void testSearch_TagNotParam_SearchDefaultPartition() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));

		addReadDefaultPartition();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID IS NULL"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));

		assertThat(ids.toString(), ids, Matchers.contains(patientIdNull));
	}

	@Test
	public void testSearch_TagNotParam_SearchOnePartition() {
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code2"));
		createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code2"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code2"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));
	}

	@Test
	public void testSearch_TagParam_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));
	}

	@Test
	public void testSearch_TagParam_SearchOnePartition() {
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code"));
		map.setLoadSynchronous(true);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);

		assertEquals(2, StringUtils.countMatches(searchSql, "JOIN"));
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));
	}

	@Test
	public void testSearch_TagParamNot_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId2 = createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientIdNull, patientId1, patientId2));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));
	}

	@Test
	public void testSearch_TagParamNot_SearchOnePartition() {
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"));
		createPatient(withPartition(null), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(withPartition(1), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(withPartition(2), withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code2").setModifier(TokenParamModifier.NOT));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM = 'http://system'"));
	}

	@Test
	public void testSearch_UniqueParam_SearchAllPartitions() {
		createUniqueCompositeSp();

		IIdType id = createPatient(withPartition(1), withBirthdate("2020-01-01"));

		addReadAllPartitions();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-01-01"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(id));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "IDX_STRING = 'Patient?birthdate=2020-01-01'"));
	}


	@Test
	public void testSearch_UniqueParam_SearchOnePartition() {
		createUniqueCompositeSp();

		IIdType id = createPatient(withPartition(1), withBirthdate("2020-01-01"));

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-01-01"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(id));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "IDX_STRING = 'Patient?birthdate=2020-01-01'"));

		// Same query, different partition
		addReadPartition(2);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Patient.SP_BIRTHDATE, new DateParam("2020-01-01"));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.empty());

	}

	@Test
	public void testSearch_RefParam_TargetPid_SearchOnePartition() {
		createUniqueCompositeSp();

		IIdType patientId = createPatient(withPartition(myPartitionId), withBirthdate("2020-01-01"));
		IIdType observationId = createObservation(withPartition(myPartitionId), withSubject(patientId));

		addReadPartition(myPartitionId);
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		IBundleProvider results = myObservationDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(observationId));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID = '1'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.SRC_PATH = 'Observation.subject'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.TARGET_RESOURCE_ID = '" + patientId.getIdPartAsLong() + "'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

		// Same query, different partition
		addReadPartition(2);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		results = myObservationDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.empty());

	}

	@Test
	public void testSearch_RefParam_TargetPid_SearchDefaultPartition() {
		createUniqueCompositeSp();

		IIdType patientId = createPatient(withPartition(null), withBirthdate("2020-01-01"));
		IIdType observationId = createObservation(withPartition(null), withSubject(patientId));

		addReadDefaultPartition();
		;
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		IBundleProvider results = myObservationDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(observationId));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.PARTITION_ID IS NULL"));
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.SRC_PATH = 'Observation.subject'"));
		assertEquals(1, StringUtils.countMatches(searchSql, "t0.TARGET_RESOURCE_ID = '" + patientId.getIdPartAsLong() + "'"));
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));

		// Same query, different partition
		addReadPartition(2);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		results = myObservationDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.empty());

	}

	@Test
	public void testSearch_RefParam_TargetForcedId_SearchOnePartition() {
		createUniqueCompositeSp();

		IIdType patientId = createPatient(withPutPartition(myPartitionId), withId("ONE"), withBirthdate("2020-01-01"));
		IIdType observationId = createObservation(withPartition(myPartitionId), withSubject(patientId));

		addReadPartition(myPartitionId);
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		IBundleProvider results = myObservationDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.contains(observationId));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "forcedid0_.PARTITION_ID='1'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "and forcedid0_.RESOURCE_TYPE='Patient'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

		// Same query, different partition
		addReadPartition(2);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		results = myObservationDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.empty());

	}

	@Test
	public void testSearch_RefParam_TargetForcedId_SearchDefaultPartition() {
		createUniqueCompositeSp();

		IIdType patientId = createPatient(withPutPartition(null), withId("ONE"), withBirthdate("2020-01-01"));
		IIdType observationId = createObservation(withPartition(null), withSubject(patientId));

		addReadDefaultPartition();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		IBundleProvider results = myObservationDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(observationId));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "forcedid0_.PARTITION_ID is null"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "forcedid0_.RESOURCE_TYPE='Patient'"), searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

		// Same query, different partition
		addReadPartition(2);
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.add(Observation.SP_SUBJECT, new ReferenceParam(patientId));
		map.setLoadSynchronous(true);
		results = myObservationDao.search(map);
		ids = toUnqualifiedVersionlessIds(results);
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(ids, Matchers.empty());

	}

	@Test
	public void testUpdate_ResourcePreExistsInWrongPartition() {
		IIdType patientId = createPatient(withPutPartition(null), withId("ONE"), withBirthdate("2020-01-01"));

		addReadAllPartitions();

		Patient p = new Patient();
		p.setId(patientId.toUnqualifiedVersionless());
		p.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(p);
	}

	@Test
	public void testHistory_Instance_CorrectPartition() {
		IIdType id = createPatient(withPartition(1), withBirthdate("2020-01-01"));

		// Update the patient
		addReadPartition(myPartitionId);
		Patient p = new Patient();
		p.setActive(false);
		p.setId(id);
		myPatientDao.update(p);

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id.withVersion("2").getValue(), id.withVersion("1").getValue()));

		assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Resolve resource
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID="), searchSql);

		// Fetch history resource
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID"), searchSql);

		// Fetch history resource
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID="), searchSql.replace(" ", "").toUpperCase());
		assertEquals(0, countMatches(searchSql, "PARTITION_IDIN"), searchSql.replace(" ", "").toUpperCase());

		// Fetch history resource
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID="), searchSql.replace(" ", "").toUpperCase());
		assertEquals(0, countMatches(searchSql, "PARTITION_IDIN"), searchSql.replace(" ", "").toUpperCase());
	}

	@Test
	public void testHistory_Instance_WrongPartition() {
		IIdType id = createPatient(withPartition(1), withBirthdate("2020-01-01"));

		// Update the patient
		addReadPartition(myPartitionId);
		Patient p = new Patient();
		p.setActive(false);
		p.setId(id);
		myPatientDao.update(p);

		addReadPartition(2);
		try {
			myPatientDao.history(id, null, null, mySrd);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	@Test
	public void testHistory_Instance_DefaultPartition() {
		IIdType id = createPatient(withPartition(null), withBirthdate("2020-01-01"));

		// Update the patient
		addReadDefaultPartition();
		Patient p = new Patient();
		p.setActive(false);
		p.setId(id);
		myPatientDao.update(p);

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id.withVersion("2").getValue(), id.withVersion("1").getValue()));

		assertEquals(4, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Resolve resource
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID="));

		// Fetch history resource
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID="));

		// Fetch history resource
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID="));

		// Fetch history resource
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID="));
	}

	@Test
	public void testHistory_Instance_AllPartitions() {
		IIdType id = createPatient(withPartition(1), withBirthdate("2020-01-01"));

		// Update the patient
		addReadPartition(myPartitionId);
		Patient p = new Patient();
		p.setActive(false);
		p.setId(id);
		myPatientDao.update(p);

		addReadAllPartitions();
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.history(id, null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id.withVersion("2").getValue(), id.withVersion("1").getValue()));
	}

	@Test
	public void testHistory_Server() {
		addReadAllPartitions();
		try {
			mySystemDao.history(null, null, mySrd).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Type- and Server- level history operation not supported across partitions on partitioned server", e.getMessage());
		}
	}

	@Test
	public void testHistory_Server_SpecificPartition() {
		IIdType id1A = createPatient(withPartition(1), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		IIdType id1B = createPatient(withPartition(1), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		IBundleProvider results = mySystemDao.history(null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id1B.withVersion("1").getValue(), id1A.withVersion("1").getValue()));

		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Count
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(1, countMatches(searchSql, "count("));
		assertEquals(1, countMatches(searchSql, "PARTITION_ID='1'"));

		// Fetch history
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(1, countMatches(searchSql, "PARTITION_ID='1'"));

		// Fetch history resource
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID="), searchSql.replace(" ", "").toUpperCase());
		assertEquals(0, countMatches(searchSql, "PARTITION_IDIN"), searchSql.replace(" ", "").toUpperCase());
	}

	@Test
	public void testHistory_Server_DefaultPartition() {
		IIdType id1A = createPatient(withPartition(null), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		IIdType id1B = createPatient(withPartition(null), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		IBundleProvider results = mySystemDao.history(null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id1B.withVersion("1").getValue(), id1A.withVersion("1").getValue()));

		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Count
		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(1, countMatches(searchSql, "PARTITION_ID is null"), searchSql);

		// Fetch history
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(1, countMatches(searchSql, "PARTITION_ID is null"), searchSql);

		// Fetch history resource
		searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true);
		ourLog.info("SQL:{}", searchSql);
		assertEquals(0, countMatches(searchSql, "PARTITION_ID="), searchSql.replace(" ", "").toUpperCase());
		assertEquals(0, countMatches(searchSql, "PARTITION_IDIN"), searchSql.replace(" ", "").toUpperCase());
	}

	@Test
	public void testHistory_Type_AllPartitions() {
		addReadAllPartitions();
		try {
			myPatientDao.history(null, null, mySrd).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Type- and Server- level history operation not supported across partitions on partitioned server", e.getMessage());
		}
	}

	@Test
	public void testHistory_Type_SpecificPartition() {
		IIdType id1A = createPatient(withPartition(1), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		IIdType id1B = createPatient(withPartition(1), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));

		addReadPartition(1);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.history(null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id1B.withVersion("1").getValue(), id1A.withVersion("1").getValue()));

		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Count
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(1, countMatches(sql, "count("));
		assertEquals(1, countMatches(sql, "PARTITION_ID='1'"));

		// Fetch history resources
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(1, countMatches(sql, "PARTITION_ID='1'"));

		// Resolve forced ID
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true);
		ourLog.info("SQL:{}", sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID='1'"));
	}


	@Test
	public void testHistory_Type_DefaultPartition() {
		IIdType id1A = createPatient(withPartition(null), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		IIdType id1B = createPatient(withPartition(null), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));
		sleepAtLeast(10);
		createPatient(withPartition(2), withBirthdate("2020-01-01"));

		addReadDefaultPartition();
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.history(null, null, mySrd);
		assertEquals(2, results.sizeOrThrowNpe());
		List<String> ids = toUnqualifiedIdValues(results);
		assertThat(ids, Matchers.contains(id1B.withVersion("1").getValue(), id1A.withVersion("1").getValue()));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());

		// Resolve resource
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true).toUpperCase();
		assertEquals(1, countMatches(sql, "PARTITION_ID IS NULL"));
		assertEquals(1, countMatches(sql, "PARTITION_ID"));

		// Fetch history resource
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(true, true).toUpperCase();
		assertEquals(1, countMatches(sql, "PARTITION_ID IS NULL"));

		// Resolve forced IDs
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(true, true).toUpperCase();
		assertEquals(1, countMatches(sql, "FORCEDID0_.RESOURCE_PID IN"), sql);
		assertEquals(0, countMatches(sql, "PARTITION_ID IS NULL"), sql);
	}

	@Test
	public void testPartitionNotify() {
		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_PARTITION_SELECTED, interceptor);
		try {
			createPatient(withPartition(1), withBirthdate("2020-01-01"));

			ArgumentCaptor<HookParams> captor = ArgumentCaptor.forClass(HookParams.class);
			verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_PARTITION_SELECTED), captor.capture());

			RequestPartitionId partitionId = captor.getValue().get(RequestPartitionId.class);
			assertEquals(1, partitionId.getPartitionIds().get(0).intValue());
			assertEquals("PART-1", partitionId.getPartitionNames().get(0));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	private void createUniqueCompositeSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();
	}


	private void dropForcedIdUniqueConstraint() {
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("alter table " + ForcedId.HFJ_FORCED_ID + " drop constraint " + ForcedId.IDX_FORCEDID_TYPE_FID).executeUpdate();
		});
		myHaveDroppedForcedIdUniqueConstraint = true;
	}

	private void addCreatePartition(Integer thePartitionId, LocalDate thePartitionDate) {
		Validate.notNull(thePartitionId);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(thePartitionId, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(requestPartitionId);
	}

	private void addCreateDefaultPartition() {
		myPartitionInterceptor.addCreatePartition(RequestPartitionId.defaultPartition());
	}

	private void addCreateDefaultPartition(LocalDate thePartitionDate) {
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(null, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(requestPartitionId);
	}

	private void addReadPartition(Integer thePartitionId) {
		Validate.notNull(thePartitionId);
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionId(thePartitionId, null));
	}

	private void addReadDefaultPartition() {
		myPartitionInterceptor.addReadPartition(RequestPartitionId.defaultPartition());
	}

	private void addReadAllPartitions() {
		myPartitionInterceptor.addReadPartition(RequestPartitionId.allPartitions());
	}

	public void createRequestId() {
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
	}

	private Consumer<IBaseResource> withPartition(Integer thePartitionId) {
		return t -> {
			if (thePartitionId != null) {
				addCreatePartition(thePartitionId, null);
			} else {
				addCreateDefaultPartition();
			}
		};
	}

	private Consumer<IBaseResource> withPutPartition(Integer thePartitionId) {
		return t -> {
			if (thePartitionId != null) {
				addReadPartition(thePartitionId);
				addCreatePartition(thePartitionId, null);
			} else {
				addReadDefaultPartition();
				addCreateDefaultPartition();
			}
		};
	}

	@Interceptor
	public static class MyReadWriteInterceptor extends MyWriteInterceptor {


		private final List<RequestPartitionId> myReadRequestPartitionIds = new ArrayList<>();

		public void addReadPartition(RequestPartitionId theRequestPartitionId) {
			myReadRequestPartitionIds.add(theRequestPartitionId);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
			RequestPartitionId retVal = myReadRequestPartitionIds.remove(0);
			ourLog.info("Returning partition for read: {}", retVal);
			return retVal;
		}

		@Override
		public void assertNoRemainingIds() {
			super.assertNoRemainingIds();
			assertEquals(0, myReadRequestPartitionIds.size());
		}

	}

	@Interceptor
	public static class MyWriteInterceptor {


		private final List<RequestPartitionId> myCreateRequestPartitionIds = new ArrayList<>();

		public void addCreatePartition(RequestPartitionId theRequestPartitionId) {
			myCreateRequestPartitionIds.add(theRequestPartitionId);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public RequestPartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
			assertNotNull(theResource);
			RequestPartitionId retVal = myCreateRequestPartitionIds.remove(0);
			ourLog.info("Returning partition for create: {}", retVal);
			return retVal;
		}

		public void assertNoRemainingIds() {
			assertEquals(0, myCreateRequestPartitionIds.size());
		}

	}

}
