package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.param.ReferenceParam;
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

public class ResourceIndexedSearchParamsTest {

	public static final String STRING_ID = "StringId";
	public static final String LONG_ID = "123";
	private ResourceIndexedSearchParams myParams;
	private ResourceTable mySource;
	private StorageSettings myStorageSettings = new StorageSettings();

	@BeforeEach
	public void before() {
		mySource = new ResourceTable();
		mySource.setResourceType("Patient");

		myParams = ResourceIndexedSearchParams.withLists(mySource);
	}

	@Test
	public void matchResourceLinksStringCompareToLong() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, LONG_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksStringCompareToString() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, STRING_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	@Test
	public void matchResourceLinksLongCompareToString() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, STRING_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksLongCompareToLong() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, LONG_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myStorageSettings, "Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	private ReferenceParam getReferenceParam(String theId) {
		ReferenceParam retVal = new ReferenceParam();
		retVal.setValue(theId);
		return retVal;
	}

	@Test
	public void testExtractCompositeStringUniquesValueChains() {
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
	public void testIsMatchSearchParams_matchesByParamNameOrHashIdentity(String theParamName,
																		 String theExpectedParamName,
																		 Long theHashIdentity,
																		 boolean theIndexStorageOptimized,
																		 boolean theShouldMatch) {
		// setup
		StorageSettings storageSettings = new StorageSettings();
		storageSettings.setIndexStorageOptimized(theIndexStorageOptimized);
		ResourceIndexedSearchParamString param = new ResourceIndexedSearchParamString();
		param.setResourceType("Patient");
		param.setParamName(theParamName);
		param.setHashIdentity(theHashIdentity);

		// execute
		boolean isMatch = ResourceIndexedSearchParams.isMatchSearchParam(storageSettings, "Patient", theExpectedParamName, param);

		// validate
		assertThat(isMatch).isEqualTo(theShouldMatch);
	}
}
