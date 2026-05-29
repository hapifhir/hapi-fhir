package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.util.DialectSvc;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Attachment;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static ca.uhn.fhir.storage.test.CircularQueueCaptureQueriesListenerAssertions.onCurrentThread;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is a test verifying that we emit the right SQL when operating in database
 * partitioning mode - Partition IDs are a part of the PKs of entities, and are
 * used in joins etc.
 */
@TestPropertySource(properties = {
	JpaConstants.HAPI_DATABASE_PARTITION_MODE + "=true"
})
public class DbpmEnabledTest extends BaseDbpmResourceProviderR5Test {

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setDatabasePartitionMode(true);
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);

		registerPartitionInterceptorAndCreatePartitions();
		initResourceTypeCacheFromConfig();
	}

	@AfterEach
	public void after() {
		DialectSvc.setForceMsSqlMode(false);
	}

	@Test
	public void testUploadIcd10cm() {
		byte[] packageBytes = ClasspathUtil.loadResourceAsByteArray("/icd/icd10cm_tabular_2021.xml");

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.ICD10CM_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("icd10cm_tabular_2021.xml").setData(packageBytes))
			.execute();

		assertThat(respParam.getParameter("success").getValueBooleanType().getValue()).isEqualTo(true);
	}

	@Test
	public void testIdHelperSvc_resolveResourceIdentityPid() {
		myPartitionSettings.setAllowReferencesAcrossPartitions(PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);
		myPartitionSelectorInterceptor.withNextPartition(1, () -> {
			createPatient(withId("A"));
		});

		// Read in the wrong partition
		runInTransaction(() -> assertThatThrownBy(() -> myIdHelperService.resolveResourceIdentityPid(RequestPartitionId.fromPartitionId(2), "Patient", "A", ResolveIdentityMode.includeDeleted().cacheOk())))
			.isInstanceOf(ResourceNotFoundException.class);

		// Read in the correct partition
		runInTransaction(() -> assertNotNull(myIdHelperService.resolveResourceIdentityPid(RequestPartitionId.fromPartitionId(1), "Patient", "A", ResolveIdentityMode.includeDeleted().cacheOk())));

		// Read in the wrong partition again, make sure caches don't prevent the right response
		runInTransaction(() -> assertThatThrownBy(() -> myIdHelperService.resolveResourceIdentityPid(RequestPartitionId.fromPartitionId(2), "Patient", "A", ResolveIdentityMode.includeDeleted().cacheOk())))
			.isInstanceOf(ResourceNotFoundException.class);
	}


	/**
	 * @see ca.uhn.fhir.jpa.search.builder.SearchBuilder#loadCurrentResourceVersionsForMsSqlDbpm(List)
	 */
	@Test
	void testSearch_MsSqlResourceLoading() {
		DialectSvc.setForceMsSqlMode(true);
		try {
			myPartitionSelectorInterceptor.setNextPartition(RequestPartitionId.fromPartitionId(1));
			createPatient(withId("A1"), withFamily("A1"));
			createPatient(withId("B1"), withFamily("B1"));
			createPatient(withId("C1"), withFamily("C1"));
			myPartitionSelectorInterceptor.setNextPartition(RequestPartitionId.fromPartitionId(2));
			createPatient(withId("A2"), withFamily("A2"));
			createPatient(withId("B2"), withFamily("B2"));
			createPatient(withId("C2"), withFamily("C2"));

			logAllResources();

			// Test
			myPartitionSelectorInterceptor.setNextPartition(RequestPartitionId.fromPartitionIds(1, 2));
			myCaptureQueriesListener.clear();
			List<String> actual = toUnqualifiedVersionlessIdValues(myPatientDao.search(SearchParameterMap.newSynchronous(), newSrd()));

			// Verify
			myCaptureQueriesListener.logSelectQueries();
			assertThat(actual).containsExactlyInAnyOrder("Patient/A1", "Patient/B1", "Patient/C1", "Patient/A2", "Patient/B2", "Patient/C2");

			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					// If the variable names in this fragment ever change, make sure they are equivalently changed
					// below. We'll be functionally correct if we do a WHERE on the HFJ_RES_VER table and tests will pass,
					// but it's slower at scale than if we do a WHERE on the HFJ_RESOURCE table
					.selectSqlAtIndex(1).withoutInlinedParams().contains(" from HFJ_RES_VER rht1_0 join HFJ_RESOURCE mrt1_0")
					.selectSqlAtIndex(1).withoutInlinedParams().contains(" where mrt1_0.RES_VER=rht1_0.RES_VER and (mrt1_0.PARTITION_ID=? and mrt1_0.RES_ID in (?,?,?,?,?,?,?) or mrt1_0.PARTITION_ID=? and mrt1_0.RES_ID in (?,?,?))")
					.selectSqlAtIndex(1).countInstancesIgnoreCase(1, "JOIN")
					.selectCount(2)
			);
		} finally {
			DialectSvc.setForceMsSqlMode(false);
		}
	}


	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testRefreshSearchParameterCache(boolean theMssqlMode) {
		// Setup
		DialectSvc.setForceMsSqlMode(theMssqlMode);

		myPartitionSelectorInterceptor.setPartitionIdForResourceType("*", 1);
		requireNonNull(myValidationSupport.fetchAllSearchParameters()).subList(0, 20).forEach(
			sp->mySearchParameterDao.update((SearchParameter) sp, newSrd())
		);

		// Test
		myCaptureQueriesListener.clear();
		mySearchParamRegistry.forceRefresh();
		myCaptureQueriesListener.logSelectQueries();

		assertThat(myCaptureQueriesListener).has(
			onCurrentThread()
				.selectCount(4)
				.selectSqlAtIndex(0).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'SearchParameter') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '0'))")
				.selectSqlAtIndex(2).startsWith("SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = 'SearchParameter') AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = '0'))")
		);

		if (theMssqlMode) {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					.selectSqlAtIndex(1).withoutInlinedParams().endsWith(" where rt1_0.PARTITION_ID=? and rt1_0.RES_ID in (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")
					.selectSqlAtIndex(3).withoutInlinedParams().endsWith(" where mrt1_0.RES_VER=rht1_0.RES_VER and (mrt1_0.PARTITION_ID=? and mrt1_0.RES_ID in (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?))")
			);
		} else {
			assertThat(myCaptureQueriesListener).has(
				onCurrentThread()
					.selectSqlAtIndex(1).withoutInlinedParams().endsWith(" where (rt1_0.RES_ID,rt1_0.PARTITION_ID) in ((?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?))")
					.selectSqlAtIndex(3).withoutInlinedParams().endsWith(" where (mrt1_0.RES_ID,mrt1_0.PARTITION_ID) in ((?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?),(?,?)) and mrt1_0.RES_VER=rht1_0.RES_VER")
			);
		}
	}




	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(DbpmEnabledTest.this, myPartitionSelectorInterceptor, true, true);
		}
	}


}
