package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.ConceptMap;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConceptMapAddAndRemoveMappingProviderTest {

	private final FhirContext myContext = FhirContext.forR5Cached();

	@Mock
	private IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;
	@SuppressWarnings("JUnitMalformedDeclaration")
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(myContext)
		.keepAliveBetweenTests()
		.withServer(t -> {
			ConceptMapAddAndRemoveMappingProvider provider = new ConceptMapAddAndRemoveMappingProvider(myConceptMapDao);
			t.registerProvider(provider);
		});
	@Captor
	private ArgumentCaptor<IFhirResourceDaoConceptMap.AddMappingRequest> myAddMappingRequestCaptor;

	@Test
	public void testAddMapping() {
		// Setup
		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.createOperationOutcome(
			OperationOutcomeUtil.OO_SEVERITY_WARN,
			"Mapping has been added",
			OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING,
			myContext,
			null
		);
		when(myConceptMapDao.addMapping(any(),any())).thenReturn(operationOutcome);

		// Test
		IGenericClient client = myServer.getFhirClient();
		client
			.operation()
			.onType(ConceptMap.class)
			.named(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING)
			.withParameter(Parameters.class, JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, new UriType("http://my-concept-map-url"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, new UriType("http://my-code-system"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, new CodeType("1.0"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, new StringType("BP-SYS"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_DISPLAY, new StringType("Systolic BP"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, new UriType("http://loinc.org"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, new CodeType("2.57"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, new CodeType("11378-7"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_DISPLAY, new StringType("Systolic blood pressure at First encounter"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_EQUIVALENCE, new CodeType("equivalent"))
			.returnResourceType(OperationOutcome.class)
			.execute();

		// Verify
		verify(myConceptMapDao, times(1)).addMapping(myAddMappingRequestCaptor.capture(), any());
		IFhirResourceDaoConceptMap.AddMappingRequest request = myAddMappingRequestCaptor.getValue();
		assertEquals("http://my-concept-map-url", request.getConceptMapUri());
		assertEquals("http://my-code-system", request.getSourceSystem());
		assertEquals("1.0", request.getSourceSystemVersion());
		assertEquals("BP-SYS", request.getSourceCode());
		assertEquals("Systolic BP", request.getSourceDisplay());
		assertEquals("http://loinc.org", request.getTargetSystem());
		assertEquals("2.57", request.getTargetSystemVersion());
		assertEquals("11378-7", request.getTargetCode());
		assertEquals("Systolic blood pressure at First encounter", request.getTargetDisplay());
		assertEquals("equivalent", request.getEquivalence());
	}

	@Test
	public void testRemoveMapping() {
		// Setup
		IBaseOperationOutcome operationOutcome = OperationOutcomeUtil.createOperationOutcome(
			OperationOutcomeUtil.OO_SEVERITY_WARN,
			"Removed 1 ConceptMap mappings",
			OperationOutcomeUtil.OO_ISSUE_CODE_PROCESSING,
			myContext,
			null
		);
		when(myConceptMapDao.addMapping(any(),any())).thenReturn(operationOutcome);

		// Test
		IGenericClient client = myServer.getFhirClient();
		client
			.operation()
			.onType(ConceptMap.class)
			.named(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING)
			.withParameter(Parameters.class, JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_CONCEPTMAP_URL, new UriType("http://my-concept-map-url"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_SYSTEM, new UriType("http://my-code-system"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_VERSION, new CodeType("1.0"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_CODE, new StringType("BP-SYS"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_SOURCE_DISPLAY, new StringType("Systolic BP"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_SYSTEM, new UriType("http://loinc.org"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_VERSION, new CodeType("2.57"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_CODE, new CodeType("11378-7"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_TARGET_DISPLAY, new StringType("Systolic blood pressure at First encounter"))
			.andParameter(JpaConstants.OPERATION_CONCEPTMAP_ADD_MAPPING_EQUIVALENCE, new CodeType("equivalent"))
			.returnResourceType(OperationOutcome.class)
			.execute();

		// Verify
		verify(myConceptMapDao, times(1)).addMapping(myAddMappingRequestCaptor.capture(), any());
		IFhirResourceDaoConceptMap.AddMappingRequest request = myAddMappingRequestCaptor.getValue();
		assertEquals("http://my-concept-map-url", request.getConceptMapUri());
		assertEquals("http://my-code-system", request.getSourceSystem());
		assertEquals("1.0", request.getSourceSystemVersion());
		assertEquals("BP-SYS", request.getSourceCode());
		assertEquals("Systolic BP", request.getSourceDisplay());
		assertEquals("http://loinc.org", request.getTargetSystem());
		assertEquals("2.57", request.getTargetSystemVersion());
		assertEquals("11378-7", request.getTargetCode());
		assertEquals("Systolic blood pressure at First encounter", request.getTargetDisplay());
		assertEquals("equivalent", request.getEquivalence());
	}

}
