package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is a test verifying that we emit the right SQL when operating in database
 * partitioning mode - Partition IDs are a part of the PKs of entities, and are
 * used in joins etc.
 */
@TestPropertySource(properties = {
	JpaConstants.HAPI_DATABASE_PARTITION_MODE + "=true"
})
public class DbpmEnabledTest extends BaseDbpmJpaR5Test {

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


	@Nested
	public class MyTestDefinitions extends TestDefinitions {
		MyTestDefinitions() {
			super(DbpmEnabledTest.this, myPartitionSelectorInterceptor, true, true);
		}
	}


}
