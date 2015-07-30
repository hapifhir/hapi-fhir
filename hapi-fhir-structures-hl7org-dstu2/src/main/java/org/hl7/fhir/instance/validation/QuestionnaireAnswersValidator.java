package org.hl7.fhir.instance.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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
import org.hl7.fhir.instance.model.InstantType;
import org.hl7.fhir.instance.model.IntegerType;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.Questionnaire;
import org.hl7.fhir.instance.model.Questionnaire.AnswerFormat;
import org.hl7.fhir.instance.model.Questionnaire.GroupComponent;
import org.hl7.fhir.instance.model.Questionnaire.QuestionComponent;
import org.hl7.fhir.instance.model.QuestionnaireAnswers;
import org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionAnswerComponent;
import org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionnaireAnswersStatus;
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
import org.hl7.fhir.instance.model.valuesets.IssueType;
import org.hl7.fhir.instance.utils.WorkerContext;

import ca.uhn.fhir.context.FhirContext;

/**
 * Validates that an instance of {@link QuestionnaireAnswers} is valid against the {@link Questionnaire} that it claims to conform to.
 * 
 * @author James Agnew
 */
public class QuestionnaireAnswersValidator extends BaseValidator {

	/*
	 * ***************************************************************** Note to anyone working on this class -
	 * 
	 * This class has unit tests which run within the HAPI project build. Please sync any changes here to HAPI and ensure that unit tests are run.
	 * ****************************************************************
	 */

	private WorkerContext myWorkerCtx;

	public QuestionnaireAnswersValidator(WorkerContext theWorkerCtx) {
		this.myWorkerCtx = theWorkerCtx;
	}

	private Set<Class<? extends Type>> allowedTypes(Class<? extends Type> theClass0) {
		HashSet<Class<? extends Type>> retVal = new HashSet<Class<? extends Type>>();
		retVal.add(theClass0);
		return Collections.unmodifiableSet(retVal);
	}

	private List<org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent> findAnswersByLinkId(List<org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent> theQuestion,
			String theLinkId) {
		Validate.notBlank(theLinkId, "theLinkId must not be blank");

		ArrayList<org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent> retVal = new ArrayList<QuestionnaireAnswers.QuestionComponent>();
		for (org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent next : theQuestion) {
			if (theLinkId.equals(next.getLinkId())) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	private List<org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent> findGroupByLinkId(List<org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent> theGroups, String theLinkId) {
		ArrayList<org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent> retVal = new ArrayList<QuestionnaireAnswers.GroupComponent>();
		for (org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent next : theGroups) {
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

	public void validate(List<ValidationMessage> theErrors, QuestionnaireAnswers theAnswers) {
		LinkedList<String> pathStack = new LinkedList<String>();
		pathStack.add("QuestionnaireAnswers");
		pathStack.add(QuestionnaireAnswers.SP_QUESTIONNAIRE);

		if (!fail(theErrors, IssueType.INVALID, pathStack, theAnswers.hasQuestionnaire(), "QuestionnaireAnswers does not specity which questionnaire it is providing answers to")) {
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

		QuestionnaireAnswersStatus status = theAnswers.getStatus();
		boolean validateRequired = false;
		if (status == QuestionnaireAnswersStatus.COMPLETED || status == QuestionnaireAnswersStatus.AMENDED) {
			validateRequired = true;
		}

		pathStack.removeLast();
		pathStack.add("group[0]");
		validateGroup(theErrors, questionnaire.getGroup(), theAnswers.getGroup(), pathStack, theAnswers, validateRequired);
	}

	private Questionnaire getQuestionnaire(QuestionnaireAnswers theAnswers, Reference theQuestionnaireRef) {
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

	private ValueSet getValueSet(QuestionnaireAnswers theAnswers, Reference theQuestionnaireRef) {
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

	private void validateGroup(List<ValidationMessage> theErrors, GroupComponent theQuestGroup, org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent theAnsGroup,
			LinkedList<String> thePathStack, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {

		for (org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent next : theAnsGroup.getQuestion()) {
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
			org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent nextQuestion = theAnsGroup.getQuestion().get(i);
			thePathStack.add("question[" + i + "]");
			rule(theErrors, IssueType.BUSINESSRULE, thePathStack, allowedQuestions.contains(nextQuestion.getLinkId()), "Found answer with linkId[{0}] but this ID is not allowed at this position",
					nextQuestion.getLinkId());
			thePathStack.remove();
		}

		validateGroupGroups(theErrors, theQuestGroup, theAnsGroup, thePathStack, theAnswers, theValidateRequired);

	}

	private void validateQuestion(List<ValidationMessage> theErrors, QuestionComponent theQuestion, org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent theAnsGroup,
			LinkedList<String> thePathStack, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {
		String linkId = theQuestion.getLinkId();
		if (!fail(theErrors, IssueType.INVALID, thePathStack, isNotBlank(linkId), "Questionnaire is invalid, question found with no link ID")) {
			return;
		}

		AnswerFormat type = theQuestion.getType();
		if (type == null) {
			if (theQuestion.getGroup().isEmpty()) {
				rule(theErrors, IssueType.INVALID, thePathStack, false, "Questionnaire in invalid, no type and no groups specified for question with link ID[{0}]", linkId);
				return;
			}
			type = AnswerFormat.NULL;
		}

		List<org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent> answers = findAnswersByLinkId(theAnsGroup.getQuestion(), linkId);
		if (answers.size() > 1) {
			rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRequired(), "Multiple answers repetitions found with linkId[{0}]", linkId);
		}
		if (answers.size() == 0) {
			if (theValidateRequired) {
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRequired(), "Missing answer to required question with linkId[{0}]", linkId);
			} else {
				hint(theErrors, IssueType.BUSINESSRULE, thePathStack, !theQuestion.getRequired(), "Missing answer to required question with linkId[{0}]", linkId);
			}
			return;
		}

		org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent answerQuestion = answers.get(0);
		try {
			thePathStack.add("question[" + answers.indexOf(answerQuestion) + "]");
			validateQuestionAnswers(theErrors, theQuestion, thePathStack, type, answerQuestion, theAnswers, theValidateRequired);
			validateQuestionGroups(theErrors, theQuestion, answerQuestion, thePathStack, theAnswers, theValidateRequired);
		} finally {
			thePathStack.removeLast();
		}
	}

	private void validateQuestionGroups(List<ValidationMessage> theErrors, QuestionComponent theQuestion, org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent theAnswerQuestion,
			LinkedList<String> thePathSpec, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {
		validateGroups(theErrors, theQuestion.getGroup(), theAnswerQuestion.getGroup(), thePathSpec, theAnswers, theValidateRequired);
	}

	private void validateGroupGroups(List<ValidationMessage> theErrors, GroupComponent theQuestGroup, org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent theAnsGroup,
			LinkedList<String> thePathSpec, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {
		validateGroups(theErrors, theQuestGroup.getGroup(), theAnsGroup.getGroup(), thePathSpec, theAnswers, theValidateRequired);
	}

	private void validateGroups(List<ValidationMessage> theErrors, List<GroupComponent> theQuestionGroups, List<org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent> theAnswerGroups,
			LinkedList<String> thePathStack, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {
		Set<String> linkIds = new HashSet<String>();
		for (GroupComponent nextQuestionGroup : theQuestionGroups) {
			String nextLinkId = StringUtils.defaultString(nextQuestionGroup.getLinkId());
			if (!linkIds.add(nextLinkId)) {
				rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Questionnaire in invalid, unable to validate QuestionnaireAnswers: Multiple groups found at this position with linkId[{0}]", nextLinkId);
			}
		}
		
		Set<String> allowedGroups = new HashSet<String>();
		for (GroupComponent nextQuestionGroup : theQuestionGroups) {
			String linkId = nextQuestionGroup.getLinkId();
			allowedGroups.add(linkId);

			List<org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent> answerGroups = findGroupByLinkId(theAnswerGroups, linkId);
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
			for (org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent nextAnswerGroup : answerGroups) {
				int index = theAnswerGroups.indexOf(nextAnswerGroup);
				thePathStack.add("group[" + index + "]");
				validateGroup(theErrors, nextQuestionGroup, nextAnswerGroup, thePathStack, theAnswers, theValidateRequired);
				thePathStack.removeLast();
			}
		}

		// Make sure there are no groups in answers that aren't in the questionnaire
		int idx = -1;
		for (org.hl7.fhir.instance.model.QuestionnaireAnswers.GroupComponent next : theAnswerGroups) {
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
			org.hl7.fhir.instance.model.QuestionnaireAnswers.QuestionComponent answerQuestion, QuestionnaireAnswers theAnswers, boolean theValidateRequired) {

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
					Coding coding = (Coding) nextAnswer.getValue();
					if (isBlank(coding.getCode()) && isBlank(coding.getSystem()) && isBlank(coding.getSystem())) {
						rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false, "Answer to question with linkId[{0}] is of type coding, but none of code, system, and display are populated", linkId);
						continue;
					} else if (isBlank(coding.getCode()) && isBlank(coding.getSystem())) {
						if (type != AnswerFormat.OPENCHOICE) {
							rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false,
									"Answer to question with linkId[{0}] is of type only has a display populated (no code or system) but question does not allow {1}", linkId, AnswerFormat.OPENCHOICE.name());
							continue;
						}
					} else if (isBlank(coding.getCode()) || isBlank(coding.getSystem())) {
						rule(theErrors, IssueType.BUSINESSRULE, thePathStack, false,
								"Answer to question with linkId[{0}] has a coding, but this coding does not contain a code and system (both must be present, or neither is the question allows {1})", linkId,
								AnswerFormat.OPENCHOICE.name());
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
			allowedAnswerTypes = allowedTypes(Coding.class);
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
