package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import fr.inria.lille.shexjava.GlobalFactory;
import fr.inria.lille.shexjava.schema.Label;
import fr.inria.lille.shexjava.schema.ShexSchema;
import fr.inria.lille.shexjava.schema.parsing.GenParser;
import fr.inria.lille.shexjava.validation.RecursiveValidation;
import fr.inria.lille.shexjava.validation.ValidationAlgorithm;
import org.apache.commons.rdf.api.Graph;
import org.apache.commons.rdf.api.IRI;
import org.apache.commons.rdf.rdf4j.RDF4J;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.SimpleIRI;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.DomainResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class RDFParserTest extends BaseTest {

	public static final String NODE_ROLE_IRI = "http://hl7.org/fhir/nodeRole";
	public static final String TREE_ROOT_IRI = "http://hl7.org/fhir/treeRoot";
	public static final String FHIR_SHAPE_PREFIX = "http://hl7.org/fhir/shape/";
	private static final FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RDFParserTest.class);
	private static ShexSchema fhirSchema = null;

	@BeforeAll
	static void parseShExSchema() throws Exception {
		Path schemaFile = Paths.get("target", "test-classes", "rdf-validation", "fhir-r4.shex");
		fhirSchema = GenParser.parseSchema(schemaFile, Collections.emptyList());
	}

	/**
	 * This test method has a method source for each JSON file in the resources/rdf-test-input directory (see #getInputFiles).
	 * Each input file is expected to be a JSON representation of an R4 FHIR resource.
	 * Each input file is put through the following steps to ensure valid RDF and round-trip-ability in HAPI-FHIR:
	 * 1. Parse the JSON into the HAPI object model -- ensure resource instance is not null
	 * 2. Encode the JSON-originated instance as an RDF string using the RDF Parser -- ensure RDF string is not null
	 * 3. Perform a graph validation on the resulting RDF using ShEx and ShEx-java -- ensure validation passed
	 * 4. Parse the RDF string into the HAPI object model -- ensure resource instance is not null
	 * 5. Perform deep equals comparison of JSON-originated instance and RDF-originated instance -- ensure equality
	 * @param inputFile -- path to resource file to be tested
	 * @throws IOException -- thrown when parsing RDF string into graph model
	 */
	@ParameterizedTest
	@MethodSource("getInputFiles")
	public void testRDFRoundTrip(String inputFile) throws IOException {
		FileInputStream inputStream = new FileInputStream(inputFile);
		IBaseResource resource;
		String resourceType;
		// Parse JSON input as Resource
		resource = ourCtx.newJsonParser().parseResource(inputStream);
		assertNotNull(resource);
		resourceType = resource.fhirType();

		// Write the resource out to an RDF String
		String rdfContent = ourCtx.newRDFParser().encodeResourceToString(resource);
		assertNotNull(rdfContent);

		// Perform ShEx validation on RDF
		RDF4J factory = new RDF4J();
		GlobalFactory.RDFFactory = factory; //set the global factory used in shexjava

		// load the model
		String baseIRI = "http://a.example.shex/";
		Model data = Rio.parse(new StringReader(rdfContent), baseIRI, RDFFormat.TURTLE);

		String rootSubjectIri = null;
		for (org.eclipse.rdf4j.model.Resource resourceStream : data.subjects()) {
			if (resourceStream instanceof SimpleIRI) {
				Model filteredModel = data.filter(resourceStream, factory.getValueFactory().createIRI(NODE_ROLE_IRI), factory.getValueFactory().createIRI(TREE_ROOT_IRI), (org.eclipse.rdf4j.model.Resource)null);
				if (filteredModel != null && filteredModel.subjects().size() == 1) {
					Optional<org.eclipse.rdf4j.model.Resource> rootResource = filteredModel.subjects().stream().findFirst();
					if (rootResource.isPresent()) {
						rootSubjectIri = rootResource.get().stringValue();
						break;
					}

				}
			}
		}

		// create the graph
		Graph dataGraph = factory.asGraph(data);

		// choose focus node and shapelabel
		IRI focusNode = factory.createIRI(rootSubjectIri);
		Label shapeLabel = new Label(factory.createIRI(FHIR_SHAPE_PREFIX + resourceType));

		ValidationAlgorithm validation = new RecursiveValidation(fhirSchema, dataGraph);
		validation.validate(focusNode, shapeLabel);
		boolean result = validation.getTyping().isConformant(focusNode, shapeLabel);
		assertTrue(result);

		// Parse RDF content as resource
		IBaseResource parsedResource = ourCtx.newRDFParser().parseResource(new StringReader(rdfContent));
		assertNotNull(parsedResource);

		// Compare original JSON-based resource against RDF-based resource
		if (parsedResource instanceof DomainResource) {
			// This is a hack because this initializes the collection if it is empty
			((DomainResource) parsedResource).getContained();
			boolean deepEquals = ((Base)parsedResource).equalsDeep((Base)resource);
			assertTrue(deepEquals);
		} else {
			ourLog.warn("Input JSON did not yield a DomainResource");
		}
	}

	private static Stream<String> getInputFiles() throws IOException {
		ClassLoader cl = RDFParserTest.class.getClassLoader();
		List<String> resourceList = new ArrayList<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources("classpath:rdf-test-input/*.json") ;
		for (Resource resource: resources){
			resourceList.add(resource.getFile().getPath());
		}

		return resourceList.stream();
	}

}
