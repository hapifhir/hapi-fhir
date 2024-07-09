package ca.uhn.fhir.jpa.dao.r4;

import static ca.uhn.fhir.jpa.model.entity.ResourceTable.IDX_RES_TYPE_FHIR_ID;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.ReadPartitionIdRequestDetails;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import com.helger.commons.lang.StackTraceHelper;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
	protected int myPartitionId3;
	protected int myPartitionId4;
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

		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(new JpaStorageSettings().isAutoCreatePlaceholderReferenceTargets());
		myStorageSettings.setMassIngestionMode(new JpaStorageSettings().isMassIngestionMode());
		myStorageSettings.setMatchUrlCacheEnabled(new JpaStorageSettings().getMatchUrlCache());
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setIncludePartitionInSearchHashes(new PartitionSettings().isIncludePartitionInSearchHashes());

		myStorageSettings.setUniqueIndexesEnabled(true);

		myStorageSettings.setDefaultSearchParamsCanBeOverridden(true);

		myPartitionDate = LocalDate.of(2020, Month.JANUARY, 14);
		myPartitionDate2 = LocalDate.of(2020, Month.FEBRUARY, 15);
		myPartitionId = 1;
		myPartitionId2 = 2;
		myPartitionId3 = 3;
		myPartitionId4 = 4;

		myPartitionInterceptor = new MyReadWriteInterceptor();
		mySrdInterceptorService.registerInterceptor(myPartitionInterceptor);

		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(myPartitionId).setName(PARTITION_1), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(myPartitionId2).setName(PARTITION_2), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(myPartitionId3).setName(PARTITION_3), null);
		myPartitionConfigSvc.createPartition(new PartitionEntity().setId(myPartitionId4).setName(PARTITION_4), null);

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		// Ensure the partition names are resolved
		myPartitionInterceptor.addReadPartition(RequestPartitionId.fromPartitionNames(JpaConstants.DEFAULT_PARTITION_NAME, PARTITION_1, PARTITION_2, PARTITION_3, PARTITION_4));
		myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true), mySrd);

		// Pre-fetch the partitions by ID
		for (int i = 1; i <= 4; i++) {
			myPartitionConfigSvc.getPartitionById(i);
		}
	}

	@Override
	public void afterPurgeDatabase() {
		super.afterPurgeDatabase();

		if (myHaveDroppedForcedIdUniqueConstraint) {
			runInTransaction(() -> {
				myEntityManager.createNativeQuery("delete from HFJ_RESOURCE").executeUpdate();
				myEntityManager.createNativeQuery("alter table " + ResourceTable.HFJ_RESOURCE +
					" add constraint " + IDX_RES_TYPE_FHIR_ID + " unique (RES_TYPE, FHIR_ID)").executeUpdate();
			});
		}
	}

	protected void createUniqueComboSp() {
		addCreateDefaultPartition();
		addReadDefaultPartition(); // one for search param validation
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		addCreateDefaultPartition();
		addReadDefaultPartition(); // one for search param validation
		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name[0].family");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		addCreateDefaultPartition();
		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

	protected void createNonUniqueComboSp() {
		addCreateDefaultPartition();
		addReadDefaultPartition(); // one for search param validation
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		addCreateDefaultPartition();
		addReadDefaultPartition(); // one for search param validation
		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-managingorg");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode(Patient.SP_ORGANIZATION);
		sp.setExpression("Patient.managingOrganization");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		addCreateDefaultPartition();
		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family-and-org");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-managingorg");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

	protected void dropForcedIdUniqueConstraint() {
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("alter table " + ResourceTable.HFJ_RESOURCE + " drop constraint " + IDX_RES_TYPE_FHIR_ID).executeUpdate();
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

	protected ICreationArgument withPartition(Integer thePartitionId) {
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
			ourLog.info("Adding partition {} for read (not have {})", theRequestPartitionId, myReadRequestPartitionIds.size());
		}

		@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
		public RequestPartitionId partitionIdentifyRead(ServletRequestDetails theRequestDetails,
																		ReadPartitionIdRequestDetails theDetails) {

			// Just to be nice, figure out the first line in the stack that isn't a part of the
			// partitioning or interceptor infrastructure, just so it's obvious who is asking
			// for a partition ID
			String stack;
			try {
				throw new Exception();
			} catch (Exception e) {
				stack = StackTraceHelper.getStackAsString(e);
				stack = Arrays.stream(stack.split("\\n"))
					.filter(t->t.contains("ca.uhn.fhir"))
					.filter(t->!t.toLowerCase().contains("interceptor"))
					.filter(t->!t.toLowerCase().contains("partitionhelper"))
					.findFirst()
					.orElse("UNKNOWN");
			}

			RequestPartitionId retVal = myReadRequestPartitionIds.remove(0);
			ourLog.info("Returning partition {} for read at: {}", retVal, stack);
			return retVal;
		}

		@Override
		public void assertNoRemainingIds() {
			super.assertNoRemainingIds();
			assertThat(myReadRequestPartitionIds).as("Found " + myReadRequestPartitionIds.size() + " READ partitions remaining in interceptor").hasSize(0);
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
			assertThat(!myCreateRequestPartitionIds.isEmpty()).as("No create partitions left in interceptor").isTrue();
			RequestPartitionId retVal = myCreateRequestPartitionIds.remove(0);
			ourLog.debug("Returning partition [{}] for create of resource {} with date {}", retVal, theResource, retVal.getPartitionDate());
			return retVal;
		}

		public void assertNoRemainingIds() {
			assertThat(myCreateRequestPartitionIds.size()).as(() -> "Still have " + myCreateRequestPartitionIds.size() + " CREATE partitions remaining in interceptor").isEqualTo(0);
		}

	}
}
