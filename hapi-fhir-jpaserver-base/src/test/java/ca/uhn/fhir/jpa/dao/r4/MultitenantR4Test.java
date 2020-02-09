package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.TenantId;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MultitenantR4Test extends BaseJpaR4SystemTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MultitenantR4Test.class);

	@After
	public void after() {
		myDaoConfig.setMultiTenancyEnabled(new DaoConfig().isMultiTenancyEnabled());

		myInterceptorRegistry.unregisterInterceptorsIf(t -> t instanceof MyInterceptor);
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
		int expectId = 3;
		LocalDate expectDate = LocalDate.of(2020, Month.JANUARY, 14);
		myInterceptorRegistry.registerInterceptor(new MyInterceptor(new TenantId(expectId, expectDate)));

		Patient p = new Patient();
		p.addName().setFamily("FAM");
		p.addIdentifier().setSystem("system").setValue("value");
		p.setBirthDate(new Date());
		Long patientId = myPatientDao.create(p).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao.findById(patientId).orElseThrow(IllegalArgumentException::new);
			assertEquals(expectId, resourceTable.getTenantId().getTenantId().intValue());
			assertEquals(expectDate, resourceTable.getTenantId().getTenantDate());

			List<ResourceIndexedSearchParamString> strings = myResourceIndexedSearchParamStringDao.findAll();
			ourLog.info("\n * {}", strings.stream().map(ResourceIndexedSearchParamString::toString).collect(Collectors.joining("\n * ")));
			assertEquals(10, strings.size());
			assertEquals(expectId, strings.get(0).getTenantId().getTenantId().intValue());
			assertEquals(expectDate, strings.get(0).getTenantId().getTenantDate());
		});

	}

	@Interceptor
	public static class MyInterceptor {

		private final List<TenantId> myTenantIds;

		public MyInterceptor(TenantId theTenantId) {
			Validate.notNull(theTenantId);
			myTenantIds = Collections.singletonList(theTenantId);
		}

		@Hook(Pointcut.STORAGE_TENANT_IDENTIFY_CREATE)
		public TenantId tenantIdentifyCreate() {
			TenantId retVal = myTenantIds.get(0);
			ourLog.info("Returning tenant ID: {}", retVal);
			return retVal;
		}

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
