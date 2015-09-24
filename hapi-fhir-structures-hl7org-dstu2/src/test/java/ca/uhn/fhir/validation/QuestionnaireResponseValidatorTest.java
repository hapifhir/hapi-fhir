package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.DataElement;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Questionnaire.AnswerFormat;
import org.hl7.fhir.instance.model.Questionnaire.GroupComponent;
import org.hl7.fhir.instance.model.QuestionnaireResponse;
import org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.utils.WorkerContext;
import org.hl7.fhir.instance.validation.QuestionnaireResponseValidator;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class QuestionnaireResponseValidatorTest {
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(QuestionnaireResponseValidatorTest.class);
  private QuestionnaireResponseValidator myVal;

  private WorkerContext myWorkerCtx;

  @Before
  public void before() {
    myWorkerCtx = new WorkerContext();
    myVal = new QuestionnaireResponseValidator(myWorkerCtx);
  }

  @Test
  public void testAnswerWithWrongType() {
    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.BOOLEAN);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("Answer to question with linkId[link0] found of type [StringType] but this is invalid for question of type [boolean]"));
  }

  @Test
  public void testCodedAnswer() {
    String questionnaireRef = "http://example.com/Questionnaire/q1";

    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.CHOICE).setOptions(new Reference("http://somevalueset"));
    myWorkerCtx.getQuestionnaires().put(questionnaireRef, q);

    ValueSet options = new ValueSet();
    options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
    options.getCompose().addInclude().setSystem("urn:system2").addConcept().setCode("code2");
    myWorkerCtx.getValueSets().put("http://somevalueset", options);

    QuestionnaireResponse qa;
    List<ValidationMessage> errors;

    // Good code

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 0, errors.size());

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system2").setCode("code2"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 0, errors.size());

    // Bad code

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(),
        containsString("message=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset]"));

    qa = new QuestionnaireResponse();

    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system2").setCode("code3"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(),
        containsString("message=Question with linkId[link0] has answer with system[urn:system2] and code[code3] but this is not a valid answer for ValueSet[http://somevalueset]"));

  }

  @Test
  public void testOpenchoiceAnswer() {
    String questionnaireRef = "http://example.com/Questionnaire/q1";

    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.OPENCHOICE).setOptions(new Reference("http://somevalueset"));
    myWorkerCtx.getQuestionnaires().put(questionnaireRef, q);

    ValueSet options = new ValueSet();
    options.getCodeSystem().setSystem("urn:system").addConcept().setCode("code0");
    options.getCompose().addInclude().setSystem("urn:system2").addConcept().setCode("code2");
    myWorkerCtx.getValueSets().put("http://somevalueset", options);

    QuestionnaireResponse qa;
    List<ValidationMessage> errors;

    // Good code

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code0"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 0, errors.size());

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system2").setCode("code2"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 0, errors.size());

    // Bad code

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("urn:system").setCode("code1"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(),
        containsString("message=Question with linkId[link0] has answer with system[urn:system] and code[code1] but this is not a valid answer for ValueSet[http://somevalueset]"));

    // Partial code

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem(null).setCode("code1"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] is of type OPENCHOICE but coding answer does not have a system"));

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("").setCode("code1"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] is of type OPENCHOICE but coding answer does not have a system"));

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("system").setCode(null));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] is of type OPENCHOICE but coding answer does not have a code"));

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new Coding().setSystem("system").setCode(null));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] is of type OPENCHOICE but coding answer does not have a code"));

    // Wrong type

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new IntegerType(123));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("message=Answer to question with linkId[link0] found of type [IntegerType] but this is invalid for question of type [open-choice]"));

    // String answer

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new StringType("Hello"));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors, empty());

    // Missing String answer

    qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference(questionnaireRef);
    qa.getGroup().addQuestion().setLinkId("link0").addAnswer().setValue(new StringType(""));
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]/answer[0]"));
    assertThat(errors.toString(), containsString("Answer to question with linkId[link0] is required but answer does not have a value"));

  }

  @Test
  public void testExtensionDereference() throws Exception {
    Questionnaire q = ourCtx.newJsonParser().parseResource(Questionnaire.class, IOUtils.toString(getClass().getResourceAsStream("/dereference-q.json")));
    QuestionnaireResponse qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, IOUtils.toString(getClass().getResourceAsStream("/dereference-qr.xml")));
    DataElement de = ourCtx.newJsonParser().parseResource(DataElement.class, IOUtils.toString(getClass().getResourceAsStream("/dereference-de.json")));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    myWorkerCtx.getDataElements().put("DataElement/4771", de);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertEquals(errors.toString(), errors.size(), 0);
  }

  @Test
  public void testGroupWithNoLinkIdInQuestionnaireResponse() {
    Questionnaire q = new Questionnaire();
    GroupComponent qGroup = q.getGroup().addGroup();
    qGroup.addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.BOOLEAN);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent qaGroup = qa.getGroup().addGroup();
    qaGroup.addQuestion().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("Answer to question with linkId[link0] found of type [StringType] but this is invalid for question of type [boolean]"));
  }

  @Test
  public void testMissingRequiredQuestion() {

    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.STRING);
    q.getGroup().addQuestion().setLinkId("link1").setRequired(true).setType(AnswerFormat.STRING);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.setStatus(QuestionnaireResponseStatus.COMPLETED);
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    qa.getGroup().addQuestion().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("Missing answer to required question with linkId[link0]"));
  }

  @Test
  public void testMultipleGroupsWithNoLinkIdInQuestionnaire() {
    Questionnaire q = new Questionnaire();
    GroupComponent qGroup = q.getGroup().addGroup();
    qGroup.addQuestion().setLinkId("link0").setRequired(true).setType(AnswerFormat.BOOLEAN);
    qGroup = q.getGroup().addGroup();
    qGroup.addQuestion().setLinkId("link1").setRequired(true).setType(AnswerFormat.BOOLEAN);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent qaGroup = qa.getGroup().addGroup();
    qaGroup.addQuestion().setLinkId("link0").addAnswer().setValue(new StringType("FOO"));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString(
        "ValidationMessage[level=FATAL,type=BUSINESSRULE,location=//QuestionnaireResponse/group[0],message=Questionnaire in invalid, unable to validate QuestionnaireResponse: Multiple groups found at this position with blank/missing linkId]"));
    assertEquals(1, errors.size());
  }

  @Test
  public void testUnexpectedAnswer() {
    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.BOOLEAN);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    qa.getGroup().addQuestion().setLinkId("link1").addAnswer().setValue(new StringType("FOO"));

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/question[0]"));
    assertThat(errors.toString(), containsString("message=Found answer with linkId[link1] but this ID is not allowed at this position"));
  }

  @Test
  public void testUnexpectedGroup() {
    Questionnaire q = new Questionnaire();
    q.getGroup().addQuestion().setLinkId("link0").setRequired(false).setType(AnswerFormat.BOOLEAN);

    QuestionnaireResponse qa = new QuestionnaireResponse();
    qa.getQuestionnaire().setReference("http://example.com/Questionnaire/q1");
    qa.getGroup().addGroup().setLinkId("link1");

    myWorkerCtx.getQuestionnaires().put(qa.getQuestionnaire().getReference(), q);
    List<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);

    ourLog.info(errors.toString());
    assertThat(errors.toString(), containsString("location=//QuestionnaireResponse/group[0]/group[0]"));
    assertThat(errors.toString(), containsString("Group with linkId[link1] found at this position, but this group does not exist at this position in Questionnaire"));
  }

  // @Test
  public void validateHealthConnexExample() throws Exception {
    String input = IOUtils.toString(QuestionnaireResponseValidatorTest.class.getResourceAsStream("/questionnaireanswers-0f431c50ddbe4fff8e0dd6b7323625fc.xml"));

    QuestionnaireResponse qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, input);
    ArrayList<ValidationMessage> errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 0, errors.size());

    /*
     * Now change a coded value
     */
    // @formatter:off
    input = input.replaceAll(
        "<answer>\n" + "					<valueCoding>\n" + "						<system value=\"f69573b8-cb63-4d31-85a4-23ac784735ab\"/>\n" + "						<code value=\"2\"/>\n"
            + "						<display value=\"Once/twice\"/>\n" + "					</valueCoding>\n" + "				</answer>",
        "<answer>\n" + "					<valueCoding>\n" + "						<system value=\"f69573b8-cb63-4d31-85a4-23ac784735ab\"/>\n" + "						<code value=\"GGG\"/>\n"
            + "						<display value=\"Once/twice\"/>\n" + "					</valueCoding>\n" + "				</answer>");
    assertThat(input, containsString("GGG"));
    // @formatter:on

    qa = ourCtx.newXmlParser().parseResource(QuestionnaireResponse.class, input);
    errors = new ArrayList<ValidationMessage>();
    myVal.validate(errors, qa);
    assertEquals(errors.toString(), 10, errors.size());
  }

}
