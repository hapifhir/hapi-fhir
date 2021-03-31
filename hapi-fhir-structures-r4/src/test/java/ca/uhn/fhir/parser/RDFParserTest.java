/** test ca.uhn.fhir.parser
 * This parses the FHIR JSON examples and round-trips them through RDF.
 * See testRDFRoundTrip() for details.
 *
 * editors:
 * - Eric Prud'hommeaux <eric@w3.org>
 *
 * TODO:
 * - Consider sharing the FHIR JSON examples in
 *   ../../../../../resources/rdf-test-input/ with other HAPI tests and move to
 *   resources/examples/JSON.
 * - Add FHIR RDF examples and validate graph isomorphism with examples/RDF (or
 *   examples/Turtle).
 *
 * see also:
 *   ../../../../../../../../hapi-fhir-base/src/main/java/ca/uhn/fhir/parser/RDFParser.java
 * run test:
 *   hapi-fhir/hapi-fhir-structures-r4$ mvn -Dtest=ca.uhn.fhir.parser.RDFParserTest test
 */

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
import org.apache.commons.rdf.api.RDFTerm;
import org.apache.commons.rdf.rdf4j.RDF4J;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Base;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
	 * @param referenceFilePath -- path to resource file to be tested
	 * @throws IOException -- thrown when parsing RDF string into graph model
	 */
	@ParameterizedTest
	@MethodSource("getInputFiles")
	public void testRDFRoundTrip(String referenceFilePath) throws IOException {
		String referenceFileName = referenceFilePath.substring(referenceFilePath.lastIndexOf("/")+1);
		IBaseResource referenceResource = parseJson(new FileInputStream(referenceFilePath));
		String referenceJson = serializeJson(ourCtx, referenceResource);

		// Perform ShEx validation on RDF
		String turtleString = serializeRdf(ourCtx, referenceResource);
		validateRdf(turtleString, referenceFileName, referenceResource);

		// Parse RDF content as resource
		IBaseResource viaTurtleResource = parseRdf(ourCtx, new StringReader(turtleString));
		assertNotNull(viaTurtleResource);

		// Compare original JSON-based resource against RDF-based resource
		String viaTurtleJson = serializeJson(ourCtx, viaTurtleResource);
		if (!((Base)viaTurtleResource).equalsDeep((Base)referenceResource)) {
			String failMessage = referenceFileName + ": failed to round-trip Turtle ";
			if (referenceJson.equals(viaTurtleJson))
				throw new Error(failMessage
					+ "\nttl: " + turtleString
					+ "\nexp: " + referenceJson);
			else
				assertEquals(referenceJson, viaTurtleJson, failMessage + "\nttl: " + turtleString);
		}
	}

	private static Stream<String> getInputFiles() throws IOException {
		ClassLoader cl = RDFParserTest.class.getClassLoader();
		List<String> resourceList = new ArrayList<>();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = resolver.getResources("classpath:rdf-test-input/*.json") ;
		for (Resource resource: resources)
			resourceList.add(resource.getFile().getPath());

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

	// Rdf (Turtle) functions
	public IBaseResource parseRdf(FhirContext ctx, StringReader inputStream) {
		IParser refParser = ctx.newRDFParser();
		IBaseResource ret = refParser.parseResource(inputStream);
		assertNotNull(ret);
		return ret;
	}

	public String serializeRdf(FhirContext ctx, IBaseResource resource) {
		IParser rdfParser = ourCtx.newRDFParser();
		rdfParser.setStripVersionsFromReferences(false);
		rdfParser.setServerBaseUrl("http://a.example/fhir/");
		String ret = rdfParser.encodeResourceToString(resource);
		assertNotNull(ret);
		return ret;
	}

	public void validateRdf(String rdfContent, String referenceFileName, IBaseResource referenceResource) throws IOException {
		String baseIRI = "http://a.example/shex/";
		RDF4J factory = new RDF4J();
		GlobalFactory.RDFFactory = factory; //set the global factory used in shexjava
		Model data = Rio.parse(new StringReader(rdfContent), baseIRI, RDFFormat.TURTLE);
		FixedShapeMapEntry fixedMapEntry = new FixedShapeMapEntry(factory, data, referenceResource.fhirType(), baseIRI);
		Graph dataGraph = factory.asGraph(data); // create the graph
		ValidationAlgorithm validation = new RecursiveValidation(fhirSchema, dataGraph);
		validation.validate(fixedMapEntry.node, fixedMapEntry.shape);
		boolean result = validation.getTyping().isConformant(fixedMapEntry.node, fixedMapEntry.shape);
		assertTrue(result,
			   referenceFileName + ": failed to validate " + fixedMapEntry
			   + "\n" + referenceFileName
			   + "\n" + rdfContent
			   );
	}

	// Shape Expressions functions
	class FixedShapeMapEntry {
		RDFTerm node;
		Label shape;

		FixedShapeMapEntry(RDF4J factory, Model data, String resourceType, String baseIRI) {
			String rootSubjectIri = null;
			// StmtIterator i = data.listStatements();
			for (org.eclipse.rdf4j.model.Resource resourceStream : data.subjects()) {
//				if (resourceStream instanceof SimpleIRI) {
					Model filteredModel = data.filter(resourceStream, factory.getValueFactory().createIRI(NODE_ROLE_IRI), factory.getValueFactory().createIRI(TREE_ROOT_IRI), (org.eclipse.rdf4j.model.Resource)null);
					if (filteredModel != null && filteredModel.subjects().size() == 1) {
						Optional<org.eclipse.rdf4j.model.Resource> rootResource = filteredModel.subjects().stream().findFirst();
						if (rootResource.isPresent()) {
							rootSubjectIri = rootResource.get().stringValue();
							break;
						}

					}
//				}
			}

			// choose focus node and shapelabel
			this.node = rootSubjectIri.indexOf(":") == -1
				? factory.createBlankNode(rootSubjectIri)
			 	: factory.createIRI(rootSubjectIri);
			Label shapeLabel = new Label(factory.createIRI(FHIR_SHAPE_PREFIX + resourceType));
//			this.node = focusNode;
			this.shape = shapeLabel;
		}

		public String toString() {
			return "<" + node.toString() + ">@" + shape.toPrettyString();
		}
	}

}
