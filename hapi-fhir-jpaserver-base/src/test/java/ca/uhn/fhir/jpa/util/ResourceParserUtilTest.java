package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.GZipUtil;
import ca.uhn.fhir.jpa.model.entity.ResourceEncodingEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.Serial;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static ca.uhn.fhir.jpa.util.ResourceParserUtil.EsrResourceDetails;
import static ca.uhn.fhir.jpa.util.ResourceParserUtil.determineTypeToParse;
import static ca.uhn.fhir.jpa.util.ResourceParserUtil.getEsrResourceDetails;
import static ca.uhn.fhir.jpa.util.ResourceParserUtil.getResourceText;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ResourceParserUtilTest {

	public static final String CUSTOM_PATIENT_PROFILE = "http://test.com/CustomPatient";
	private FhirContext myFhirContext;
	private Class<? extends IBaseResource> myDefaultResourceType;

	@BeforeEach
	void setUp() {
		myDefaultResourceType = Binary.class;
		myFhirContext = FhirContext.forR4();
	}

	@Test
	void testDetermineTypeToParse_withNullTagList_returnsDefaultType() {
		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, null);

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withEmptyTagList_returnsDefaultType() {
		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of());

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withNullTagDefinition_returnsDefaultType() {
		// setup
		ResourceTag tag = new ResourceTag();
		tag.setTag(null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_contextHasNoDefaultTypeForProfile_returnsDefaultType() {
		// setup
		ResourceTag tag = new ResourceTag();
		tag.setTag(null);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@ParameterizedTest
	@EnumSource(value = TagTypeEnum.class, names = {"TAG", "SECURITY_LABEL"})
	void testDetermineTypeToParse_tagIsNotProfileType_returnsDefaultType(TagTypeEnum theTagType) {
		// setup
		ResourceTag tag = createTag(theTagType, null, null, null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withBlankProfile_returnsDefaultType() {
		// setup
		ResourceTag tag = createTag(TagTypeEnum.PROFILE, null, "", null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_resourceTypeIsNotAssignable_returnsDefaultType() {
		// setup
		ResourceTag tag = createTag(TagTypeEnum.PROFILE, null, CUSTOM_PATIENT_PROFILE, null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		// validate
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withMultipleProfiles_returnsFirstMatch() {
		// setup
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		myFhirContext.setDefaultTypeForProfile("http://test.com/CustomObservation", CustomObservation.class);

		List<ResourceTag> tags = List.of(
			createTag(TagTypeEnum.PROFILE, "http://test.com", CUSTOM_PATIENT_PROFILE, "PatientDisplay"),
			createTag(TagTypeEnum.PROFILE, "http://test.com", "http://test.com/CustomObservation", "ObservationDisplay")
		);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, BaseResource.class, tags);

		// validate
		assertEquals(CustomPatient.class, type);
	}

	@Test
	void testDetermineTypeToParse_withMixedTagTypes_findsProfile() {
		// setup
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		List<ResourceTag> tags = List.of(
			createTag(TagTypeEnum.TAG, "http://example.com", "tag", "tagDisplay"),
			createTag(TagTypeEnum.SECURITY_LABEL, "http://terminology.hl7.org", "security", "securityLabelDisplay"),
			createTag(TagTypeEnum.PROFILE, "http://test.com", CUSTOM_PATIENT_PROFILE, "profileDisplay")
		);

		// execute
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, BaseResource.class, tags);

		// validate
		assertEquals(CustomPatient.class, type);
	}

	@Test
	void testDecodedResourceText_withResourceText_returnsProvidedText() {
		// setup
		String providedText = "{\"resourceType\":\"Patient\"}";
		byte[] resourceBytes = "byte content".getBytes(StandardCharsets.UTF_8);

		// execute
		String result = getResourceText(resourceBytes, providedText, ResourceEncodingEnum.JSON);

		// validate
		assertEquals(providedText, result);
	}

	@Test
	void testDecodedResourceText_withJsonEncoding_decodesFromBytes() {
		// setup
		String expectedText = "{\"resourceType\":\"Patient\",\"id\":\"123\"}";
		byte[] resourceBytes = expectedText.getBytes(StandardCharsets.UTF_8);

		// execute
		String result = getResourceText(resourceBytes, null, ResourceEncodingEnum.JSON);

		// validate
		assertEquals(expectedText, result);
	}

	@Test
	void testDecodedResourceText_withJsoncEncoding_decompressesAndDecodes() {
		// setup
		String originalText = "{\"resourceType\":\"Patient\",\"id\":\"456\"}";
		byte[] compressedBytes = GZipUtil.compress(originalText);

		// execute
		String result = getResourceText(compressedBytes, null, ResourceEncodingEnum.JSONC);

		// validate
		assertEquals(originalText, result);
	}

	@ParameterizedTest
	@EnumSource(value = ResourceEncodingEnum.class, names = {"DEL", "ESR"})
	void testDecodedResourceText_withDelOrEsrEncoding_returnsNull(ResourceEncodingEnum  theEncoding) {
		// setup
		byte[] resourceBytes = "content".getBytes(StandardCharsets.UTF_8);

		// execute
		String result = getResourceText(resourceBytes, null, theEncoding);

		// validate
		assertNull(result);
	}

	@Test
	void testGetEsrResourceDetails_withValidAddress_parsesCorrectly() {
		// execute
		EsrResourceDetails result = getEsrResourceDetails("providerId:address");

		// validate
		assertNotNull(result);
		assertEquals("providerId", result.providerId());
		assertEquals("address", result.address());
	}

	@ParameterizedTest
	@CsvSource({
		"invalidAddress, Invalid ESR address",
		":address  	   , Invalid ESR address",
		"'   :address' , No provider ID in ESR address",
		"'provider:   ', No address in ESR address",
		"provider:	   , No address in ESR address",
		"''			   , Invalid ESR address"
	})
	void testGetEsrResourceDetails_withInvalidAddress_throwsException(String theEsrAddress, String theExpectedMessage) {
		assertThatThrownBy(() -> getEsrResourceDetails(theEsrAddress))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining(theExpectedMessage);
	}

	@ResourceDef(name = "Patient", profile = CUSTOM_PATIENT_PROFILE)
	static class CustomPatient extends org.hl7.fhir.r4.model.Patient {
		@Serial
		private static final long serialVersionUID = 1L;
	}

	@ResourceDef(name = "Observation", profile = "http://test.com/CustomObservation")
	static class CustomObservation extends org.hl7.fhir.r4.model.Observation {
		@Serial
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Creates a ResourceTag with all TagDefinition fields
	 */
	private static ResourceTag createTag(TagTypeEnum theTagType, String theSystem, String theCode, String theDisplay) {
		ResourceTag tag = new ResourceTag();
		TagDefinition tagDef = new TagDefinition(theTagType, theSystem, theCode, theDisplay);
		tag.setTag(tagDef);
		return tag;
	}
}
