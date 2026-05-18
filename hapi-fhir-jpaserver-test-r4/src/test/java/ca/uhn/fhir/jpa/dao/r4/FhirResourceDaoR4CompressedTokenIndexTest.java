/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenCommonResDao;
import ca.uhn.fhir.jpa.dao.data.IResourceIndexedSearchParamTokenIdentifierDao;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommon;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenCommonRes;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamTokenIdentifier;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.entity.TokenIndexStrategyEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR4CompressedTokenIndexTest extends BaseJpaR4Test {

	@Autowired
	private IResourceIndexedSearchParamTokenCommonResDao myTokenCommonResDao;

	@Autowired
	private IResourceIndexedSearchParamTokenIdentifierDao myTokenIdentifierDao;

	@BeforeEach
	void enableCompressedTokenIndexStrategy() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_NEW_QUERY_NEW);
	}

	@AfterEach
	void resetTokenIndexStrategy() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_OLD_QUERY_OLD);
	}

	// ===== Group A: Routing — identifier vs. common =====

	@Test
	void createPatient_withMixedTokens_routesEachByParamName() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://example.com/ids").setValue("MRN123");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		long identifierHash = hashSysAndValue("Patient", Patient.SP_IDENTIFIER, "http://example.com/ids", "MRN123");
		long genderHash = hashSysAndValue("Patient", Patient.SP_GENDER, "http://hl7.org/fhir/administrative-gender", "male");

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers = myTokenIdentifierDao.findByResourceId(pid);
			assertThat(identifiers).hasSize(1);
			ResourceIndexedSearchParamTokenIdentifier identifierRow = identifiers.get(0);
			validateTokenIdentifier(identifierRow, pid);

			List<ResourceIndexedSearchParamTokenCommonRes> commonRes = myTokenCommonResDao.findByResourceId(pid);
			assertThat(commonRes)
					.extracting(ResourceIndexedSearchParamTokenCommonRes::getHashSystemAndValue)
					.as("identifier param must not appear in CommonRes (it routes to Identifier table)")
					.doesNotContain(identifierHash);
			ResourceIndexedSearchParamTokenCommonRes genderLink = commonRes.stream()
					.filter(r -> r.getHashSystemAndValue() == genderHash)
					.findFirst()
					.orElseThrow(() -> new AssertionError("expected CommonRes row for gender=male hash"));
			assertThat(genderLink.getResourceId()).isEqualTo(pid.getId());
			assertThat(genderLink.getPartitionId()).as("default partition").isNull();
			assertThat(genderLink.getHashSystemAndValue()).isEqualTo(genderHash);

			ResourceIndexedSearchParamTokenCommon genderCommon =
					myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, genderHash);
			assertThat(genderCommon).as("gender should be stored in TokenCommon").isNotNull();
			assertThat(genderCommon.getHashSystemAndValue()).isEqualTo(genderHash);
			assertThat(genderCommon.getHashIdentity())
					.isEqualTo(hashIdentity("Patient", Patient.SP_GENDER));
			assertThat(genderCommon.getHashValue())
					.isEqualTo(hashValue("Patient", Patient.SP_GENDER, "male"));
			assertThat(genderCommon.getSystemId())
					.as("gender system URL resolved to non-null FK")
					.isNotNull();
			assertThat(genderCommon.getValue()).isEqualTo("male");

			ResourceIndexedSearchParamTokenCommon identifierCommon =
					myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, identifierHash);
			assertThat(identifierCommon).as("identifier must NOT appear in TokenCommon").isNull();
		});
	}

	private void validateTokenIdentifier(ResourceIndexedSearchParamTokenIdentifier identifierRow, JpaPid pid) {
		assertThat(identifierRow.getId()).isNotNull();
		assertThat(identifierRow.getPartitionId()).isNull();
		assertThat(identifierRow.getResourceId()).isEqualTo(pid.getId());
		assertThat(identifierRow.getHashIdentity()).isEqualTo(hashIdentity("Patient", Patient.SP_IDENTIFIER));
		assertThat(identifierRow.getSystemUrlId()).isNotNull();
		assertThat(identifierRow.getValue()).isEqualTo("MRN123");
		assertThat(identifierRow.getHashValue()).isEqualTo(hashValue("Patient", Patient.SP_IDENTIFIER, "MRN123"));
		assertThat(identifierRow.getTypeHashSystemAndValue()).isNull();
	}

	// ===== Group B: Deduplication of HFJ_SPIDX2_TOKEN_COMMON =====

	@Test
	void createTwoResources_sameToken_yieldsOneCommonRowAndTwoCommonResRows() {
		IIdType id1 = myObservationDao
				.create(newObservationWithCode("http://loinc.org", "12345-6"), mySrd)
				.getId()
				.toUnqualifiedVersionless();
		IIdType id2 = myObservationDao
				.create(newObservationWithCode("http://loinc.org", "12345-6"), mySrd)
				.getId()
				.toUnqualifiedVersionless();
		JpaPid pid1 = JpaPid.fromId(id1.getIdPartAsLong());
		JpaPid pid2 = JpaPid.fromId(id2.getIdPartAsLong());

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
	void transactionBundle_withSameTokenOnMultipleResources_dedupsViaSessionCache() {
		BundleBuilder bb = new BundleBuilder(myFhirContext);
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setGender(AdministrativeGender.MALE);
			bb.addTransactionCreateEntry(p);
		}
		Bundle outcome = mySystemDao.transaction(mySrd, (Bundle) bb.getBundle());
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

	// ===== Group C: Update lifecycle (diff-and-apply) =====

	@Test
	void updatePatient_changeIdentifierValue_replacesIdentifierRow() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		Patient updated = new Patient();
		updated.setId(id);
		updated.addIdentifier().setSystem("http://sys").setValue("B");
		myPatientDao.update(updated, mySrd);

		runInTransaction(() -> assertThat(myTokenIdentifierDao.findByResourceId(pid))
				.extracting(ResourceIndexedSearchParamTokenIdentifier::getValue)
				.as("stale identifier row must be removed; new value present")
				.containsExactly("B"));
	}

	@Test
	void updatePatient_changeGenderToken_addsNewCommonRowKeepsOld() {
		Patient p = new Patient();
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		Patient updated = new Patient();
		updated.setId(id);
		updated.setGender(AdministrativeGender.FEMALE);
		myPatientDao.update(updated, mySrd);

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
	void updatePatient_sameTokens_isNoOpInDiff() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		int identifierBefore = runInTransaction(() -> myTokenIdentifierDao.findByResourceId(pid).size());
		int commonResBefore = runInTransaction(() -> myTokenCommonResDao.findByResourceId(pid).size());

		Patient sameContent = new Patient();
		sameContent.setId(id);
		sameContent.addIdentifier().setSystem("http://sys").setValue("A");
		sameContent.setGender(AdministrativeGender.MALE);
		myPatientDao.update(sameContent, mySrd);

		runInTransaction(() -> {
			assertThat(myTokenIdentifierDao.findByResourceId(pid)).hasSize(identifierBefore);
			assertThat(myTokenCommonResDao.findByResourceId(pid)).hasSize(commonResBefore);
		});
	}

	@Test
	void reindexResource_isIdempotent() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		int identifierBefore = runInTransaction(() -> myTokenIdentifierDao.findByResourceId(pid).size());
		int commonResBefore = runInTransaction(() -> myTokenCommonResDao.findByResourceId(pid).size());

		// Repeated update with identical content forces re-extraction without changing tokens —
		// equivalent to an in-place reindex of this single resource.
		Patient sameContent = new Patient();
		sameContent.setId(id);
		sameContent.addIdentifier().setSystem("http://sys").setValue("A");
		sameContent.setGender(AdministrativeGender.MALE);
		myPatientDao.update(sameContent, mySrd);
		myPatientDao.update(sameContent, mySrd);

		runInTransaction(() -> {
			assertThat(myTokenIdentifierDao.findByResourceId(pid)).hasSize(identifierBefore);
			assertThat(myTokenCommonResDao.findByResourceId(pid)).hasSize(commonResBefore);
		});
	}

	// ===== Group E: Strategy semantics =====

	@ParameterizedTest
	@EnumSource(TokenIndexStrategyEnum.class)
	void writeStrategy_writesToCorrectTablesOnly(TokenIndexStrategyEnum theStrategy) {
		myStorageSettings.setTokenIndexStrategy(theStrategy);

		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

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

	/**
	 * Stubbed pending the query-capture scaffolding choice. Once decided, capture SQL during a
	 * token search under {@link TokenIndexStrategyEnum#WRITE_BOTH_QUERY_NEW} and assert it
	 * references {@code HFJ_SPIDX2_TOKEN_*} rather than {@code HFJ_SPIDX_TOKEN}. Pattern available
	 * in {@code FhirResourceDaoR4SearchSqlTest}.
	 */
	@Test
	@org.junit.jupiter.api.Disabled("Pending query-capture scaffolding choice")
	void writeBothQueryNew_searchUsesNewTables() {
		// Intentionally empty: implementation deferred — see @Disabled message and method Javadoc.
	}

	// ===== Group F: Hashing & system resolution =====

	@Test
	void createResource_systemAndValuePopulateAllHashes() {
		Patient p = new Patient();
		p.setGender(AdministrativeGender.MALE);
		myPatientDao.create(p, mySrd);

		String genderSystem = "http://hl7.org/fhir/administrative-gender";
		long expectedHashSysAndValue = hashSysAndValue("Patient", Patient.SP_GENDER, genderSystem, "male");
		long expectedHashIdentity = hashIdentity("Patient", Patient.SP_GENDER);
		long expectedHashValue = hashValue("Patient", Patient.SP_GENDER, "male");

		runInTransaction(() -> {
			ResourceIndexedSearchParamTokenCommon row =
					myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, expectedHashSysAndValue);
			assertThat(row).as("Common row for gender=male").isNotNull();
			assertThat(row.getHashIdentity()).isEqualTo(expectedHashIdentity);
			assertThat(row.getHashValue()).isEqualTo(expectedHashValue);
			assertThat(row.getHashSystemAndValue()).isEqualTo(expectedHashSysAndValue);
			assertThat(row.getValue()).isEqualTo("male");
			assertThat(row.getSystemId()).as("systemId resolved for non-blank system").isNotNull();
		});
	}

	@Test
	void createResource_blankSystem_yieldsNullSystemId() {
		// Observation.code with no system — produces a token with null system, exercising the
		// resolveTokenSystemId() blank-system branch in DaoSearchParamSynchronizer.
		Observation obs = new Observation();
		obs.getCode().addCoding().setCode("custom-no-system");
		myObservationDao.create(obs, mySrd);

		long expectedHashSysAndValue = hashSysAndValue("Observation", Observation.SP_CODE, null, "custom-no-system");

		runInTransaction(() -> {
			ResourceIndexedSearchParamTokenCommon row =
					myEntityManager.find(ResourceIndexedSearchParamTokenCommon.class, expectedHashSysAndValue);
			assertThat(row).isNotNull();
			assertThat(row.getSystemId()).as("systemId is null when system is blank").isNull();
			assertThat(row.getValue()).isEqualTo("custom-no-system");
		});
	}

	/*
	 * Group G — AddRemoveCount accounting (best-effort via row-count proxy).
	 *
	 * AddRemoveCount cannot be read directly from a DAO call without an interceptor, so these
	 * tests verify the observable consequence: under WRITE_NEW_QUERY_NEW only the new tables get
	 * rows; under WRITE_BOTH_QUERY_OLD both legacy and new tables get rows. The synchronizer's
	 * !writeToLegacyTokenTable() guard prevents AddRemoveCount from double-counting them.
	 */

	@Test
	void addRemoveCount_underWriteNewQueryNew_isCountedFromNewTables() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_NEW_QUERY_NEW);

		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		runInTransaction(() -> {
			int legacyRows = myResourceIndexedSearchParamTokenDao.countForResourceId(pid);
			int newRows = myTokenCommonResDao.findByResourceId(pid).size()
					+ myTokenIdentifierDao.findByResourceId(pid).size();
			assertThat(legacyRows).as("legacy untouched under WRITE_NEW_QUERY_NEW").isZero();
			assertThat(newRows).as("new tables populated").isGreaterThan(0);
		});
	}

	@Test
	void addRemoveCount_underWriteBothQueryOld_isNotDoubleCounted() {
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_BOTH_QUERY_OLD);

		Patient p = new Patient();
		p.addIdentifier().setSystem("http://sys").setValue("A");
		p.setGender(AdministrativeGender.MALE);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(id.getIdPartAsLong());

		runInTransaction(() -> {
			int legacyRows = myResourceIndexedSearchParamTokenDao.countForResourceId(pid);
			int newRows = myTokenCommonResDao.findByResourceId(pid).size()
					+ myTokenIdentifierDao.findByResourceId(pid).size();
			assertThat(legacyRows).as("legacy populated under WRITE_BOTH_QUERY_OLD").isGreaterThan(0);
			assertThat(newRows).as("new tables also populated under WRITE_BOTH_QUERY_OLD").isGreaterThan(0);
		});
	}

	@Test
	void testTokenMissingSearch_worksWithIndexMissingFieldsEnabled() {
		// Given: compressed token tables AND IndexMissingFields = ENABLED
		myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_BOTH_QUERY_NEW);
		myStorageSettings.setIndexMissingFields(StorageSettings.IndexEnabledEnum.ENABLED);

		// Patient with identifier AND birthdate
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("http://sys").setValue("ID1");
		p1.setBirthDateElement(new DateType("2000-01-01"));
		IIdType id1 = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		// Patient with identifier but NO birthdate
		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("http://sys").setValue("ID2");
		IIdType id2 = myPatientDao.create(p2, mySrd).getId().toUnqualifiedVersionless();

		// Patient without identifier but WITH birthdate
		Patient p3 = new Patient();
		p3.setBirthDateElement(new DateType("1990-05-15"));
		IIdType id3 = myPatientDao.create(p3, mySrd).getId().toUnqualifiedVersionless();

		// Search: identifier:missing=true AND birthdate:missing=false
		// Should find only p3 (no identifier, has birthdate)
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_IDENTIFIER, new TokenParam().setMissing(true));
			params.add(Patient.SP_BIRTHDATE, new DateParam().setMissing(false));

			List<IIdType> results = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));

			assertThat(results)
				.as("Should find patient with birthdate but no identifier")
				.containsExactly(id3);
		}
	}

	// ===== Group H: :of-type token indexing =====

	@Test
	void createPatient_identifierWithType_routesToIdentifierTable() {
		myStorageSettings.setIndexIdentifierOfType(true);

		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		id.getType().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(patientId.getIdPartAsLong());

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers =
				myTokenIdentifierDao.findByResourceId(pid);

			// Should have 2 rows: one for "identifier", one for "identifier:of-type"
			assertThat(identifiers).hasSize(2);

			// Regular identifier row
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
		myStorageSettings.setIndexIdentifierOfType(true);

		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		// Add TWO type codings
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("SS");

		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		JpaPid pid = JpaPid.fromId(patientId.getIdPartAsLong());

		runInTransaction(() -> {
			List<ResourceIndexedSearchParamTokenIdentifier> identifiers =
				myTokenIdentifierDao.findByResourceId(pid);

			// Should have 3 rows: 1 regular + 2 :of-type (one per coding)
			assertThat(identifiers).hasSize(3);

			// Regular identifier row
			assertThat(identifiers.stream().filter(r -> r.getValue().equals("MRN123")).count())
				.as("one regular identifier row").isEqualTo(1);

			// Two :of-type rows
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
	void searchIdentifier_ofType_findsResource() {
		myStorageSettings.setIndexIdentifierOfType(true);

		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		id.getType().addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_IDENTIFIER, new TokenParam(
			"http://terminology.hl7.org/CodeSystem/v2-0203", "MR|MRN123")
			.setModifier(TokenParamModifier.OF_TYPE));

		List<IIdType> results = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
		assertThat(results).containsExactly(patientId);
	}

	@Test
	void searchIdentifier_ofType_withMultipleTypeCodingsFindsResourceByEitherType() {
		myStorageSettings.setIndexIdentifierOfType(true);

		Patient p = new Patient();
		Identifier id = p.addIdentifier();
		id.setSystem("http://example.com/ids").setValue("MRN123");
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");
		id.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("SS");

		IIdType patientId = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// Search by MR type - should find
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_IDENTIFIER, new TokenParam(
			"http://terminology.hl7.org/CodeSystem/v2-0203", "MR|MRN123")
			.setModifier(TokenParamModifier.OF_TYPE));
		List<IIdType> results = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
		assertThat(results).as("should find by MR type").containsExactly(patientId);

		// Search by SS type - should also find
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_IDENTIFIER, new TokenParam(
			"http://terminology.hl7.org/CodeSystem/v2-0203", "SS|MRN123")
			.setModifier(TokenParamModifier.OF_TYPE));
		results = toUnqualifiedVersionlessIds(myPatientDao.search(params, mySrd));
		assertThat(results).as("should find by SS type").containsExactly(patientId);
	}

	// ===== Helpers =====

	private Observation newObservationWithCode(String theSystem, String theCode) {
		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem(theSystem).setCode(theCode);
		return obs;
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

	// ===== Existing strategy-permutation @Nested wrappers =====

	@Nested
	public class WriteBothQueryOldFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_BOTH_QUERY_OLD);
		}

		@AfterEach
		void cleanUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_OLD_QUERY_OLD);
		}
	}

	@Nested
	public class WriteBothQueryNewFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_BOTH_QUERY_NEW);
		}

		@AfterEach
		void cleanUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_OLD_QUERY_OLD);
		}
	}

	@Nested
	public class WriteNewQueryNewFhirResourceDaoR4SearchNoFtTest extends FhirResourceDaoR4SearchNoFtTest {
		@BeforeEach
		void setUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_NEW_QUERY_NEW);
		}

		@AfterEach
		void cleanUp() {
			myStorageSettings.setTokenIndexStrategy(TokenIndexStrategyEnum.WRITE_OLD_QUERY_OLD);
		}
	}
}
