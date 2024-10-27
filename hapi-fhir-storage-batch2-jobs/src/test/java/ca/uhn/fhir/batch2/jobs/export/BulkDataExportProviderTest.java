package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class BulkDataExportProviderTest {
	private static final Set<FhirVersionEnum> PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE = Set.of(FhirVersionEnum.DSTU2, FhirVersionEnum.DSTU2_1, FhirVersionEnum.DSTU2_HL7ORG, FhirVersionEnum.DSTU3, FhirVersionEnum.R4, FhirVersionEnum.R4B);

	private static Stream<Arguments> fhirContexts() {
			return Stream.of(
				Arguments.arguments(FhirContext.forDstu2()),
				Arguments.arguments(FhirContext.forDstu2Cached()),
				Arguments.arguments(FhirContext.forDstu2Hl7Org()),
				Arguments.arguments(FhirContext.forDstu2Hl7OrgCached()),
				Arguments.arguments(FhirContext.forDstu3()),
				Arguments.arguments(FhirContext.forDstu3Cached()),
				Arguments.arguments(FhirContext.forR4()),
				Arguments.arguments(FhirContext.forR4Cached()),
				Arguments.arguments(FhirContext.forR4B()),
				Arguments.arguments(FhirContext.forR4BCached()),
				Arguments.arguments(FhirContext.forR5()),
				Arguments.arguments(FhirContext.forR5Cached())
			);
		}

	@ParameterizedTest
	@MethodSource("fhirContexts")
	void checkDeviceIsSupportedInPatientCompartment(FhirContext theFhirContext) {
		Set<String> resourceNames = new BulkDataExportProvider().getPatientCompartmentResources(theFhirContext);
		if (PATIENT_COMPARTMENT_FHIR_VERSIONS_SUPPORT_DEVICE.contains(theFhirContext.getVersion().getVersion())) {
			assertThat(resourceNames).contains("Device");
		} else {
			assertThat(resourceNames).doesNotContain("Device");
		}
	}
}
