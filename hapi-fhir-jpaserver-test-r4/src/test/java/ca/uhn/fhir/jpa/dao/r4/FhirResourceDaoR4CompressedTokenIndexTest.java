package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenCommonResDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenIdentifierDao;
import ca.uhn.fhir.jpa.search.TokenSearchParameterTestCases;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommon;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommonRes;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenIdentifier;
import ca.uhn.fhir.jpa.model.entity.TokenIndexStrategy;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static ca.uhn.fhir.jpa.model.entity.TokenIndexStrategy.TokenIndex.COMPRESSED;
import static ca.uhn.fhir.jpa.model.entity.TokenIndexStrategy.TokenIndex.LEGACY;
import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR4CompressedTokenIndexTest extends BaseJpaR4Test {

	@Autowired
	private IResourceIndexedSearchParamTokenCommonResDao myTokenCommonResDao;

	@Autowired
	private IResourceIndexedSearchParamTokenIdentifierDao myTokenIdentifierDao;

	@BeforeEach
	void enableCompressedTokenIndexStrategy() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(COMPRESSED), COMPRESSED));
	}

	@AfterEach
	void resetTokenIndexStrategy() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY), LEGACY));
	}

	@Test
	void createPatient_withMixedTokens_routesEachByParamName() {
		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://example.com/ids").setValue("MRN123");
		p.setGender(AdministrativeGender.MALE);

		// execute
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		// validate
		long identifierHash = hashSysAndValue("Patient", Patient.SP_IDENTIFIER, "http://example.com/ids", "MRN123");
		long genderHash = hashSysAndValue("Patient", Patient.SP_GENDER, "http://hl7.org/fhir/administrative-gender", "male");

		runInTransaction(() -> {
			// identifier routes to the Identifier table
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers = myTokenIdentifierDao.findByResourceId(pid);
			assertThat(identifiers).hasSize(1);
			ResourceIndexedSearchParamTokenIdentifier identifierRow = identifiers.get(0);
			validateTokenIdentifier(identifierRow, pid);

			// identifier must not leak into the commonRes table
			List<ResourceIndexedSearchParamTokenCommonRes> commonRes = myTokenCommonResDao.findByResourceId(pid);
			assertThat(commonRes)
				.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
				.as("identifier param must not appear in CommonRes (it routes to Identifier table)")
				.doesNotContain(identifierHash);

			// gender routes to the Common tables
			ResourceIndexedSearchParamTokenCommonRes genderLink = commonRes.stream()
				.filter(r -> r.getHashSystemAndValue() == genderHash)
				.findFirst()
				.orElseThrow(() -> new AssertionError("expected CommonRes row for gender=male hash"));
			assertThat(genderLink.getResourceId()).isEqualTo(pid.getId());
			assertThat(genderLink.getPartitionId()).as("default partition").isNull();
			assertThat(genderLink.getHashSystemAndValue()).isEqualTo(genderHash);

			// gender's deduplicated TokenCommon row exists
			ResourceIndexedSearchParamTokenCommon genderCommon =
				myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, genderHash);
			assertThat(genderCommon).as("gender routes to TokenCommon").isNotNull();
			assertThat(genderCommon.getValue()).isEqualTo("male");

			// identifier must not leak into the Common value table
			ResourceIndexedSearchParamTokenCommon identifierCommon =
				myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, identifierHash);
			assertThat(identifierCommon).as("identifier must NOT appear in TokenCommon").isNull();
		});
	}

	private void validateTokenIdentifier(ResourceIndexedSearchParamTokenIdentifier theIdentifierRow, JpaPid thePid) {
		assertThat(theIdentifierRow.getResourceId()).isEqualTo(thePid.getId());
		assertThat(theIdentifierRow.getHashIdentity()).isEqualTo(hashIdentity("Patient", Patient.SP_IDENTIFIER));
		assertThat(theIdentifierRow.getSystemUrlId()).as("system URL resolved to non-null FK").isNotNull();
		assertThat(theIdentifierRow.getValue()).isEqualTo("MRN123");
		assertThat(theIdentifierRow.getHashValue()).isEqualTo(hashValue("Patient", Patient.SP_IDENTIFIER, "MRN123"));
		assertThat(theIdentifierRow.getTypeHashSystemAndValue()).as("plain identifier has no :of-type hash").isNull();
	}

	@Test
	void createObservation_withCodeInIdentifierSearchParams_routesToIdentifierTable() {
		// setup
		myStorageSettings.setIdentifierTokenSearchParams(Set.of("identifier", "code"));
		Observation obs = newObservationWithCode();

		// execute
		IIdType id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		// validate
		long codeHash = hashSysAndValue("Observation", Observation.SP_CODE, "http://loinc.org", "12345-6");

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers = myTokenIdentifierDao.findByResourceId(pid);
			assertThat(identifiers)
				.extracting(ResourceIndexedSearchParamTokenIdentifier::getValue)
				.as("code param should route to IDENTIFIER table when configured")
				.contains("12345-6");

			List<ResourceIndexedSearchParamTokenCommonRes> commonRes = myTokenCommonResDao.findByResourceId(pid);
			assertThat(commonRes)
				.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
				.as("code param must not appear in CommonRes when routed to Identifier table")
				.doesNotContain(codeHash);
		});

		myStorageSettings.setIdentifierTokenSearchParams(Set.of("identifier"));
	}

	@Test
	void createTwoResources_sameToken_reuseOneCommonRowAndCreateTwoCommonResRows() {
		// execute
		JpaPid pid1 = createObservationWithCode();
		JpaPid pid2 = createObservationWithCode();

		// validate
		long codeHash = hashSysAndValue("Observation", Observation.SP_CODE, "http://loinc.org", "12345-6");

		runInTransaction(() -> {
			assertThat(countCommonByHash(codeHash)).as("exactly one Common row per unique token").isEqualTo(1);

			assertThat(myTokenCommonResDao.findByResourceId(pid1))
				.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
				.contains(codeHash);
			assertThat(myTokenCommonResDao.findByResourceId(pid2))
				.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
				.contains(codeHash);
		});
	}

	@Test
	void transactionBundle_sameTokenOnMultipleResources_sharesOneCommonRow() {
		// setup
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setGender(AdministrativeGender.MALE);
			bb.addTransactionCreateEntry(p);
		}

		// execute
		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) bb.getBundle());

		// validate
		assertThat(outcome.getEntry()).hasSize(5);

		long genderHash = hashSysAndValue("Patient", Patient.SP_GENDER, "http://hl7.org/fhir/administrative-gender", "male");

		runInTransaction(() -> {
			assertThat(countCommonByHash(genderHash))
				.as("session identity-map dedup must collapse 5 inserts into 1 row")
				.isEqualTo(1);
			assertThat(countCommonResByHash(genderHash))
				.as("each of the 5 resources gets its own CommonRes link row")
				.isEqualTo(5);
		});
	}

	@Test
	void updatePatient_changeIdentifierValue_replacesIdentifierRow() {
		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		// execute
		Patient updated = new Patient();
		updated.setId(id);
		updated.addIdentifier().setSystem("http://sys").setValue("B");
		myPatientDao.update(updated, mySrd);

		// validate
		runInTransaction(() -> assertThat(myTokenIdentifierDao.findByResourceId(pid))
			.extracting(ResourceIndexedSearchParamTokenIdentifier::getValue)
			.as("stale identifier row must be removed; new value present")
			.containsExactly("B"));
	}

	@Test
	void updatePatient_changeGenderToken_addsNewCommonRowKeepsOld() {
		// setup
		Patient p = new Patient();
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		// execute
		Patient updated = new Patient();
		updated.setId(id);
		updated.setGender(AdministrativeGender.FEMALE);
		myPatientDao.update(updated, mySrd);

		// validate
		long maleHash = hashSysAndValue("Patient", Patient.SP_GENDER, "http://hl7.org/fhir/administrative-gender", "male");
		long femaleHash = hashSysAndValue("Patient", Patient.SP_GENDER, "http://hl7.org/fhir/administrative-gender", "female");

		runInTransaction(() -> {
			List<Long> commonResHashes = myTokenCommonResDao.findByResourceId(pid).stream()
				.map(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
				.toList();
			assertThat(commonResHashes)
				.as("CommonRes points only at female; male link is removed")
				.contains(femaleHash)
				.doesNotContain(maleHash);

			assertThat(countCommonByHash(maleHash))
				.as("male Common row retained — insert-only")
				.isEqualTo(1);
			assertThat(countCommonByHash(femaleHash)).isEqualTo(1);
		});
	}

	@Test
	void updatePatient_withIdenticalTokens_leavesIndexRowsUnchanged() {
		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		int identifierBefore = runInTransaction(() -> myTokenIdentifierDao.findByResourceId(pid).size());
		int commonResBefore = runInTransaction(() -> myTokenCommonResDao.findByResourceId(pid).size());

		// execute
		Patient sameContent = new Patient();
		sameContent.setId(id);
		sameContent.addIdentifier().setSystem("http://sys").setValue("A");
		sameContent.setGender(AdministrativeGender.MALE);
		myPatientDao.update(sameContent, mySrd);

		// validate
		runInTransaction(() -> {
			assertThat(myTokenIdentifierDao.findByResourceId(pid)).hasSize(identifierBefore);
			assertThat(myTokenCommonResDao.findByResourceId(pid)).hasSize(commonResBefore);
		});
	}

	@Test
	void create_doesNotQueryCompressedTokenTables() {
		// setup
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://example.com/ids").setValue("MRN999");
		p.setGender(AdministrativeGender.FEMALE);
		myCaptureQueriesListener.clear();

		// execute
		myPatientDao.create(p, mySrd);

		// validate
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueriesForCurrentThread();
		// value table HFJ_SPIDX2_TOKEN_COMMON is intentionally excluded: CREATE may read it to dedup a shared token
		assertThat(selectQueries)
			.extracting(q -> q.getSql(false, false).toUpperCase())
			.as("CREATE must not issue SELECT against compressed token tables")
			.noneMatch(sql -> sql.contains(ResourceIndexedSearchParamTokenCommonRes.HFJ_SPIDX2_TOKEN_COMMON_RES))
			.noneMatch(sql -> sql.contains(ResourceIndexedSearchParamTokenIdentifier.HFJ_SPIDX2_TOKEN_IDENTIFIER));
	}

	private static Stream<TokenIndexStrategy> tokenIndexStrategies() {
		return Stream.of(
			TokenIndexStrategy.of(EnumSet.of(LEGACY), LEGACY),
			TokenIndexStrategy.of(EnumSet.of(LEGACY, COMPRESSED), LEGACY),
			TokenIndexStrategy.of(EnumSet.of(LEGACY, COMPRESSED), COMPRESSED),
			TokenIndexStrategy.of(EnumSet.of(COMPRESSED), COMPRESSED));
	}

	@ParameterizedTest
	@MethodSource("tokenIndexStrategies")
	void writeStrategy_writesToCorrectTablesOnly(TokenIndexStrategy theStrategy) {
		// setup
		myStorageSettings.setTokenIndexStrategy(theStrategy);
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);

		// execute
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		// validate
		runInTransaction(() -> {
			int legacyRows = myResourceIndexedSearchParamTokenDao.countForResourceId(pid);
			int compressedRows = myTokenCommonResDao.findByResourceId(pid).size()
				+ myTokenIdentifierDao.findByResourceId(pid).size();

			if (theStrategy.writeToLegacyTokenTable()) {
				assertThat(legacyRows).as("legacy populated under %s", theStrategy).isGreaterThan(0);
			} else {
				assertThat(legacyRows).as("legacy empty under %s", theStrategy).isZero();
			}
			if (theStrategy.writeToCompressedTokenTables()) {
				assertThat(compressedRows).as("compressed populated under %s", theStrategy).isGreaterThan(0);
			} else {
				assertThat(compressedRows).as("compressed empty under %s", theStrategy).isZero();
			}
		});
	}

	@ParameterizedTest
	@CsvSource(nullValues = "null", value = {
		"http://loinc.org, 12345-6, false",
		"null, custom-no-system, true"
	})
	void createObservation_codeToken_populatesAllHashesAndResolvesSystemId(
		String theSystem, String theValue, boolean theExpectNullSystemId) {
		// setup
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem(theSystem).setCode(theValue);

		// execute
		myObservationDao.create(obs, mySrd);

		// validate
		long expectedHashSysAndValue = hashSysAndValue("Observation", Observation.SP_CODE, theSystem, theValue);
		long expectedHashIdentity = hashIdentity("Observation", Observation.SP_CODE);
		long expectedHashValue = hashValue("Observation", Observation.SP_CODE, theValue);

		runInTransaction(() -> {
			ResourceIndexedSearchParamTokenCommon row =
				myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, expectedHashSysAndValue);
			assertThat(row).isNotNull();
			assertThat(row.getHashIdentity()).isEqualTo(expectedHashIdentity);
			assertThat(row.getHashValue()).isEqualTo(expectedHashValue);
			assertThat(row.getHashSystemAndValue()).isEqualTo(expectedHashSysAndValue);
			assertThat(row.getValue()).isEqualTo(theValue);
			if (theExpectNullSystemId) {
				assertThat(row.getSystemId()).as("systemId is null when system is blank").isNull();
			} else {
				assertThat(row.getSystemId()).as("systemId resolved for non-blank system").isNotNull();
			}
		});
	}

	@Test
	void createPatient_identifierWithType_routesToIdentifierTable() {
		// setup
		myStorageSettings.setIndexIdentifierOfType(true);
		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		id.getType().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		// execute
		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(patientId.getIdPartAsLong());

		// validate
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers =
				myTokenIdentifierDao.findByResourceId(pid);

			// should have 2 rows: one for "identifier", one for "identifier:of-type"
			assertThat(identifiers).hasSize(2);

			// regular identifier row
			ResourceIndexedSearchParamTokenIdentifier regularRow = identifiers.stream()
				.filter(r -> r.getValue().equals("MRN123"))
				.findFirst().orElseThrow();
			assertThat(regularRow.getTypeHashSystemAndValue())
				.as("regular identifier should NOT have TYPE_HASH_SYS_AND_VALUE")
				.isNull();

			// :of-type row (value = "MR|MRN123")
			ResourceIndexedSearchParamTokenIdentifier ofTypeRow = identifiers.stream()
				.filter(r -> r.getValue().equals("MR|MRN123"))
				.findFirst().orElseThrow();
			assertThat(ofTypeRow.getTypeHashSystemAndValue())
				.as(":of-type row should have TYPE_HASH_SYS_AND_VALUE populated")
				.isNotNull();

			long expectedHash = hashSysAndValue("Patient", "identifier:of-type",
				"http://terminology.hl7.org/CodeSystem/v2-0203", "MR|MRN123");
			assertThat(ofTypeRow.getTypeHashSystemAndValue()).isEqualTo(expectedHash);
		});
	}

	@Test
	void createPatient_identifierWithMultipleTypeCodingsRoutes_createsMultipleOfTypeRows() {
		// setup
		myStorageSettings.setIndexIdentifierOfType(true);
		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		// add 2 type codings
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("SS");

		// execute
		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(patientId.getIdPartAsLong());

		// validate
		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers =
				myTokenIdentifierDao.findByResourceId(pid);

			// should have 3 rows: 1 regular + 2 :of-type (one per coding)
			assertThat(identifiers).hasSize(3);

			// regular identifier row
			assertThat(identifiers.stream().filter(r -> r.getValue().equals("MRN123")).count())
				.as("one regular identifier row").isEqualTo(1);

			// two :of-type rows
			ResourceIndexedSearchParamTokenIdentifier mrRow = identifiers.stream()
				.filter(r -> r.getValue().equals("MR|MRN123"))
				.findFirst().orElseThrow();
			ResourceIndexedSearchParamTokenIdentifier ssRow = identifiers.stream()
				.filter(r -> r.getValue().equals("SS|MRN123"))
				.findFirst().orElseThrow();

			assertThat(mrRow.getTypeHashSystemAndValue()).isNotNull();
			assertThat(ssRow.getTypeHashSystemAndValue()).isNotNull();
			assertThat(mrRow.getTypeHashSystemAndValue())
				.as("different type codes should have different hashes")
				.isNotEqualTo(ssRow.getTypeHashSystemAndValue());
		});
	}

	@Test
	void createPatient_identifierWithType_routesOfTypeToIdentifierTableEvenWhenParamNotConfigured() {
		// setup: remove "identifier" from the identifier-token set so routing can't rely on it.
		// :of-type tokens must still land in the IDENTIFIER table, because the common tables have
		// no TYPE_HASH_SYS_AND_VALUE column for the read path to query them back from.
		myStorageSettings.setIndexIdentifierOfType(true);
		myStorageSettings.setIdentifierTokenSearchParams(Set.of());
		try {
			Patient p = new Patient();
			Identifier id = p.addIdentifier();
			id.setSystem("http://example.com/ids").setValue("MRN123");
			id.getType().addCoding()
				.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
				.setCode("MR");

			// execute
			IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
			JpaPid pid = JpaPid.fromId(patientId.getIdPartAsLong());

			// validate
			long plainIdentifierHash =
				hashSysAndValue("Patient", Patient.SP_IDENTIFIER, "http://example.com/ids", "MRN123");

			runInTransaction(() -> {
				// the :of-type token always routes to the IDENTIFIER table, regardless of config
				List<ResourceIndexedSearchParamTokenIdentifier> identifiers = myTokenIdentifierDao.findByResourceId(pid);
				assertThat(identifiers)
					.as(":of-type token must always land in the IDENTIFIER table")
					.extracting(ResourceIndexedSearchParamTokenIdentifier::getValue)
					.containsExactly("MR|MRN123");
				assertThat(identifiers.get(0).getTypeHashSystemAndValue()).isNotNull();

				// the plain identifier token follows config: not configured here, so it routes to the Common tables
				assertThat(myTokenCommonResDao.findByResourceId(pid))
					.as("plain identifier follows config (not configured -> COMMON)")
					.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
					.contains(plainIdentifierHash);
			});
		} finally {
			myStorageSettings.setIdentifierTokenSearchParams(Set.of("identifier"));
			myStorageSettings.setIndexIdentifierOfType(false);
		}
	}

	@Nested
	public class LegacyTokenSearch extends TokenSearchParameterTestCases {
		public LegacyTokenSearch() {
			super(tokenSearchSupport(), myTestDaoSearch, myStorageSettings);
		}

		@BeforeEach
		void setStrategy() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY), LEGACY));
		}
	}

	@Nested
	public class CompressedWriteCompressedQueryCompressed extends TokenSearchParameterTestCases {
		public CompressedWriteCompressedQueryCompressed() {
			super(tokenSearchSupport(), myTestDaoSearch, myStorageSettings);
		}

		@BeforeEach
		void setStrategy() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(COMPRESSED), COMPRESSED));
		}
	}

	@Nested
	public class CompressedWriteBothQueryCompressed extends TokenSearchParameterTestCases {
		public CompressedWriteBothQueryCompressed() {
			super(tokenSearchSupport(), myTestDaoSearch, myStorageSettings);
		}

		@BeforeEach
		void setStrategy() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY, COMPRESSED), COMPRESSED));
		}
	}

	@Nested
	public class CompressedWriteBothQueryLegacy extends TokenSearchParameterTestCases {
		public CompressedWriteBothQueryLegacy() {
			super(tokenSearchSupport(), myTestDaoSearch, myStorageSettings);
		}

		@BeforeEach
		void setStrategy() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY, COMPRESSED), LEGACY));
		}
	}

	@Nested
	public class WriteBothQueryCompressedFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY, COMPRESSED), COMPRESSED));
		}

		@AfterEach
		void cleanUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY), LEGACY));
		}

		@Override
		protected boolean readsFromLegacyTokenTable() {
			return false;
		}
	}

	@Nested
	public class WriteCompressedQueryCompressedFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(COMPRESSED), COMPRESSED));
		}

		@AfterEach
		void cleanUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategy.of(EnumSet.of(LEGACY), LEGACY));
		}

		@Override
		protected boolean writesToLegacyTokenTable() {
			return false;
		}

		@Override
		protected boolean readsFromLegacyTokenTable() {
			return false;
		}
	}

	private Observation newObservationWithCode() {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("http://loinc.org").setCode("12345-6");
		return obs;
	}

	private JpaPid createObservationWithCode() {
		IIdType id = myObservationDao.create(newObservationWithCode(), mySrd).getId();
		return JpaPid.fromId(id.getIdPartAsLong());
	}

	private long hashIdentity(String theResourceType, String theParamName) {
		return BaseResourceIndexedSearchParam.calculateHashIdentity(
			myPartitionSettings, (PartitionablePartitionId) null, theResourceType, theParamName);
	}

	private long hashValue(String theResourceType, String theParamName, String theValue) {
		return ResourceIndexedSearchParamToken.calculateHashValue(
			myPartitionSettings, (PartitionablePartitionId) null, theResourceType, theParamName, theValue);
	}

	private long hashSysAndValue(String theResourceType, String theParamName, String theSystem, String theValue) {
		return ResourceIndexedSearchParamToken.calculateHashSystemAndValue(
			myPartitionSettings,
			(PartitionablePartitionId) null,
			theResourceType,
			theParamName,
			theSystem,
			theValue);
	}

	private int countCommonByHash(long theHash) {
		return myEntityManager
			.createQuery(
				"SELECT COUNT(t) FROM ResourceIndexedSearchParamTokenCommon t WHERE t.myHashSystemAndValue = :h",
				Long.class)
			.setParameter("h", theHash)
			.getSingleResult()
			.intValue();
	}

	private int countCommonResByHash(long theHash) {
		return myEntityManager
			.createQuery(
				"SELECT COUNT(t) FROM ResourceIndexedSearchParamTokenCommonRes t WHERE t.myHashSystemAndValue = :h",
				Long.class)
			.setParameter("h", theHash)
			.getSingleResult()
			.intValue();
	}

	private ITestDataBuilder.Support tokenSearchSupport() {
		return new ITestDataBuilder.Support() {
			@Override
			public FhirContext getFhirContext() {
				return FhirResourceDaoR4CompressedTokenIndexTest.this.getFhirContext();
			}

			@Override
			public IIdType doCreateResource(IBaseResource theResource) {
				return FhirResourceDaoR4CompressedTokenIndexTest.this.doCreateResource(theResource);
			}

			@Override
			public IIdType doUpdateResource(IBaseResource theResource) {
				return FhirResourceDaoR4CompressedTokenIndexTest.this.doUpdateResource(theResource);
			}
		};
	}
}
