package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedCompositeStringUnique;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class MultitenantR4Test extends BaseJpaR4SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultitenantR4Test.class);
	private MyInterceptor myTenantInterceptor;

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

		addTenant(3, LocalDate.of(2020, Month.JANUARY, 14));
		addTenant(3, LocalDate.of(2020, Month.JANUARY, 14));

		Organization org = new Organization();
		org.setName("org");
		IIdType orgId = myOrganizationDao.create(org).getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.getMeta().addTag("http://system", "code", "diisplay");
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		p.getManagingOrganization().setReferenceElement(orgId);
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
		Long patientId = myPatientDao.create(p, mySrd).getId().getIdPartAsLong();

		runInTransaction(() -> {
			// HFJ_RESOURCE
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(3, resourceTable.getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), resourceTable.getTenantId().getTenantDate());

			resourceTable.getProfile()

			// HFJ_RES_VER
			ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, 1L);
			assertEquals(3, version.getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), version.getTenantId().getTenantDate());

			// HFJ_SPIDX_STRING
			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAllForResourceId(patientId);
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(3, strings.get(0).getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), strings.get(0).getTenantId().getTenantDate());

			// HFJ_RES_LINK
			List<ResourceLink> resourceLinks = myResourceLinkDao.findAllForResourceId(patientId);
			assertEquals(1, resourceLinks.size());
			assertEquals(3, resourceLinks.get(0).getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), resourceLinks.get(0).getTenantId().getTenantDate());

			// HFJ_RES_PARAM_PRESENT
			List<SearchParamPresent> presents = mySearchParamPresentDao.findAllForResource(resourceTable);
			assertEquals(3, presents.size());
			assertEquals(3, presents.get(0).getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), presents.get(0).getTenantId().getTenantDate());

			// HFJ_IDX_CMP_STRING_UNIQ
			List<ResourceIndexedCompositeStringUnique> uniques = myResourceIndexedCompositeStringUniqueDao.findAllForResourceId(patientId);
			assertEquals(3, uniques.size());
			assertEquals(3, uniques.get(0).getTenantId().getTenantId().intValue());
			assertEquals(LocalDate.of(2020, Month.JANUARY, 14), uniques.get(0).getTenantId().getTenantDate());
		});

	}

	@Test
	public void testUpdateResourceWithTenant() {
		createUniqueCompositeSp();

		addTenant(3, LocalDate.of(2020, Month.JANUARY, 14));

		Patient p = new Patient();
		p.setActive(true);
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();

		p = new Patient();
		p.setId("Patient/" + patientId);
		p.setActive(false);
		myPatientDao.update(p);

		runInTransaction(() -> {
			// HFJ_RES_VER
			ResourceHistoryTable resVer = myResourceHistoryTableDao.findForIdAndVersionAndFetchProvenance(patientId, 2);
			assertEquals(tenantId, resVer.getTenantId().getTenantId().intValue());
			assertEquals(tenantDate, resVer.getTenantId().getTenantDate());

		});

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
		sp.setId("SearchParameter/patient-birthdate");
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

	public void addTenant(int theTenantId, LocalDate theTenantDate) {
		if (myTenantInterceptor == null) {
			myTenantInterceptor = new MyInterceptor();
			myInterceptorRegistry.registerInterceptor(myInterceptor);
		}
		myTenantInterceptor.addTenant(new TenantId(theTenantId, theTenantDate));
	}

	@Interceptor
	public static class MyInterceptor {

		private final List<TenantId> myTenantIds = new ArrayList<>();

		public void addTenant(TenantId theTenantId) {
			Validate.notNull(theTenantId);
			myTenantIds.add(theTenantId);
		}

		@Hook(Pointcut.STORAGE_TENANT_IDENTIFY_CREATE)
		public TenantId tenantIdentifyCreate() {
			TenantId retVal = myTenantIds.remove(0);
			ourLog.info("Returning tenant ID: {}", retVal);
			return retVal;
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
