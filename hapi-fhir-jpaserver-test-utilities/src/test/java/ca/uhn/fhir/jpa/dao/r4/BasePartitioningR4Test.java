package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ForcedId;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public abstract class BasePartitioningR4Test extends BaseJpaR4SystemTest {
	static final String PARTITION_1 = "PART-1";
	static final String PARTITION_2 = "PART-2";
	static final String PARTITION_3 = "PART-3";
	static final String PARTITION_4 = "PART-4";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PartitioningSqlR4Test.class);
	protected MyReadWriteInterceptor myPartitionInterceptor;
	protected LocalDate myPartitionDate;
	protected LocalDate myPartitionDate2;
	protected int myPartitionId;
	protected int myPartitionId2;
	private boolean myHaveDroppedForcedIdUniqueConstraint;
	@Autowired
	private IPartitionLookupSvc myPartitionConfigSvc;

	@AfterEach
	public void after() {
		myPartitionInterceptor.assertNoRemainingIds();

		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());
		myPartitionSettings.setPartitioningEnabled(new PartitionSettings().isPartitioningEnabled());
		myPartitionSettings.setAllowReferencesAcrossPartitions(new PartitionSettings().getAllowReferencesAcrossPartitions());
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());

		mySrdInterceptorService.unregisterInterceptorsIf(t -> t instanceof MyReadWriteInterceptor);
		myInterceptor = null;

		if (myHaveDroppedForcedIdUniqueConstraint) {
			runInTransaction(() -> {
				myEntityManager.createNativeQuery("delete from HFJ_FORCED_ID").executeUpdate();
				myEntityManager.createNativeQuery("alter table HFJ_FORCED_ID add constraint IDX_FORCEDID_TYPE_FID unique (RESOURCE_TYPE, FORCED_ID)");
			});
		}

		myDaoConfig.setIndexMissingFields(new DaoConfig().getIndexMissingFields());
		myDaoConfig.setAutoCreatePlaceholderReferenceTargets(new DaoConfig().isAutoCreatePlaceholderReferenceTargets());
		myDaoConfig.setMassIngestionMode(new DaoConfig().isMassIngestionMode());
		myDaoConfig.setMatchUrlCacheEnabled(new DaoConfig().getMatchUrlCache());
	}

	@BeforeEach
	public void before() throws ServletException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());

		myDaoConfig.setUniqueIndexesEnabled(true);

		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionDate2 = LocalDate.of(2020, Month.FEBRUARY, 15);
		myPartitionId = 1;
		myPartitionId2 = 2;

		myPartitionInterceptor = new MyReadWriteInterceptor();
		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(1).setName(PARTITION_1));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(2).setName(PARTITION_2));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(3).setName(PARTITION_3));
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(4).setName(PARTITION_4));

		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);

		// Ensure the partition names are resolved
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionNames(JpaConstants.DEFAULT_PARTITION_NAME, PARTITION_1, PARTITION_2, PARTITION_3, PARTITION_4));
		myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true), mySrd);

	}

	protected void createUniqueCompositeSp() {
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

	protected void dropForcedIdUniqueConstraint() {
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("alter table " + ForcedId.HFJ_FORCED_ID + " drop constraint " + ForcedId.IDX_FORCEDID_TYPE_FID).executeUpdate();
		});
		myHaveDroppedForcedIdUniqueConstraint = true;
	}

	protected void addCreatePartition(Integer thePartitionId) {
		addCreatePartition(thePartitionId, null);
	}

	protected void addCreatePartition(Integer thePartitionId, LocalDate thePartitionDate) {
		Validate.notNull(thePartitionId);
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(thePartitionId, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(requestPartitionId);
	}

	protected void addCreateDefaultPartition() {
		myPartitionInterceptor.addCreatePartition(RequestPartitionId.defaultPartition());
	}

	protected void addCreateDefaultPartition(LocalDate thePartitionDate) {
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(null, thePartitionDate);
		myPartitionInterceptor.addCreatePartition(requestPartitionId);
	}

	protected void addReadPartition(Integer... thePartitionId) {
		Validate.notNull(thePartitionId);
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionIds(thePartitionId));
	}

	protected void addReadPartitions(String... thePartitionNames) {
		Validate.notNull(thePartitionNames);
		Validate.isTrue(thePartitionNames.length > 0);
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionNames(thePartitionNames));
	}

	protected void addReadDefaultPartition() {
		myPartitionInterceptor.addReadPartition(RequestPartitionId.defaultPartition());
	}

	protected void addReadAllPartitions() {
		myPartitionInterceptor.addReadPartition(RequestPartitionId.allPartitions());
	}

	public void createRequestId() {
		when(mySrd.getRequestId()).thenReturn("REQUEST_ID");
	}

	protected Consumer<IBaseResource> withPartition(Integer thePartitionId) {
		return t -> {
			if (thePartitionId != null) {
				addCreatePartition(thePartitionId, null);
			} else {
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
		public RequestPartitionId partitionIdentifyRead(ServletRequestDetails theRequestDetails) {
			RequestPartitionId retVal = myReadRequestPartitionIds.remove(0);
			ourLog.info("Returning partition for read: {}", retVal);
			return retVal;
		}

		@Override
		public void assertNoRemainingIds() {
			super.assertNoRemainingIds();
			assertEquals(0, myReadRequestPartitionIds.size(), () -> "Found " + myReadRequestPartitionIds.size() + " READ partitions remaining in interceptor");
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
			assertTrue(!myCreateRequestPartitionIds.isEmpty(), "No create partitions left in interceptor");
			RequestPartitionId retVal = myCreateRequestPartitionIds.remove(0);
			ourLog.debug("Returning partition [{}] for create of resource {} with date {}", retVal, theResource, retVal.getPartitionDate());
			return retVal;
		}

		public void assertNoRemainingIds() {
			assertEquals(0, myCreateRequestPartitionIds.size(), () -> "Still have " + myCreateRequestPartitionIds.size() + " CREATE partitions remaining in interceptor");
		}

	}
}
