package org.hl7.fhir.r4.utils.transform;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.*;

import static org.junit.Assert.*;

public class FhirTransformationEngineTest {

	/**
	 * The basic fhir context used to parse structure definitions.
	 */
	private FhirContext context;

	/**
	 * HapiFhirContext used when building strucutre map utilities.
	 */
	private HapiWorkerContext hapiContext;

	/**
	 * Used to validate definitions as well as add new structure definitions to a registry.
	 */
	private PrePopulatedValidationSupport validationSupport;

	/**
	 * The path to the logical profile used to test this class.
	 */
	private final String relativePathToLogicalProfile = "transform/logical/CODED_TEXT";

	/**
	 * Convenience method returning a File pointer to a resource located at the specified path
	 * in the classpath.
	 *
	 * @return
	 */
	private File getFileFromURL(String localFilePath) {
		URL url = this.getClass().getClassLoader().getResource(localFilePath);
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (Exception e) {
			fail("Error identifying local path for file " + localFilePath + ": " + e.getMessage());
		}
		return file;
	}

	/**
	 * Method attempts to load structure definition file from classpath and deserialize it into a StructureDefinition object.
	 *
	 * @return
	 * @throws FileNotFoundException
	 */
	private StructureDefinition loadFhirStructureDefinitionFromResources(String structureDefinitionRelativePath) throws FileNotFoundException {
		return this.context.newXmlParser().parseResource(StructureDefinition.class, new FileReader((getFileFromURL(structureDefinitionRelativePath))));
	}

	/**
	 * Method loads all core FHIR Structure Definitions
	 */
	private void loadFhirStructureDefinitions() {
		this.validationSupport = new PrePopulatedValidationSupport();
		DefaultProfileValidationSupport validation = new DefaultProfileValidationSupport();
		for (StructureDefinition sd : new DefaultProfileValidationSupport().fetchAllStructureDefinitions(this.context)) {
			this.validationSupport.addStructureDefinition(sd);
		}
	}


	/**
	 * Sets up the contexts using a default validator to start with.
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.context = FhirContext.forR4();
		loadFhirStructureDefinitions();
	}

	/**
	 * Make the context objects null, safely removing them from memory.
	 *
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.context = null;
		this.hapiContext = null;
	}

	/**
	 * Unit end-to-end test for the transformation of a simple logical profile
	 * to a FHIR resource profile.
	 *
	 * @throws Exception
	 */
	@Test
	public void testCodingFromCodedTextTransform() {
		try {
			Map<String, StructureMap> maps = new HashMap<>();
			StructureDefinition codedTextLogicalProfile = loadFhirStructureDefinitionFromResources("transform/logical/CODED_TEXT.xml");
			this.validationSupport.addStructureDefinition(codedTextLogicalProfile);
			this.hapiContext = new HapiWorkerContext(this.context, this.validationSupport);
			StructureMap codedTextToCodingMap = null;
			FhirTransformationEngine transformEngine = new FhirTransformationEngine(hapiContext, maps, null, null);
			codedTextToCodingMap = createCodedTextToCodingMap();
			maps.put(codedTextToCodingMap.getUrl(), codedTextToCodingMap);
			BatchContext batchContext = new BatchContext();
			List<StructureDefinition> result = transformEngine.analyse(batchContext,null, codedTextToCodingMap).getProfiles();
			File currentDir = new File(".");
			String filePath = currentDir.getAbsolutePath();

			assertTrue(result.size() > 0);

			StructureDefinition generatedFhirProfile = result.get(0);
			List<ElementDefinition> definitions = generatedFhirProfile.getDifferential().getElement();
			assertEquals(7, definitions.size());//TODO Really should be 8 if we include the slicing definition for extensions.
			final Map<String, ElementDefinition> definitionMap = new HashMap<>();
			for(ElementDefinition eltDefinition : definitions) {
				definitionMap.put(eltDefinition.getPath(), eltDefinition);
			}
			assertNotNull(definitionMap.get("Coding"));
			assertNotNull(definitionMap.get("Coding.system"));
			assertNotNull(definitionMap.get("Coding.version"));
			assertNotNull(definitionMap.get("Coding.code"));
			assertNotNull(definitionMap.get("Coding.display"));
			assertNotNull(definitionMap.get("Coding.userSelected"));
			assertNotNull(definitionMap.get("Coding.extension"));
			ElementDefinition userSelected = definitionMap.get("Coding.userSelected");
			assertTrue(userSelected.getFixed() instanceof BooleanType);
			assertFalse(((BooleanType)userSelected.getFixed()).booleanValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Test failed. Exception thrown: " + e.getMessage());
		}
	}

	/**
	 * Creates a hard coded Structure Map for testing
	 * @return
	 * @throws Exception
	 */
	public StructureMap createCodedTextToCodingMap() throws Exception {
		StructureMap retMap = new StructureMap();
		retMap.setUrl("http://opencimi.org/structuremap/testtransform");
		retMap.setName("TestTransform");
		retMap.setStatus(Enumerations.PublicationStatus.DRAFT);
		retMap.setStructure(this.createMapStructureList());
		retMap.setGroup(this.buildTestGroup());
		return retMap;
	}

	/**
	 *
	 * @return
	 */
	public List<StructureMap.StructureMapStructureComponent> createMapStructureList(){
		List<StructureMap.StructureMapStructureComponent> retVal = new ArrayList<>();
		StructureMap.StructureMapStructureComponent source = new StructureMap.StructureMapStructureComponent();
		StructureMap.StructureMapStructureComponent target = new StructureMap.StructureMapStructureComponent();
		source.setUrl("http://opencimi.org/logical-model/fhir/CODED_TEXT");
		source.setMode(StructureMap.StructureMapModelMode.SOURCE);
		target.setUrl("http://hl7.org/fhir/StructureDefinition/Coding");
		target.setMode(StructureMap.StructureMapModelMode.TARGET);
		retVal.add(source);
		retVal.add(target);
		return retVal;
	}

	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public List<StructureMap.StructureMapGroupComponent> buildTestGroup() throws Exception{
		List<StructureMap.StructureMapGroupComponent> retVal = new ArrayList<>();
		StructureMap.StructureMapGroupComponent group = new StructureMap.StructureMapGroupComponent();
		group.setName("TestStructureToCoding");
		group.setTypeMode(StructureMap.StructureMapGroupTypeMode.TYPEANDTYPES);
		group.setInput(this.buildTestInput());
		group.setRule(this.buildTestRules());
		retVal.add(group);
		return retVal;
	}

	/**
	 * Builds teh structure map inputs for testing
	 * @return
	 */
	public List<StructureMap.StructureMapGroupInputComponent> buildTestInput(){
		List<StructureMap.StructureMapGroupInputComponent> retVal = new ArrayList<>();
		StructureMap.StructureMapGroupInputComponent sourceIn = new StructureMap.StructureMapGroupInputComponent();
		StructureMap.StructureMapGroupInputComponent targetIn = new StructureMap.StructureMapGroupInputComponent();
		sourceIn.setName("source");
		sourceIn.setType("CODED_TEXT");
		sourceIn.setMode(StructureMap.StructureMapInputMode.SOURCE);
		targetIn.setName("target");
		targetIn.setType("Coding");
		targetIn.setMode(StructureMap.StructureMapInputMode.TARGET);
		retVal.add(sourceIn);
		retVal.add(targetIn);
		return retVal;
	}

	/**
	 *
	 * @return Creates Rules for testing
	 * @throws Exception
	 */
	public List<StructureMap.StructureMapGroupRuleComponent> buildTestRules() throws Exception{
		List<StructureMap.StructureMapGroupRuleComponent> retVal = new ArrayList<>();
		StructureMap.StructureMapGroupRuleComponent userSelected = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent system = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent extension = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent code = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent version = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent display = new StructureMap.StructureMapGroupRuleComponent();

        /*
         * Coding.System
         */
		system.setName("Coding.system");
		StructureMap.StructureMapGroupRuleSourceComponent source = new StructureMap.StructureMapGroupRuleSourceComponent();
		StructureMap.StructureMapGroupRuleTargetComponent target = new StructureMap.StructureMapGroupRuleTargetComponent();
		source.setContext("source");
		source.setElement("terminology_id");
		source.setVariable("term");
		source.setMin(1);
		source.setMax("1");
		source.setType("uri");
		system.addSource(source);
		target.setContext("target");
		target.setElement("system");
		target.setVariable("sys");
		system.addTarget(target);

		retVal.add(system);

        /*
         * Coding.version
         */
		version.setName("Coding.version");
		source = new StructureMap.StructureMapGroupRuleSourceComponent();
		target = new StructureMap.StructureMapGroupRuleTargetComponent();
		source.setContext("source");
		source.setElement("terminology_version");
		source.setVariable("tv");
		version.addSource(source);
		target.setContext("target");
		target.setElement("version");
		target.setTransform(StructureMap.StructureMapTransform.COPY);
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType("tv")));
		version.addTarget(target);
		retVal.add(version);

        /*
         * Coding.code
         */
		code.setName("Coding.code");
		source = new StructureMap.StructureMapGroupRuleSourceComponent();
		target = new StructureMap.StructureMapGroupRuleTargetComponent();
		source.setContext("source");
		source.setElement("code");
		source.setVariable("c");
		code.addSource(source);
		target.setContext("target");
		target.setElement("code");
		target.setTransform(StructureMap.StructureMapTransform.COPY);
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType("c")));
		code.addTarget(target);
		retVal.add(code);

        /*
         * Coding.display
         */
		display.setName("Coding.display");
		source = new StructureMap.StructureMapGroupRuleSourceComponent();
		target = new StructureMap.StructureMapGroupRuleTargetComponent();
		source.setContext("source");
		source.setElement("term");
		source.setVariable("term");
		display.addSource(source);
		target.setContext("target");
		target.setElement("display");
		target.setTransform(StructureMap.StructureMapTransform.COPY);
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType("term")));
		display.addTarget(target);
		retVal.add(display);

        /*
         * Coding.UserSelected
         */
		userSelected.setName("Coding.userSelected");
		source = new StructureMap.StructureMapGroupRuleSourceComponent();
		target = new StructureMap.StructureMapGroupRuleTargetComponent();
		source.setContext("source");
		userSelected.addSource(source);
		target.setContext("target");
		target.setElement("userSelected");
		target.setTransform(StructureMap.StructureMapTransform.COPY);
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue(new BooleanType(false)));
		userSelected.addTarget(target);

		retVal.add(userSelected);

		/**
		 * Add URI Extension
		 */
//		extension.setName("Coding.extension");
//		source = new StructureMap.StructureMapGroupRuleSourceComponent();
//		source.setContext("source");
//		source.setElement("uri");
//		source.setVariable("uri");
//		extension.addSource(source);
//		target = new StructureMap.StructureMapGroupRuleTargetComponent();
//		target.setContext("target");
//		target.setElement("extension");
//		target.setVariable("ex");
//		extension.addTarget(target);
//		target = new StructureMap.StructureMapGroupRuleTargetComponent();
//		target.setContext("ex");
//		target.setElement("url");
//		target.setTransform(StructureMap.StructureMapTransform.COPY);
//		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new UriType("http://opencimi.org/fhir/extension/coding/coded_text_uri.html")));
//		extension.addTarget(target);
//		target = new StructureMap.StructureMapGroupRuleTargetComponent();
//		target.setContext("ex");
//		target.setElement("value");
//		target.setTransform(StructureMap.StructureMapTransform.COPY);
//		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IdType("uri")));
//		extension.addTarget(target);

		extension.setName("Coding.uri");
		source = new StructureMap.StructureMapGroupRuleSourceComponent();
		source.setContext("source");
		source.setElement("uri");
		source.setVariable("uri");
		extension.addSource(source);
		target = new StructureMap.StructureMapGroupRuleTargetComponent();
		target.setContext("target");
		target.setElement("url");
		target.setVariable("ex");
		target.setTransform(StructureMap.StructureMapTransform.EXTENSION);
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("http://opencimi.org/fhir/extension/coding/coded_text_uri.html")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("coded_text_uri")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("Resource")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("Coding")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("URI representing the specific concept")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("URI representing the specific concept")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new IntegerType(0)));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("1")));
		target.addParameter(new StructureMap.StructureMapGroupRuleTargetParameterComponent(new StringType("uri")));
		extension.addTarget(target);


		retVal.add(extension);

		return retVal;
	}


}
