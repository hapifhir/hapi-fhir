package org.hl7.fhir.dstu2016may.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.hl7.fhir.dstu2016may.formats.JsonParser;
import org.hl7.fhir.dstu2016may.model.Appointment;
import org.hl7.fhir.dstu2016may.model.Base;
import org.hl7.fhir.dstu2016may.model.Bundle;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.DecimalType;
import org.hl7.fhir.dstu2016may.model.ElementDefinition;
import org.hl7.fhir.dstu2016may.model.ExpressionNode;
import org.hl7.fhir.dstu2016may.model.Observation;
import org.hl7.fhir.dstu2016may.model.Patient;
import org.hl7.fhir.dstu2016may.model.Questionnaire;
import org.hl7.fhir.dstu2016may.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu2016may.model.Range;
import org.hl7.fhir.dstu2016may.model.Resource;
import org.hl7.fhir.dstu2016may.model.RiskAssessment;
import org.hl7.fhir.dstu2016may.model.SimpleQuantity;
import org.hl7.fhir.dstu2016may.model.StructureDefinition;
import org.hl7.fhir.dstu2016may.model.UriType;
import org.hl7.fhir.dstu2016may.model.ValueSet;
import org.hl7.fhir.dstu2016may.utils.FHIRPathEngine;
import org.hl7.fhir.dstu2016may.utils.SimpleWorkerContext;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.junit.Test;

import junit.framework.Assert;

public class FluentPathTests {

  static private Patient patient;
  static private Appointment appointment;
  static private Observation observation;
  static private ValueSet valueset;
  static private Questionnaire questionnaire;

  private Patient patient() throws FHIRFormatError, FileNotFoundException, IOException {
    if (patient == null)
      patient = (Patient) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/patient-example.json"));
    return patient;
  }

  private Appointment appointment() throws FHIRFormatError, FileNotFoundException, IOException {
    if (appointment == null)
      appointment = (Appointment) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/appointment-example-request.json"));
    return appointment;
  }

  private Questionnaire questionnaire() throws FHIRFormatError, FileNotFoundException, IOException {
    if (questionnaire == null)
      questionnaire = (Questionnaire) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/questionnaire-example.json"));
    return questionnaire;
  }

  private ValueSet valueset() throws FHIRFormatError, FileNotFoundException, IOException {
    if (valueset == null)
      valueset = (ValueSet) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/valueset-example-expansion.json"));
    return valueset;
  }

  private Observation observation() throws FHIRFormatError, FileNotFoundException, IOException {
    if (observation == null)
      observation = (Observation) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/observation-example.json"));
    return observation;
  }

  @SuppressWarnings("deprecation")
  private void test(Resource resource, String expression, int count, String... types) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");
    FHIRPathEngine fp = new FHIRPathEngine(TestingUtilities.context);

    ExpressionNode node = fp.parse(expression);
    fp.check(null, resource.getResourceType().toString(), resource.getResourceType().toString(), node);
    List<Base> outcome = fp.evaluate(resource, node);
    if (fp.hasLog())
      System.out.println(fp.takeLog());

    Assert.assertTrue(String.format("Expected %d objects but found %d", count, outcome.size()), outcome.size() == count);
    CommaSeparatedStringBuilder msg = new CommaSeparatedStringBuilder();
    for (String t : types)
      msg.append(t);
    for (Base b : outcome) {
      boolean found = false;
      String type = b.fhirType();
      for (String t : types)
        if (type.equals(t)) 
          found = true;
      Assert.assertTrue(String.format("Object type %s not ok from %s", type, msg), found);
    }
  }

  @SuppressWarnings("deprecation")
  private void testBoolean(Resource resource, String expression, boolean value) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");
    FHIRPathEngine fp = new FHIRPathEngine(TestingUtilities.context);

    ExpressionNode node = fp.parse(expression);
    fp.check(null, null, resource.getResourceType().toString(), node);
    List<Base> outcome = fp.evaluate(null, null, resource, node);
    if (fp.hasLog())
      System.out.println(fp.takeLog());

    Assert.assertTrue("Wrong answer", fp.convertToBoolean(outcome) == value);
  }

  @SuppressWarnings("deprecation")
  private void testBoolean(Resource resource, Base focus, String focusType, String expression, boolean value) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");
    FHIRPathEngine fp = new FHIRPathEngine(TestingUtilities.context);

    ExpressionNode node = fp.parse(expression);
    fp.check(null, resource == null ? null : resource.getResourceType().toString(), focusType, node);
    List<Base> outcome = fp.evaluate(null, resource, focus, node);
    if (fp.hasLog())
      System.out.println(fp.takeLog());

    Assert.assertTrue("Wrong answer", fp.convertToBoolean(outcome) == value);
  }

  private void testWrong(Resource resource, String expression) throws FileNotFoundException, IOException, FHIRException {
    if (TestingUtilities.context == null)
    	TestingUtilities.context = SimpleWorkerContext.fromPack("C:\\work\\org.hl7.fhir\\build\\publish\\validation-min.xml.zip");
    FHIRPathEngine fp = new FHIRPathEngine(TestingUtilities.context);

    try {
      ExpressionNode node = fp.parse(expression);
      fp.check(null, null, resource.getResourceType().toString(), node);
      fp.evaluate(null, null, resource, node);
      if (fp.hasLog())
        System.out.println(fp.takeLog());
      Assert.assertTrue("Fail expected", false);
    } catch (PathEngineException e) {
      // ok  
    }
  }

  @Test
  public void testSimple() throws FHIRException, IOException {
    test(patient(), "name.given", 3, "string");
  }

  @Test
  public void testSimpleNone() throws FHIRException, IOException {
    test(patient(), "name.period", 0);
  }

  @Test
  public void testSimpleDoubleQuotes() throws FHIRException, IOException {
    test(patient(), "name.\"given\"", 3, "string");
  }

  @Test
  public void testSimpleFail() throws FHIRException, IOException {
    testWrong(patient(), "name.given1");
  }

  @Test
  public void testSimpleWithContext() throws FHIRException, IOException {
    test(patient(), "Patient.name.given", 3, "string");
  }

  @Test
  public void testSimpleWithWrongContext() throws FHIRException, IOException {
    testWrong(patient(), "Encounter.name.given");
  }

  @Test
  public void testPolymorphismA() throws FHIRException, IOException {
    test(observation(), "Observation.value.unit", 1, "string");
  }

  @Test
  public void testPolymorphismB() throws FHIRException, IOException {
    testWrong(observation(), "Observation.valueQuantity.unit");
  }

  @Test
  public void testPolymorphismIsA() throws FHIRException, IOException {
    testBoolean(observation(), "Observation.value.is(Quantity)", true);
    testBoolean(observation(), "Observation.value is Quantity", true);
  }

  @Test
  public void testPolymorphismIsB() throws FHIRException, IOException {
    testBoolean(observation(), "Observation.value.is(Period).not()", true);
  }

  @Test
  public void testPolymorphismAsA() throws FHIRException, IOException {
    testBoolean(observation(), "Observation.value.as(Quantity).unit", true);
    testBoolean(observation(), "(Observation.value as Quantity).unit", true);
  }

  @Test
  public void testPolymorphismAsB() throws FHIRException, IOException {
    testWrong(observation(), "(Observation.value as Period).unit");
  }

  @Test
  public void testPolymorphismAsC() throws FHIRException, IOException {
    test(observation(), "Observation.value.as(Period).start", 0);
  }

  @Test
  public void testDollarThis1() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    test(patient(), "Patient.name.given.where(substring($this.length()-3) = 'out')", 0);
  }

  @Test
  public void testDollarThis2() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    test(patient(), "Patient.name.given.where(substring($this.length()-3) = 'ter')", 1, "string");
  }

  @Test
  public void testDollarOrderAllowed() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    test(patient(), "Patient.name.skip(1).given", 1, "string");
  }

  @Test
  public void testDollarOrderAllowedA() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    test(patient(), "Patient.name.skip(3).given", 0);
  }

  @Test
  public void testDollarOrderNotAllowed() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testWrong(patient(), "Patient.children().skip(1)");
  }

  @Test
  public void testLiteralTrue() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.exists() = true", true);
  }

  @Test
  public void testLiteralFalse() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.empty() = false", true);
  }

  @Test
  public void testLiteralString() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.given.first() = 'Peter'", true);
  }

  @Test
  public void testLiteralInteger() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "-3 != 3", true);
    testBoolean(patient(), "Patient.name.given.count() = 3", true);
    testBoolean(patient(), "Patient.name.given.count() > -3", true);
    testBoolean(patient(), "Patient.name.given.count() != 0", true);
  }

  @Test
  public void testLiteralDecimal() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(observation(), "Observation.value.value > 180.0", true);
    testBoolean(observation(), "Observation.value.value > 0.0", true);
    testBoolean(observation(), "Observation.value.value > 0", true);
    testBoolean(observation(), "Observation.value.value < 190", true);
    testBoolean(observation(), "Observation.value.value < 'test'", false);
  }

  @Test
  public void testLiteralDate() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.birthDate = @1974-12-25", true);
    testBoolean(patient(), "Patient.birthDate != @1974-12-25T12:34:00", true);
    testBoolean(patient(), "Patient.birthDate != @1974-12-25T12:34:00-10:00", true);
    testBoolean(patient(), "Patient.birthDate != @1974-12-25T12:34:00+10:00", true);
    testBoolean(patient(), "Patient.birthDate != @1974-12-25T12:34:00Z", true);
    testBoolean(patient(), "Patient.birthDate != @T12:14:15", true);
    testBoolean(patient(), "Patient.birthDate != @T12:14", true);
  }


  @Test
  public void testLiteralUnicode() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.given.first() = 'P\\u0065ter'", true);
  }

  @Test
  public void testLiteralEmptyCollection() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.given != {}", true);
  }

  @Test
  public void testExpressions() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.select(given | family).distinct()", true);
    testBoolean(patient(), "Patient.name.given.count() = 1 + 2", true);
  }

  @Test
  public void testEmpty() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.empty().not()", true);
    testBoolean(patient(), "Patient.link.empty()", true);
  }

  @Test
  public void testNot() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "true.not() = false", true);
    testBoolean(patient(), "false.not() = true", true);
    testBoolean(patient(), "(0).not() = false", true);
    testBoolean(patient(), "(1).not() = false", true);
    testBoolean(patient(), "(1|2).not() = false", true);
  }

  @Test
  public void testAll() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.select(given.exists()).all()", true);
    testBoolean(patient(), "Patient.name.select(family.exists()).all()", false);
  }

  @Test
  public void testSubSetOf() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.first().subsetOf($this.name)", true);
    testBoolean(patient(), "Patient.name.subsetOf($this.name.first()).not()", true);
  }

  @Test
  public void testSuperSetOf() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.first().supersetOf($this.name).not()", true);
    testBoolean(patient(), "Patient.name.supersetOf($this.name.first())", true);
  }

  @Test
  public void testDistinct() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(1 | 2 | 3).isDistinct()", true);
    testBoolean(questionnaire(), "Questionnaire.descendents().linkId.isDistinct()", true);
    testBoolean(questionnaire(), "Questionnaire.descendents().linkId.select(substring(0,1)).isDistinct().not()", true);
    test(patient(), "(1 | 2 | 3).distinct()", 3, "integer");
    test(questionnaire(), "Questionnaire.descendents().linkId.distinct()", 9, "string");
    test(questionnaire(), "Questionnaire.descendents().linkId.select(substring(0,1)).distinct()", 2, "string");
  }

  @Test
  public void testCount() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    test(patient(), "Patient.name.count()", 1, "integer");
    testBoolean(patient(), "Patient.name.count() = 2", true);
    test(patient(), "Patient.name.first().count()", 1, "integer");
    testBoolean(patient(), "Patient.name.first().count() = 1", true);
  }

  @Test
  public void testWhere() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.count() = 2", true);
    testBoolean(patient(), "Patient.name.where(given = 'Jim').count() = 1", true);
    testBoolean(patient(), "Patient.name.where(given = 'X').count() = 0", true);
    testBoolean(patient(), "Patient.name.where($this.given = 'Jim').count() = 1", true);
  }

  @Test
  public void testSelect() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.select(given) = 'Peter' | 'James' | 'Jim'", true);
    testBoolean(patient(), "Patient.name.select(given | family) = 'Peter' | 'James' | 'Chalmers' | 'Jim'", true);
  }

  @Test
  public void testRepeat() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(valueset(), "ValueSet.expansion.repeat(contains).count() = 10", true);
    testBoolean(questionnaire(), "Questionnaire.repeat(item).concept.count() = 10", true);
    testBoolean(questionnaire(), "Questionnaire.descendents().concept.count() = 10", true);
    testBoolean(questionnaire(), "Questionnaire.children().concept.count() = 2", true);
  }

  @Test
  public void testIndexer() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name[0].given = 'Peter' | 'James'", true);
    testBoolean(patient(), "Patient.name[1].given = 'Jim'", true);
  }

  @Test
  public void testSingle() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.first().single().exists()", true);
    testWrong(patient(), "Patient.name.single().exists()");
  }

  @Test
  public void testFirstLast() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.name.first().given = 'Peter' | 'James'", true);
    testBoolean(patient(), "Patient.name.last().given = 'Jim'", true);
  }

  @Test
  public void testTail() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(0 | 1 | 2).tail() = 1 | 2", true);
    testBoolean(patient(), "Patient.name.tail().given = 'Jim'", true);
  }

  @Test
  public void testSkip() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(0 | 1 | 2).skip(1) = 1 | 2", true);
    testBoolean(patient(), "(0 | 1 | 2).skip(2) = 2", true);
    testBoolean(patient(), "Patient.name.skip(1).given = 'Jim'", true);
    testBoolean(patient(), "Patient.name.skip(2).given.exists() = false", true);
  }

  @Test
  public void testTake() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(0 | 1 | 2).take(1) = 0", true);
    testBoolean(patient(), "(0 | 1 | 2).take(2) = 0 | 1", true);
    testBoolean(patient(), "Patient.name.take(1).given = 'Peter' | 'James'", true);
    testBoolean(patient(), "Patient.name.take(2).given = 'Peter' | 'James' | 'Jim'", true);
    testBoolean(patient(), "Patient.name.take(3).given = 'Peter' | 'James' | 'Jim'", true);
    testBoolean(patient(), "Patient.name.take(0).given.exists() = false", true);
  }


  @Test
  public void testIif() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "iif(Patient.name.exists(), 'named', 'unnamed') = 'named'", true);
    testBoolean(patient(), "iif(Patient.name.empty(), 'unnamed', 'named') = 'named'", true);
  }

  @Test
  public void testToInteger() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'1'.toInteger() = 1", true);
    testBoolean(patient(), "'-1'.toInteger() = -1", true);
    testBoolean(patient(), "'0'.toInteger() = 0", true);
    testBoolean(patient(), "'0.0'.toInteger().empty()", true);
    testBoolean(patient(), "'st'.toInteger().empty()", true);
  }

  @Test
  public void testToDecimal() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'1'.toDecimal() = 1", true);
    testBoolean(patient(), "'-1'.toInteger() = -1", true);
    testBoolean(patient(), "'0'.toDecimal() = 0", true);
    testBoolean(patient(), "'0.0'.toDecimal() = 0.0", true);
    testBoolean(patient(), "'st'.toDecimal().empty()", true);
  }

  @Test
  public void testToString() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1.toString() = '1'", true);
    testBoolean(patient(), "'-1'.toInteger() = -1", true);
    testBoolean(patient(), "0.toString() = '0'", true);
    testBoolean(patient(), "0.0.toString() = '0.0'", true);
    testBoolean(patient(), "@2014-12-14.toString() = '2014-12-14'", true);
  }

  @Test
  public void testSubstring() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'12345'.substring(2) = '345'", true);
    testBoolean(patient(), "'12345'.substring(2,1) = '3'", true);
    testBoolean(patient(), "'12345'.substring(2,5) = '345'", true);
    testBoolean(patient(), "'12345'.substring(25).empty()", true);
    testBoolean(patient(), "'12345'.substring(-1).empty()", true);
  }

  @Test
  public void testStartsWith() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'12345'.startsWith('2') = false", true);
    testBoolean(patient(), "'12345'.startsWith('1') = true", true);
    testBoolean(patient(), "'12345'.startsWith('12') = true", true);
    testBoolean(patient(), "'12345'.startsWith('13') = false", true);
    testBoolean(patient(), "'12345'.startsWith('12345') = true", true);
    testBoolean(patient(), "'12345'.startsWith('123456') = false", true);
    testBoolean(patient(), "'12345'.startsWith('') = false", true);
  }

  @Test
  public void testEndsWith() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'12345'.endsWith('2') = false", true);
    testBoolean(patient(), "'12345'.endsWith('5') = true", true);
    testBoolean(patient(), "'12345'.endsWith('45') = true", true);
    testBoolean(patient(), "'12345'.endsWith('35') = false", true);
    testBoolean(patient(), "'12345'.endsWith('12345') = true", true);
    testBoolean(patient(), "'12345'.endsWith('012345') = false", true);
    testBoolean(patient(), "'12345'.endsWith('') = false", true);
  }

  @Test
  public void testContainsString() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'12345'.contains('6') = false", true);
    testBoolean(patient(), "'12345'.contains('5') = true", true);
    testBoolean(patient(), "'12345'.contains('45') = true", true);
    testBoolean(patient(), "'12345'.contains('35') = false", true);
    testBoolean(patient(), "'12345'.contains('12345') = true", true);
    testBoolean(patient(), "'12345'.contains('012345') = false", true);
    testBoolean(patient(), "'12345'.contains('') = false", true);
  }


  @Test
  public void testLength() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "'123456'.length() = 6", true);
    testBoolean(patient(), "'12345'.length() = 5", true);
    testBoolean(patient(), "'123'.length() = 3", true);
    testBoolean(patient(), "'1'.length() = 1", true);
    testBoolean(patient(), "''.length() = 0", true);
  }

  @Test
  public void testTrace() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "name.given.trace('test').count() = 3", true);
  }

  @Test
  public void testToday() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.birthDate < today()", true);
    testBoolean(patient(), "today().toString().length() = 10", true);
  }

  @Test
  public void testNow() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.birthDate < now()", true);
    testBoolean(patient(), "now().toString().length() > 10", true);
  }

  @Test
  public void testEquality() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 = 1", true);
    testBoolean(patient(), "{} = {}", true);
    testBoolean(patient(), "1 = 2", false);
    testBoolean(patient(), "'a' = 'a'", true);
    testBoolean(patient(), "'a' = 'A'", false);
    testBoolean(patient(), "'a' = 'b'", false);
    testBoolean(patient(), "1.1 = 1.1", true);
    testBoolean(patient(), "1.1 = 1.2", false);
    testBoolean(patient(), "1.10 = 1.1", false);
    testBoolean(patient(), "0 = 0", true);
    testBoolean(patient(), "0.0 = 0", false);
    testBoolean(patient(), "@2012-04-15 = @2012-04-15", true);
    testBoolean(patient(), "@2012-04-15 = @2012-04-16", false);
    testBoolean(patient(), "@2012-04-15 = @2012-04-15T10:00:00", false);
    testBoolean(patient(), "name = name", true);
    testBoolean(patient(), "name = name.first() | name.last()", true);
    testBoolean(patient(), "name = name.last() | name.first()", false);
  }

  @Test
  public void testNEquality() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 != 1", false);
    testBoolean(patient(), "{} != {}", false);
    testBoolean(patient(), "1 != 2", true);
    testBoolean(patient(), "'a' != 'a'", false);
    testBoolean(patient(), "'a' != 'b'", true);
    testBoolean(patient(), "1.1 != 1.1", false);
    testBoolean(patient(), "1.1 != 1.2", true);
    testBoolean(patient(), "1.10 != 1.1", true);
    testBoolean(patient(), "0 != 0", false);
    testBoolean(patient(), "0.0 != 0", true);
    testBoolean(patient(), "@2012-04-15 != @2012-04-15", false);
    testBoolean(patient(), "@2012-04-15 != @2012-04-16", true);
    testBoolean(patient(), "@2012-04-15 != @2012-04-15T10:00:00", true);
    testBoolean(patient(), "name != name", false);
    testBoolean(patient(), "name != name.first() | name.last()", false);
    testBoolean(patient(), "name != name.last() | name.first()", true);
  }

  @Test
  public void testEquivalent() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 ~ 1", true);
    testBoolean(patient(), "{} ~ {}", true);
    testBoolean(patient(), "1 ~ 2", false);
    testBoolean(patient(), "'a' ~ 'a'", true);
    testBoolean(patient(), "'a' ~ 'A'", true);
    testBoolean(patient(), "'a' ~ 'b'", false);
    testBoolean(patient(), "1.1 ~ 1.1", true);
    testBoolean(patient(), "1.1 ~ 1.2", false);
    testBoolean(patient(), "1.10 ~ 1.1", true);
    testBoolean(patient(), "0 ~ 0", true);
    testBoolean(patient(), "0.0 ~ 0", true);
    testBoolean(patient(), "@2012-04-15 ~ @2012-04-15", true);
    testBoolean(patient(), "@2012-04-15 ~ @2012-04-16", false);
    testBoolean(patient(), "@2012-04-15 ~ @2012-04-15T10:00:00", true);
    //    testBoolean(patient(), "name ~ name", true);
    testBoolean(patient(), "name.given ~ name.first().given | name.last().given", true);
    testBoolean(patient(), "name.given ~ name.last().given | name.first().given", true);
  }

  @Test
  public void testNotEquivalent() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 !~ 1", false);
    testBoolean(patient(), "{} !~ {}", false);
    testBoolean(patient(), "1 !~ 2", true);
    testBoolean(patient(), "'a' !~ 'a'", false);
    testBoolean(patient(), "'a' !~ 'A'", false);
    testBoolean(patient(), "'a' !~ 'b'", true);
    testBoolean(patient(), "1.1 !~ 1.1", false);
    testBoolean(patient(), "1.1 !~ 1.2", true);
    testBoolean(patient(), "1.10 !~ 1.1", false);
    testBoolean(patient(), "0 !~ 0", false);
    testBoolean(patient(), "0.0 !~ 0", false);
    testBoolean(patient(), "@2012-04-15 !~ @2012-04-15", false);
    testBoolean(patient(), "@2012-04-15 !~ @2012-04-16", true);
    testBoolean(patient(), "@2012-04-15 !~ @2012-04-15T10:00:00", false);
    //    testBoolean(patient(), "name !~ name", true);
    testBoolean(patient(), "name.given !~ name.first().given | name.last().given", false);
    testBoolean(patient(), "name.given !~ name.last().given | name.first().given", false);
  }

  @Test
  public void testLessThan() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 < 2", true);
    testBoolean(patient(), "1.0 < 1.2", true);
    testBoolean(patient(), "'a' < 'b'", true);
    testBoolean(patient(), "'A' < 'a'", true);
    testBoolean(patient(), "@2014-12-12 < @2014-12-13", true);
    testBoolean(patient(), "@2014-12-13T12:00:00 < @2014-12-13T12:00:01", true);
    testBoolean(patient(), "@T12:00:00 < @T14:00:00", true);

    testBoolean(patient(), "1 < 1", false);
    testBoolean(patient(), "1.0 < 1.0", false);
    testBoolean(patient(), "'a' < 'a'", false);
    testBoolean(patient(), "'A' < 'A'", false);
    testBoolean(patient(), "@2014-12-12 < @2014-12-12", false);
    testBoolean(patient(), "@2014-12-13T12:00:00 < @2014-12-13T12:00:00", false);
    testBoolean(patient(), "@T12:00:00 < @T12:00:00", false);

    testBoolean(patient(), "2 < 1", false);
    testBoolean(patient(), "1.1 < 1.0", false);
    testBoolean(patient(), "'b' < 'a'", false);
    testBoolean(patient(), "'B' < 'A'", false);
    testBoolean(patient(), "@2014-12-13 < @2014-12-12", false);
    testBoolean(patient(), "@2014-12-13T12:00:01 < @2014-12-13T12:00:00", false);
    testBoolean(patient(), "@T12:00:01 < @T12:00:00", false);
  }

  @Test
  public void testLessOrEqual() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 <= 2", true);
    testBoolean(patient(), "1.0 <= 1.2", true);
    testBoolean(patient(), "'a' <= 'b'", true);
    testBoolean(patient(), "'A' <= 'a'", true);
    testBoolean(patient(), "@2014-12-12 <= @2014-12-13", true);
    testBoolean(patient(), "@2014-12-13T12:00:00 <= @2014-12-13T12:00:01", true);
    testBoolean(patient(), "@T12:00:00 <= @T14:00:00", true);

    testBoolean(patient(), "1 <= 1", true);
    testBoolean(patient(), "1.0 <= 1.0", true);
    testBoolean(patient(), "'a' <= 'a'", true);
    testBoolean(patient(), "'A' <= 'A'", true);
    testBoolean(patient(), "@2014-12-12 <= @2014-12-12", true);
    testBoolean(patient(), "@2014-12-13T12:00:00 <= @2014-12-13T12:00:00", true);
    testBoolean(patient(), "@T12:00:00 <= @T12:00:00", true);

    testBoolean(patient(), "2 <= 1", false);
    testBoolean(patient(), "1.1 <= 1.0", false);
    testBoolean(patient(), "'b' <= 'a'", false);
    testBoolean(patient(), "'B' <= 'A'", false);
    testBoolean(patient(), "@2014-12-13 <= @2014-12-12", false);
    testBoolean(patient(), "@2014-12-13T12:00:01 <= @2014-12-13T12:00:00", false);
    testBoolean(patient(), "@T12:00:01 <= @T12:00:00", false);
  }

  @Test
  public void testGreatorOrEqual() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 >= 2", false);
    testBoolean(patient(), "1.0 >= 1.2", false);
    testBoolean(patient(), "'a' >= 'b'", false);
    testBoolean(patient(), "'A' >= 'a'", false);
    testBoolean(patient(), "@2014-12-12 >= @2014-12-13", false);
    testBoolean(patient(), "@2014-12-13T12:00:00 >= @2014-12-13T12:00:01", false);
    testBoolean(patient(), "@T12:00:00 >= @T14:00:00", false);

    testBoolean(patient(), "1 >= 1", true);
    testBoolean(patient(), "1.0 >= 1.0", true);
    testBoolean(patient(), "'a' >= 'a'", true);
    testBoolean(patient(), "'A' >= 'A'", true);
    testBoolean(patient(), "@2014-12-12 >= @2014-12-12", true);
    testBoolean(patient(), "@2014-12-13T12:00:00 >= @2014-12-13T12:00:00", true);
    testBoolean(patient(), "@T12:00:00 >= @T12:00:00", true);

    testBoolean(patient(), "2 >= 1", true);
    testBoolean(patient(), "1.1 >= 1.0", true);
    testBoolean(patient(), "'b' >= 'a'", true);
    testBoolean(patient(), "'B' >= 'A'", true);
    testBoolean(patient(), "@2014-12-13 >= @2014-12-12", true);
    testBoolean(patient(), "@2014-12-13T12:00:01 >= @2014-12-13T12:00:00", true);
    testBoolean(patient(), "@T12:00:01 >= @T12:00:00", true);
  }

  @Test
  public void testGreatorThan() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 > 2", false);
    testBoolean(patient(), "1.0 > 1.2", false);
    testBoolean(patient(), "'a' > 'b'", false);
    testBoolean(patient(), "'A' > 'a'", false);
    testBoolean(patient(), "@2014-12-12 > @2014-12-13", false);
    testBoolean(patient(), "@2014-12-13T12:00:00 > @2014-12-13T12:00:01", false);
    testBoolean(patient(), "@T12:00:00 > @T14:00:00", false);

    testBoolean(patient(), "1 > 1", false);
    testBoolean(patient(), "1.0 > 1.0", false);
    testBoolean(patient(), "'a' > 'a'", false);
    testBoolean(patient(), "'A' > 'A'", false);
    testBoolean(patient(), "@2014-12-12 > @2014-12-12", false);
    testBoolean(patient(), "@2014-12-13T12:00:00 > @2014-12-13T12:00:00", false);
    testBoolean(patient(), "@T12:00:00 > @T12:00:00", false);

    testBoolean(patient(), "2 > 1", true);
    testBoolean(patient(), "1.1 > 1.0", true);
    testBoolean(patient(), "'b' > 'a'", true);
    testBoolean(patient(), "'B' > 'A'", true);
    testBoolean(patient(), "@2014-12-13 > @2014-12-12", true);
    testBoolean(patient(), "@2014-12-13T12:00:01 > @2014-12-13T12:00:00", true);
    testBoolean(patient(), "@T12:00:01 > @T12:00:00", true);
  }

  @Test
  public void testUnion() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(1 | 2 | 3).count() = 3", true);
    testBoolean(patient(), "(1 | 2 | 2).count() = 2", true); // merge duplicates
  }
  
  @Test
  public void testIn() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 in (1 | 2 | 3)", true);
    testBoolean(patient(), "1 in (2 | 3)", false);
    testBoolean(patient(), "'a' in ('a' | 'c' | 'd')", true);
    testBoolean(patient(), "'b' in ('a' | 'c' | 'd')", false);
  }

  @Test
  public void testContainsCollection() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(1 | 2 | 3) contains 1", true);
    testBoolean(patient(), "(2 | 3) contains 1 ", false);
    testBoolean(patient(), "('a' | 'c' | 'd') contains 'a'", true);
    testBoolean(patient(), "('a' | 'c' | 'd') contains 'b'", false);
  }

  @Test
  public void testBooleanLogicAnd() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(true and true) = true", true);
    testBoolean(patient(), "(true and false) = false", true);
    testBoolean(patient(), "(true and {}) = {}", true);
    
    testBoolean(patient(), "(false and true) = false", true);
    testBoolean(patient(), "(false and false) = false", true);
    testBoolean(patient(), "(false and {}) = false", true);

    testBoolean(patient(), "({} and true) = {}", true);
    testBoolean(patient(), "({} and false) = false", true);
    testBoolean(patient(), "({} and {}) = {}", true);
  }

  @Test
  public void testBooleanLogicOr() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(true or true) = true", true);
    testBoolean(patient(), "(true or false) = true", true);
    testBoolean(patient(), "(true or {}) = true", true);
    
    testBoolean(patient(), "(false or true) = true", true);
    testBoolean(patient(), "(false or false) = false", true);
    testBoolean(patient(), "(false or {}) = {}", true);

    testBoolean(patient(), "({} or true) = true", true);
    testBoolean(patient(), "({} or false) = {}", true);
    testBoolean(patient(), "({} or {}) = {}", true);
  }

  @Test
  public void testBooleanLogicXOr() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(true xor true) = false", true);
    testBoolean(patient(), "(true xor false) = true", true);
    testBoolean(patient(), "(true xor {}) = {}", true);
    
    testBoolean(patient(), "(false xor true) = true", true);
    testBoolean(patient(), "(false xor false) = false", true);
    testBoolean(patient(), "(false xor {}) = {}", true);

    testBoolean(patient(), "({} xor true) = {}", true);
    testBoolean(patient(), "({} xor false) = {}", true);
    testBoolean(patient(), "({} xor {}) = {}", true);
  }
  
  @Test
  public void testBooleanImplies() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "(true implies true) = true", true);
    testBoolean(patient(), "(true implies false) = false", true);
    testBoolean(patient(), "(true implies {}) = {}", true);
    
    testBoolean(patient(), "(false implies true) = true", true);
    testBoolean(patient(), "(false implies false) = true", true);
    testBoolean(patient(), "(false implies {}) = true", true);

    testBoolean(patient(), "({} implies true) = true", true);
    testBoolean(patient(), "({} implies false) = true", true);
    testBoolean(patient(), "({} implies {}) = true", true);
  }
  
  @Test
  public void testPlus() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 + 1 = 2", true);
    testBoolean(patient(), "1 + 0 = 1", true);
    testBoolean(patient(), "1.2 + 1.8 = 3.0", true);    
    testBoolean(patient(), "'a'+'b' = 'ab'", true);
  }

  @Test
  public void testConcatenate() throws FileNotFoundException, IOException, FHIRException {
    testBoolean(patient(), "1 & 1 = '11'", true);
    testBoolean(patient(), "1 & 'a' = '1a'", true);
    testBoolean(patient(), "{} & 'b' = 'b'", true);    
    testBoolean(patient(), "(1 | 2 | 3) & 'b' = '1,2,3b'", true);    
    testBoolean(patient(), "'a'&'b' = 'ab'", true);
  }
  

  @Test
  public void testMinus() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 - 1 = 0", true);
    testBoolean(patient(), "1 - 0 = 1", true);
    testBoolean(patient(), "1.8 - 1.2 = 0.6", true);    
    testWrong(patient(), "'a'-'b' = 'ab'");
  }
  
  @Test
  public void testMultiply() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 * 1 = 1", true);
    testBoolean(patient(), "1 * 0 = 0", true);
    testBoolean(patient(), "1.2 * 1.8 = 2.16", true);    
  }
  
  @Test
  public void testDivide() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 / 1 = 1", true);
    testBoolean(patient(), "4 / 2 = 2", true);
    testBoolean(patient(), "1 / 2 = 0.5", true);
    testBoolean(patient(), "1.2 / 1.8 = 0.67", true);    
  }
  
  @Test
  public void testDiv() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 div 1 = 1", true);
    testBoolean(patient(), "4 div 2 = 2", true);
    testBoolean(patient(), "5 div 2 = 2", true);
    testBoolean(patient(), "2.2 div 1.8 = 1", true);    
  }
  
  @Test
  public void testMod() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1 mod 1 = 0", true);
    testBoolean(patient(), "4 mod 2 = 0", true);
    testBoolean(patient(), "5 mod 2 = 1", true);
    testBoolean(patient(), "2.2 mod 1.8 = 0.4", true);    
  }
  
  @Test
  public void testPrecedence() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "1+2*3+4 = 11", true);
  }
  
  @Test
  public void testVariables() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "%sct = 'http://snomed.info/sct'", true);
    testBoolean(patient(), "%loinc = 'http://loinc.org'", true);
    testBoolean(patient(), "%ucum = 'http://unitsofmeasure.org'", true);
    testBoolean(patient(), "%\"vs-administrative-gender\" = 'http://hl7.org/fhir/ValueSet/administrative-gender'", true);
  }
  
  @Test
  public void testExtension() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), "Patient.birthDate.extension('http://hl7.org/fhir/StructureDefinition/patient-birthTime').exists()", true);
    testBoolean(patient(), "Patient.birthDate.extension(%\"ext-patient-birthTime\").exists()", true);
    testBoolean(patient(), "Patient.birthDate.extension('http://hl7.org/fhir/StructureDefinition/patient-birthTime1').empty()", true);
  }
  
  @Test
  public void testDollarResource() throws FileNotFoundException, FHIRFormatError, IOException, FHIRException {
    testBoolean(patient(), patient().getManagingOrganization(), "Reference", "reference.startsWith('#').not() or (reference.substring(1).trace('url') in %resource.contained.id.trace('ids'))", true);
    testBoolean(patient(), patient(), "Patient", "contained.select(('#'+id in %resource.descendents().reference).not()).empty()", true);
    testWrong(patient(), "contained.select(('#'+id in %resource.descendents().reference).not()).empty()");
  }
  
  @Test
  public void testTyping() throws FileNotFoundException, IOException, FHIRException {
    ElementDefinition ed = new ElementDefinition();
    ed.getBinding().setValueSet(new UriType("http://test.org"));
    testBoolean(null, ed.getBinding().getValueSet(), "ElementDefinition.binding.valueSetUri", "startsWith('http:') or startsWith('https') or startsWith('urn:')", true);
  }
  
  @Test
  public void testDecimalRA() throws FileNotFoundException, IOException, FHIRException {
    RiskAssessment r = new RiskAssessment();
    SimpleQuantity sq = new SimpleQuantity();
    sq.setValue(0.2);
    sq.setUnit("%");
    sq.setCode("%");
    sq.setSystem("http://unitsofmeasure.org");
    SimpleQuantity sq1 = new SimpleQuantity();
    sq1.setValue(0.4);
    sq1.setUnit("%");
    sq1.setCode("%");
    sq1.setSystem("http://unitsofmeasure.org");
    r.addPrediction().setProbability(new Range().setLow(sq).setHigh(sq1));
    testBoolean(r, r.getPrediction().get(0).getProbability(), "RiskAssessment.prediction.probabilityRange", 
        "(low.empty() or ((low.code = '%') and (low.system = %ucum))) and (high.empty() or ((high.code = '%') and (high.system = %ucum)))", true);
    testBoolean(r, r.getPrediction().get(0), "RiskAssessment.prediction", "probability is decimal implies probability.as(decimal) <= 100", true);
    r.getPrediction().get(0).setProbability(new DecimalType(80));
    testBoolean(r, r.getPrediction().get(0), "RiskAssessment.prediction", "probability.as(decimal) <= 100", true);
  }
  
  
  @Test
  public void testAppointment() throws FileNotFoundException, IOException, FHIRException {
    testBoolean(appointment(), "(start and end) or status = 'proposed' or status = 'cancelled'", true);
    testBoolean(appointment(), "start.empty() xor end.exists()", true);
  }
  
  @Test
  public void testQuestionnaire() throws FileNotFoundException, IOException, FHIRException {
    Questionnaire q = (Questionnaire) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/questionnaire-example-gcs.json"));
    for (QuestionnaireItemComponent qi : q.getItem()) {
      testQItem(qi);
    }
  }

  private void testQItem(QuestionnaireItemComponent qi) throws FileNotFoundException, IOException, FHIRException {
    testBoolean(null, qi, "Questionnaire.item", "(type = 'choice' or type = 'open-choice') or (options.empty() and option.empty())", true);
  }
   
  @Test
  public void testExtensionDefinitions() throws FileNotFoundException, IOException, FHIRException {
    Bundle b = (Bundle) new JsonParser().parse(new FileInputStream("C:/work/org.hl7.fhir.2016May/build/publish/extension-definitions.json"));
    for (BundleEntryComponent be : b.getEntry()) {
      testStructureDefinition((StructureDefinition) be.getResource());
    }
  }

  private void testStructureDefinition(StructureDefinition sd) throws FileNotFoundException, IOException, FHIRException {
    testBoolean(sd, sd, "StructureDefinition", "snapshot.element.tail().all(path.startsWith(%resource.snapshot.element.first().path&'.')) and differential.element.tail().all(path.startsWith(%resource.differential.element.first().path&'.'))", true); 
  }

  @Test
  public void testDoubleEntryPoint() throws FileNotFoundException, IOException, FHIRException {
    testBoolean(patient(), "(Patient.name | Patient.address).count() = 3", true);
  }

}

