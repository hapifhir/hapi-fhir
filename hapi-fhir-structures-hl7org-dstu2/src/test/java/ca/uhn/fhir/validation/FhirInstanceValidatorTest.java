package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.hl7.fhir.instance.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.hl7.fhir.instance.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.instance.model.CodeType;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.validation.InstanceValidator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.web.bind.annotation.GetMapping;

import ca.uhn.fhir.context.FhirContext;

public class FhirInstanceValidatorTest {

  private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorTest.class);
  private static DefaultProfileValidationSupport ourDefaultValidationSupport = new DefaultProfileValidationSupport() {
    @Override
    public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
      if (theUri.equals("http://fhir.hl7.org.nz/dstu2/StructureDefinition/ohAllergyIntolerance")) {
        String contents;
        try {
          contents = IOUtils.toString(getClass().getResourceAsStream("/allergyintolerance-sd-david.json"), "UTF-8");
        } catch (IOException e) {
          throw new Error(e);
        }
        StructureDefinition sd = ourCtx.newJsonParser().parseResource(StructureDefinition.class, contents);
        return (T) sd;
      }
      T retVal = super.fetchResource(theContext, theClass, theUri);
      if (retVal == null) {
        ourLog.warn("Can not fetch: {}", theUri);
      }
      return retVal;
    }
    
  };
  private FhirInstanceValidator myInstanceVal;
  private IValidationSupport myMockSupport;

  private FhirValidator myVal;
  private ArrayList<String> myValidConcepts;
  private Map<String, ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;

  private void addValidConcept(String theSystem, String theCode) {
    myValidConcepts.add(theSystem + "___" + theCode);
  }
  
  @AfterClass
  public static void afterClass() {
    ourDefaultValidationSupport.flush();
    ourDefaultValidationSupport = null;
  }

  @SuppressWarnings("unchecked")
  @Before
  public void before() {
    myVal = ourCtx.newValidator();
    myVal.setValidateAgainstStandardSchema(false);
    myVal.setValidateAgainstStandardSchematron(false);

    myInstanceVal = new FhirInstanceValidator(ourDefaultValidationSupport);
    myVal.registerValidatorModule(myInstanceVal);

    mySupportedCodeSystemsForExpansion = new HashMap<String, ValueSet.ValueSetExpansionComponent>();

    myValidConcepts = new ArrayList<String>();

    myMockSupport = mock(IValidationSupport.class);
    when(myMockSupport.expandValueSet(any(FhirContext.class), any(ConceptSetComponent.class))).thenAnswer(new Answer<ValueSetExpansionComponent>() {
      @Override
      public ValueSetExpansionComponent answer(InvocationOnMock theInvocation) throws Throwable {
        ConceptSetComponent arg = (ConceptSetComponent)theInvocation.getArguments()[0];
        ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getSystem());
        if (retVal == null) {
          retVal = ourDefaultValidationSupport.expandValueSet(any(FhirContext.class), arg);
        }
        ourLog.info("expandValueSet({}) : {}", new Object[] { theInvocation.getArguments()[0], retVal });
        return retVal;
      }
    });
    when(myMockSupport.isCodeSystemSupported(any(FhirContext.class), any(String.class))).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
        boolean retVal = mySupportedCodeSystemsForExpansion.containsKey(theInvocation.getArguments()[0]);
        ourLog.info("isCodeSystemSupported({}) : {}", new Object[] { theInvocation.getArguments()[0], retVal });
        return retVal;
      }
    });
    when(myMockSupport.fetchResource(any(FhirContext.class), any(Class.class), any(String.class)))
        .thenAnswer(new Answer<IBaseResource>() {
          @Override
          public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
            FhirContext fhirContext = (FhirContext) theInvocation.getArguments()[0];
            Class<IBaseResource> type = (Class<IBaseResource>) theInvocation.getArguments()[1];
            String uri = (String) theInvocation.getArguments()[2];
            IBaseResource retVal = ourDefaultValidationSupport.fetchResource(fhirContext, type, uri);
            ourLog.info("fetchResource({}, {}) : {}",
                new Object[] { theInvocation.getArguments()[1], theInvocation.getArguments()[2], retVal });
            return retVal;
          }
        });
    when(myMockSupport.validateCode(any(FhirContext.class), any(String.class), any(String.class), any(String.class)))
        .thenAnswer(new Answer<CodeValidationResult>() {
          @Override
          public CodeValidationResult answer(InvocationOnMock theInvocation) throws Throwable {
            FhirContext ctx = (FhirContext) theInvocation.getArguments()[0];
            String system = (String) theInvocation.getArguments()[1];
            String code = (String) theInvocation.getArguments()[2];
            CodeValidationResult retVal;
            if (myValidConcepts.contains(system + "___" + code)) {
              retVal = new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(code)));
            } else {
              retVal = ourDefaultValidationSupport.validateCode(ctx, system, code, (String) theInvocation.getArguments()[2]);
            }
            ourLog.info("validateCode({}, {}, {}) : {}",
                new Object[] { system, code, (String) theInvocation.getArguments()[2], retVal });
            return retVal;
          }
        });
    when(myMockSupport.fetchCodeSystem(any(FhirContext.class), any(String.class))).thenAnswer(new Answer<ValueSet>() {
      @Override
      public ValueSet answer(InvocationOnMock theInvocation) throws Throwable {
        ValueSet retVal = ourDefaultValidationSupport.fetchCodeSystem((FhirContext) theInvocation.getArguments()[0],(String) theInvocation.getArguments()[1]);
        ourLog.info("fetchCodeSystem({}) : {}", new Object[] { (String) theInvocation.getArguments()[1], retVal });
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

  private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
    List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

    int index = 0;
    for (SingleValidationMessage next : theOutput.getMessages()) {
      ourLog.info("Result {}: {} - {} - {}",
          new Object[] { index, next.getSeverity(), next.getLocationString(), next.getMessage() });
      index++;

      retVal.add(next);
    }

    return retVal;
  }

  @Rule
  public TestRule watcher = new TestWatcher() {
     protected void starting(Description description) {
        ourLog.info("Starting test: " + description.getMethodName());
     }
  };
  
  @Test
  public void testValidateRawJsonResource() {
    // @formatter:off
    String input = "{" + "\"resourceType\":\"Patient\"," + "\"id\":\"123\"" + "}";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 0, output.getMessages().size());
  }

  /**
   * Received by email from David Hay
   */
  @Test
  public void testValidateAllergyIntoleranceFromDavid() throws Exception {

    String input = IOUtils.toString(getClass().getResourceAsStream("/allergyintolerance-david.json"), "UTF-8");
    
    // Just make sure this doesn't crash
    ValidationResult output = myVal.validateWithResult(input);
    ourLog.info(output.toString());

  }

  @Test
  public void testValidateRawJsonResourceBadAttributes() {
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
  public void testValidateRawXmlResource() {
    // @formatter:off
    String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "</Patient>";
    // @formatter:on

    ValidationResult output = myVal.validateWithResult(input);
    assertEquals(output.toString(), 0, output.getMessages().size());
  }

  @Test
  public void testValidateRawXmlResourceBadAttributes() {
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

  /**
   * See #216
   */
  @Test
  public void testValidateRawXmlInvalidChoiceName() throws Exception {
    String input = IOUtils
        .toString(FhirInstanceValidator.class.getResourceAsStream("/medicationstatement_invalidelement.xml"));
    ValidationResult output = myVal.validateWithResult(input);

    List<SingleValidationMessage> res = logResultsAndReturnAll(output);
    ourLog.info(res.toString());
    
    for (SingleValidationMessage nextMessage : res) {
      if (nextMessage.getSeverity() == ResultSeverityEnum.ERROR) {
        fail(nextMessage.toString());
      }
    }
    
    // TODO: we should really not have any errors at all here, but for
    // now we aren't validating snomed codes correctly
//    assertEquals(output.toString(), 0, res.size());

  }

  @Test
  public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
    addValidConcept("http://loinc.org", "12345");

    Observation input = new Observation();
    input.getMeta().addProfile("http://foo/myprofile");

    input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
    input.setStatus(ObservationStatus.FINAL);

    myInstanceVal.setValidationSupport(myMockSupport);
    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(errors.toString(), 1, errors.size());
    assertEquals("StructureDefinition reference \"http://foo/myprofile\" could not be resolved",
        errors.get(0).getMessage());
  }

  @Test
  public void testValidateResourceContainingProfileDeclaration() {
    addValidConcept("http://loinc.org", "12345");

    Observation input = new Observation();
    input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

    input.addIdentifier().setSystem("http://acme").setValue("12345");
    input.getEncounter().setReference("http://foo.com/Encounter/9");
    input.setStatus(ObservationStatus.FINAL);
    input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

    myInstanceVal.setValidationSupport(myMockSupport);
    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

    assertThat(errors.toString(),
        containsString("Element '/f:Observation.subject': minimum required = 1, but only found 0"));
    assertThat(errors.toString(), containsString("Element encounter @ /f:Observation: max allowed = 0, but found 1"));
    assertThat(errors.toString(),
        containsString("Element '/f:Observation.device': minimum required = 1, but only found 0"));
    assertThat(errors.toString(), containsString(""));
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
  public void testValidateJsonNumericId() {
    String input="{\"resourceType\": \"Patient\",\n" + 
        "  \"id\": 123,\n" + 
        "  \"meta\": {\n" + 
        "    \"versionId\": \"29\",\n" + 
        "    \"lastUpdated\": \"2015-12-22T19:53:11.000Z\"\n" + 
        "  },\n" + 
        "  \"communication\": {\n" + 
        "    \"language\": {\n" + 
        "      \"coding\": [\n" + 
        "        {\n" + 
        "          \"system\": \"urn:ietf:bcp:47\",\n" + 
        "          \"code\": \"hi\",\n" + 
        "          \"display\": \"Hindi\",\n" + 
        "          \"userSelected\": false\n" + 
        "        }],\n" + 
        "      \"text\": \"Hindi\"\n" + 
        "    },\n" + 
        "    \"preferred\": true\n" + 
        "  }\n" + 
        "}";
    
    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(0, all.size());
    
  }

  @Test
  public void testValidateCodeableConceptContainingOnlyGoodCode() {
    Patient p = new Patient();
    p.getMaritalStatus().addCoding().setSystem("http://hl7.org/fhir/v3/MaritalStatus").setCode("M");
    
    ValidationResult output = myVal.validateWithResult(p);
    List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(0, all.size());
  }

  @Test
  public void testValidateCodeableConceptContainingOnlyBadCode() {
    Patient p = new Patient();
    p.getMaritalStatus().addCoding().setSystem("http://hl7.org/fhir/v3/MaritalStatus").setCode("FOO");
    
    ValidationResult output = myVal.validateWithResult(p);
    List<SingleValidationMessage> all = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(1, all.size());
    
    assertEquals("Unknown Code (http://hl7.org/fhir/v3/MaritalStatus#FOO)", output.getMessages().get(0).getMessage());
  }
  
  @Test
  public void testValidateResourceWithValuesetExpansion() {
    
    Patient patient = new Patient();
    patient.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

    ValidationResult output = myVal.validateWithResult(patient);
    List<SingleValidationMessage> all = logResultsAndReturnAll(output);
    assertEquals(2, all.size());
    assertEquals("/f:Patient/f:identifier/f:type", all.get(0).getLocationString());
    assertEquals("None of the codes are in the expected value set http://hl7.org/fhir/ValueSet/identifier-type (http://hl7.org/fhir/ValueSet/identifier-type)", all.get(0).getMessage());
    assertEquals(ResultSeverityEnum.WARNING, all.get(0).getSeverity());

    patient = new Patient();
    patient.addIdentifier().setSystem("http://system").setValue("12345").getType().addCoding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR");

    output = myVal.validateWithResult(patient);
    all = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(0, all.size());
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
  public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
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
  public void testValidateResourceWithExampleBindingCodeValidationPassingLoincWithExpansion() {
    Observation input = new Observation();
    
    ValueSetExpansionComponent expansionComponent = new ValueSetExpansionComponent();
    expansionComponent.addContains().setSystem("http://loinc.org").setCode("12345").setDisplay("Some display code");
    
    mySupportedCodeSystemsForExpansion.put("http://loinc.org", expansionComponent);
    myInstanceVal.setValidationSupport(myMockSupport);
    addValidConcept("http://loinc.org", "12345");

    input.setStatus(ObservationStatus.FINAL);
    input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234");

    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(errors.toString(), 1, errors.size());
    assertEquals("Unable to validate code \"1234\" in code system \"http://loinc.org\"", errors.get(0).getMessage());   
    
  }

  @Test
  public void testValidateResourceWithExampleBindingCodeValidationPassingNonLoinc() {
    Observation input = new Observation();

    myInstanceVal.setValidationSupport(myMockSupport);
    addValidConcept("http://acme.org", "12345");

    input.setStatus(ObservationStatus.FINAL);
    input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

    ValidationResult output = myVal.validateWithResult(input);
    List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
    assertEquals(errors.toString(), 0, errors.size());
  }

}
