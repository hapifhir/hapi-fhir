package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.hapi.fhir.cdshooks.svc.cr.BaseCrTest;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opencds.cqf.fhir.api.Repository;

import java.util.Arrays;

import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CRMI_EFFECTIVE_DATA_REQUIREMENTS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PrefetchTemplateBuilderR4Test extends BaseCrTest {
	@Mock
	Repository myRepository;

	@InjectMocks
	@Spy
	PrefetchTemplateBuilderR4 myFixture;

	@Test
	public void testR4DiscoveryServiceWithEffectiveDataRequirements() {
		PlanDefinition planDefinition = new PlanDefinition();
		planDefinition.addExtension(CRMI_EFFECTIVE_DATA_REQUIREMENTS,
			new CanonicalType("http://hl7.org/fhir/uv/crmi/Library/moduledefinition-example"));
		planDefinition.setId("ModuleDefinitionTest");
		Library library = ClasspathUtil.loadResource(myFhirContext, Library.class, "ModuleDefinitionExample.json");
		doReturn(library).when(myFixture).resolvePrimaryLibrary(planDefinition);
		PrefetchUrlList actual = myFixture.getPrefetchUrlList(planDefinition);
		assertNotNull(actual);
		PrefetchUrlList expected = new PrefetchUrlList();
		expected.addAll(Arrays.asList("Patient?_id={{context.patientId}}",
			"Encounter?status=finished&subject=Patient/{{context.patientId}}&type:in=http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.113883.3.117.1.7.1.292",
			"Coverage?policy-holder=Patient/{{context.patientId}}&type:in=http://cts.nlm.nih.gov/fhir/ValueSet/2.16.840.1.114222.4.11.3591"));
		assertEquals(expected, actual);
	}
}
