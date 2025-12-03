package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.BaseTag;
import ca.uhn.fhir.jpa.model.entity.ResourceTag;
import ca.uhn.fhir.jpa.model.entity.TagDefinition;
import ca.uhn.fhir.jpa.model.entity.TagTypeEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.Serial;
import java.util.List;

import static ca.uhn.fhir.jpa.util.ResourceParserUtil.determineTypeToParse;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, null);
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withEmptyTagList_returnsDefaultType() {
		List<? extends BaseTag> tags = List.of();
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, tags);
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withNullTagDefinition_returnsDefaultType() {
		ResourceTag tag = new ResourceTag();
		tag.setTag(null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));

		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_contextHasNoDefaultTypeForProfile_returnsDefaultType() {
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of());
		assertEquals(myDefaultResourceType, type);
	}

	@ParameterizedTest
	@EnumSource(value = TagTypeEnum.class, names = {"TAG", "SECURITY_LABEL"})
	void testDetermineTypeToParse_tagIsNotProfileType_returnsDefaultType(TagTypeEnum theTagType) {
		ResourceTag tag = createTag(theTagType, null, null, null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withBlankProfile_returnsDefaultType() {
		ResourceTag tag = createTag(TagTypeEnum.PROFILE, null, "", null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_resourceTypeIsNotAssignable_returnsDefaultType() {
		ResourceTag tag = createTag(TagTypeEnum.PROFILE, null, CUSTOM_PATIENT_PROFILE, null);
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, List.of(tag));
		assertEquals(myDefaultResourceType, type);
	}

	@Test
	void testDetermineTypeToParse_withMultipleProfiles_returnsFirstMatch() {
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		myFhirContext.setDefaultTypeForProfile("http://test.com/CustomObservation", CustomObservation.class);

		List<ResourceTag> tags = List.of(
			createTag(TagTypeEnum.PROFILE, "http://test.com", CUSTOM_PATIENT_PROFILE, "PatientDisplay"),
			createTag(TagTypeEnum.PROFILE, "http://test.com", "http://test.com/CustomObservation", "ObservationDisplay")
		);

		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, tags);

		assertEquals(CustomPatient.class, type);
	}

	@Test
	void testDetermineTypeToParse_withMixedTagTypes_findsProfile() {
		myFhirContext.setDefaultTypeForProfile(CUSTOM_PATIENT_PROFILE, CustomPatient.class);
		List<ResourceTag> tags = List.of(
			createTag(TagTypeEnum.TAG, "http://example.com", "tag", "tagDisplay"),
			createTag(TagTypeEnum.SECURITY_LABEL, "http://terminology.hl7.org", "security", "securityLabelDisplay"),
			createTag(TagTypeEnum.PROFILE, "http://test.com", CUSTOM_PATIENT_PROFILE, "profileDisplay")
		);

		Class<? extends IBaseResource> type = determineTypeToParse(myFhirContext, myDefaultResourceType, tags);
		assertEquals(CustomPatient.class, type);
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
