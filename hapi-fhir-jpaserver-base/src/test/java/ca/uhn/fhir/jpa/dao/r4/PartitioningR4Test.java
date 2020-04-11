package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.partition.IPartitionConfigSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class PartitioningR4Test extends BaseJpaR4SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PartitioningR4Test.class);

	private MyInterceptor myPartitionInterceptor;
	private LocalDate myPartitionDate;
	private int myPartitionId;
	private boolean myHaveDroppedForcedIdUniqueConstraint;
	@Autowired
	private IPartitionConfigSvc myPartitionConfigSvc;

	@After
	public void after() {
		myPartitionInterceptor.assertNoRemainingIds();

		myDaoConfig.setPartitioningEnabled(new DaoConfig().isPartitioningEnabled());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyInterceptor);
		myInterceptor = null;

		if (myHaveDroppedForcedIdUniqueConstraint) {
			runInTransaction(() -> {
				myEntityManager.createNativeQuery("delete from HFJ_FORCED_ID").executeUpdate();
				myEntityManager.createNativeQuery("alter table HFJ_FORCED_ID add constraint IDX_FORCEDID_TYPE_FID unique (RESOURCE_TYPE, FORCED_ID)");
			});
		}
	}

	@Override
	@Before
	public void before() throws ServletException {
		super.before();

		myDaoConfig.setPartitioningEnabled(true);
		myDaoConfig.setUniqueIndexesEnabled(true);
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionId = 3;

		myPartitionInterceptor = new MyInterceptor();
		myInterceptorRegistry.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName("PART-1"));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName("PART-2"));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(3).setName("PART-3"));
	}

	@Test
	public void testCreateSearchParameter_DefaultPartition() {
		addCreateNoPartition();

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
			assertNull(resourceTable.getPartitionId());
		});
	}

	@Test
	public void testCreateSearchParameter_DefaultPartitionWithDate() {
		addCreateNoPartitionId(myPartitionDate);

		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("extpatorg");
		sp.setName("extpatorg");
		sp.setExpression("Patient.extension('http://patext').value.as(Reference)");
		Long id = mySearchParameterDao.create(sp).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(id).orElseThrow(IllegalArgumentException::new);
			assertEquals(null, resourceTable.getPartitionId().getPartitionId());
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
			mySearchParameterDao.create(sp);
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
		} catch (InvalidRequestException e) {
			assertEquals("Unknown partition ID: 99", e.getMessage());
		}

	}

	@Test
	public void testCreate_ServerId_NoPartition() {
		addCreateNoPartition();

		Patient p = new Patient();
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertNull(resourceTable.getPartitionId());
		});
	}


	@Test
	public void testCreate_ServerId_WithPartition() {
		createUniqueCompositeSp();
		createRequestId();

		addCreatePartition(myPartitionId, myPartitionDate);
		addCreatePartition(myPartitionId, myPartitionDate);

		Organization org = new Organization();
		org.setName("org");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

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
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(myPartitionId, resourceLinks.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, resourceLinks.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(myPartitionId, presents.size());
			assertEquals(myPartitionId, presents.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, presents.get(0).getPartitionId().getPartitionDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceId(patientId);
			assertEquals(1, uniques.size());
			assertEquals(myPartitionId, uniques.get(0).getPartitionId().getPartitionId().intValue());
			assertEquals(myPartitionDate, uniques.get(0).getPartitionId().getPartitionDate());
		});

	}

	@Test
	public void testCreate_ServerId_DefaultPartition() {
		createUniqueCompositeSp();
		createRequestId();

		addCreateNoPartitionId(myPartitionDate);
		addCreateNoPartitionId(myPartitionDate);

		Organization org = new Organization();
		org.setName("org");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

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
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(null, resourceLinks.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, resourceLinks.get(0).getPartitionId().getPartitionDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(3, presents.size());
			assertEquals(null, presents.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, presents.get(0).getPartitionId().getPartitionDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceId(patientId);
			assertEquals(1, uniques.size());
			assertEquals(null, uniques.get(0).getPartitionId().getPartitionId());
			assertEquals(myPartitionDate, uniques.get(0).getPartitionId().getPartitionDate());
		});

	}


	@Test
	public void testCreate_ForcedId_WithPartition() {
		addCreatePartition(myPartitionId, myPartitionDate);
		addCreatePartition(myPartitionId, myPartitionDate);

		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

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
		addCreateNoPartition();
		addCreateNoPartition();

		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.setId("pat");
		p.getManagingOrganization().setReferenceElement(orgId);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_FORCED_ID
			List<ForcedId> forcedIds = myForcedIdDao.findAll();
			assertEquals(2, forcedIds.size());
			assertEquals(null, forcedIds.get(0).getPartitionId());
			assertEquals(null, forcedIds.get(1).getPartitionId());
		});

	}

	@Test
	public void testCreate_ForcedId_DefaultPartition() {
		addCreateNoPartitionId(myPartitionDate);
		addCreateNoPartitionId(myPartitionDate);

		Organization org = new Organization();
		org.setId("org");
		org.setName("org");
		IIdType orgId = myOrganizationDao.update(org).getId().toUnqualifiedVersionless();

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
	public void testUpdateResourceWithPartition() {
		createRequestId();
		addCreatePartition(3, LocalDate.of(2020, Month.JANUARY, 14));
		addCreatePartition(3, LocalDate.of(2020, Month.JANUARY, 14));

		// Create a resource
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
	public void testRead_PidId_AllPartitions() {
		IIdType patientId1 = createPatient(1, withActiveTrue());
		IIdType patientId2 = createPatient(2, withActiveTrue());

		{
			addReadPartition(null);
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
			addReadPartition(null);
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
		IIdType patientIdNull = createPatient(null, withActiveTrue());
		IIdType patientId1 = createPatient(1, withActiveTrue());
		IIdType patientId2 = createPatient(2, withActiveTrue());

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
	public void testRead_PidId_DefaultPartition() {
		IIdType patientIdNull = createPatient(null, withActiveTrue());
		IIdType patientId1 = createPatient(1, withActiveTrue());
		createPatient(2, withActiveTrue());

		// Read in correct Partition
		{
			myCaptureQueriesListener.clear();
			addDefaultReadPartition();
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
			addDefaultReadPartition();
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
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withId("ONE"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withId("TWO"));

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
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withId("ONE"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withId("TWO"));

		// Read in correct Partition
		addDefaultReadPartition();
		IdType gotId1 = myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
		assertEquals(patientIdNull, gotId1);

		// Read in null Partition
		addDefaultReadPartition();
		try {
			myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/ONE is not known"));
		}

		// Read in wrong Partition
		addDefaultReadPartition();
		try {
			myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
			fail();
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage(), matchesPattern("Resource Patient/TWO is not known"));
		}
	}

	@Test
	public void testRead_ForcedId_AllPartition() {
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withId("NULL"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withId("ONE"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withId("TWO"));
		{
			addReadPartition(null);
			IdType gotId1 = myPatientDao.read(patientIdNull, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientIdNull, gotId1);
		}
		{
			addReadPartition(null);
			IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId1, gotId1);
		}
		{
			// Read in wrong Partition
			addReadPartition(null);
			IdType gotId1 = myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
			assertEquals(patientId2, gotId1);
		}
	}

	@Test
	public void testRead_ForcedId_AllPartition_WithDuplicate() {
		dropForcedIdUniqueConstraint();
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withId("FOO"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withId("FOO"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withId("FOO"));
		assertEquals(patientIdNull, patientId1);
		assertEquals(patientIdNull, patientId2);

		{
			addReadPartition(null);
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
		IIdType patientIdNull = createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		IIdType patientId2 = createPatient(2, withFamily("FAMILY"));

		// :missing=true
		{
			addReadPartition(null);
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
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='true'"));
		}

		// :missing=false
		{
			addReadPartition(null);
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
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='false'"));
		}
	}


	@Test
	public void testSearch_MissingParamString_SearchOnePartition() {
		createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

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
			assertEquals(1, StringUtils.countMatches(searchSql, "myparamsto1_.PARTITION_ID='1'"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='true'"));
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
			assertEquals(1, StringUtils.countMatches(searchSql, "myparamsst1_.PARTITION_ID='1'"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='false'"));
		}
	}

	@Test
	public void testSearch_MissingParamString_SearchDefaultPartition() {
		IIdType patientIdNull = createPatient(null, withFamily("FAMILY"));
		createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

		// :missing=true
		{
			addDefaultReadPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_ACTIVE, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID is null"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='true'"));
		}

		// :missing=false
		{
			addDefaultReadPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_FAMILY, new StringParam().setMissing(false));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdNull));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID is null"));
			assertEquals(1, StringUtils.countMatches(searchSql, "SP_MISSING='false'"));
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		IIdType patientId2 = createPatient(2, withFamily("FAMILY"));

		// :missing=true
		{
			addReadPartition(null);
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
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE='1919227773735728687'"));
		}
	}

	@Test
	public void testSearch_MissingParamReference_SearchOnePartition() {
		createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

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
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
			assertEquals(1, StringUtils.countMatches(searchSql, "mysearchpa1_.PARTITION_ID='1'"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE='1919227773735728687'"));
		}
	}


	@Test
	public void testSearch_MissingParamReference_SearchDefaultPartition() {
		IIdType patientIdDefault = createPatient(null, withFamily("FAMILY"));
		createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

		// :missing=true
		{
			addDefaultReadPartition();
			myCaptureQueriesListener.clear();
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_GENERAL_PRACTITIONER, new StringParam().setMissing(true));
			map.setLoadSynchronous(true);
			IBundleProvider results = myPatientDao.search(map);
			List<IIdType> ids = toUnqualifiedVersionlessIds(results);
			assertThat(ids, Matchers.contains(patientIdDefault));

			String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
			ourLog.info("Search SQL:\n{}", searchSql);
			assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
			assertEquals(1, StringUtils.countMatches(searchSql, "mysearchpa1_.PARTITION_ID is null"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HFJ_RES_PARAM_PRESENT"));
			assertEquals(1, StringUtils.countMatches(searchSql, "HASH_PRESENCE='1919227773735728687'"));
		}
	}


	@Test
	public void testSearch_NoParams_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(null, withActiveTrue());
		IIdType patientId1 = createPatient(1, withActiveTrue());
		IIdType patientId2 = createPatient(2, withActiveTrue());

		addReadPartition(null);

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
		createPatient(null, withActiveTrue());
		IIdType patientId1 = createPatient(1, withActiveTrue());
		createPatient(2, withActiveTrue());

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
	public void testSearch_StringParam_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		IIdType patientId2 = createPatient(2, withFamily("FAMILY"));

		addReadPartition(null);

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
		IIdType patientIdNull = createPatient(null, withFamily("FAMILY"));
		createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

		addDefaultReadPartition();

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
		createPatient(null, withFamily("FAMILY"));
		IIdType patientId1 = createPatient(1, withFamily("FAMILY"));
		createPatient(2, withFamily("FAMILY"));

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
	public void testSearch_TagParam_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withTag("http://system", "code"));

		addReadPartition(null);

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
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM='http://system'"));
	}

	@Test
	public void testSearch_TagParam_SearchOnePartition() {
		createPatient(null, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withTag("http://system", "code"));
		createPatient(2, withActiveTrue(), withTag("http://system", "code"));

		addReadPartition(1);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_TAG, new TokenParam("http://system", "code"));
		map.setLoadSynchronous(true);
		IBundleProvider results = myPatientDao.search(map);
		List<IIdType> ids = toUnqualifiedVersionlessIds(results);
		assertThat(ids, Matchers.contains(patientId1));

		String searchSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, true);
		ourLog.info("Search SQL:\n{}", searchSql);
		assertEquals(1, StringUtils.countMatches(searchSql, "PARTITION_ID"));
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM='http://system'"));
	}

	@Test
	public void testSearch_TagParamNot_SearchAllPartitions() {
		IIdType patientIdNull = createPatient(null, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId2 = createPatient(2, withActiveTrue(), withTag("http://system", "code"));
		createPatient(null, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(1, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(2, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));

		addReadPartition(null);

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
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM='http://system'"));
	}

	@Test
	public void testSearch_TagParamNot_SearchOnePartition() {
		createPatient(null, withActiveTrue(), withTag("http://system", "code"));
		IIdType patientId1 = createPatient(1, withActiveTrue(), withTag("http://system", "code"));
		createPatient(2, withActiveTrue(), withTag("http://system", "code"));
		createPatient(null, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(1, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));
		createPatient(2, withActiveTrue(), withTag("http://system", "code"), withTag("http://system", "code2"));

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
		assertEquals(1, StringUtils.countMatches(searchSql, "TAG_SYSTEM='http://system'"));
	}

	@Test
	public void testSearch_UniqueParam_SearchAllPartitions() {
		createUniqueCompositeSp();

		IIdType id = createPatient(1, withBirthdate("2020-01-01"));

		addReadPartition(null);

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
		assertEquals(1, StringUtils.countMatches(searchSql, "IDX_STRING='Patient?birthdate=2020-01-01'"));
	}


	@Test
	public void testSearch_UniqueParam_SearchOnePartition() {
		createUniqueCompositeSp();

		IIdType id = createPatient(1, withBirthdate("2020-01-01"));

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
		assertEquals(1, StringUtils.countMatches(searchSql, "IDX_STRING='Patient?birthdate=2020-01-01'"));

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

	private void createUniqueCompositeSp() {
		addCreateNoPartition();
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		addCreateNoPartition();
		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
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
		PartitionId partitionId = new PartitionId(thePartitionId, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(partitionId);
	}

	private void addCreateNoPartition() {
		myPartitionInterceptor.addCreatePartition(null);
	}

	private void addCreateNoPartitionId(LocalDate thePartitionDate) {
		PartitionId partitionId = new PartitionId(null, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(partitionId);
	}

	private void addReadPartition(Integer thePartitionId) {
		PartitionId partitionId = null;
		if (thePartitionId != null) {
			partitionId = new PartitionId(thePartitionId, null);
		}
		myPartitionInterceptor.addReadPartition(partitionId);
	}

	private void addDefaultReadPartition() {
		PartitionId partitionId = new PartitionId(null, null);
		myPartitionInterceptor.addReadPartition(partitionId);
	}

	public IIdType createPatient(Integer thePartitionId, Consumer<Patient>... theModifiers) {
		if (thePartitionId != null) {
			addCreatePartition(thePartitionId, null);
		} else {
			addCreateNoPartition();
		}


		Patient p = new Patient();
		for (Consumer<Patient> next : theModifiers) {
			next.accept(p);
		}

		if (isNotBlank(p.getId())) {
			return myPatientDao.update(p, mySrd).getId().toUnqualifiedVersionless();
		} else {
			return myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		}
	}

	public void createRequestId() {
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
	}

	private Consumer<Patient> withActiveTrue() {
		return t -> t.setActive(true);
	}

	private Consumer<Patient> withFamily(String theFamily) {
		return t -> t.addName().setFamily(theFamily);
	}

	private Consumer<Patient> withBirthdate(String theBirthdate) {
		return t -> t.getBirthDateElement().setValueAsString(theBirthdate);
	}

	private Consumer<Patient> withId(String theId) {
		return t -> {
			assertThat(theId, matchesPattern("[a-zA-Z0-9]+"));
			t.setId("Patient/" + theId);
		};
	}

	private Consumer<Patient> withTag(String theSystem, String theCode) {
		return t -> t.getMeta().addTag(theSystem, theCode, theCode);
	}


	@Interceptor
	public static class MyInterceptor {


		private final List<PartitionId> myCreatePartitionIds = new ArrayList<>();
		private final List<PartitionId> myReadPartitionIds = new ArrayList<>();

		public void addCreatePartition(PartitionId thePartitionId) {
			myCreatePartitionIds.add(thePartitionId);
		}

		public void addReadPartition(PartitionId thePartitionId) {
			myReadPartitionIds.add(thePartitionId);
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
		public PartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
			assertNotNull(theResource);
			PartitionId retVal = myCreatePartitionIds.remove(0);
			ourLog.info("Returning partition for create: {}", retVal);
			return retVal;
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public PartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
			PartitionId retVal = myReadPartitionIds.remove(0);
			ourLog.info("Returning partition for read: {}", retVal);
			return retVal;
		}

		public void assertNoRemainingIds() {
			assertEquals(0, myCreatePartitionIds.size());
			assertEquals(0, myReadPartitionIds.size());
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
