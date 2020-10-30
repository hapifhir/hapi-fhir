package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class JsonRoundTripTest extends BaseTest {

	private static final FhirContext ourCtx = FhirContext.forR4();

	/**
	 * This test method has a method source for each JSON file in the resources/rdf-test-input directory (see #getInputFiles).
	 * Each input file is expected to be a JSON representation of an R4 FHIR resource.
	 * Each input file is put through the following steps to ensure JSON round-trip-ability in HAPI-FHIR:
	 * 1. Parse the JSON into the HAPI object model -- ensure resource instance is not null
	 * 2. Serialize the resource to JSON -- ensure JSON string is not null
	 * 3. Parse the JSON string into the HAPI object model -- ensure resource instance is not null
	 * 4. Perform deep equals comparison of JSON-originated instance and round-tripped instance -- ensure equality
	 * @param referenceFileName -- path to resource file to be tested
	 */
	@ParameterizedTest
	@MethodSource("getInputFiles")
	public void testJsonRoundTrip(String referenceFileName) throws IOException {
		IBaseResource referenceResource = parseJson(new FileInputStream(referenceFileName));
		String sourceJson = serializeJson(ourCtx, referenceResource);
		IBaseResource viaJsonResource = parseJson(new ByteArrayInputStream(sourceJson.getBytes()));
		assertTrue(((Base)viaJsonResource).equalsDeep((Base)referenceResource),
			   "Failed to round-trip JSON " + referenceFileName
			   + "\ngot: " + serializeJson(ourCtx, viaJsonResource)
			   + "\nexp: " + sourceJson);
	}

	private static Stream<String> getInputFiles() throws IOException {
		ClassLoader cl = JsonRoundTripTest.class.getClassLoader();
		List<String> resourceList = new ArrayList<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources("classpath:rdf-test-input/*.json") ;
		for (Resource resource: resources){
			resourceList.add(resource.getFile().getPath());
		}

		return resourceList.stream();
	}

	// JSON functions
	public IBaseResource parseJson(InputStream inputStream) {
		IParser refParser = ourCtx.newJsonParser();
		refParser.setStripVersionsFromReferences(false);
		// parser.setDontStripVersionsFromReferencesAtPaths();
		IBaseResource ret = refParser.parseResource(inputStream);
		assertNotNull(ret);
		return ret;
	}

	public String serializeJson(FhirContext ctx, IBaseResource resource) {
		IParser jsonParser = ctx.newJsonParser();
		jsonParser.setStripVersionsFromReferences(false);
		String ret = jsonParser.encodeResourceToString(resource);
		assertNotNull(ret);
		return ret;
	}
}
