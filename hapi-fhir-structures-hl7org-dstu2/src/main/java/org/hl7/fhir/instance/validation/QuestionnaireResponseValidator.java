package org.hl7.fhir.instance.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.Attachment;
import org.hl7.fhir.instance.model.BooleanType;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.DateType;
import org.hl7.fhir.instance.model.DecimalType;
import org.hl7.fhir.instance.model.Extension;
import org.hl7.fhir.instance.model.InstantType;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.OperationOutcome.IssueType;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Questionnaire.AnswerFormat;
import org.hl7.fhir.instance.model.Questionnaire.GroupComponent;
import org.hl7.fhir.instance.model.Questionnaire.QuestionComponent;
import org.hl7.fhir.instance.model.QuestionnaireResponse;
import org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionAnswerComponent;
import org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.TimeType;
import org.hl7.fhir.instance.model.Type;
import org.hl7.fhir.instance.model.UriType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptReferenceComponent;
import org.hl7.fhir.instance.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.instance.utils.WorkerContext;

/**
 * Validates that an instance of {@link QuestionnaireResponse} is valid against the {@link Questionnaire} that it claims to conform to.
 * 
 * @author James Agnew
 */
public class QuestionnaireResponseValidator extends BaseValidator {

  // @formatter:off
  /*
   * ***************************************************************** 
   * Note to anyone working on this class -
   * 
   * This class has unit tests which run within the HAPI project build. Please sync any changes here to HAPI and ensure that unit tests are run.
   * ****************************************************************
   */
  // @formatter:on

  private WorkerContext myWorkerCtx;

  public QuestionnaireResponseValidator(WorkerContext theWorkerCtx) {
    this.myWorkerCtx = theWorkerCtx;
  }

  private Set<Class<? extends Type>> allowedTypes(Class<? extends Type> theClass0) {
    HashSet<Class<? extends Type>> retVal = new HashSet<Class<? extends Type>>();
    retVal.add(theClass0);
    return Collections.unmodifiableSet(retVal);
  }

  private Set<Class<? extends Type>> allowedTypes(Class<? extends Type> theClass0, Class<? extends Type> theClass1) {
    HashSet<Class<? extends Type>> retVal = new HashSet<Class<? extends Type>>();
    retVal.add(theClass0);
    retVal.add(theClass1);
    return Collections.unmodifiableSet(retVal);
  }

  private List<org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent> findAnswersByLinkId(List<org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent> theQuestion,
      String theLinkId) {
    Validate.notBlank(theLinkId, "theLinkId must not be blank");

    ArrayList<org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent> retVal = new ArrayList<QuestionnaireResponse.QuestionComponent>();
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent next : theQuestion) {
      if (theLinkId.equals(next.getLinkId())) {
        retVal.add(next);
      }
    }
    return retVal;
  }

  private List<org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent> findGroupByLinkId(List<org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent> theGroups, String theLinkId) {
    ArrayList<org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent> retVal = new ArrayList<QuestionnaireResponse.GroupComponent>();
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent next : theGroups) {
      if (theLinkId == null) {
        if (next.getLinkId() == null) {
          retVal.add(next);
        }
      } else if (theLinkId.equals(next.getLinkId())) {
        retVal.add(next);
      }
    }
    return retVal;
  }

  // protected boolean fail(List<ValidationMessage> errors, IssueType type, List<String> pathParts, boolean thePass, String msg) {
  // return test(errors, type, pathParts, thePass, msg, IssueSeverity.FATAL);
  // }

  public void validate(List<ValidationMessage> theErrors, QuestionnaireResponse theAnswers) {
    LinkedList<String> pathStack = new LinkedList<String>();
    pathStack.add("QuestionnaireResponse");
    pathStack.add(QuestionnaireResponse.SP_QUESTIONNAIRE);

    if (!super.fail(theErrors, IssueType.INVALID, pathStack, theAnswers.hasQuestionnaire(), "QuestionnaireResponse does not specity which questionnaire it is providing answers to")) {
      return;
    }

    Reference questionnaireRef = theAnswers.getQuestionnaire();
    Questionnaire questionnaire = getQuestionnaire(theAnswers, questionnaireRef);
    if (questionnaire == null && theErrors.size() > 0 && theErrors.get(theErrors.size() - 1).getLevel() == IssueSeverity.FATAL) {
      return;
    }
    if (!fail(theErrors, IssueType.INVALID, pathStack, questionnaire != null, "Questionnaire {0} is not found in the WorkerContext", theAnswers.getQuestionnaire().getReference())) {
      return;
    }

    QuestionnaireResponseStatus status = theAnswers.getStatus();
    boolean validateRequired = false;
    if (status == QuestionnaireResponseStatus.COMPLETED || status == QuestionnaireResponseStatus.AMENDED) {
      validateRequired = true;
    }

    pathStack.removeLast();
    pathStack.add("group[0]");
    validateGroup(theErrors, questionnaire.getGroup(), theAnswers.getGroup(), pathStack, theAnswers, validateRequired);

    /*
     * If we found any fatal errors, any other errors will be removed since the fatal error means the parsing was invalid
     */
    for (ValidationMessage next : theErrors) {
      if (next.getLevel() == IssueSeverity.FATAL) {
        for (Iterator<ValidationMessage> iter = theErrors.iterator(); iter.hasNext();) {
          if (iter.next().getLevel() != IssueSeverity.FATAL) {
            iter.remove();
          }
        }
        break;
      }
    }

  }

  private Questionnaire getQuestionnaire(QuestionnaireResponse theAnswers, Reference theQuestionnaireRef) {
    Questionnaire retVal;
    if (theQuestionnaireRef.getReferenceElement().isLocal()) {
      retVal = (Questionnaire) theQuestionnaireRef.getResource();
      if (retVal == null) {
        for (Resource next : theAnswers.getContained()) {
          if (theQuestionnaireRef.getReferenceElement().getValue().equals(next.getId())) {
            retVal = (Questionnaire) next;
          }
        }
      }
    } else {
      retVal = myWorkerCtx.getQuestionnaires().get(theQuestionnaireRef.getReferenceElement().getValue());
    }
    return retVal;
  }

  private ValueSet getValueSet(QuestionnaireResponse theAnswers, Reference theQuestionnaireRef) {
    ValueSet retVal;
    if (theQuestionnaireRef.getReferenceElement().isLocal()) {
      retVal = (ValueSet) theQuestionnaireRef.getResource();
      if (retVal == null) {
        for (Resource next : theAnswers.getContained()) {
          if (theQuestionnaireRef.getReferenceElement().getValue().equals(next.getId())) {
            retVal = (ValueSet) next;
          }
        }
      }
    } else {
      retVal = myWorkerCtx.getValueSets().get(theQuestionnaireRef.getReferenceElement().getValue());
    }
    return retVal;
  }

  private void validateGroup(List<ValidationMessage> theErrors, GroupComponent theQuestGroup, org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent theAnsGroup,
      LinkedList<String> thePathStack, QuestionnaireResponse theAnswers, boolean theValidateRequired) {

    for (org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent next : theAnsGroup.getQuestion()) {
      rule(theErrors, IssueType.INVALID, thePathStack, isNotBlank(next.getLinkId()), "Question found with no linkId");
    }

    Set<String> allowedQuestions = new HashSet<String>();
    for (QuestionComponent nextQuestion : theQuestGroup.getQuestion()) {
      allowedQuestions.add(nextQuestion.getLinkId());
    }

    for (int i = 0; i < theQuestGroup.getQuestion().size(); i++) {
      QuestionComponent nextQuestion = theQuestGroup.getQuestion().get(i);
      validateQuestion(theErrors, nextQuestion, theAnsGroup, thePathStack, theAnswers, theValidateRequired);
    }

    // Check that there are no extra answers
    for (int i = 0; i < theAnsGroup.getQuestion().size(); i++) {
      org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent nextQuestion = theAnsGroup.getQuestion().get(i);
      thePathStack.add("question[" + i + "]");
      rule(theErrors, IssueType.BUSINESSRULE, thePathStack, allowedQuestions.contains(nextQuestion.getLinkId()), "Found answer with linkId[{0}] but this ID is not allowed at this position",
          nextQuestion.getLinkId());
      thePathStack.remove();
    }

    validateGroupGroups(theErrors, theQuestGroup, theAnsGroup, thePathStack, theAnswers, theValidateRequired);

  }

  private void validateQuestion(List<ValidationMessage> theErrors, QuestionComponent theQuestion, org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent theAnsGroup,
      LinkedList<String> thePathStack, QuestionnaireResponse theAnswers, boolean theValidateRequired) {
    QuestionComponent question = theQuestion;
    String linkId = question.getLinkId();
    if (!fail(theErrors, IssueType.INVALID, thePathStack, isNotBlank(linkId), "Questionnaire is invalid, question found with no link ID")) {
      return;
    }

    AnswerFormat type = question.getType();
    if (type == null) {
      // Support old format/casing and new
      List<Extension> extensions = question.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/questionnaire-deReference");
      if (extensions.isEmpty()) {
        extensions = question.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/questionnaire-dereference");
      }
      if (extensions.isEmpty() == false) {
        if (extensions.size() > 1) {
          warning(theErrors, IssueType.BUSINESSRULE, thePathStack, false,
              "Questionnaire is invalid, element contains multiple extensions with URL 'questionnaire-dereference', maximum one may be contained in a single element");
        }
        return;
        /*
         * Hopefully we will implement this soon...
         */

        // Extension ext = extensions.get(0);
        // Reference ref = (Reference) ext.getValue();
        // DataElement de = myWorkerCtx.getDataElements().get(ref.getReference());
        // if (de.getElement().size() != 1) {
        // warning(theErrors, IssueType.BUSINESSRULE, EMPTY_PATH, false, "DataElement {0} has wrong number of elements: {1}", ref.getReference(),
        // de.getElement().size());
        // }
        // ElementDefinition element = de.getElement().get(0);
        // question = toQuestion(element);
      } else {
        if (question.getGroup().isEmpty()) {
          rule(theErrors, IssueType.INVALID, thePathStack, false, "Questionnaire is invalid, no type and no groups specified for question with link ID[{0}]", linkId);
          return;
        }
        type = AnswerFormat.NULL;
      }
    }

    List<org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent> answers = findAnswersByLinkId(theAnsGroup.getQuestion(), linkId);
    if (answers.size() > 1) {
      rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !question.getRequired(), "Multiple answers repetitions found with linkId[{0}]", linkId);
    }
    if (answers.size() == 0) {
      if (theValidateRequired) {
        rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !question.getRequired(), "Missing answer to required question with linkId[{0}]", linkId);
      } else {
        hint(theErrors, IssueType.BUSINESSRULE, thePathStack, !question.getRequired(), "Missing answer to required question with linkId[{0}]", linkId);
      }
      return;
    }

    org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent answerQuestion = answers.get(0);
    try {
      thePathStack.add("question[" + answers.indexOf(answerQuestion) + "]");
      validateQuestionAnswers(theErrors, question, thePathStack, type, answerQuestion, theAnswers, theValidateRequired);
      validateQuestionGroups(theErrors, question, answerQuestion, thePathStack, theAnswers, theValidateRequired);
    } finally {
      thePathStack.removeLast();
    }
  }

  private void validateQuestionGroups(List<ValidationMessage> theErrors, QuestionComponent theQuestion, org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent theAnswerQuestion,
      LinkedList<String> thePathSpec, QuestionnaireResponse theAnswers, boolean theValidateRequired) {
    for (QuestionAnswerComponent nextAnswer : theAnswerQuestion.getAnswer()) {
      validateGroups(theErrors, theQuestion.getGroup(), nextAnswer.getGroup(), thePathSpec, theAnswers, theValidateRequired);
    }
  }

  private void validateGroupGroups(List<ValidationMessage> theErrors, GroupComponent theQuestGroup, org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent theAnsGroup,
      LinkedList<String> thePathSpec, QuestionnaireResponse theAnswers, boolean theValidateRequired) {
    validateGroups(theErrors, theQuestGroup.getGroup(), theAnsGroup.getGroup(), thePathSpec, theAnswers, theValidateRequired);
  }

  private void validateGroups(List<ValidationMessage> theErrors, List<GroupComponent> theQuestionGroups, List<org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent> theAnswerGroups,
      LinkedList<String> thePathStack, QuestionnaireResponse theAnswers, boolean theValidateRequired) {
    Set<String> linkIds = new HashSet<String>();
    for (GroupComponent nextQuestionGroup : theQuestionGroups) {
      String nextLinkId = StringUtils.defaultString(nextQuestionGroup.getLinkId());
      if (!linkIds.add(nextLinkId)) {
        if (isBlank(nextLinkId)) {
          fail(theErrors, IssueType.BUSINESSRULE, thePathStack, false,
              "Questionnaire in invalid, unable to validate QuestionnaireResponse: Multiple groups found at this position with blank/missing linkId", nextLinkId);
        } else {
          fail(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Questionnaire in invalid, unable to validate QuestionnaireResponse: Multiple groups found at this position with linkId[{0}]",
              nextLinkId);
        }
      }
    }

    Set<String> allowedGroups = new HashSet<String>();
    for (GroupComponent nextQuestionGroup : theQuestionGroups) {
      String linkId = nextQuestionGroup.getLinkId();
      allowedGroups.add(linkId);

      List<org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent> answerGroups = findGroupByLinkId(theAnswerGroups, linkId);
      if (answerGroups.isEmpty()) {
        if (nextQuestionGroup.getRequired()) {
          if (theValidateRequired) {
            rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Missing required group with linkId[{0}]", linkId);
          } else {
            hint(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Missing required group with linkId[{0}]", linkId);
          }
        }
        continue;
      }
      if (answerGroups.size() > 1) {
        if (nextQuestionGroup.getRepeats() == false) {
          int index = theAnswerGroups.indexOf(answerGroups.get(1));
          thePathStack.add("group[" + index + "]");
          rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Multiple repetitions of group with linkId[{0}] found at this position, but this group can not repeat", linkId);
          thePathStack.removeLast();
        }
      }
      for (org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent nextAnswerGroup : answerGroups) {
        int index = theAnswerGroups.indexOf(nextAnswerGroup);
        thePathStack.add("group[" + index + "]");
        validateGroup(theErrors, nextQuestionGroup, nextAnswerGroup, thePathStack, theAnswers, theValidateRequired);
        thePathStack.removeLast();
      }
    }

    // Make sure there are no groups in answers that aren't in the questionnaire
    int idx = -1;
    for (org.hl7.fhir.instance.model.QuestionnaireResponse.GroupComponent next : theAnswerGroups) {
      idx++;
      if (!allowedGroups.contains(next.getLinkId())) {
        thePathStack.add("group[" + idx + "]");
        rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Group with linkId[{0}] found at this position, but this group does not exist at this position in Questionnaire",
            next.getLinkId());
        thePathStack.removeLast();
      }
    }
  }

  private void validateQuestionAnswers(List<ValidationMessage> theErrors, QuestionComponent theQuestion, LinkedList<String> thePathStack, AnswerFormat type,
      org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionComponent answerQuestion, QuestionnaireResponse theAnswers, boolean theValidateRequired) {

    String linkId = theQuestion.getLinkId();
    Set<Class<? extends Type>> allowedAnswerTypes = determineAllowedAnswerTypes(type);
    if (allowedAnswerTypes.isEmpty()) {
      rule(theErrors, IssueType.BUSINESSRULE, thePathStack, answerQuestion.isEmpty(), "Question with linkId[{0}] has no answer type but an answer was provided", linkId);
    } else {
      rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !(answerQuestion.getAnswer().size() > 1 && !theQuestion.getRepeats()), "Multiple answers to non repeating question with linkId[{0}]",
          linkId);
      if (theValidateRequired) {
        rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !(theQuestion.getRequired() && answerQuestion.getAnswer().isEmpty()), "Missing answer to required question with linkId[{0}]", linkId);
      } else {
        hint(theErrors, IssueType.BUSINESSRULE, thePathStack, !(theQuestion.getRequired() && answerQuestion.getAnswer().isEmpty()), "Missing answer to required question with linkId[{0}]", linkId);
      }
    }

    int answerIdx = -1;
    for (QuestionAnswerComponent nextAnswer : answerQuestion.getAnswer()) {
      answerIdx++;
      try {
        thePathStack.add("answer[" + answerIdx + "]");
        Type nextValue = nextAnswer.getValue();
        if (!allowedAnswerTypes.contains(nextValue.getClass())) {
          rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] found of type [{1}] but this is invalid for question of type [{2}]", linkId,
              nextValue.getClass().getSimpleName(), type.toCode());
          continue;
        }

        // Validate choice answers
        if (type == AnswerFormat.CHOICE || type == AnswerFormat.OPENCHOICE) {
          if (nextAnswer.getValue() instanceof StringType) {
            StringType answer = (StringType) nextAnswer.getValue();
            if (answer == null || isBlank(answer.getValueAsString())) {
              rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is required but answer does not have a value", linkId);
              continue;
            }
          } else {
            Coding coding = (Coding) nextAnswer.getValue();
            if (isBlank(coding.getCode())) {
              rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is of type {1} but coding answer does not have a code", linkId, type.name());
              continue;
            }
            if (isBlank(coding.getSystem())) {
              rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is of type {1} but coding answer does not have a system", linkId, type.name());
              continue;
            }

            String optionsRef = theQuestion.getOptions().getReference();
            if (isNotBlank(optionsRef)) {
              ValueSet valueSet = getValueSet(theAnswers, theQuestion.getOptions());
              if (valueSet == null) {
                rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Question with linkId[{0}] has options ValueSet[{1}] but this ValueSet can not be found", linkId, optionsRef);
                continue;
              }

              boolean found = false;
              if (coding.getSystem().equals(valueSet.getCodeSystem().getSystem())) {
                for (ConceptDefinitionComponent next : valueSet.getCodeSystem().getConcept()) {
                  if (coding.getCode().equals(next.getCode())) {
                    found = true;
                    break;
                  }
                }
              }
              if (!found) {
                for (ConceptSetComponent nextCompose : valueSet.getCompose().getInclude()) {
                  if (coding.getSystem().equals(nextCompose.getSystem())) {
                    for (ConceptReferenceComponent next : nextCompose.getConcept()) {
                      if (coding.getCode().equals(next.getCode())) {
                        found = true;
                        break;
                      }
                    }
                  }
                  if (found) {
                    break;
                  }
                }
              }

              rule(theErrors, IssueType.BUSINESSRULE, thePathStack, found, "Question with linkId[{0}] has answer with system[{1}] and code[{2}] but this is not a valid answer for ValueSet[{3}]",
                  linkId, coding.getSystem(), coding.getCode(), optionsRef);
            }
          }
        }

      } finally {
        thePathStack.removeLast();
      }

    } // for answers
  }

  private Set<Class<? extends Type>> determineAllowedAnswerTypes(AnswerFormat type) {
    Set<Class<? extends Type>> allowedAnswerTypes;
    switch (type) {
    case ATTACHMENT:
      allowedAnswerTypes = allowedTypes(Attachment.class);
      break;
    case BOOLEAN:
      allowedAnswerTypes = allowedTypes(BooleanType.class);
      break;
    case CHOICE:
      allowedAnswerTypes = allowedTypes(Coding.class);
      break;
    case DATE:
      allowedAnswerTypes = allowedTypes(DateType.class);
      break;
    case DATETIME:
      allowedAnswerTypes = allowedTypes(DateTimeType.class);
      break;
    case DECIMAL:
      allowedAnswerTypes = allowedTypes(DecimalType.class);
      break;
    case INSTANT:
      allowedAnswerTypes = allowedTypes(InstantType.class);
      break;
    case INTEGER:
      allowedAnswerTypes = allowedTypes(IntegerType.class);
      break;
    case OPENCHOICE:
      allowedAnswerTypes = allowedTypes(Coding.class, StringType.class);
      break;
    case QUANTITY:
      allowedAnswerTypes = allowedTypes(Quantity.class);
      break;
    case REFERENCE:
      allowedAnswerTypes = allowedTypes(Reference.class);
      break;
    case STRING:
      allowedAnswerTypes = allowedTypes(StringType.class);
      break;
    case TEXT:
      allowedAnswerTypes = allowedTypes(StringType.class);
      break;
    case TIME:
      allowedAnswerTypes = allowedTypes(TimeType.class);
      break;
    case URL:
      allowedAnswerTypes = allowedTypes(UriType.class);
      break;
    case NULL:
    default:
      allowedAnswerTypes = Collections.emptySet();
    }
    return allowedAnswerTypes;
  }
}
