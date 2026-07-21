package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.model.util.SearchParamHash;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourceIndexedSearchParamsTest {

	public static final String STRING_ID = "StringId";
	public static final String LONG_ID = "123";
	private ResourceIndexedSearchParams myParams;
	private ResourceTable mySource;
	private StorageSettings myStorageSettings = new StorageSettings();

	@BeforeEach
	void before() {
		mySource = new ResourceTable();
		mySource.setResourceType("Patient");

		myParams = ResourceIndexedSearchParams.withLists(mySource);
	}

	@Test
	void matchResourceLinksStringCompareToLong() {
		ResourceLink link = getResourceLinkForLocalReference(LONG_ID);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	void matchResourceLinksStringCompareToString() {
		ResourceLink link = getResourceLinkForLocalReference(STRING_ID);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	@Test
	void matchResourceLinksLongCompareToString() {
		ResourceLink link = getResourceLinkForLocalReference(STRING_ID);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	void matchResourceLinksLongCompareToLong() {
		ResourceLink link = getResourceLinkForLocalReference(LONG_ID);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	private ResourceLink getResourceLinkForLocalReference(String theTargetResourceId){

		ResourceLink.ResourceLinkForLocalReferenceParams params = ResourceLink.ResourceLinkForLocalReferenceParams
			.instance()
			.setSourcePath("organization")
			.setSourceResource(mySource)
			.setTargetResourceType("Organization")
			.setTargetResourcePid(123L)
			.setTargetResourceId(theTargetResourceId)
			.setUpdated(new Date());

		return ResourceLink.forLocalReference(params);

	}

	private ReferenceParam getReferenceParam(String theId) {
		ReferenceParam retVal = new ReferenceParam();
		retVal.setValue(theId);
		return retVal;
	}

	@Test
	void match_withLogicalReference_works() {
		ResourceTable table = new ResourceTable();
		table.setResourceType("QuestionnaireResponse");
		ResourceIndexedSearchParams matcher = ResourceIndexedSearchParams
			.withLists(table);
		ResourceLink resourceLink = ResourceLink.forLogicalReference(
			"QuestionnaireResponse.questionnaire",
			table,
			"http://localhost:8000/Questionnaire/a1",
			new Date()
		);
		matcher.getResourceLinks()
			.add(resourceLink);

		// test
		boolean match = matcher.matchResourceLinks(
			myStorageSettings,
			"QuestionnaireResponse",
			"questionnaire",
			new ReferenceParam("http://localhost:8000/Questionnaire/a1"),
			"QuestionnaireResponse.questionnaire"
		);

		// validate
		assertTrue(match);
	}

	@Test
	void testExtractCompositeStringUniquesValueChains() {
		List<List<String>> partsChoices;
		Set<String> values;

		partsChoices = Lists.newArrayList(
			Lists.newArrayList("gender=male"),
			Lists.newArrayList("name=SMITH", "name=JOHN")
		);
		values = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains("Patient", partsChoices);
		assertThat(values).as(values.toString()).containsExactlyInAnyOrder("Patient?gender=male&name=JOHN", "Patient?gender=male&name=SMITH");

		partsChoices = Lists.newArrayList(
			Lists.newArrayList("gender=male", ""),
			Lists.newArrayList("name=SMITH", "name=JOHN", "")
		);
		values = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains("Patient", partsChoices);
		assertThat(values).as(values.toString()).containsExactlyInAnyOrder("Patient?gender=male&name=JOHN", "Patient?gender=male&name=SMITH");

		partsChoices = Lists.newArrayList(
		);
		values = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains("Patient", partsChoices);
		assertThat(values).as(values.toString()).isEmpty();
	}

	@ParameterizedTest
	@CsvSource({
		"name,  name,                      , false, true",
		"name,  NAME,                      , false, true",
		"name,  name,                  7000, false, true",
		"name,  param,                     , false, false",
		"name,  param,                 7000, false, false",
		"    ,  name,  -1575415002568401616, true,  true",
		"param, name,  -1575415002568401616, true,  true",
		"    ,  param, -1575415002568401616, true,  false",
		"name,  param, -1575415002568401616, true,  false",
	})
	void testIsMatchSearchParams_matchesByParamNameOrHashIdentity(String theParamName,
																		 String theExpectedParamName,
																		 Long theHashIdentity,
																		 boolean theIndexStorageOptimized,
																		 boolean theShouldMatch) {
		// setup
		PartitionSettings partitionSettings = new PartitionSettings();
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setIndexStorageOptimized(theIndexStorageOptimized);
		ResourceIndexedSearchParamString param = new ResourceIndexedSearchParamString();
		param.setResourceType("Patient");
		param.setParamName(theParamName);
		param.setHashIdentity(theHashIdentity);

		// execute
		boolean isMatch = ResourceIndexedSearchParams.isMatchSearchParam(partitionSettings, storageSettings, "Patient", theExpectedParamName, param);

		// validate
		assertThat(isMatch).isEqualTo(theShouldMatch);
	}

	@Test
	void testIsMatchSearchParam_withConfiguredDefaultPartition_usesConfiguredPartitionInHash() {
		// setup: partitioning enabled with the partition mixed into search hashes, and a configured default partition
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setPartitioningEnabled(true);
		partitionSettings.setIncludePartitionInSearchHashes(true);
		partitionSettings.setDefaultPartitionId(7);

		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setIndexStorageOptimized(true);

		// a param whose stored hash identity was computed with the configured default partition (id 7) mixed in
		long hashWithConfiguredDefault = SearchParamHash.hashSearchParam(
				partitionSettings, partitionSettings.getDefaultRequestPartitionId(), "Patient", "name");
		ResourceIndexedSearchParamString param = new ResourceIndexedSearchParamString();
		param.setResourceType("Patient");
		param.setParamName("name");
		param.setHashIdentity(hashWithConfiguredDefault);

		// execute + validate: isMatchSearchParam now honors the configured default partition, so the hashes match
		assertThat(ResourceIndexedSearchParams.isMatchSearchParam(
						partitionSettings, storageSettings, "Patient", "name", param))
				.isTrue();

		// and the configured-default hash genuinely differs from the unconfigured (null-partition) hash that the old
		// hard-coded new PartitionSettings()/defaultPartition() would have produced
		long hashWithDefaultSettings = SearchParamHash.hashSearchParam(
				new PartitionSettings(), new PartitionSettings().getDefaultRequestPartitionId(), "Patient", "name");
		assertThat(hashWithConfiguredDefault).isNotEqualTo(hashWithDefaultSettings);
	}

	@Test
	void matchParam_deprecatedOverload_matchesByValueAsBefore() {
		StorageSettings storageSettings = new StorageSettings();
		RuntimeSearchParam nameParamDef = new RuntimeSearchParam(
				null,
				null,
				"name",
				"name",
				"Patient.name",
				RestSearchParameterTypeEnum.STRING,
				Set.of(),
				Set.of(),
				RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE,
				null,
				null,
				null);
		// valueNormalized is upper-cased to mirror StringUtil.normalizeStringForSearchIndexing, so the query matches
		myParams.myStringParams.add(new ResourceIndexedSearchParamString(
				new PartitionSettings(), storageSettings, "Patient", "name", "SMITH", "Smith"));

		// as before: a matching value matches, a non-matching value does not
		assertThat(myParams.matchParam(storageSettings, "Patient", "name", nameParamDef, new StringParam("smith")))
				.isTrue();
		assertThat(myParams.matchParam(storageSettings, "Patient", "name", nameParamDef, new StringParam("jones")))
				.isFalse();
	}

	@Test
	void isMatchSearchParam_deprecatedOverload_assumesDefaultPartitionAsBefore() {
		StorageSettings storageSettings = new StorageSettings();

		// param-name branch (index storage not optimized): as before, matches case-insensitively by param name
		ResourceIndexedSearchParamString byName = new ResourceIndexedSearchParamString();
		byName.setResourceType("Patient");
		byName.setParamName("name");
		storageSettings.setIndexStorageOptimized(false);
		assertThat(ResourceIndexedSearchParams.isMatchSearchParam(storageSettings, "Patient", "NAME", byName))
				.isTrue();
		assertThat(ResourceIndexedSearchParams.isMatchSearchParam(storageSettings, "Patient", "other", byName))
				.isFalse();

		// hash-identity branch (index storage optimized): as before, the deprecated overload assumes the
		// default (null) partition when computing the hash, ignoring any configured non-null default partition
		storageSettings.setIndexStorageOptimized(true);

		// a param indexed under the default (null) partition matches
		long defaultPartitionHash = SearchParamHash.hashSearchParam(
				new PartitionSettings(), new PartitionSettings().getDefaultRequestPartitionId(), "Patient", "name");
		ResourceIndexedSearchParamString indexedUnderDefault = new ResourceIndexedSearchParamString();
		indexedUnderDefault.setResourceType("Patient");
		indexedUnderDefault.setParamName("name");
		indexedUnderDefault.setHashIdentity(defaultPartitionHash);
		assertThat(ResourceIndexedSearchParams.isMatchSearchParam(
						storageSettings, "Patient", "name", indexedUnderDefault))
				.isTrue();

		// but a param indexed under a configured non-default partition must NOT match: the deprecated overload
		// cannot honor a configured default partition
		PartitionSettings configuredDefault = new PartitionSettings();
		configuredDefault.setPartitioningEnabled(true);
		configuredDefault.setIncludePartitionInSearchHashes(true);
		configuredDefault.setDefaultPartitionId(7);
		long configuredPartitionHash = SearchParamHash.hashSearchParam(
				configuredDefault, configuredDefault.getDefaultRequestPartitionId(), "Patient", "name");
		ResourceIndexedSearchParamString indexedUnderPartition7 = new ResourceIndexedSearchParamString();
		indexedUnderPartition7.setResourceType("Patient");
		indexedUnderPartition7.setParamName("name");
		indexedUnderPartition7.setHashIdentity(configuredPartitionHash);
		assertThat(ResourceIndexedSearchParams.isMatchSearchParam(
						storageSettings, "Patient", "name", indexedUnderPartition7))
				.isFalse();
	}
}
