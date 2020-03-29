package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.SearchParamPresent;
import ca.uhn.fhir.jpa.model.entity.TenantId;
import ca.uhn.fhir.jpa.searchparam.SearchParamConstants;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.Validate;
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

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class MultitenantR4Test extends BaseJpaR4SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultitenantR4Test.class);
	private MyInterceptor myTenantInterceptor;
	private LocalDate myTenantDate;
	private int myTenantId;

	@After
	public void after() {
		myDaoConfig.setMultiTenancyEnabled(new DaoConfig().isMultiTenancyEnabled());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyInterceptor);
		myInterceptor = null;
	}

	@Override
	@Before
	public void before() throws ServletException {
		super.before();

		myDaoConfig.setMultiTenancyEnabled(true);
		myDaoConfig.setUniqueIndexesEnabled(true);
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		myTenantDate = LocalDate.of(2020, Month.JANUARY, 14);
		myTenantId = 3;
	}


	@Test
	public void testCreateResourceNoTenant() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertNull(resourceTable.getTenantId());
		});
	}


	@Test
	public void testCreateResourceWithTenant() {
		createUniqueCompositeSp();
		createRequestId();

		addCreateTenant(myTenantId, myTenantDate);
		addCreateTenant(myTenantId, myTenantDate);

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
			assertEquals(myTenantId, resourceTable.getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resourceTable.getTenantId().getTenantDate());

			// HFJ_RES_VER
			ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, 1L);
			assertEquals(myTenantId, version.getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, version.getTenantId().getTenantDate());

			// HFJ_RES_VER_PROV
			assertNotNull(version.getProvenance());
			assertEquals(myTenantId, version.getProvenance().getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, version.getProvenance().getTenantId().getTenantDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(myTenantId, strings.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, strings.get(0).getTenantId().getTenantDate());

			// HFJ_SPIDX_DATE
			List<ResourceIndexedSearchParamDate> dates = myResourceIndexedSearchParamDateDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", dates.stream().map(ResourceIndexedSearchParamDate::toString).collect(Collectors.joining("\n * ")));
			assertEquals(2, dates.size());
			assertEquals(myTenantId, dates.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, dates.get(0).getTenantId().getTenantDate());
			assertEquals(myTenantId, dates.get(1).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, dates.get(1).getTenantId().getTenantDate());

			// HFJ_RES_LINK
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(myTenantId, resourceLinks.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resourceLinks.get(0).getTenantId().getTenantDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(myTenantId, presents.size());
			assertEquals(myTenantId, presents.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, presents.get(0).getTenantId().getTenantDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceId(patientId);
			assertEquals(1, uniques.size());
			assertEquals(myTenantId, uniques.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, uniques.get(0).getTenantId().getTenantDate());
		});

	}

	@Test
	public void testCreateWithForcedId() {
		addCreateTenant(myTenantId, myTenantDate);
		addCreateTenant(myTenantId, myTenantDate);

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
			assertEquals(myTenantId, forcedIds.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, forcedIds.get(0).getTenantId().getTenantDate());
			assertEquals(myTenantId, forcedIds.get(1).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, forcedIds.get(1).getTenantId().getTenantDate());
		});

	}

	@Test
	public void testUpdateResourceWithTenant() {
		createRequestId();
		addCreateTenant(3, LocalDate.of(2020, Month.JANUARY, 14));

		// Create a resource
		Patient p = new Patient();
		p.setActive(true);
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();
		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myTenantId, resourceTable.getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resourceTable.getTenantId().getTenantDate());
		});

		// Update that resource
		p = new Patient();
		p.setId("Patient/" + patientId);
		p.setActive(false);
		myPatientDao.update(p, mySrd);

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(myTenantId, resourceTable.getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resourceTable.getTenantId().getTenantDate());

			// HFJ_RES_VER
			int version = 2;
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, version);
			assertEquals(myTenantId, resVer.getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resVer.getTenantId().getTenantDate());

			// HFJ_RES_VER_PROV
			assertNotNull(resVer.getProvenance());
			assertNotNull(resVer.getTenantId());
			assertEquals(myTenantId, resVer.getProvenance().getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, resVer.getProvenance().getTenantId().getTenantDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(myTenantId, strings.get(0).getTenantId().getTenantId().intValue());
			assertEquals(myTenantDate, strings.get(0).getTenantId().getTenantDate());

		});

	}

	@Test
	public void testReadAcrossTenants() {
		IIdType patientId1 = createPatient(1, withActiveTrue());
		IIdType patientId2 = createPatient(1, withActiveTrue());

		IdType gotId1 = myPatientDao.read(patientId1, mySrd).getIdElement().toUnqualifiedVersionless();
		assertEquals(patientId1, gotId1);
		IdType gotId2 = myPatientDao.read(patientId2, mySrd).getIdElement().toUnqualifiedVersionless();
		assertEquals(patientId2, gotId2);
	}

	@Test
	public void testSearchAcrossAllTenants() {

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
			.setUrl(SearchParamConstants.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();
	}



	private void addCreateTenant(int theTenantId, LocalDate theTenantDate) {
		if (myTenantInterceptor == null) {
			myTenantInterceptor = new MyInterceptor();
			myInterceptorRegistry.registerInterceptor(myTenantInterceptor);
		}
		myTenantInterceptor.addCreateTenant(new TenantId(theTenantId, theTenantDate));
	}

	public IIdType createPatient(int theTenantId, Consumer<Patient>... theModifiers) {
		addCreateTenant(theTenantId, null);
		Patient p = new Patient();
		for (Consumer<Patient> next : theModifiers) {
			next.accept(p);
		}

		return myPatientDao.create(p).getId().toUnqualifiedVersionless();
	}

	public void createRequestId() {
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
	}

	private Consumer<Patient> withActiveTrue() {
		return t->t.setActive(true);
	}

	@Interceptor
	public static class MyInterceptor {


		private final List<TenantId> myCreateTenantIds = new ArrayList<>();

		public void addCreateTenant(TenantId theTenantId) {
			Validate.notNull(theTenantId);
			myCreateTenantIds.add(theTenantId);
		}

		@Hook(Pointcut.STORAGE_TENANT_IDENTIFY_CREATE)
		public TenantId tenantIdentifyCreate() {
			TenantId retVal = myCreateTenantIds.remove(0);
			ourLog.info("Returning tenant ID: {}", retVal);
			return retVal;
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
