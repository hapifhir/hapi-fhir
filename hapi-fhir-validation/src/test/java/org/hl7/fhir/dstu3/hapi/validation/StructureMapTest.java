package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.utils.StructureMapUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StructureMapTest {

	/**
	 * The logger object.
	 */
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(StructureMapTest.class);

	/**
	 * The basic fhir context used to parse structure definitions.
	 */
	FhirContext myCtx;

	/**
	 * HapiFhirContext used when building strucutre map utilities.
	 */
	IWorkerContext hapiContext;

	/**
	 * path to the files used to test the profile generator.
	 */
	String resourcePath = null;

	/**
	 * Used to validate definitions as well as add new structure definitions to a registry.
	 */
	ValidationSupportChain validationSupport;

	public StructureMap.StructureMapGroupRuleSourceComponent buildSource(String context, @Nullable String element, @Nullable String variable, @Nullable String type, @Nullable Integer min,
																								@Nullable String max) {
		StructureMap.StructureMapGroupRuleSourceComponent retVal = new StructureMap.StructureMapGroupRuleSourceComponent();
		retVal.setContext(context);
		if (element != null)
			retVal.setElement(element);
		if (variable != null)
			retVal.setVariable(variable);
		if (type != null)
			retVal.setType(type);
		if (min != null)
			retVal.setMin(min);
		if (max != null)
			retVal.setMax(max);
		return retVal;
	}

	public List<StructureMap.StructureMapGroupRuleSourceComponent> buildSourceList(StructureMap.StructureMapGroupRuleSourceComponent[] sources) {
		List<StructureMap.StructureMapGroupRuleSourceComponent> retVal = new ArrayList<StructureMap.StructureMapGroupRuleSourceComponent>();
		retVal.addAll(Arrays.asList(sources));
		return retVal;
	}

	public StructureMap.StructureMapGroupRuleTargetComponent buildTarget(@Nullable String context, @Nullable String element, @Nullable String variable,
																								@Nullable StructureMap.StructureMapTransform transform, @Nullable TargetParam[] params) throws Exception {
		StructureMap.StructureMapGroupRuleTargetComponent retVal = new StructureMap.StructureMapGroupRuleTargetComponent();
		if (context != null)
			retVal.setContext(context);
		if (element != null)
			retVal.setElement(element);
		if (variable != null)
			retVal.setVariable(variable);
		if (transform != null)
			retVal.setTransform(transform);
		if (params != null) {
			if (params.length > 0)
				retVal.setParameter(this.constructParameters(params));
		}
		return retVal;
	}

	public List<StructureMap.StructureMapGroupRuleTargetComponent> buildTargetList(StructureMap.StructureMapGroupRuleTargetComponent[] sources) {
		List<StructureMap.StructureMapGroupRuleTargetComponent> retVal = new ArrayList<StructureMap.StructureMapGroupRuleTargetComponent>();
		retVal.addAll(Arrays.asList(sources));
		return retVal;
	}

	public List<StructureMap.StructureMapGroupComponent> buildTestGroup() throws Exception {
		List<StructureMap.StructureMapGroupComponent> retVal = new ArrayList<StructureMap.StructureMapGroupComponent>();
		StructureMap.StructureMapGroupComponent group = new StructureMap.StructureMapGroupComponent();
		group.setName("TestStructureToCoding");
		group.setTypeMode(StructureMap.StructureMapGroupTypeMode.TYPEANDTYPES);
		group.setInput(this.buildTestInput());
		group.setRule(this.buildTestRules());
		retVal.add(group);
		return retVal;
	}

	public List<StructureMap.StructureMapGroupInputComponent> buildTestInput() {
		List<StructureMap.StructureMapGroupInputComponent> retVal = new ArrayList<StructureMap.StructureMapGroupInputComponent>();
		StructureMap.StructureMapGroupInputComponent sourceIn = new StructureMap.StructureMapGroupInputComponent();
		StructureMap.StructureMapGroupInputComponent targetIn = new StructureMap.StructureMapGroupInputComponent();
		sourceIn.setName("source");
		sourceIn.setType("TestStructure");
		sourceIn.setMode(StructureMap.StructureMapInputMode.SOURCE);
		targetIn.setName("target");
		targetIn.setType("http://hl7.org/fhir/StructureDefinition/Coding");
		targetIn.setMode(StructureMap.StructureMapInputMode.TARGET);
		retVal.add(sourceIn);
		retVal.add(targetIn);
		return retVal;
	}

	public List<StructureMap.StructureMapGroupRuleComponent> buildTestRules() throws Exception {
		List<StructureMap.StructureMapGroupRuleComponent> retVal = new ArrayList<StructureMap.StructureMapGroupRuleComponent>();
		StructureMap.StructureMapGroupRuleComponent codingSystem = new StructureMap.StructureMapGroupRuleComponent();
		StructureMap.StructureMapGroupRuleComponent codingExtension = new StructureMap.StructureMapGroupRuleComponent();
		codingSystem.setName("Coding.System");
		codingSystem.setSource(this.buildSourceList(new StructureMap.StructureMapGroupRuleSourceComponent[]{
			this.buildSource("source", "system", "v", null, null, null)
		}));
		codingSystem.setTarget(this.buildTargetList(new StructureMap.StructureMapGroupRuleTargetComponent[]{
			this.buildTarget("target", "system", null, StructureMap.StructureMapTransform.COPY, new TargetParam[]{new TargetParam("Id", "v")})
		}));
		codingExtension.setName("Coding.Extension");
		codingExtension.setSource(this.buildSourceList(new StructureMap.StructureMapGroupRuleSourceComponent[]{
			this.buildSource("source", "system", "v", null, null, null)
		}));
		codingExtension.setTarget(this.buildTargetList(new StructureMap.StructureMapGroupRuleTargetComponent[]{
			this.buildTarget("target", "extension", "ex", null, new TargetParam[]{new TargetParam("", "")}),
			this.buildTarget("ex", "url", null, StructureMap.StructureMapTransform.COPY, new TargetParam[]{new TargetParam("Id", "v")}),
			this.buildTarget("ex", "value", null, StructureMap.StructureMapTransform.COPY, new TargetParam[]{new TargetParam("String", "v")})
		}));
		retVal.add(codingSystem);
		retVal.add(codingExtension);
		return retVal;
	}

	public List<StructureMap.StructureMapGroupRuleTargetParameterComponent> constructParameters(TargetParam[] params) throws Exception {
		List<StructureMap.StructureMapGroupRuleTargetParameterComponent> parameterComponents = new ArrayList<StructureMap.StructureMapGroupRuleTargetParameterComponent>();
		for (TargetParam tp : params) {
			if (tp.getType() == "Id") // TODO: Convert TypeParam.Type into an Enum.
				parameterComponents.add(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue(new IdType().setValue(tp.getValue())));
			else if (tp.getType() == "String")
				parameterComponents.add(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue((new StringType().setValue(tp.getValue()))));
			else if (tp.getType() == "Boolean") {
				boolean bValue = Boolean.getBoolean(tp.getValue());
				parameterComponents.add(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue(new BooleanType().setValue(bValue)));
			} else if (tp.getType() == "Integer") {
				int iValue = Integer.getInteger(tp.getValue());
				parameterComponents.add(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue(new IntegerType().setValue(iValue)));
			} else if (tp.getType() == "Decimal") {
				long lValue = Long.getLong(tp.getValue());
				parameterComponents.add(new StructureMap.StructureMapGroupRuleTargetParameterComponent().setValue(new DecimalType(lValue)));
			}
		}
		return parameterComponents;
	}

	public List<StructureMap.StructureMapStructureComponent> createMapStructureList() {
		List<StructureMap.StructureMapStructureComponent> retVal = new ArrayList<>();
		StructureMap.StructureMapStructureComponent source = new StructureMap.StructureMapStructureComponent();
		StructureMap.StructureMapStructureComponent target = new StructureMap.StructureMapStructureComponent();
		source.setUrl("http://opencimi.org/structuredefinition/TestStructure");
		source.setMode(StructureMap.StructureMapModelMode.SOURCE);
		target.setUrl("http://hl7.org/fhir/StructureDefinition/Coding");
		target.setMode(StructureMap.StructureMapModelMode.TARGET);
		retVal.add(source);
		retVal.add(target);
		return retVal;
	}

	public StructureDefinition.StructureDefinitionDifferentialComponent createTestDiff() {
		StructureDefinition.StructureDefinitionDifferentialComponent retVal = new StructureDefinition.StructureDefinitionDifferentialComponent();
		List<ElementDefinition> eList = new ArrayList<>();
		ElementDefinition ed0 = new ElementDefinition();
		// ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		// base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed0.setId("TestStructure");
		ed0.setSliceName("TestStructure");
		ed0.setPath("TestStructure");
		// ed0.setBase(base);
		ed0.setMin(1);
		ed0.setMax("1");
		eList.add(ed0);

		ElementDefinition ed = new ElementDefinition();
		// ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		// base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed.setId("system");
		ed.setSliceName("system");
		ed.setPath("TestStructure.system");
		// ed.setBase(base);
		ed.setFixed(new UriType().setValue("HTTP://opencimi.org/structuredefinition/TestStructure.html#Debugging"));
		// ed.setType(this.createTypeRefList());
		eList.add(ed);
		retVal.setElement(eList);
		return retVal;
	}

	public StructureDefinition.StructureDefinitionSnapshotComponent createTestSnapshot() {
		StructureDefinition.StructureDefinitionSnapshotComponent retVal = new StructureDefinition.StructureDefinitionSnapshotComponent();
		List<ElementDefinition> eList = new ArrayList<>();
		ElementDefinition ed0 = new ElementDefinition();
		// ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		// base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed0.setId("TestStructure");
		ed0.setSliceName("TestStructure");
		ed0.setPath("TestStructure");
		// ed0.setBase(base);
		ed0.setMin(1);
		ed0.setMax("1");
		eList.add(ed0);

		ElementDefinition ed = new ElementDefinition();
		// ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		// base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed.setId("system");
		ed.setSliceName("system");
		ed.setPath("TestStructure.system");
		// ed.setBase(base);
		ed.setFixed(new UriType().setValue("HTTP://opencimi.org/structuredefinition/TestStructure.html#Debugging"));
		// ed.setType(this.createTypeRefList());
		ed.setMin(1);
		ed.setMax("1");
		eList.add(ed);
		retVal.setElement(eList);
		return retVal;

	}

	public StructureDefinition createTestStructure() {
		StructureDefinition sd = new StructureDefinition();
		sd.setId("TestStructure");
		sd.setUrl("http://opencimi.org/structuredefinition/TestStructure");
		sd.setStatus(Enumerations.PublicationStatus.DRAFT);
		sd.setName("TestStructure");
		sd.setType("TestStructure");
		sd.setSnapshot(this.createTestSnapshot());
		sd.setDifferential(this.createTestDiff());
		sd.setKind(StructureDefinition.StructureDefinitionKind.LOGICAL);

		return sd;
	}

	public StructureMap createTestStructuremap() throws Exception {
		StructureMap retMap = new StructureMap();
		retMap.setUrl("http://opencimi.org/structuremap/testtransform");
		retMap.setName("TestTransform");
		retMap.setStatus(Enumerations.PublicationStatus.DRAFT);
		retMap.setStructure(this.createMapStructureList());
		retMap.setGroup(this.buildTestGroup());
		return retMap;
	}

	/**
	 * Sets up the resource paths as well as create the contexts using a defalut validator to start with.
	 */
	@BeforeEach
	public void setUp() {
		if (this.myCtx == null) {
			this.myCtx = FhirContext.forDstu3();
		}
	}

	/**
	 * See #682
	 */
	@Test
	public void testMappingTransform() throws Exception {
		Map<String, StructureMap> maps = new HashMap<>(); // Instantiate a hashmap for StructureMaps
		PrePopulatedValidationSupport prePopulatedValidationSupport = new PrePopulatedValidationSupport(myCtx);
		this.validationSupport = new ValidationSupportChain(prePopulatedValidationSupport, new DefaultProfileValidationSupport(myCtx));

		StructureDefinition sd1 = this.createTestStructure(); // Calls a method that constructs a comp
		prePopulatedValidationSupport.addStructureDefinition(sd1); // Add custom structure to validation support.
		this.hapiContext = new HapiWorkerContext(this.myCtx, this.validationSupport);// Init the Hapi Work
		StructureMap map = this.createTestStructuremap();
		maps.put(map.getUrl(), map);
		StructureMapUtilities scu = new StructureMapUtilities(hapiContext, maps, null, null);
		List<StructureDefinition> result = scu.analyse(null, map).getProfiles();

		assertEquals(1, result.size());

		ourLog.info(myCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.get(0)));
	}

	public class TargetParam {
		private String type;

		private String value;

		public TargetParam(String type, String value) {

			this.type = type;
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
