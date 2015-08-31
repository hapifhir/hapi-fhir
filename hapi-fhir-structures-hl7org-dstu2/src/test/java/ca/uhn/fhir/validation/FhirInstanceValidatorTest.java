package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.CodeType;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;

public class FhirInstanceValidatorTest {

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorTest.class);
  private DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
  private FhirInstanceValidator myInstanceVal;
  private IValidationSupport myMockSupport;

  private FhirValidator myVal;
  private ArrayList<String> myValidConcepts;

  private void addValidConcept(String theSystem, String theCode) {
    myValidConcepts.add(theSystem + "___" + theCode);
  }

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    myVal = ourCtx.newValidator();
    myVal.setValidateAgainstStandardSchema(false);
    myVal.setValidateAgainstStandardSchematron(false);

    myInstanceVal = new FhirInstanceValidator();
    myVal.registerValidatorModule(myInstanceVal);

    myValidConcepts = new ArrayList<String>();

    myMockSupport = mock(IValidationSupport.class);
    when(myMockSupport.isCodeSystemSupported(any(String.class))).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
        boolean retVal = myDefaultValidationSupport.isCodeSystemSupported((String) theInvocation.getArguments()[0]);
        ourLog.info("isCodeSystemSupported({}) : {}", new Object[] { theInvocation.getArguments()[0], retVal });
        return retVal;
      }
    });
    when(myMockSupport.fetchResource(any(FhirContext.class), any(Class.class), any(String.class)))
        .thenAnswer(new Answer<IBaseResource>() {
          @Override
          public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
            IBaseResource retVal = myDefaultValidationSupport.fetchResource(
                (FhirContext) theInvocation.getArguments()[0], (Class<IBaseResource>) theInvocation.getArguments()[1],
                (String) theInvocation.getArguments()[2]);
            ourLog.info("fetchResource({}, {}) : {}",
                new Object[] { theInvocation.getArguments()[1], theInvocation.getArguments()[2], retVal });
            return retVal;
          }
        });
    when(myMockSupport.validateCode(any(String.class), any(String.class), any(String.class)))
        .thenAnswer(new Answer<org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult>() {
          @Override
          public org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult answer(InvocationOnMock theInvocation)
              throws Throwable {
            String system = (String) theInvocation.getArguments()[0];
            String code = (String) theInvocation.getArguments()[1];
            org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult retVal;
            if (myValidConcepts.contains(system + "___" + code)) {
              retVal = new org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult(
                  new ConceptDefinitionComponent(new CodeType(code)));
            } else {
              retVal = myDefaultValidationSupport.validateCode(system, code, (String) theInvocation.getArguments()[2]);
            }
            ourLog.info("validateCode({}, {}, {}) : {}",
                new Object[] { system, code, (String) theInvocation.getArguments()[2], retVal });
            return retVal;
          }
        });
    when(myMockSupport.fetchCodeSystem(any(String.class))).thenAnswer(new Answer<ValueSet>() {
      @Override
      public ValueSet answer(InvocationOnMock theInvocation) throws Throwable {
        ValueSet retVal = myDefaultValidationSupport.fetchCodeSystem((String) theInvocation.getArguments()[0]);
        ourLog.info("fetchCodeSystem({}) : {}", new Object[] { (String) theInvocation.getArguments()[0], retVal });
        return retVal;
      }
    });

  }

  private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
    List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

    int index = 0;
    for (SingleValidationMessage next : theOutput.getMessages()) {
      ourLog.info("Result {}: {} - {} - {}",
          new Object[] { index, next.getSeverity(), next.getLocationString(), next.getMessage() });
      index++;

      if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
        retVal.add(next);
      }
    }

    return retVal;
  }

  @Test
  public void testValidateJsonResource() {
    // @formatter:off
    String input = "{" + "\"resourceType\":\"Patient\"," + "\"id\":\"123\"" + "}";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 0, output.getMessages().size());
  }

  @Test
  public void testValidateJsonResourceBadAttributes() {
    // @formatter:off
    String input = "{" + "\"resourceType\":\"Patient\"," + "\"id\":\"123\"," + "\"foo\":\"123\"" + "}";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 1, output.getMessages().size());
    ourLog.info(output.getMessages().get(0).getLocationString());
    ourLog.info(output.getMessages().get(0).getMessage());
    assertEquals("/foo", output.getMessages().get(0).getLocationString());
    assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
  }

  @Test
  public void testValidateResourceFailingInvariant() {
    Observation input = new Observation();

    // Has a value, but not a status (which is required)
    input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
    input.setValue(new StringType("AAA"));

    ValidationResult output = myVal.validateWithResult(input);
    assertThat(output.getMessages().size(), greaterThan(0));
    assertEquals("Element '/f:Observation.status': minimum required = 1, but only found 0",
        output.getMessages().get(0).getMessage());

  }

  @Test
  public void testValidateResourceWithExampleBindingCodeValidationFailing() {
    Observation input = new Observation();

    myInstanceVal.setValidationSupport(myMockSupport);

    input.setStatus(ObservationStatus.FINAL);
    input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(errors.toString(), 1, errors.size());
    assertEquals("Unable to validate code \"12345\" in code system \"http://loinc.org\"", errors.get(0).getMessage());

  }

  @Test
  public void testValidateResourceWithExampleBindingCodeValidationPassing() {
    Observation input = new Observation();

    myInstanceVal.setValidationSupport(myMockSupport);
    addValidConcept("http://loinc.org", "12345");

    input.setStatus(ObservationStatus.FINAL);
    input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(errors.toString(), 0, errors.size());
  }

  @Test
  public void testValidateResourceWithDefaultValueset() {
    Observation input = new Observation();

    input.setStatus(ObservationStatus.FINAL);
    input.getCode().setText("No code here!");

    ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.getMessages().size(), 0);
  }

  @Test
  public void testValidateResourceWithDefaultValuesetBadCode() {
    String input = "<Observation xmlns=\"http://hl7.org/fhir\">\n" + "   <status value=\"notvalidcode\"/>\n"
        + "   <code>\n" + "      <text value=\"No code here!\"/>\n" + "   </code>\n" + "</Observation>";
    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(
        "Coded value notvalidcode is not in value set http://hl7.org/fhir/ValueSet/observation-status (http://hl7.org/fhir/ValueSet/observation-status)",
        output.getMessages().get(0).getMessage());
  }

  @Test
  public void testValidateXmlResource() {
    // @formatter:off
    String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "</Patient>";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 0, output.getMessages().size());
  }

  @Test
  public void testValidateXmlResourceBadAttributes() {
    // @formatter:off
    String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "<foo value=\"222\"/>"
        + "</Patient>";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 1, output.getMessages().size());
    ourLog.info(output.getMessages().get(0).getLocationString());
    ourLog.info(output.getMessages().get(0).getMessage());
    assertEquals("/f:Patient/f:foo", output.getMessages().get(0).getLocationString());
    assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
  }
}
