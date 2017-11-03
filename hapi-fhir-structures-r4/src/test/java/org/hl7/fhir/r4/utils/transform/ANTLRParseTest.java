package org.hl7.fhir.r4.utils.transform;

import org.antlr.v4.runtime.tree.ParseTree;
import org.hl7.fhir.r4.utils.transform.deserializer.*;
import org.hl7.fhir.r4.utils.transform.deserializer.grammar.antlr.javaAntlr.FhirMapJavaParser;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.AdditionalAnswers.answerVoid;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
* For testing the ANTLR parser, uses a Mock object in most cases before
* asserting which pieces of the mapping language is correct. Errors will be
* thrown if the value recieved is incorrect.
*/
@SuppressWarnings("unchecked")
public class ANTLRParseTest {

	FhirMapProcessor createProcessor() throws Exception {
		return new FhirMapProcessor();
	}

	public void testRuleInput(String fhirMapText, String name, String type, FhirMapInputModes inputMode) throws Exception{
		MapHandler mock = Mockito.mock(MapHandler.class);

		doAnswer(invocation ->
		{
			Object[] args = invocation.getArguments();
			Assert.assertTrue(name.equals(args[0]));
			Assert.assertTrue(type.equals(args[1]));
			Assert.assertTrue(inputMode.equals(args[2]));
			return null;
		}).when(mock).groupInput(anyString(), anyString(), any(FhirMapInputModes.class));

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
		MapHandler mock = Mockito.mock(MapHandler.class);

		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			if (type != null)
				Assert.assertTrue(type.equals(args[1]));
			if (defaultValue != null)
				Assert.assertTrue(defaultValue.equals(args[2]));
			if (variable != null)
				Assert.assertTrue(variable.equals(args[3]));
			if (wherePath != null)
				Assert.assertTrue(wherePath.equals(args[4]));
			if (checkPath != null)
				Assert.assertTrue(checkPath.equals(args[5]));
			return null;
		}).when(mock).ruleSource(anyList(), any(FhirMapRuleType.class), anyString(), any(FhirMapListOptions.class), anyString(), anyString(), anyString());
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
		testRuleSource("a.b.c", Arrays.asList("a","b","c"), null, null, FhirMapListOptions.NotSet, null, null, null);
		testRuleSource("a.b.c : typeName 1..3 as variableValue", new ArrayList<String>(Arrays.asList("a", "b", "c")), new FhirMapRuleType("typeName", Arrays.asList(1,3)) , null, null, "variableValue", null, null);
	}


	public void testRuleAssign(String fhirMapText, List<String> context, String assignValue, String targetValue) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(assignValue.equals(args[1]));
			if (targetValue != null)
				Assert.assertTrue(targetValue.equals(args[2]));
			return null;
		}).when(mock).transformCopy(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(obj.equals(args[1]));
			Assert.assertTrue(objElement.equals(args[2]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[3]));
			return null;
		}).when(mock).transformEvaluate(anyList(), anyString(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(resource.equals(args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformPointer(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(resource.equals(args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformQty(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData urlData = new UrlData();
		urlData.CompleteUrl = system;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(value.equals(args[1]));
			Assert.assertTrue(unitString.equals(args[2]));
			UrlData arg = (UrlData) args[3];
			Assert.assertTrue(system.equals(arg.CompleteUrl));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformQty(anyList(), anyString(), anyString(), any(UrlData.class), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(value.equals(args[1]));
			Assert.assertTrue(unitString.equals(args[2]));
			Assert.assertTrue(type.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformQty(anyList(), anyString(), anyString(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData systemUrl = new UrlData();
		systemUrl.CompleteUrl = system;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			UrlData arg = (UrlData) args[1];
			Assert.assertTrue(system.equals(arg.CompleteUrl));
			Assert.assertTrue(value.equals(args[2]));
			Assert.assertTrue(type.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformId(anyList(), any(UrlData.class), anyString(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(copyValue.equals(args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformCopy(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(copyValue.equals(args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformCreate(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(appendValues.containsAll((Collection<?>) args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformAppend(anyList(), anyList(), anyString());
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
		testRuleAppend("cdr.subject = append(a, \"b\n\", \"d e f\")", Arrays.asList("cdr", "subject"), Arrays.asList("a", "\"b\n\"", "\"d e f\""), null);
		testRuleAppend("cdr.subject = append(a, \"b\n\", \"d e f\") as xyzzy", Arrays.asList("cdr", "subject"), Arrays.asList("a", "\"b\n\"", "\"d e f\""), "xyzzy");
	}

	public void testGroupCall(String fhirMapText, String groupName, List<String> parameters) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(groupName.equals(args[0]));
			Assert.assertTrue(parameters.containsAll((Collection<?>) args[1]));
			return null;
		}).when(mock).groupCall(anyString(), anyList());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(truncateVariable.equals(args[1]));
			Integer arg = (Integer) args[2];
			Assert.assertTrue(length == arg);
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[3]));
			return null;
		}).when(mock).transformTruncate(anyList(), anyString(), anyInt(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(source.equals(args[1]));
			Assert.assertTrue(type.equals(args[2]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[3]));
			return null;
		}).when(mock).transformCast(anyList(), anyString(), anyString(), anyString());
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
		testRuleCast("cdr.subject = cast(source, xxYYz)", Arrays.asList("cdr", "subject"), "source", "xxYYz", null);
		testRuleCast("cdr.subject = cast(source, xxYYz) as xyzzy", Arrays.asList("cdr", "subject"), "source", "xxYYz", "xyzzy");
	}

	public void testRuleCoding(String fhirMapText, List<String> context, String completeUrl, String system, String code, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData _completeUrl = new UrlData();
		_completeUrl.CompleteUrl = completeUrl;
		doAnswer(invocation ->{
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			UrlData arg = (UrlData) args[1];
			Assert.assertTrue(completeUrl.equals(arg.CompleteUrl));
			Assert.assertTrue(system.equals(args[2]));
			Assert.assertTrue(code.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformCoding(anyList(), any(UrlData.class), anyString(), anyString(), anyString());
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
		testRuleCoding("a.b = c(\"http://fhir.hl7.org.au\", system)", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", null, null);
		testRuleCoding("a.b = c(\"http://fhir.hl7.org.au\", system, \"display\")", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", "display", null);
		testRuleCoding("a.b = c(\"http://fhir.hl7.org.au\", system, \"display\") as xyzzy", Arrays.asList("a", "b"), "http://fhir.hl7.org.au", "system", "display", "xyzzy");
	}

	public void testRuleCodeableConcept1(String fhirMapText, List<String> context, String textValue, String targetValue) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(textValue.equals(args[1]));
			if (targetValue != null)
				Assert.assertTrue(targetValue.equals(args[2]));
			return null;
		}).when(mock).transformCodeableConcept(context, textValue, targetValue);
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

	public void testRuleCodeableConcept2(String fhirMapText, List<String> context, String completeUrl, String code, String display, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData _completeUrl = new UrlData();
		_completeUrl.CompleteUrl = completeUrl;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			UrlData arg = (UrlData) args[1];
			Assert.assertTrue(completeUrl.equals(arg.CompleteUrl));
			Assert.assertTrue(code.equals(args[2]));
			Assert.assertTrue(display.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformCodeableConcept(anyList(), any(UrlData.class), anyString(), anyString(), anyString());
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}

	}

	@Test
	public void parseRuleCodeableConcept2() throws Exception {
		testRuleCodeableConcept2("a.b = cc(\"http://fhir.hl7.org.au\", \"code\")", Arrays.asList("a","b"), "http://fhir.hl7.org.au", "\"code\"", null, null);
		testRuleCodeableConcept2("a.b = cc(\"http://fhir.hl7.org.au\", \"code\", \"display name\")", Arrays.asList("a","b"), "http://fhir.hl7.org.au", "\"code\"", "display name", null);
		testRuleCodeableConcept2("a.b = cc(\"http://fhir.hl7.org.au\", \"code\", \"display name\") as xyzzy", Arrays.asList("a","b"), "http://fhir.hl7.org.au", "\"code\"", "display name", "xyzzy");
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

	public void testRuleCp(String fhirMapText, List<String> context, String completeUrl, String variable, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData data = new UrlData();
		data.CompleteUrl = completeUrl;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			UrlData arg = (UrlData) args[1];
			Assert.assertTrue(completeUrl.equals(arg.CompleteUrl));
			Assert.assertTrue(variable.equals(args[2]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[3]));
			return null;
		}).when(mock).transformCp(anyList(), any(UrlData.class), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData data = new UrlData();
		data.CompleteUrl = mapUrl;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(variable.equals(args[1]));
			UrlData arg = (UrlData) args[2];
			Assert.assertTrue(mapUrl.equals(arg.CompleteUrl));
			Assert.assertTrue(outputType.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformTranslate(anyList(), anyString(), any(UrlData.class), any(FhirMapTranslateOutputTypes.class), anyString());
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
			Assert.assertTrue(parsed.Occurrences.size() == 2);
			Assert.assertTrue(parsed.Occurrences.get(0) == 0);
			Assert.assertTrue(parsed.Occurrences.get(1) == 1);
		}
	}

	@Test
	public void parseKeyMap() throws Exception {
		UrlData urlData = new UrlData();
		String name = "Colorectal --> DiagnosticReport Map";
		urlData.CompleteUrl = "http://fhir.hl7.org.au/fhir/rcpa/StructureMap/ColorectalMap";
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			UrlData arg = (UrlData) args[0];
			Assert.assertTrue(urlData.CompleteUrl.equals(arg.CompleteUrl));
			Assert.assertTrue(name.equals(args[1]));
			return null;
		}).when(mock).map(any(UrlData.class), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(identifier.equals(args[1]));
			return null;
		}).when(mock).transformAs(anyList(), anyString());
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("do.requester as prr");
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	public void testRuleExtension1(String fhirMapText, List<String> context, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[1]));
			return null;
		}).when(mock).transformExtension(anyList(), anyString());
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseRuleExtension1() throws Exception {
		testRuleExtension1("a.b = extension()", Arrays.asList("a", "b"),  null);
		testRuleExtension1("a.b = extension() as xyzzy", Arrays.asList("a","b"),  "xyzzy");
	}


	public void testRuleExtension2(String fhirMapText, List<String> context, String extUri, String title, String mode, String parent, String text1, String text2, Integer min, String max, String type, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		UrlData urlData = new UrlData();
		urlData.CompleteUrl = extUri;
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			UrlData arg = (UrlData) args[1];
			Assert.assertTrue(extUri.equals(arg.CompleteUrl));
			Assert.assertTrue(title.equals(args[2]));
			Assert.assertTrue(mode.equals(args[3]));
			Assert.assertTrue(parent.equals(args[4]));
			Assert.assertTrue(text1.equals(args[5]));
			Assert.assertTrue(text2.equals(args[6]));
			Assert.assertTrue(min.equals(args[7]));
			Assert.assertTrue(max.equals(args[8]));
			Assert.assertTrue(type.equals(args[9]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[10]));
			return null;
		}).when(mock).transformExtension(anyList(), any(UrlData.class), anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyString(), anyString(), anyString());
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleTarget();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}


//

		@Test
	public void parseRuleExtension2() throws Exception {

		testRuleExtension2("a.b = extension(\"http://opencimi.org/fhir/extension/coding/coded_text_uri.html\", \"coded_text_uri\", \"Resource\", \"Coding\", \"URI representing the specific concept\", \"URI representing the specific concept\", 0, \"1\", \"uri\")", Arrays.asList("a","b"),
			"http://opencimi.org/fhir/extension/coding/coded_text_uri.html", "coded_text_uri", "Resource", "Coding", "URI representing the specific concept", "URI representing the specific concept", 0, "1", "uri", null);
		testRuleExtension2("a.b = extension(\"http://opencimi.org/fhir/extension/coding/coded_text_uri.html\", \"coded_text_uri\", \"Resource\", \"Coding\", \"URI representing the specific concept\", \"URI representing the specific concept\", 0, \"1\", \"uri\") as xyzzy", Arrays.asList("a","b"),
			"http://opencimi.org/fhir/extension/coding/coded_text_uri.html", "coded_text_uri", "Resource", "Coding", "URI representing the specific concept", "URI representing the specific concept", 0, "1", "uri", "xyzzy");
	}
	public void testRuleEscape(String fhirMapText, List<String> context, String variable, String string1, String string2, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(variable.equals(args[1]));
			Assert.assertTrue(string1.equals(args[2]));
			if (string2 != null)
				Assert.assertTrue(string2.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformEscape(anyList(), anyString(), anyString(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(variable.equals(args[1]));
			Assert.assertTrue(string1.equals(args[2]));
			Assert.assertTrue(string2.equals(args[3]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[4]));
			return null;
		}).when(mock).transformDateOp(anyList(), anyString(), anyString(), anyString(), anyString());
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
		//testRuleDateOp("a.b = dateOp(abcdef, \"aaa\", \"bbb\")", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", null);
		//testRuleDateOp("a.b = dateOp(abcdef, \"aaa\", \"bbb\") as xyzzy", Arrays.asList("a", "b"), "abcdef", "aaa", "bbb", "xyzzy");
	}

	public void testRuleReference(String fhirMapText, List<String> context, String variable, String targetVariable) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			Assert.assertTrue(variable.equals(args[1]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[2]));
			return null;
		}).when(mock).transformReference(anyList(), anyString(), anyString());
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
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(context.containsAll((Collection<?>) args[0]));
			if (targetVariable != null)
				Assert.assertTrue(targetVariable.equals(args[1]));
			return null;
		}).when(mock).transformUuid(anyList(), anyString());
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

	public void testRuleName(String fhirMapText, List<String> excpectedValue) throws Exception {
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.ruleContext();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			List<String> parsedValue = (List<String>) visitor.visit(parseTree);
			Assert.assertTrue(excpectedValue.containsAll(parsedValue));
		}
	}

	@Test
	public void parseRuleName() throws Exception {
		testRuleName("xxyyzz", Collections.singletonList("xxyyzz"));
		testRuleName("a.b.ccc.def", Arrays.asList("a","b","ccc","def"));
		testRuleName("a.b.\"c c ccc\".def", Arrays.asList("a", "b", "\"c c ccc\"", "def"));
	}

	@Test
	public void parseKeyImport() throws Exception {
		UrlData urlData = new UrlData();
		urlData.CompleteUrl = "http://fhir.hl7.org.au";
		MapHandler mock = Mockito.mock(MapHandler.class);

		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("imports \"http://fhir.hl7.org.au\"");
			ParseTree parseTree = grammar.keyImports();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
			Assert.assertTrue(Objects.equals(urlData.CompleteUrl, "http://fhir.hl7.org.au"));
		}
	}

	public void testKeyUses(String fhirMapText, String url, FhirMapUseNames useNames) throws Exception {
		UrlData urlData = new UrlData();
		urlData.CompleteUrl = url;
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			UrlData arg = (UrlData) args[0];
			Assert.assertTrue(url.equals(arg.CompleteUrl));
			Assert.assertTrue(useNames.equals(args[1]));
			return null;
		}).when(mock).uses(any(UrlData.class), any(FhirMapUseNames.class));
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.keyUses();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void  parseKeyUses() throws Exception {
		testKeyUses("uses \"http://www.xyz.com\" as target", "http://www.xyz.com", FhirMapUseNames.Target);
		testKeyUses("uses \"http://www.xyz.com\" as source", "http://www.xyz.com", FhirMapUseNames.Source);
		testKeyUses("uses \"http://www.xyz.com\" as produced", "http://www.xyz.com", FhirMapUseNames.Produced);
		testKeyUses("uses \"http://www.xyz.com\" as queried", "http://www.xyz.com", FhirMapUseNames.Queried);
	}

	public void testKeyUsesName(String fhirMapText, FhirMapUseNames useName) throws Exception{
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.keyUsesName();
			FhirMapVisitor visitor = new FhirMapVisitor(null);
			FhirMapUseNames parsedData = (FhirMapUseNames)visitor.visit(parseTree);
			Assert.assertTrue(parsedData == useName);
		}
	}

	@Test
	public void parseKeyUsesNames() throws Exception {
		testKeyUsesName("source", FhirMapUseNames.Source);
		testKeyUsesName("target", FhirMapUseNames.Target);
		testKeyUsesName("queried", FhirMapUseNames.Queried);
		testKeyUsesName("produced", FhirMapUseNames.Produced);
	}

	public void testGroupStart(String fhirMapText, String groupName, FhirMapGroupTypes groupTypes, String extendsName) throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			Assert.assertTrue(groupName.equals(args[0]));
			Assert.assertTrue(groupTypes.equals(args[1]));
			if (extendsName != null)
				Assert.assertTrue(extendsName.equals(args[2]));
			return null;
		}).when(mock).groupStart(anyString(), any(FhirMapGroupTypes.class), anyString());
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar(fhirMapText);
			ParseTree parseTree = grammar.groupStart();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}

	@Test
	public void parseGroupStart() throws Exception {
		testGroupStart("group for types cleverGroupName endgroup", "cleverGroupName", FhirMapGroupTypes.Types, null);
		testGroupStart("group for type+types cleverGroupName endgroup", "cleverGroupName", FhirMapGroupTypes.TypeTypes, null);
		testGroupStart("group cleverGroupName extends xxx endgroup", "cleverGroupName", FhirMapGroupTypes.NotSet, "xxx");
	}

	@Test
	public void parseGroupEnd() throws Exception {
		MapHandler mock = Mockito.mock(MapHandler.class);
		doAnswer(invocation -> null).when(mock).groupEnd();
		FhirMapProcessor p = this.createProcessor();
		{
			FhirMapJavaParser grammar = p.loadGrammar("endgroup");
			ParseTree parseTree = grammar.groupEnd();
			FhirMapVisitor visitor = new FhirMapVisitor(mock);
			visitor.visit(parseTree);
		}
	}
}
