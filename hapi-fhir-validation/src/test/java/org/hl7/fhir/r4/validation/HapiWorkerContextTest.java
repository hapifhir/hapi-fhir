package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.test.BaseTest;
import com.google.common.base.Charsets;
import org.apache.commons.lang.Validate;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.validation.ValidationOptions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HapiWorkerContextTest extends BaseTest {
	FhirContext myCtx = FhirContext.forR4();

	@Test
	public void testCodeInPrePopulatedValidationSupport() throws IOException {

		PrePopulatedValidationSupport prePopulatedValidationSupport = new PrePopulatedValidationSupport(myCtx);

		getResources("/r4/carin/carin/codesystem/").forEach(t -> prePopulatedValidationSupport.addCodeSystem(t));
		getResources("/r4/carin/uscore/codesystem/").forEach(t -> prePopulatedValidationSupport.addCodeSystem(t));
		getResources("/r4/carin/carin/valueset/").forEach(t -> prePopulatedValidationSupport.addValueSet((ValueSet) t));
		getResources("/r4/carin/uscore/valueset/").forEach(t -> prePopulatedValidationSupport.addValueSet((ValueSet) t));
		getResources("/r4/carin/carin/structuredefinition/").forEach(t -> prePopulatedValidationSupport.addStructureDefinition(t));
		getResources("/r4/carin/uscore/structuredefinition/").forEach(t -> prePopulatedValidationSupport.addStructureDefinition(t));

		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
			new DefaultProfileValidationSupport(myCtx),
			prePopulatedValidationSupport,
			new InMemoryTerminologyServerValidationSupport(myCtx)
		);
		HapiWorkerContext workerCtx = new HapiWorkerContext(myCtx, validationSupportChain);

		ValueSet vs = new ValueSet();
		IWorkerContext.ValidationResult outcome;

		// Built-in Codes

		vs.setUrl("http://hl7.org/fhir/ValueSet/fm-status");
		ValidationOptions options = new ValidationOptions().guessSystem();
		outcome = workerCtx.validateCode(options, "active", vs);
		assertEquals(true, outcome.isOk(), outcome.getMessage());

		outcome = workerCtx.validateCode(options, "active2", vs);
		assertEquals(false, outcome.isOk(), outcome.getMessage());
		assertEquals("Unknown code[active2] in system[(none)]", outcome.getMessage());

		// PrePopulated codes

		vs.setUrl("http://hl7.org/fhir/us/core/ValueSet/birthsex");
		outcome = workerCtx.validateCode(options, "F", vs);
		assertEquals(true, outcome.isOk(), outcome.getMessage());

		outcome = workerCtx.validateCode(options, "F2", vs);
		assertEquals(false, outcome.isOk(), outcome.getMessage());
		assertEquals("Unknown code[F2] in system[(none)]", outcome.getMessage());

	}


	private List<IBaseResource> getResources(String theDirectory) throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(HapiWorkerContext.class.getClassLoader());
		List<Resource> resources;
		String path = "classpath*:" + theDirectory + "*.json";
		try {
			resources = Arrays.asList(resolver.getResources(path));
		} catch (IOException theIoe) {
			throw new InternalErrorException("Unable to get resources from path: " + path, theIoe);
		}
		List<IBaseResource> retVal = new ArrayList<>();

		for (Resource nextFileResource : resources) {
			try (InputStream is = nextFileResource.getInputStream()) {
				Reader reader = new InputStreamReader(is, Charsets.UTF_8);
				retVal.add(myCtx.newJsonParser().parseResource(reader));
			}
		}

		Validate.isTrue(retVal.size() > 0, "No files found in " + path);

		return retVal;
	}

}
