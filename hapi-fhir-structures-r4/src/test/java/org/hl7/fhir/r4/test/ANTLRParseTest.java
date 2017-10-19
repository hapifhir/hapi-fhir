package org.hl7.fhir.r4.test;

import ca.uhn.fhir.context.FhirContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.StructureMapUtilities;
import org.hl7.fhir.r4.utils.transform.BatchContext;
import org.hl7.fhir.r4.utils.transform.FhirTransformationEngine;
import org.hl7.fhir.r4.utils.transform.MappingIO;
import org.hl7.fhir.r4.utils.transform.ParseImpl;
import org.hl7.fhir.r4.utils.transform.deserializer.*;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.VoidAnswer1;
import org.mockito.stubbing.VoidAnswer3;

import java.io.File;
import java.io.FileReader;
import java.util.*;

import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.Mockito.doAnswer;

public class ANTLRParseTest {

	@Test
	public void FilePathing(){
		File file = new File("");
		System.out.println(file.getAbsolutePath());
	}

	@Test
	public void TestFHIRAntlr() throws Exception {
		//Init a implementor of the code
		ParseImpl parse = new ParseImpl();


		//Init a new visitor
		FhirMapVisitor visitor = new FhirMapVisitor(parse);

		MappingIO mappingIO = new MappingIO();
		mappingIO.setMappingFile(new File("colorectal3.map"));
		String map = mappingIO.readFile();

		//Make a processsor
		FhirMapProcessor p = new FhirMapProcessor(); //Is this part correct in the implementation?
		{
			FhirMapJavaParser grammar = p.loadGrammar(map);
			ParseTree tree = grammar.structureMap();
			visitor.visit(tree);
		}
		parse = (ParseImpl) visitor.getExecutor();//And this? I need to be able to pull a structure map from this object
		StructureMap structureMap = parse.structureMap;
	}

	@Test
	public void testparse() throws Exception {
		ParseImpl parse = new ParseImpl();

		//Read the Map into a string
		MappingIO mapping = new MappingIO();
		mapping.setMappingFile(new File("simpleMapTest.map"));
		String map = mapping.readFile();

		FhirMapProcessor processor = new FhirMapProcessor();

		processor.parseFhirMap(map, parse);

		System.out.println(parse.structureMap);

	}

	@Test
	public void testUrlPrse() throws Exception{
		UrlProcessor processor = new UrlProcessor();
		processor.parseUrl("\"http://fhir.hl7.org.au/fhir/rcpa/StructureMap/ColorectalMap\"");
	}

	@Test
	public void testTransform() throws Exception {
		FhirTransformationEngine scu = null;
		StructureDefinition sd1 = null;
		BatchContext bc = new BatchContext();
		PrePopulatedValidationSupport validation = new PrePopulatedValidationSupport();
		Map<String, StructureMap> maps = new HashMap<String, StructureMap>();

		FhirContext context = FhirContext.forR4();
		context.setValidationSupport(validation);
		sd1 = this.createTestStructure();

		//sd1 = context.newXmlParser().parseResource(StructureDefinition.class, new FileReader(new File("C:\\JCimiProject\\hapi-fhir-resource-profile-generator\\target\\classes\\mapping\\logical\\structuredefinition-colorectal.xml")));
		if (sd1.getId().contains("/"))
			sd1.setId(sd1.getId().split("/")[sd1.getId().split("/").length - 1]);
		validation.addStructureDefinition(sd1);
		bc.addStructureDefinition(sd1);
		for (StructureDefinition sd : new DefaultProfileValidationSupport().fetchAllStructureDefinitions(FhirContext.forR4())){
			bc.addStructureDefinition(sd);
			validation.addStructureDefinition(sd);
		}
		StructureMap map = null;
		ParseImpl parse = new ParseImpl();
		HapiWorkerContext hapiContext = new HapiWorkerContext(context, validation);
		scu = new FhirTransformationEngine(hapiContext);
		MappingIO mapping = new MappingIO();
		//mapping.setMappingFile(new File("colorectal3.map"));
		mapping.setMappingFile(new File("simpleMapTest.map"));
		String mapText = mapping.readFile();

		FhirMapProcessor processor = new FhirMapProcessor();

		processor.parseFhirMap(mapText, parse);
		map = parse.structureMap;
		List<StructureDefinition> result = scu.analyse(bc, null, map).getProfiles();

		ProfileUtilities profileUtilities = new ProfileUtilities(hapiContext, null, null);

		StructureDefinition newCode = result.get(0);
		profileUtilities.generateSnapshot(validation.fetchStructureDefinition(context, "http://hl7.org/fhir/StructureDefinition/Coding"), newCode, "http://foo.com/StructureDefinition/MyMap-Coding", "MyMap-Coding");

		for (StructureDefinition sd : result) {
			System.out.println(sd.toString());
			System.out.println(context.newXmlParser().setPrettyPrint(true).encodeResourceToString(sd));
		}
	}

	@Test
	public void legacyTestTransform() throws Exception {
		StructureMapUtilities scu = null;
		StructureDefinition sd1 = null;
		PrePopulatedValidationSupport validation = new PrePopulatedValidationSupport();
		Map<String, StructureMap> maps = new HashMap<String, StructureMap>();

		FhirContext context = FhirContext.forR4();
		context.setValidationSupport(validation);
		sd1 = this.createTestStructure();

		//sd1 = context.newXmlParser().parseResource(StructureDefinition.class, new FileReader(new File("C:\\JCimiProject\\hapi-fhir-resource-profile-generator\\target\\classes\\mapping\\logical\\structuredefinition-colorectal.xml")));
		if (sd1.getId().contains("/"))
			sd1.setId(sd1.getId().split("/")[sd1.getId().split("/").length - 1]);
		validation.addStructureDefinition(sd1);
		for (StructureDefinition sd : new DefaultProfileValidationSupport().fetchAllStructureDefinitions(FhirContext.forR4())){
			validation.addStructureDefinition(sd);
		}
		StructureMap map = null;
		ParseImpl parse = new ParseImpl();
		HapiWorkerContext hapiContext = new HapiWorkerContext(context, validation);
		scu = new StructureMapUtilities(hapiContext);
		MappingIO mapping = new MappingIO();
		//mapping.setMappingFile(new File("colorectal3.map"));
		mapping.setMappingFile(new File("simpleMapTestLegacy.map"));
		String mapText = mapping.readFile();

		FhirMapProcessor processor = new FhirMapProcessor();
		processor.parseFhirMap(mapText, parse);
		map = parse.structureMap;
		List<StructureDefinition> result = scu.analyse( null, map).getProfiles();

		ProfileUtilities profileUtilities = new ProfileUtilities(hapiContext, null, null);

		StructureDefinition newCode = result.get(0);
		profileUtilities.generateSnapshot(validation.fetchStructureDefinition(context, "http://hl7.org/fhir/StructureDefinition/Coding"), newCode, "http://foo.com/StructureDefinition/MyMap-Coding", "MyMap-Coding");

		for (StructureDefinition sd : result) {
			System.out.println(sd.toString());
			System.out.println(context.newXmlParser().setPrettyPrint(true).encodeResourceToString(sd));
		}
	}



	/*
		FROM HERE: Tests for each part of the ANTLR parse
	 */


	FhirMapProcessor createProcessor() throws Exception {
		return new FhirMapProcessor();
	}


	public void testRuleInput(String fhirMapText, String name, String type, FhirMapInputModes inputMode) throws Exception{
		ParseImpl mock = Mockito.mock(ParseImpl.class);

		doAnswer(answerVoid(
			(VoidAnswer3<String, String, FhirMapInputModes>) (_name, _type, _inputMode) -> System.out.println("Pass")
		)).when(mock).groupInput(name, type, inputMode);

		FhirMapProcessor p = createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.groupInput();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleInput() throws Exception {
		//testRuleInput("input nameX : typeX ;", "nameX", "typeX", FhirMapInputModes.NotSet);
		testRuleInput("input nameX : typeX as source ;", "nameX", "typeX", FhirMapInputModes.Source);
		testRuleInput("input nameX : typeX as target;", "nameX", "typeX", FhirMapInputModes.Target);
	}

	public void testRuleSource(String fhirMapText, List<String> context, FhirMapRuleType type, String defaultValue, FhirMapListOptions listOptions, String variable, String wherePath, String checkPath) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).ruleSource(context, type, defaultValue, listOptions, variable, wherePath, checkPath);

		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleSource();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleSource() throws Exception {
		FhirMapRuleType ruletype = new FhirMapRuleType();
		ruletype.Occurances = Arrays.asList(1, 3);
		ruletype.TypeName = "typeName";
		testRuleSource("a.b.c : typeName 1..3 default defaultValue as variableValue where \"whereFhirePath\" check \"checkFhirePath\"",
			new ArrayList<String>(Arrays.asList("a", "b", "c")),
			ruletype,
			"defaultValue",
			FhirMapListOptions.NotSet,
			"variableValue",
			"whereFhirePath",
			"checkFhirePath");
	}


	public void testRuleAssign(String fhirMapText, List<String> context, String assignValue, String targetValue) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCopy(context, assignValue, targetValue);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleAssign() throws Exception {
		testRuleAssign("cdr.subject = v", Arrays.asList("cdr", "subject"), "v", null);
		testRuleAssign("cdr.subject = \"quoted\"", Arrays.asList("cdr", "subject"), "\"quoted\"", null);
		testRuleAssign("cdr.subject = v as xyzzy", Arrays.asList("cdr", "subject"), "v", "xyzzy");

	}

	public void testRuleEvaluate(String fhirMapText, List<String> context, String obj, String objElement, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformEvaluate(context, obj, objElement, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleEvaluate() throws Exception {
		testRuleEvaluate("cdr.subject = evaluate(v, wwww)", Arrays.asList("cdr", "subject"), "v", "wwww", null);
		testRuleEvaluate("cdr.subject = evaluate(v, wwww) as xyzzy", Arrays.asList("cdr", "subject"), "v", "wwww", "xyzzy");
	}

	public void testRulePointer(String fhirMapText, List<String> context, String resource, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformPointer(context, resource, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRulePointer() throws Exception {
		testRulePointer("cdr.subject = pointer(v)", Arrays.asList("cdr", "subject"), "v", null);
		testRulePointer("cdr.subject = pointer(v) as xyzzy", Arrays.asList("cdr", "subject"), "v", "xyzzy");
	}

	public void testRuleQty1(String fhirMapText, List<String> context, String resource, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformQty(context, resource, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleQty1() throws Exception {
		testRuleQty1("cdr.subject = qty(\"v\")", Arrays.asList("cdr", "subject"), "v", null);
		testRuleQty1("cdr.subject = qty(\"v\") as xyzzy", Arrays.asList("cdr", "subject"), "v", "xyzzy");
	}


	public void testRuleQty2(String fhirMapText, List<String> context, String value, String unitString, String system, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformQty(context, value, unitString, system, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleQty2() throws Exception {
		testRuleQty2("cdr.subject = qty(xxxx, \"yyyy\", \"http://www.xyz.com\")", Arrays.asList("cdr", "subject"), "xxxx", "yyyy", "http://www.xyz.com", null);
		testRuleQty2("cdr.subject = qty(xxxx, \"yyyy\", \"http://www.xyz.com\") as xyzzy", Arrays.asList("cdr", "subject"), "xxxx", "yyyy", "http://www.xyz.com", "xyzzy");
	}

	public void testRuleQty3(String fhirMapText, List<String> context, String value, String unitString, String type, String targetVariable) throws  Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformQty(context, value, unitString, type, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleQty3() throws Exception {
		testRuleQty3("cdr.subject = qty(xxxx, \"yyyy\", zzz)", Arrays.asList("cdr", "subject"), "xxxx", "yyyy", "zzz", null);
		testRuleQty3("cdr.subject = qty(xxxx, \"yyyy\", zzz) as xyzzy", Arrays.asList("cdr", "subject"), "xxxx", "yyyy", "zzz", "xyzzy");
	}

	public void testRuleId(String fhirMapText, List<String> context, String system, String value, String type, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		UrlData systemUrl = new UrlData();
		systemUrl.CompleteUrl = system;
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformId(context, systemUrl, value, type, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleId() throws Exception {
		UrlData systemUrl = new UrlData();
		systemUrl.CompleteUrl = "http://www.xyz.com";
		testRuleId("cdr.subject = id(\"http://www.xyz.com\", xxxx)", Arrays.asList("cdr", "subject"), "http://www.xyz.com", "xxxx", null, null);
		testRuleId("cdr.subject = id(\"http://www.xyz.com\", xxxx, yyyy)", Arrays.asList("cdr", "subject"), "http://www.xyz.com", "xxxx", "yyyy", null);
		testRuleId("cdr.subject = id(\"http://www.xyz.com\", xxxx, yyyy) as xyzzy", Arrays.asList("cdr", "subject"), "http://www.xyz.com", "xxxx", "yyyy", "xyzzy");
	}

	public void testRuleCopy(String fhirMapText, List<String> context, String copyValue, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCopy(context, copyValue, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCopy() throws Exception{
		testRuleCopy("cdr.subject = copy(v)", Arrays.asList("cdr", "subject"), "v", null);
		testRuleCopy("cdr.subject = copy(v) as xyzzy", Arrays.asList("cdr", "subject"), "v", "xyzzy");
	}

	public void testRuleCreate(String fhirMapText, List<String> context, String copyValue, String targetVariable) throws Exception{
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCreate(context, copyValue, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCreate() throws Exception {
		testRuleCreate("cdr.subject = create(\"quoted\")", Arrays.asList("cdr", "subject"), "quoted", null);
		testRuleCreate("cdr.subject = create(\"quoted\") as xyzzy", Arrays.asList("cdr", "subject"), "quoted", "xyzzy");
	}

	public void testRuleAppend(String fhirMapText, List<String> context, List<String> appendValues, String targetVariable) throws Exception{
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformAppend(context, appendValues, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleAppend() throws Exception {
		testRuleAppend("cdr.subject = append(a, \"bn\", \"d e f\")", Arrays.asList("cdr", "subject"), Arrays.asList("a", "b\n", "d e f"), null);
		testRuleAppend("cdr.subject = append(a, \"bn\", \"d e f\") as xyzzy", Arrays.asList("cdr", "subject"), Arrays.asList("a", "b\n", "d e f"), "xyzzy");
	}

	public void testGroupCall(String fhirMapText, String groupName, List<String> parameters) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).groupCall(groupName, parameters);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.groupCall();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseGroupCall() throws Exception {
		testGroupCall("XXYYZ(a, b)", "XXYYZ", Arrays.asList("a", "b"));
	}

	void testRuleTruncate(String fhirMapText, List<String> context, String truncateVariable, int length, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformTruncate(context, truncateVariable, length, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleTruncate() throws Exception {
		testRuleTruncate("cdr.subject = truncate(\"quoted\", 32)", Arrays.asList("cdr", "subject"), "\"quoted\"", 32, null);
		testRuleTruncate("cdr.subject = truncate(\"quoted\", 32) as xyzzy", Arrays.asList("cdr", "subject"), "\"quoted\"", 32, "xyzzy");
	}

	public void testRuleCast(String fhirMapText, List<String> context, String source, String type, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCast(context, source, type, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCast() throws Exception {
		testRuleCast("cdr.subject = cast(source, xxYYz)", Arrays.asList(), "source", "xxYYz", null);
		testRuleCast("cdr.subject = cast(source, xxYYz) as xyzzy", Arrays.asList(), "source", "xxYYz", "xyzzy");
	}

	public void testRuleCoding(String fhirMapText, List<String> context, String completeUrl, String system, String code, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		UrlData _completeUrl = new UrlData();
		_completeUrl.CompleteUrl = completeUrl;
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCoding(context, _completeUrl, system, code, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCoding() throws Exception {
		testRuleCoding("a.b = c\"http://fhir.hl7.org.au\", system)", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", null, null);
		testRuleCoding("a.b = c\"http://fhir.hl7.org.au\", system, \"display\")", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", "display", null);
		testRuleCoding("a.b = c\"http://fhir.hl7.org.au\", system, \"display\") as xyzzy", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", "display", "xyzzy");
	}

	public void testRuleCodeableConcept1(String fhirMapText, List<String> context, String textValue, String targetValue) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCodeableConcept(context, textValue, targetValue);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCodeableConcept1() throws Exception {
		testRuleCodeableConcept1("a.b = cc(\"abcdef\")", Arrays.asList("a", "b"), "abcdef", null);
		testRuleCodeableConcept1("a.b = cc(\"abcdef\") as xyzzy", Arrays.asList("a", "b"), "abcdef", "xyzzy");
	}

	public void testRuleCodeableConcept2(String fhirMapText, List<String> context, String completeUrl, String system, String code, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		UrlData _completeUrl = new UrlData();
		_completeUrl.CompleteUrl = completeUrl;
	}

	@Test
	public void parseRuleCodeableConcept2() throws Exception {
		//TODO: the C# test method is incorrect.
	}

	@Test
	public void parseQuotedString() throws Exception {
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("\"Hello, this is a test string for the quotedString ANTLR Rule\"");
			ParseTree parseTree = grammar.quotedString();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			String parsedData = (String)visitor.visit(parseTree);
			Assert.assertTrue(parsedData.equals("Hello, this is a test string for the quotedString ANTLR Rule"));
		}
	}

	public void testRuleCp(String fhirMapText, List<String> context, String completeUrl, String varialbe, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		UrlData data = new UrlData();
		data.CompleteUrl = completeUrl;
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).transformCp(context, data, varialbe, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleCp() throws Exception {
		testRuleCp("a.b = cp(system)", Arrays.asList("a", "b"), null, "system", null);
		testRuleCp("a.b = cp(\"http://fhir.hl7.org.au\", system)", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", null);
		testRuleCp("a.b = cp(\"http://fhir.hl7.org.au\", system) as xyzzy", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", "xyzzy");
	}

	public void testRuleTranslate(String fhirMapText, List<String> context, String variable, String mapUrl, FhirMapTranslateOutputTypes outputType, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		UrlData data = new UrlData();
		data.CompleteUrl = mapUrl;
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText + " passes")
		)).when(mock).transformTranslate(context, variable, data, outputType, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleTranslate() throws Exception{
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", code)", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.Code, null);
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", system)", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.System, null);
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", display)", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.Display, null);
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", coding)", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.Coding, null);
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", codeableConcept)", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.CodeableConcept, null);
		testRuleTranslate("a.b = translate(system, \"http://www.xyz.com\", codeableConcept) as xyzzy", Arrays.asList("a", "b"), "system", "http://www.xyz.com", FhirMapTranslateOutputTypes.CodeableConcept, "xyzzy");
	}

	@Test
	public void parseRuleType() throws Exception {
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(": code");
			ParseTree parseTree = grammar.ruleType();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			FhirMapRuleType parsed = (FhirMapRuleType)visitor.visit(parseTree);
			Assert.assertTrue(parsed.TypeName.equals("code"));
		}
		{
			FhirMapJavaParser grammar = p.loadGrammar(": code 0..1");
			ParseTree parseTree = grammar.ruleType();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			FhirMapRuleType parsed = (FhirMapRuleType)visitor.visit(parseTree);
			Assert.assertTrue(parsed.TypeName.equals("code"));
			Assert.assertTrue(parsed.Occurances.size() == 2);
			Assert.assertTrue(parsed.Occurances.get(0) == 0);
			Assert.assertTrue(parsed.Occurances.get(1) == 1);
		}
	}

	@Test
	public void parseKeyMap() throws Exception {
		UrlData urlData = new UrlData();
		String name = "Colorectal --> DiagnosticReport Map";
		urlData.CompleteUrl = "http://fhir.hl7.org.au/fhir/rcpa/StructureMap/ColorectalMap";
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Pass")
		)).when(mock).map(urlData, name);
		FhirMapProcessor p = this.createProcessor();
		{

			FhirMapJavaParser grammar = p.loadGrammar("map \"http://fhir.hl7.org.au/fhir/rcpa/StructureMap/ColorectalMap\" = \"Colorectal --> DiagnosticReport Map\"");
			ParseTree parseTree = grammar.keyMap();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			String parsedData = (String)visitor.visit(parseTree);
		}

	}

	@Test
	public void parseQuotedIdentifier() throws Exception {
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("\"Lorem.ipsum.dolor\"");
			ParseTree parseTree = grammar.quotedIdentifier();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			String parsedValue = (String) visitor.visit(parseTree);
			Assert.assertTrue(parsedValue.equals("Lorem.ipsum.dolor"));
		}
	}

	@Test
	public void parseIdentifier() throws Exception {
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("xyzzy");
			ParseTree parseTree = grammar.identifier();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			String parsedValue = (String) visitor.visit(parseTree);
			Assert.assertTrue(parsedValue.equals("xyzzy"));
		}
	}

	@Test
	public void parseRuleTargetAs() throws Exception {
		final List<String> context = Arrays.asList("do", "requester");
		final String identifier = "prr";
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println("Identifier Pass")
		)).when(mock).transformAs(context, identifier);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("do.requester as prr");
			ParseTree parseTree = grammar.ruleContext();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	public void testRuleExtension(String fhirMapText, List<String> context, String variable, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText+" passes")
		)).when(mock).transformExtension(context, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleExtension() throws Exception {
		testRuleExtension("a.b = extension()", Arrays.asList("a", "b"), null, null);
		testRuleExtension("a.b = extension() as xyzzy", Arrays.asList("a","b"), null, "xyzzy");
	}

	public void testRuleEscape(String fhirMapText, List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText+" passes")
		)).when(mock).transformEscape(context, variable, string1, string2, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleEscape() throws Exception {
		testRuleEscape("a.b = escape(abcdef, \"aaa\")", Arrays.asList("a", "b"), "abcdef", "aaa", null, null);
		testRuleEscape("a.b = escape(abcdef, \"aaa\", \"bbb\")", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", null);
		testRuleEscape("a.b = escape(abcdef, \"aaa\", \"bbb\") as xyzzy", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", "xyzzy");
	}


	public void testRuleDateOp(String fhirMapText, List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText+" passes")
		)).when(mock).transformDateOp(context, variable, string1, string2, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleDateOp() throws Exception {
		testRuleDateOp("a.b = dateOp(abcdef, \"aaa\")", Arrays.asList("a", "b"), "abcdef", "aaa", null, null);
		testRuleDateOp("a.b = dateOp(abcdef, \"aaa\", \"bbb\")", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", null);
		testRuleDateOp("a.b = dateOp(abcdef, \"aaa\", \"bbb\") as xyzzy", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", "xyzzy");
	}

	public void testRuleReference(String fhirMapText, List<String> context, String variable, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText+" passes")
		)).when(mock).transformReference(context, variable, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleReference() throws Exception {
		testRuleReference("a.b = reference(abcdef)", Arrays.asList("a","b"), "abcdef", null);
		testRuleReference("a.b = reference(abcdef) as xyzzy", Arrays.asList("a","b"), "abcdef", "xyzzy");
	}


	public void testRuleUuid(String fhirMapText, List<String> context, String targetVariable) throws Exception {
		ParseImpl mock = Mockito.mock(ParseImpl.class);
		doAnswer(answerVoid(
			(VoidAnswer1<?>) (_answer) -> System.out.println(fhirMapText+" passes")
		)).when(mock).transformUuid(context, targetVariable);
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleUuid() throws Exception {
		testRuleUuid("a.b = uuid()", Arrays.asList("a","b"), null);
		testRuleUuid("a.b = uuid() as xyzzy", Arrays.asList("a","b"), "xyzzy");
	}







	public StructureDefinition createTestStructure(){
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
	public StructureDefinition.StructureDefinitionSnapshotComponent createTestSnapshot(){
		StructureDefinition.StructureDefinitionSnapshotComponent retVal = new StructureDefinition.StructureDefinitionSnapshotComponent();
		List<ElementDefinition> eList = new ArrayList<>();
		ElementDefinition ed0 = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed0.setId("TestStructure");
		ed0.setSliceName("TestStructure");
		ed0.setPath("TestStructure");
		// ed0.setBase(base);
		ed0.setMin(1);
		ed0.setMax("1");
		eList.add(ed0);


		ElementDefinition ed = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed.setId("system");
		ed.setSliceName("system");
		ed.setPath("TestStructure.system");
		//ed.setBase(base);
		ed.setFixed(new UriType().setValue("HTTP://opencimi.org/structuredefinition/TestStructure.html#Debugging"));
		//ed.setType(this.createTypeRefList());
		ed.setMin(1);
		ed.setMax("1");
		eList.add(ed);

		ed = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed.setId("someValue");
		ed.setSliceName("someValue");
		ed.setPath("TestStructure.someValue");
		//ed.setBase(base);
		ed.setFixed(new StringType().setValue("my value"));
		//ed.setType(this.createTypeRefList());
		ed.setMin(1);
		ed.setMax("0");
		eList.add(ed);


		retVal.setElement(eList);
		return retVal;


	}

	public StructureDefinition.StructureDefinitionDifferentialComponent createTestDiff(){
		StructureDefinition.StructureDefinitionDifferentialComponent retVal = new StructureDefinition.StructureDefinitionDifferentialComponent();
		List<ElementDefinition> eList = new ArrayList<>();
		ElementDefinition ed0 = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed0.setId("TestStructure");
		ed0.setSliceName("TestStructure");
		ed0.setPath("TestStructure");
		// ed0.setBase(base);
		ed0.setMin(1);
		ed0.setMax("1");
		eList.add(ed0);


		ElementDefinition ed = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
//		ed.setId("system");
//		ed.setSliceName("system");
//		ed.setPath("TestStructure.system");
//		//ed.setBase(base);
//		ed.setFixed(new UriType().setValue("HTTP://opencimi.org/structuredefinition/TestStructure.html#Debugging"));
//		//ed.setType(this.createTypeRefList());
//		eList.add(ed);

		ed = new ElementDefinition();
		//ElementDefinition.ElementDefinitionBaseComponent base = new ElementDefinition.ElementDefinitionBaseComponent();
		//base.setId("http://hl7.org/fhir/StructureDefinition/Element");
		ed.setId("someValue");
		ed.setSliceName("someValue");
		ed.setPath("TestStructure.someValue");
		//ed.setBase(base);
		ed.setFixed(new StringType().setValue("my value"));
		//ed.setType(this.createTypeRefList());
		eList.add(ed);

		retVal.setElement(eList);
		return retVal;
	}



}
