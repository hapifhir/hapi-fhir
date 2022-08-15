package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.param.ReferenceParam;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceIndexedSearchParamsTest {

	public static final String STRING_ID = "StringId";
	public static final String LONG_ID = "123";
	private ResourceIndexedSearchParams myParams;
	private ResourceTable mySource;
	private ModelConfig myModelConfig = new ModelConfig();

	@BeforeEach
	public void before() {
		mySource = new ResourceTable();
		mySource.setResourceType("Patient");

		myParams = new ResourceIndexedSearchParams(mySource);
	}

	@Test
	public void matchResourceLinksStringCompareToLong() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, LONG_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myModelConfig, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksStringCompareToString() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, STRING_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(STRING_ID);
		boolean result = myParams.matchResourceLinks(myModelConfig, "Patient", "organization", referenceParam, "organization");
		assertTrue(result);
	}

	@Test
	public void matchResourceLinksLongCompareToString() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, STRING_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myModelConfig, "Patient", "organization", referenceParam, "organization");
		assertFalse(result);
	}

	@Test
	public void matchResourceLinksLongCompareToLong() {
		ResourceLink link = ResourceLink.forLocalReference("organization", mySource, "Organization", 123L, LONG_ID, new Date(), null);
		myParams.getResourceLinks().add(link);

		ReferenceParam referenceParam = getReferenceParam(LONG_ID);
		boolean result = myParams.matchResourceLinks(myModelConfig, "Patient", "organization", referenceParam, "organization");
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
		assertThat(values.toString(), values, containsInAnyOrder("Patient?gender=male&name=JOHN", "Patient?gender=male&name=SMITH"));

		partsChoices = Lists.newArrayList(
			Lists.newArrayList("gender=male", ""),
			Lists.newArrayList("name=SMITH", "name=JOHN", "")
		);
		values = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains("Patient", partsChoices);
		assertThat(values.toString(), values, containsInAnyOrder("Patient?gender=male&name=JOHN", "Patient?gender=male&name=SMITH"));

		partsChoices = Lists.newArrayList(
		);
		values = ResourceIndexedSearchParams.extractCompositeStringUniquesValueChains("Patient", partsChoices);
		assertThat(values.toString(), values, empty());
	}

}
